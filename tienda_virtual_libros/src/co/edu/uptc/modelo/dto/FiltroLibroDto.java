package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.Categoria;



/**
 * DTO FiltroLibroDto  (paquete: modelo.dto)
 * ---------------------------------------------------------------------------
 * Agrupa los criterios OPCIONALES y COMBINABLES de RF04 (listar catálogo):
 *  categoria, autor, titulo, precioMin, precioMax.
 * Además incluye "texto", que es lo que escribe el administrador en la caja
 * "Búsqueda por ISBN o Título" del prototipo (busca por contiene en ambos).
 *
 * Un campo en null significa "no filtrar por esto".
 * Usamos wrappers (Double) justamente para poder representar "no informado".
 */
public class FiltroLibroDto {

    private String texto;          // ISBN o título (búsqueda parcial)
    private Categoria categoria;
    private String autor;          // búsqueda parcial
    private String titulo;         // búsqueda parcial
    private Double precioMin;
    private Double precioMax;

    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Double getPrecioMin() { return precioMin; }
    public void setPrecioMin(Double precioMin) { this.precioMin = precioMin; }

    public Double getPrecioMax() { return precioMax; }
    public void setPrecioMax(Double precioMax) { this.precioMax = precioMax; }
}
