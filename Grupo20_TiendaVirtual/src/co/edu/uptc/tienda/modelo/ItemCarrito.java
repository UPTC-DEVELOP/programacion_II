package co.edu.uptc.tienda.modelo;

public class ItemCarrito {

    private final Libro libro;
    private int cantidad;

    public ItemCarrito(Libro libro, int cantidad) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        validarCantidad(cantidad);
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() {
        return libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        validarCantidad(cantidad);
        this.cantidad = cantidad;
    }

    public double obtenerSubtotalItem() {
        return libro.getPrecioVenta() * cantidad;
    }

    public double obtenerImpuestoItem() {
        return obtenerSubtotalItem() * libro.getIvaPorcentaje() / 100.0;
    }

    private static void validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que 0.");
        }
    }
}
