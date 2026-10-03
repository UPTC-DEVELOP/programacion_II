package co.edu.uptc.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * ENTIDAD DE DOMINIO Libro  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * Representa un libro del catálogo. Es la clase "Libro" del diagrama de clases
 * de negocio. Solo contiene DATOS y comportamiento propio del libro
 * (principio SRP: no sabe de pantallas, ni de archivos, ni de validaciones).
 *
 * Atributos tomados de RF01: isbn, titulo, autores, anioPublicacion,
 * categoria, editorial, numPaginas, precioVenta (IVA incluido), stock, formato.
 */
public class Libro {

    private String isbn;                 // identificador único (13 caracteres)
    private String titulo;
    private List<String> autores;
    private int anioPublicacion;
    private Categoria categoria;
    private String editorial;
    private int numPaginas;
    private double precioVenta;          // IVA incluido
    private int stock;
    private Formato formato;

    public Libro() {
        this.autores = new ArrayList<>();
    }

    public Libro(String isbn, String titulo, List<String> autores, int anioPublicacion,
                 Categoria categoria, String editorial, int numPaginas,
                 double precioVenta, int stock, Formato formato) {
        this.isbn = isbn;
        this.titulo = titulo;
        // Copia defensiva: evitamos que alguien modifique la lista desde afuera.
        this.autores = new ArrayList<>(autores);
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.numPaginas = numPaginas;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.formato = formato;
    }

    // ----------------------- Comportamiento propio -----------------------

    /** Un libro está "Disponible" cuando tiene al menos una unidad (RF04). */
    public boolean estaDisponible() {
        return stock > 0;
    }

    /**
     * Valor del IVA contenido en el precio. Como el precio YA incluye IVA:
     *   precioBase = precio / (1 + %/100)   ->   iva = precio - precioBase
     * Ejemplo del caso de estudio: 50.000 con 19% -> iva = 7.983,19 (aprox.)
     */
    public double calcularValorIva() {
        double base = precioVenta / (1 + formato.getPorcentajeIva() / 100.0);
        return precioVenta - base;
    }

    // ----------------------- Getters y setters -----------------------

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<String> getAutores() { return new ArrayList<>(autores); }
    public void setAutores(List<String> autores) { this.autores = new ArrayList<>(autores); }

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



