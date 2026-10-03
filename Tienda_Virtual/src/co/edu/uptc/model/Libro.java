package co.edu.uptc.model;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;
    private String categoria;
    private String editorial;
    private int numeroPaginas;
    private double precio;
    private double porcentajeIva;
    private double porcentajeDescuento; // Requerido en el parcial (Estudiante A)
    private int cantidadInventario;
    private String formato;

    public Libro() {
    }

    public Libro(String isbn, String titulo, String autor, int anioPublicacion, String categoria, 
                 String editorial, int numeroPaginas, double precio, double porcentajeIva, 
                 double porcentajeDescuento, int cantidadInventario, String formato) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.numeroPaginas = numeroPaginas;
        this.precio = precio;
        this.porcentajeIva = porcentajeIva;
        this.porcentajeDescuento = porcentajeDescuento;
        this.cantidadInventario = cantidadInventario;
        this.formato = formato;
    }

    // Método solicitado en el parcial con Descuento e IVA
    public double calcularPrecioFinal() {
        double precioConDescuento = precio * (1 - (porcentajeDescuento / 100.0));
        return precioConDescuento * (1 + (porcentajeIva / 100.0));
    }

    // Getters y Setters
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

    public int getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(int numeroPaginas) { this.numeroPaginas = numeroPaginas; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getPorcentajeIva() { return porcentajeIva; }
    public void setPorcentajeIva(double porcentajeIva) { this.porcentajeIva = porcentajeIva; }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }

    public int getCantidadInventario() { return cantidadInventario; }
    public void setCantidadInventario(int cantidadInventario) { this.cantidadInventario = cantidadInventario; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    @Override
    public String toString() {
        return titulo;
    }
}