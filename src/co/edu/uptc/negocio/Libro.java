package co.edu.uptc.negocio;


public abstract class Libro {

    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;
    private String categoria;
    private String editorial;
    private double precioBase;
    private int cantidadDisponible;

    protected Libro(String isbn, String titulo, String autor, int anioPublicacion,
                    String categoria, String editorial, double precioBase,
                    int cantidadDisponible) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.precioBase = precioBase;
        this.cantidadDisponible = cantidadDisponible;
    }

    public abstract FormatoLibro getFormato();

    public abstract double getPorcentajeIva();

    public abstract int getNumeroPaginas();

    public double getValorIva() {
        return redondear(precioBase * getPorcentajeIva());
    }
    public double getPrecioVenta() {
        return redondear(precioBase + getValorIva());
    }

    private static double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    // ---- Getters y setters ----
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public double getPrecioBase() { return precioBase; }
    public void setPrecioBase(double precioBase) { this.precioBase = precioBase; }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public String toString() {
        return "Libro [isbn=" + isbn + ", titulo=" + titulo + ", formato=" + getFormato() + "]";
    }
}
