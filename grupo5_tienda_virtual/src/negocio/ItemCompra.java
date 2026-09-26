package negocio;

/**
 * Cada linea del recibo: un libro con su cantidad, subtotal e impuestos.
 */
public class ItemCompra {

    private Libro libro;
    private int cantidad;
    private double subtotal;
    private double impuestos;

    public ItemCompra() {
    }

    public ItemCompra(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
        calcularSubtotal();
        calcularImpuestos();
    }

    public void calcularSubtotal() {
        if (libro != null) {
            this.subtotal = libro.getPrecio() * cantidad;
        }
    }

    public void calcularImpuestos() {
        if (libro != null) {
            this.impuestos = subtotal * (libro.getPorcentajeIva() / 100.0);
        }
    }

    public double getTotalLinea() {
        return subtotal + impuestos;
    }

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

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(double impuestos) {
        this.impuestos = impuestos;
    }

    @Override
    public String toString() {
        return libro.getTitulo() + " x" + cantidad
                + " = " + subtotal + " (+ IVA " + impuestos + ")";
    }
}
