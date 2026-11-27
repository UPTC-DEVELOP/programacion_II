package co.edu.uptc.caso.estudio.model;

public class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String categoria;
    private String editorial;
    private int paginas;
    private double precioVenta;   // Precio con IVA incluido
    private int stock;
    private String formato;       // "Físico" o "Digital"
    private double porcentajeIva; // 19 o 5 (o 0.19 / 0.05)

    public Libro() {
    }

    public Libro(String isbn, String titulo, String autor, int anio, String categoria,
                 String editorial, int paginas, double precioVenta, int stock,
                 String formato, double porcentajeIva) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.categoria = categoria;
        this.editorial = editorial;
        this.paginas = paginas;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.formato = formato;
        this.porcentajeIva = porcentajeIva;
    }

    // Normaliza IVA tanto si llega como 19 / 5 o como 0.19 / 0.05
    public double getTasaIvaDecimal() {
        if (porcentajeIva <= 0) {
            return 0.0;
        }
        return porcentajeIva > 1.0 ? porcentajeIva / 100.0 : porcentajeIva;
    }

    // Calcula el total bruto (con IVA) para una cantidad dada
    public double calcularSubtotal(int cantidad) {
        if (cantidad <= 0) {
            return 0.0;
        }
        return precioVenta * cantidad;
    }

    // Calcula la base sin IVA a partir del precio de venta que ya incluye IVA
    public double calcularSubtotalSinIva(int cantidad) {
        double totalConIva = calcularSubtotal(cantidad);
        double tasa = getTasaIvaDecimal();
        if (tasa <= 0.0) {
            return totalConIva;
        }
        return totalConIva / (1.0 + tasa);
    }

    // Calcula el valor del impuesto incluido en el precio de venta (SRP)
    public double calcularImpuesto(int cantidad) {
        double totalConIva = calcularSubtotal(cantidad);
        return totalConIva - calcularSubtotalSinIva(cantidad);
    }

    // Polimorfismo (LSP): define si requiere logística de envío físico
    public boolean requiereEnvioFisico() {
        return "Físico".equalsIgnoreCase(formato);
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getPaginas() {
        return paginas;
    }

    public void setPaginas(int paginas) {
        this.paginas = paginas;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public double getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(double porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }
}