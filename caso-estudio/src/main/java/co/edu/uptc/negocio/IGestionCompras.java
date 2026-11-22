package co.edu.uptc.negocio;

import java.util.List;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.Libro;
import co.edu.uptc.negocio.dto.PagoDto;

public interface IGestionCompras {
    Carrito obtenerCarritoActual();
    void agregarAlCarrito(Libro libro, int cantidad);
    String actualizarCantidadCarrito(String isbn, int nuevaCantidad);
    boolean eliminarDelCarrito(List<String> isbns);
    void vaciarCarrito();
    String registrarPago(PagoDto pago);
    boolean finalizarCompra();
}
