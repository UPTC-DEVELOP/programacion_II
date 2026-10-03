package co.edu.uptc.negocio.admin;

import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.Libro;



/**
 * CLASE LibroMapper  (paquete: negocio)
 * ---------------------------------------------------------------------------
 * Convierte Libro (entidad)  <->  LibroDto (transporte).
 * SRP: la conversión tiene su propia clase para que GestionLibro no se llene
 * de código repetitivo de "copiar campo por campo".
 */
public final class LibroMapper {

    private LibroMapper() { }   // clase de utilidad: no se instancia

    public static Libro aEntidad(LibroDto dto) {
        return new Libro(dto.getIsbn().trim(), dto.getTitulo().trim(), dto.getAutores(),
                dto.getAnioPublicacion(), dto.getCategoria(), dto.getEditorial().trim(),
                dto.getNumPaginas(), dto.getPrecioVenta(), dto.getStock(), dto.getFormato());
    }

    public static LibroDto aDto(Libro libro) {
        LibroDto dto = new LibroDto();
        dto.setIsbn(libro.getIsbn());
        dto.setTitulo(libro.getTitulo());
        dto.setAutores(libro.getAutores());
        dto.setAnioPublicacion(libro.getAnioPublicacion());
        dto.setCategoria(libro.getCategoria());
        dto.setEditorial(libro.getEditorial());
        dto.setNumPaginas(libro.getNumPaginas());
        dto.setPrecioVenta(libro.getPrecioVenta());
        dto.setStock(libro.getStock());
        dto.setFormato(libro.getFormato());
        return dto;
    }
}
