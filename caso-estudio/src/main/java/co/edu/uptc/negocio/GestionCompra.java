package co.edu.uptc.negocio;

import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.excepciones.ClienteNoEncontradoException;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.ICompraRepository;
import co.edu.uptc.interfaces.IGestionCompra;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
import co.edu.uptc.modelo.dto.CompraDtos.ItemCompraDto;
import co.edu.uptc.modelo.dto.CompraDtos.PagoDto;
import co.edu.uptc.modelo.dto.CompraDtos.SolicitudCompraDto;
/*
 * LOGICA DE NEGOCIO PARA GESTIONAR COMPRAS Y FACTURAS, IMPLEMENTA IGestionCompra
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
/*modificacion:Brayan Panqueva*/
public class GestionCompra implements IGestionCompra {
    private final ICarritoRepository carritoRepository;
    private final ICarritoRepository carritoRepositoryJson;
    private final IClienteRepositorio clienteRepository;
    private final ICompraRepository compraRepository;

    public GestionCompra(ICarritoRepository carritoRepository,
                         IClienteRepositorio clienteRepository,
                         ICompraRepository compraRepository) {
        this(carritoRepository, clienteRepository, compraRepository, null);
    }

    /*modificacion:Brayan Panqueva*/
    public GestionCompra(ICarritoRepository carritoRepository,
                         IClienteRepositorio clienteRepository,
                         ICompraRepository compraRepository,
                         ICarritoRepository carritoRepositoryJson) {
        this.carritoRepository = carritoRepository;
        this.clienteRepository = clienteRepository;
        this.compraRepository = compraRepository;
        this.carritoRepositoryJson = carritoRepositoryJson;
    }

    @Override
    public FacturaDto finalizarCompra(int idCliente) throws ReglaNegocioException {
        return finalizarCompra(idCliente, new PagoDto("EFECTIVO", "", "", Double.MAX_VALUE, 0));
    }

    public FacturaDto finalizarCompra(int idCliente, PagoDto pago) throws ReglaNegocioException {
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No existe el cliente que intenta comprar.");
        }
        CarritoCompras carrito = carritoRepository.obtenerCarritoPorCliente(idCliente);
        if (carrito == null || carrito.estaVacio()) {
            throw new ReglaNegocioException("El carrito está vacío. Agregue al menos un libro.");
        }
        carrito.setTipoCliente(cliente.getTipoCliente());
        double subtotal = carrito.calcularSubtotalGeneral();
        double impuestos = carrito.calcularTotalImpuestos();
        double descuento = carrito.calcularDescuento(cliente);
        double total = carrito.calcularTotalFinal(cliente);
        List<ItemCompraDto> items = new ArrayList<>();
        for (ItemCarrito item : carrito.obtenerItems()) {
            if (item.getCantidad() > 0) {
                items.add(new ItemCompraDto(item.getIsbn(), item.getCantidad(),
                        item.getPrecioUnitario(), item.getSubtotal()));
            }
        }
        if (items.isEmpty()) {
            throw new ReglaNegocioException("El carrito no contiene unidades válidas para comprar.");
        }
        PagoDto pagoValidado = validarPago(pago, total);
        int idVenta = compraRepository.finalizarCompra(
                new SolicitudCompraDto(idCliente, subtotal, impuestos, descuento, total,
                        pagoValidado.metodo(), items));
        if (carritoRepositoryJson != null) carritoRepositoryJson.eliminarCarrito(idCliente);
        return compraRepository.consultarFactura(idVenta);
    }

    private PagoDto validarPago(PagoDto pago, double total) throws ReglaNegocioException {
        if (pago == null || pago.metodo() == null) throw new ReglaNegocioException("Seleccione un método de pago.");
        if ("TARJETA".equalsIgnoreCase(pago.metodo())) {
            if (pago.numeroTarjeta() == null || !pago.numeroTarjeta().matches("\\d{16}"))
                throw new ReglaNegocioException("La tarjeta debe tener exactamente 16 dígitos.");
            if (pago.contrasena() == null || !pago.contrasena().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$"))
                throw new ReglaNegocioException("La contraseña de tarjeta debe tener mínimo 8 caracteres, una mayúscula, un número y un carácter especial.");
            return new PagoDto("TARJETA", pago.numeroTarjeta(), pago.contrasena(), total, 0);
        }
        if (!"EFECTIVO".equalsIgnoreCase(pago.metodo())) throw new ReglaNegocioException("Método de pago no válido.");
        if (pago.montoPagado() < 1000) throw new ReglaNegocioException("El monto en efectivo debe ser mínimo de $1.000.");
        if (pago.montoPagado() < total) throw new ReglaNegocioException("El efectivo recibido es menor al total de la compra.");
        return new PagoDto("EFECTIVO", "", "", pago.montoPagado(), redondear(pago.montoPagado() - total));
    }

    private double redondear(double valor) { return Math.round(valor * 100.0) / 100.0; }

    @Override
    public FacturaDto consultarFactura(int idVenta) throws ReglaNegocioException {
        if (idVenta <= 0) {
            throw new ReglaNegocioException("El identificador de la venta no es válido.");
        }
        FacturaDto factura = compraRepository.consultarFactura(idVenta);
        if (factura == null) {
            throw new ReglaNegocioException("No se encontró la factura solicitada.");
        }
        return factura;
    }
}
