package co.edu.uptc.caso.estudio.model;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String categoria;
    private String editorial;
    private int paginas;
    private double precioVenta; // Precio con IVA incluido[cite: 285]
    private int stock;
    private String formato;     // "Físico" o "Digital"[cite: 285]
    private double porcentajeIva; // 19 o 5 (o 0.19 / 0.05)[cite: 285]

    public Libro() {}

    public Libro(String isbn, String titulo, String autor, int anio, String categoria,
                 String editorial, int paginas, double precioVenta, int stock, String formato, double porcentajeIva) {
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
        return porcentajeIva > 1 ? porcentajeIva / 100.0 : porcentajeIva;
    }

    // Calcula el valor del impuesto para una cantidad dada (SRP)[cite: 286]
    public double calcularImpuesto(int cantidad) {
        return (precioVenta * cantidad) * getTasaIvaDecimal();
    }

    // Calcula el subtotal sin impuestos para una cantidad dada (SRP)[cite: 286]
    public double calcularSubtotal(int cantidad) {
        return precioVenta * cantidad;
    }

    // Polimorfismo (LSP): cada subclase define si requiere logística de envío físico
    public boolean requiereEnvioFisico() {
        return "Físico".equalsIgnoreCase(formato);
    }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public int getAnio() { return anio; }
    public void setAnio(int anio) { this.anio = anio; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public int getPaginas() { return paginas; }
    public void setPaginas(int paginas) { this.paginas = paginas; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }
    public double getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(double porcentajeIva) { this.porcentajeIva = porcentajeIva; }
}