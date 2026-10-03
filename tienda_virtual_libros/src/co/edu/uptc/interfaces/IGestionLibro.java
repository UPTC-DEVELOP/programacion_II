package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;


/**
 * INTERFAZ IGestionLibro  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de negocio para el CRUD de libros (RF01-RF04).
 * Equivale a IGestionEmpleadoFijo del proyecto del profesor.
 *
 * SOLID aplicado:
 *  - DIP: el controlador depende de ESTA interfaz, no de la clase concreta.
 *  - ISP: solo contiene operaciones de libros (los reportes van en otra).
 *  - LSP/OCP: se puede cambiar la implementación sin tocar la GUI.
 */
public interface IGestionLibro {

    /** RF01 - Registrar un libro nuevo. */
    void registrar(LibroDto libro) throws ReglaNegocioException;

    /** RF02 - Actualizar un libro existente (el ISBN no se modifica). */
    void actualizar(LibroDto libro) throws ReglaNegocioException;

    /** RF03 - Eliminar un libro (solo si no tiene ventas asociadas). */
    void eliminar(String isbn) throws ReglaNegocioException;

    /** RF04 - Listar el catálogo con filtros opcionales, ordenado por título. */
    List<LibroDto> listar(FiltroLibroDto filtro);

    /** Busca un libro por ISBN; devuelve null si no existe. */
    LibroDto buscarPorIsbn(String isbn);
}
