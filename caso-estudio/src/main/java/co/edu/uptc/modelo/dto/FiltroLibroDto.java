package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.enums.Categoria;

/**
 * DTO para encapsular los criterios de búsqueda de libros.
 * Principio: Encapsulamiento. Evita pasar múltiples parámetros sueltos en los métodos.
 */
public class FiltroLibroDto {
    private String titulo;
    private String autor;
    private Categoria categoria;

    public FiltroLibroDto() {
    }

    public FiltroLibroDto(String titulo, String autor, Categoria categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}