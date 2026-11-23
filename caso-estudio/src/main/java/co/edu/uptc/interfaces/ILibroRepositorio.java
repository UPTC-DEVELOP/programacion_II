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

    /**
     * Indica si un libro tiene ventas asociadas (RF03).
     *
     * <p>Se declara como método default para no romper las implementaciones
     * existentes (por ejemplo LocalLibro), que no conocen el repositorio de
     * ventas y devuelven false. La implementación JSON lo sobrescribe cuando
     * recibe un IVentaRepository.</p>
     *
     * <p>Aplica: ISP (Interface Segregation Principle) y OCP - se amplía el
     * contrato con una operación opcional sin obligar a reescribir las clases
     * que no la necesitan.</p>
     *
     * @param isbn ISBN del libro a verificar
     * @return true si existe al menos una venta que referencia el ISBN
     */
    default boolean tieneVentas(String isbn) {
        return false;
    }
}
