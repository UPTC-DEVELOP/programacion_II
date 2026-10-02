package co.edu.uptc.negocio.admin.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.negocio.modelo.Categoria;
import co.edu.uptc.negocio.modelo.Formato;

/**
 * DTO (Data Transfer Object) LibroDto  (paquete: negocio.dto)
 * ---------------------------------------------------------------------------
 * Es el "sobre" con el que la CAPA DE PRESENTACIÓN le envía datos a la CAPA DE
 * NEGOCIO (y viceversa). Igual que CredencialDto en el proyecto del profesor.
 *
 * ¿Por qué no pasar directamente la entidad Libro a la GUI?
 *  - La GUI no debe depender del modelo de dominio (bajo acoplamiento).
 *  - Si el modelo cambia, la pantalla no se rompe.
 *  - El DTO no tiene lógica: solo transporta datos (SRP).
 */
public class LibroDto {

    private String isbn;
    private String titulo;
    private List<String> autores = new ArrayList<>();
    private int anioPublicacion;
    private Categoria categoria;
    private String editorial;
    private int numPaginas;
    private double precioVenta;
    private int stock;
    private Formato formato;

    public LibroDto() { }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<String> getAutores() { return autores; }
    public void setAutores(List<String> autores) { this.autores = autores; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

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
}
