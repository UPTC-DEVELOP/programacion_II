package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.enums.Categoria;

/**
 * DTO para encapsular los criterios de búsqueda de libros (RF04).
 * Principio: Encapsulamiento. Evita pasar múltiples parámetros sueltos en los
 * métodos y permite agregar nuevos criterios sin cambiar las firmas existentes
 * (OCP).
 */
public class FiltroLibroDto {
    private String titulo;
    private String autor;
    private Categoria categoria;
    private Double precioMin;
    private Double precioMax;

    public FiltroLibroDto() {
    }

    /**
     * Constructor compatible con los llamados existentes de la GUI.
     *
     * @param titulo    Texto a buscar en el título (puede ser null)
     * @param autor     Texto a buscar en los autores (puede ser null)
     * @param categoria Categoría exacta a filtrar (puede ser null)
     */
    public FiltroLibroDto(String titulo, String autor, Categoria categoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
    }

    /**
     * Constructor completo con rango de precios.
     *
     * @param titulo    Texto a buscar en el título (puede ser null)
     * @param autor     Texto a buscar en los autores (puede ser null)
     * @param categoria Categoría exacta a filtrar (puede ser null)
     * @param precioMin Precio mínimo inclusivo (puede ser null)
     * @param precioMax Precio máximo inclusivo (puede ser null)
     */
    public FiltroLibroDto(String titulo, String autor, Categoria categoria,
                          Double precioMin, Double precioMax) {
        this(titulo, autor, categoria);
        this.precioMin = precioMin;
        this.precioMax = precioMax;
    }

    // Getters y Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public Double getPrecioMin() { return precioMin; }
    public void setPrecioMin(Double precioMin) { this.precioMin = precioMin; }
    public Double getPrecioMax() { return precioMax; }
    public void setPrecioMax(Double precioMax) { this.precioMax = precioMax; }
}
