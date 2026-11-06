package co.edu.uptc.modelo;

import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;
import java.util.ArrayList;
import java.util.List;

public class Libro {
    private String isbn;
    private String titulo;
    private List<String> autores;
    private int anioPublicacion;
    private String editorial;
    private int numPaginas;
    private double precioVenta;
    private int stock;
    private Formato formato;
    private Categoria categoria; // Debe ser ENUM, NO STRING

    /**
     * Atributos opcionales exigidos por RF01 para libros FÍSICOS.
     * Se declaran como objetos (Double/String) para poder distinguir
     * "no informado" (null) de un valor realmente suministrado.
     */
    private Double peso;
    private String dimensiones;

    /**
     * Constructor vacío requerido por Gson para deserializar los libros
     * almacenados en data/libros.json.
     */
    public Libro() {
        this.autores = new ArrayList<>();
    }

    public Libro(String isbn, String titulo, List<String> autores, int anioPublicacion,
                 String editorial, int numPaginas, double precioVenta, int stock,
                 Formato formato, Categoria categoria) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autores = autores;
        this.anioPublicacion = anioPublicacion;
        this.editorial = editorial;
        this.numPaginas = numPaginas;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.formato = formato;
        this.categoria = categoria;
    }

    //  GETTERS Y SETTERS
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public List<String> getAutores() { return autores; }
    public void setAutores(List<String> autores) { this.autores = autores; }
    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public Formato getFormato() { return formato; }
    public void setFormato(Formato formato) { this.formato = formato; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }
    public String getDimensiones() { return dimensiones; }
    public void setDimensiones(String dimensiones) { this.dimensiones = dimensiones; }
}