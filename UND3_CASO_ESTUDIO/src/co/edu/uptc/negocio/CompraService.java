package co.edu.uptc.negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompraService {

    private final LibroRepositorio libroRepositorio;
    private final CompraRepositorio compraRepositorio;
    private final CarritoRepositorio carritoRepositorio;
    private final ReciboService reciboService;
    private final BitacoraRepositorio bitacora;

    public CompraService(LibroRepositorio libroRepositorio, CompraRepositorio compraRepositorio,
                         CarritoRepositorio carritoRepositorio, ReciboService reciboService,
                         BitacoraRepositorio bitacora) {
        this.libroRepositorio = libroRepositorio;
        this.compraRepositorio = compraRepositorio;
        this.carritoRepositorio = carritoRepositorio;
        this.reciboService = reciboService;
        this.bitacora = bitacora;
    }

    public Compra confirmarCompra(Cliente cliente, Carrito carrito, MetodoPago metodoPago)
            throws ValidacionException {
        if (cliente == null) {
            throw new ValidacionException("Debe iniciar sesión antes de finalizar la compra.");
        }
        if (carrito == null || carrito.getItems().isEmpty()) {
            throw new ValidacionException("El carrito está vacío.");
        }
        if (metodoPago == null) {
            throw new ValidacionException("Debe seleccionar un método de pago.");
        }

        List<DetalleCompra> detalles = construirDetallesYValidarStock(carrito);
        double subtotal = 0;
        double impuestos = 0;
        for (DetalleCompra detalle : detalles) {
            subtotal += detalle.getSubtotal();
            impuestos += detalle.getImpuesto();
        }
        subtotal = redondear(subtotal);
        impuestos = redondear(impuestos);
        double antesPremium = redondear(subtotal + impuestos);
        double descuentoPremium = cliente.esPremium()
                ? redondear(antesPremium * Cliente.DESCUENTO_PREMIUM / 100.0) : 0.0;
        double total = redondear(antesPremium - descuentoPremium);

        Compra compra = new Compra(compraRepositorio.siguienteId(), LocalDateTime.now(),
                cliente.getCorreoElectronico(), cliente.getNombreCompleto(), cliente.getTipoCliente(),
                metodoPago, detalles, subtotal, impuestos, descuentoPremium, total);

        Map<Libro, Integer> stockOriginal = new LinkedHashMap<>();
        boolean compraGuardada = false;
        try {
            for (DetalleCompra detalle : detalles) {
                Libro libro = libroRepositorio.buscarPorIsbn(detalle.getIsbn())
                        .orElseThrow(() -> new ValidacionException("El libro " + detalle.getIsbn()
                                + " ya no existe en el catálogo."));
                stockOriginal.put(libro, libro.getStock());
                libro.disminuirStock(detalle.getCantidad());
                libroRepositorio.guardar(libro);
            }
            compraRepositorio.guardar(compra);
            compraGuardada = true;
            carritoRepositorio.eliminar(carrito.getCorreoCliente());
            carrito.vaciar();
            bitacora.registrar("CONFIRMAR_COMPRA", "Compra #" + compra.getId()
                    + ", cliente=" + cliente.getCorreoElectronico() + ", total=" + compra.getTotal());

            // El recibo es un comprobante de la compra ya persistida.
            reciboService.generar(compra);
            return compra;
        } catch (ValidacionException e) {
            rollbackStock(stockOriginal);
            throw e;
        } catch (RuntimeException e) {
            // Si la compra no alcanzó a persistirse, se revierte el inventario para
            // conservar la consistencia entre catálogo y compras.
            if (!compraGuardada) {
                rollbackStock(stockOriginal);
            }
            throw e;
        }
    }

    public List<Compra> listarPorCliente(String correoCliente) {
        return compraRepositorio.obtenerPorCliente(correoCliente);
    }

    public List<Compra> listarTodas() {
        return compraRepositorio.obtenerTodas();
    }

    private void rollbackStock(Map<Libro, Integer> stockOriginal) {
        for (Map.Entry<Libro, Integer> entry : stockOriginal.entrySet()) {
            Libro libro = entry.getKey();
            libro.setStock(entry.getValue());
            try {
                libroRepositorio.guardar(libro);
            } catch (RuntimeException ignorada) {
                // Se continúa con el resto de libros para maximizar la recuperación.
            }
        }
    }

    private List<DetalleCompra> construirDetallesYValidarStock(Carrito carrito) throws ValidacionException {
        List<DetalleCompra> detalles = new ArrayList<>();
        for (CarritoItem item : carrito.getItems()) {
            Libro libro = libroRepositorio.buscarPorIsbn(item.getIsbn())
                    .orElseThrow(() -> new ValidacionException("El libro \"" + item.getTitulo()
                            + "\" ya no está disponible en el catálogo."));
            if (item.getCantidad() > libro.getStock()) {
                throw new ValidacionException("Stock insuficiente para \"" + libro.getTitulo()
                        + "\". Disponible: " + libro.getStock() + ", solicitado: " + item.getCantidad() + ".");
            }
            double precioUnitario = redondear(libro.getPrecioBase()
                    * (1 - libro.getPorcentajeDescuento() / 100.0));
            double subtotal = redondear(precioUnitario * item.getCantidad());
            double impuesto = redondear(subtotal * libro.getImpuestoIVA() / 100.0);
            double total = redondear(subtotal + impuesto);
            detalles.add(new DetalleCompra(libro.getIsbn(), libro.getTitulo(), libro.getFormato(),
                    item.getCantidad(), precioUnitario, subtotal, impuesto, total));
        }
        return detalles;
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
