package co.uptc.edu.model;

/** Modelo de datos de un libro del inventario. */
public class Libro {
    private final int id;
    private final String titulo;
    private final String autor;
    private final double precio;
    private int stock;
    private final String categoria;

    public Libro(int id, String titulo, String autor, double precio, int stock, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.precio = precio;
        this.stock = stock;
        this.categoria = categoria;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public String getCategoria() { return categoria; }
}
