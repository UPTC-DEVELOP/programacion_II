package co.edu.uptc.model;

public class ItemCarrito {
    private Libro libro;
    private int cantidad;

    public ItemCarrito(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    // Calcula el subtotal del libro según la cantidad seleccionada
    public double calcularSubtotal() {
        return libro.getPrecio() * cantidad;
    }

    // Calcula los impuestos (IVA) correspondientes a este ítem
    public double calcularImpuesto() {
        double subtotal = calcularSubtotal();
        return subtotal * (libro.getPorcentajeIva() / 100.0);
    }

    // Getters y Setters
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}