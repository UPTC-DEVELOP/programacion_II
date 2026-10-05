package co.uptc.edu.model;

/** Elemento del carrito: un libro y la cantidad seleccionada. */
public class ItemCarrito {
    private final Libro libro;
    private int cantidad;

    public ItemCarrito(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() { return libro; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getSubtotal() { return libro.getPrecioVenta() * cantidad; }
}
