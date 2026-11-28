package co.uptc.edu.caso.estudio.service;

import java.util.List;

import co.uptc.edu.caso.estudio.negocio.Compra;
import co.uptc.edu.caso.estudio.negocio.DetalleCompra;
import co.uptc.edu.caso.estudio.negocio.Libro;
import co.uptc.edu.caso.estudio.negocio.Pago;
import co.uptc.edu.caso.estudio.repository.CompraRepository;

public class CompraService {

    private CompraRepository repoBD;
    private CompraRepository repoJson;
    private LibroService libroService;

    public CompraService(CompraRepository repoBD, CompraRepository repoJson, LibroService libroService) {
        this.repoBD = repoBD;
        this.repoJson = repoJson;
        this.libroService = libroService;
        Compra.sincronizarListaMemoria(listar());
    }

    public String registrarCompra(Compra compra, String metodoPago) {
        if (compra == null) {
            return "Datos de compra inválidos.";
        }
        if (compra.getIdCompra() <= 0) {
            return "El ID de la compra debe ser mayor a 0.";
        }
        if (buscarPorId(compra.getIdCompra()) != null) {
            return "Ya existe una compra registrada con el ID " + compra.getIdCompra() + ".";
        }
        if (compra.getDetalles() == null || compra.getDetalles().isEmpty()) {
            return "Debe agregar al menos un libro al carrito antes de guardar la compra.";
        }

        // 1. Validar stock disponible de cada libro en el carrito
        for (DetalleCompra detalle : compra.getDetalles()) {
            Libro libroActual = libroService.buscarPorIsbn(detalle.getLibro().getIsbn());
            if (libroActual == null) {
                return "El libro con ISBN " + detalle.getLibro().getIsbn() + " ya no existe.";
            }
            if (detalle.getCantidad() <= 0) {
                return "La cantidad debe ser mayor a 0.";
            }
            if (detalle.getCantidad() > libroActual.getCantidadInventario()) {
                return "Stock insuficiente para '" + libroActual.getTitulo()
                        + "'. Disponible: " + libroActual.getCantidadInventario();
            }
        }

        // 2. Calcular total (aplica descuento si es Premium) y procesar pago
        double totalPagar = compra.calcularTotal();
        Pago pago = new Pago(metodoPago, totalPagar);
        if (!pago.procesarPago()) {
            return "El pago fue rechazado. Verifique el método de pago.";
        }
        compra.setPago(pago);
        compra.setEstado("Confirmada");

        // 3. Descontar inventario en LibroService
        for (DetalleCompra detalle : compra.getDetalles()) {
            Libro libroActual = libroService.buscarPorIsbn(detalle.getLibro().getIsbn());
            libroActual.setCantidadInventario(libroActual.getCantidadInventario() - detalle.getCantidad());
            libroService.actualizar(libroActual);
        }

        // 4. Persistir en BD, JSON y memoria
        repoBD.guardar(compra);
        repoJson.guardar(compra);
        Compra.sincronizarListaMemoria(listar());

        return "OK";
    }

    public List<Compra> listar() {
        List<Compra> compras = repoBD.listar();
        if (compras == null || compras.isEmpty()) {
            compras = repoJson.listar();
        }
        return compras;
    }

    public List<Compra> listarJson() {
        return repoJson.listar();
    }

    public Compra buscarPorId(int idCompra) {
        Compra c = repoBD.buscarPorId(idCompra);
        if (c == null) {
            c = repoJson.buscarPorId(idCompra);
        }
        return c;
    }

    public boolean actualizarEstado(int idCompra, String nuevoEstado) {
        Compra c = buscarPorId(idCompra);
        if (c == null || nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            return false;
        }
        c.setEstado(nuevoEstado.trim());
        repoBD.actualizar(c);
        repoJson.actualizar(c);
        Compra.sincronizarListaMemoria(listar());
        return true;
    }

    public boolean eliminar(int idCompra) {
        Compra c = buscarPorId(idCompra);
        if (c == null) {
            return false;
        }
        repoBD.eliminar(idCompra);
        repoJson.eliminar(idCompra);
        Compra.sincronizarListaMemoria(listar());
        return true;
    }
}
