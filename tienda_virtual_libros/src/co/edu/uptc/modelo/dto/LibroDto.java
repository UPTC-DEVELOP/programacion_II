package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;
import java.util.ArrayList;
import java.util.List;

public class LibroDto {
    private String isbn;
    private String titulo;
    private List<String> autores;
    private int anioPublicacion;
    private String editorial;
    private int numPaginas;
    private double precioVenta;
    private int stock;
    private Formato formato;
    private Categoria categoria;

    // CONSTRUCTOR VACÍO (para que funcione con setters como en tu Eclipse)
    public LibroDto() {
        this.autores = new ArrayList<>();
    }

    // CONSTRUCTOR COMPLETO (para cuando necesites inicializar todo)
    public LibroDto(String isbn, String titulo, List<String> autores, int anioPublicacion,
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

    // GETTERS Y SETTERS
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
}