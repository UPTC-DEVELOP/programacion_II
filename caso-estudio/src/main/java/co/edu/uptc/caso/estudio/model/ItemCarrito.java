package co.edu.uptc.caso.estudio.model;

public class ItemCarrito {

    private Libro libro;
    private int cantidad;
    private Double precioUnitarioOverride;

    public ItemCarrito() {
    }

    public ItemCarrito(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public ItemCarrito(Libro libro, int cantidad, double precioUnitario) {
        this.libro = libro;
        this.cantidad = cantidad;
        this.precioUnitarioOverride = precioUnitario;
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

    public Double getPrecioUnitarioOverride() {
        return precioUnitarioOverride;
    }

    public void setPrecioUnitarioOverride(Double precioUnitarioOverride) {
        this.precioUnitarioOverride = precioUnitarioOverride;
    }

    // Prioriza el precio override si es válido (>= 0), de lo contrario usa el precio del libro
    private double obtenerPrecioUnitarioEfectivo() {
        if (precioUnitarioOverride != null && precioUnitarioOverride >= 0.0) {
            return precioUnitarioOverride;
        }
        if (libro != null && libro.getPrecioVenta() >= 0.0) {
            return libro.getPrecioVenta();
        }
        return 0.0;
    }

    // El precio del libro YA incluye IVA, por eso el valor sin IVA se obtiene
    // dividiendo: precioUnitario / (1 + tasaIva). Para el total con IVA no se recalcula nada.
    public double getSubtotalBase() {
        if (libro == null || cantidad <= 0) {
            return 0.0;
        }
        double precioUnitario = obtenerPrecioUnitarioEfectivo();
        double tasaIva = libro.getTasaIvaDecimal();
        double precioSinIva = tasaIva > 0.0 ? precioUnitario / (1.0 + tasaIva) : precioUnitario;
        return precioSinIva * cantidad;
    }

    public double getImpuestos() {
        if (libro == null || cantidad <= 0) {
            return 0.0;
        }
        return getSubtotalBase() * libro.getTasaIvaDecimal();
    }

    public double getSubtotalConIva() {
        if (libro == null || cantidad <= 0) {
            return 0.0;
        }
        return obtenerPrecioUnitarioEfectivo() * cantidad;
    }

    // Alias compatibles con Carrito y el resto del sistema
    public double getSubtotalSinIva() {
        return getSubtotalBase();
    }

    public double getValorIva() {
        return getImpuestos();
    }

    public double calcularSubtotal() {
        return getSubtotalBase();
    }

    public double calcularImpuesto() {
        return getImpuestos();
    }
}