package co.edu.uptc.model;

// LineaPedido es una copia fija de lo comprado para que el historial no cambie con el catálogo.
public class LineaPedido extends LineaCompra {
    public LineaPedido(String isbn, String titulo, double precioBase, double descuento,
                       double impuestoIVA, int cantidad) {
        super(isbn, titulo, precioBase, descuento, impuestoIVA, cantidad);
    }
}
