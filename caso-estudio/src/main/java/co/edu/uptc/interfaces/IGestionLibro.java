package co.edu.uptc.interfaces;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;
import java.util.List;

/**
 * INTERFAZ IGestionLibro  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Contrato para la gestión (CRUD) de libros.
 * La capa de Negocio implementa esto, y la capa GUI depende de esta abstracción
 * (principio DIP: la GUI nunca conoce la clase concreta GestionLibro).
 */
public interface IGestionLibro {

    /** CREATE: registra un libro nuevo validando las reglas de negocio. */
    void guardarLibro(LibroDto libroDto) throws ReglaNegocioException;

    /** UPDATE: actualiza un libro existente. */
    void actualizarLibro(LibroDto libroDto) throws ReglaNegocioException;

    /** DELETE: elimina un libro por su ISBN. */
    void eliminarLibro(String isbn) throws ReglaNegocioException;

    /** READ: lista los libros, aplicando el filtro cuando no es nulo. */
    List<LibroDto> listar(FiltroLibroDto filtro);

    /** READ: consulta un libro por ISBN; devuelve null si no existe. */
    LibroDto consultarPorIsbn(String isbn);
}
