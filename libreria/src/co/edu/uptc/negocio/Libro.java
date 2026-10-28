package co.edu.uptc.negocio;

public abstract class Libro extends Producto {

    private String autor;
    private int anioPublicacion;
    private String categoria;
    private String editorial;

    protected Libro(String isbn, String titulo, String autor, int anioPublicacion,
                    String categoria, String editorial, double precioBase,
                    int cantidadDisponible, double porcentajeDescuento, double impuestoIVA) {
        super(isbn, titulo, precioBase, cantidadDisponible, porcentajeDescuento, impuestoIVA);
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
    }

    // ---- Métodos polimórficos ----
    public abstract FormatoLibro getFormato();

    public abstract int getNumeroPaginas();

    // ---- Alias con el vocabulario del DAD ----
    public String getIsbn() { return getCodigo(); }
    public void setIsbn(String isbn) { setCodigo(isbn); }

    public String getTitulo() { return getNombre(); }
    public void setTitulo(String titulo) { setNombre(titulo); }

    public int getCantidadDisponible() { return getStock(); }
    public void setCantidadDisponible(int cantidadDisponible) { setStock(cantidadDisponible); }

    // ---- Atributos propios del libro ----
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public String toString() {
        return "Libro [isbn=" + getIsbn() + ", titulo=" + getTitulo() + ", formato=" + getFormato() + "]";
    }
}
