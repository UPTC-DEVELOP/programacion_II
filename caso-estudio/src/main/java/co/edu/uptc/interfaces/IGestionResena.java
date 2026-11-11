package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IReviewRepository.Review;

/**
 * INTERFAZ IGestionResena  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de NEGOCIO para el CRUD de reseñas y calificaciones de
 * libros (RF19 y RF20). La GUI solo conoce esta interfaz (DIP).
 *
 * Todos los métodos reciben el id del cliente que ejecuta la acción para poder
 * validar la propiedad de la reseña. Los errores de negocio se informan con
 * ReglaNegocioException (mensaje listo para mostrar al usuario).
 */
public interface IGestionResena {

    /** CREATE: registra la reseña de un cliente sobre un libro (una por cliente y libro). */
    void crear(int idCliente, String isbn, int calificacion, String comentario)
            throws ReglaNegocioException;

    /** READ: reseñas de un libro, de la más reciente a la más antigua. */
    List<Review> listarPorLibro(String isbn) throws ReglaNegocioException;

    /**
     * READ: nombres de quienes reseñaron el libro (id de cliente -> nombre).
     * Si no se pueden obtener devuelve un mapa vacío: la GUI usa "Cliente #id".
     */
    java.util.Map<Integer, String> nombresAutores(String isbn);

    /**
     * READ: fecha de registro de cada reseña del libro (id de reseña -> fecha).
     * Si no se puede obtener devuelve un mapa vacío: la GUI muestra "-".
     */
    java.util.Map<Integer, java.time.LocalDateTime> fechasResenas(String isbn);

    /** READ: promedio de calificaciones del libro (0.0 si no tiene reseñas). */
    double promedioCalificacion(String isbn) throws ReglaNegocioException;

    /** UPDATE: solo el dueño de la reseña puede modificarla. */
    void actualizar(int idCliente, String isbn, int idResena, int calificacion, String comentario)
            throws ReglaNegocioException;

    /** DELETE: el dueño o un administrador pueden eliminar la reseña. */
    void eliminar(int idCliente, String isbn, int idResena) throws ReglaNegocioException;
}
