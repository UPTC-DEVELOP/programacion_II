package co.edu.uptc.interfaces;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import java.util.List;

/**
 * INTERFAZ ILibroRepositorio  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de PERSISTENCIA de libros. La capa de negocio (GestionLibro) solo
 * conoce esta interfaz, nunca la clase concreta que guarda los datos.
 * Hoy la implementa LocalLibro (lista en RAM); para usar archivos o JDBC basta
 * crear otra clase que la implemente y cambiar UNA línea en AppLibros (OCP / DIP).
 */
public interface ILibroRepositorio {

    void guardar(Libro libro);
    void actualizar(Libro libro);          // reemplaza el que tenga el mismo ISBN
    void eliminar(String isbn);
    boolean existePorIsbn(String isbn);
    Libro consultarPorIsbn(String isbn);
    List<Libro> listarTodos();
    List<Libro> listarConFiltro(FiltroLibroDto filtro);
}
