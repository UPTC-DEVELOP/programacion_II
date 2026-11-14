package co.edu.uptc.model;

import java.util.ArrayList;

// Libro agrega la información editorial común a los formatos físico y digital.
// Hereda los cálculos de precio e inventario definidos por Producto.
public abstract class Libro extends Producto {
    private ArrayList<String> autores;
    private int anioPublicacion;
    private Categoria categoria;
    private String editorial;
    private int numeroPaginas;

    protected Libro(String isbn, String titulo, ArrayList<String> autores, int anioPublicacion,
                    Categoria categoria, String editorial, int numeroPaginas, double precioBase,
                    int stock, double porcentajeDescuento, double impuestoIVA) {
        super(isbn, titulo, precioBase, stock, porcentajeDescuento, impuestoIVA);
        this.autores = new ArrayList<>(autores);
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.numeroPaginas = numeroPaginas;
    }

    public String getIsbn() { return getCodigo(); }
    public String getTitulo() { return getNombre(); }
    public abstract FormatoLibro getFormato();
    public ArrayList<String> getAutores() { return new ArrayList<>(autores); }
    public void setAutores(ArrayList<String> autores) { this.autores = new ArrayList<>(autores); }
    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public int getNumeroPaginas() { return numeroPaginas; }
    public void setNumeroPaginas(int numeroPaginas) { this.numeroPaginas = numeroPaginas; }
}
