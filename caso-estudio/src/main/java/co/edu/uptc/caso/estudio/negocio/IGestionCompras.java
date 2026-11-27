package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Compra;
import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.negocio.dto.PagoDto;
import java.util.List;

// Contrato de negocio para gestión del carrito, validación de pagos y registro de compras
public interface IGestionCompras {

    Carrito obtenerCarritoActual();

    void agregarAlCarrito(Libro libro, int cantidad);

    String actualizarCantidadCarrito(String isbn, int nuevaCantidad);

    boolean eliminarDelCarrito(List<String> isbns);

    void vaciarCarrito();

    String registrarPago(PagoDto pago);

    boolean finalizarCompra();

    double calcularTotalFinal();

    List<Compra> obtenerHistorialCompras();
}