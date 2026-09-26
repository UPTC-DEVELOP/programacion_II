package negocio;

/**
 * Libro del catalogo de la tienda virtual.
 */
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
    private int cantidadDisponible;
    private FormatoLibro formato;

    public Libro() {
    }

    public Libro(String isbn, String titulo, String autor, double precio,
                 double porcentajeIva, int cantidadDisponible, FormatoLibro formato) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.porcentajeIva = porcentajeIva;
        this.cantidadDisponible = cantidadDisponible;
        this.formato = formato;
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

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
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

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        this.numeroPaginas = numeroPaginas;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(double porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public FormatoLibro getFormato() {
        return formato;
    }

    public void setFormato(FormatoLibro formato) {
        this.formato = formato;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Libro otro = (Libro) obj;
        return isbn != null && isbn.equals(otro.isbn);
    }

    @Override
    public int hashCode() {
        return isbn == null ? 0 : isbn.hashCode();
    }

    @Override
    public String toString() {
        return titulo + " - " + autor + " (ISBN " + isbn + ")";
    }
}
