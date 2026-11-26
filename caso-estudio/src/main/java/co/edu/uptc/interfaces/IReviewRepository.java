package co.edu.uptc.interfaces;

import java.util.List;
/*
* INTERFAZ IReviewRepository QUE DEFINE LOS MÉTODOS PARA GESTIONAR RESEÑAS DE LIBROS
* @author: Equipo de desarrollo grupo 7 UPTC
*/
public interface IReviewRepository {
    record Review(int id, String isbn, int idCliente, int calificacion, String comentario) { }
    void guardar(Review review);
    List<Review> listarPorLibro(String isbn);
    void actualizar(Review review);
    void eliminar(int id, int idCliente);

    /**
     * Nombres de los clientes que reseñaron un libro (id de cliente -> nombre).
     * Método default para no romper otras implementaciones: por defecto no
     * hay nombres y la GUI muestra "Cliente #id".
     */
    default java.util.Map<Integer, String> nombresClientes(String isbn) {
        return java.util.Map.of();
    }

    /**
     * Fecha de registro de cada reseña de un libro (id de reseña -> fecha).
     * Método default por la misma razón: no obliga a otras implementaciones.
     */
    default java.util.Map<Integer, java.time.LocalDateTime> fechasResenas(String isbn) {
        return java.util.Map.of();
    }

    /**
     * Reemplaza TODAS las reseñas de un libro por las indicadas, conservando sus
     * ids y fechas. Lo usa el espejo JSON: GestionResena lee las reseñas del
     * libro en la BD (fuente de verdad) y las copia aquí, de modo que los ids
     * coinciden. Método default para no obligar a otras implementaciones: por
     * defecto no hace nada.
     *
     * @param isbn    ISBN del libro
     * @param resenas Reseñas vigentes del libro (con su id)
     * @param fechas  Fecha de cada reseña (id de reseña -> fecha)
     */
    default void sincronizarLibro(String isbn, List<Review> resenas,
                                  java.util.Map<Integer, java.time.LocalDateTime> fechas) {
        // Sin efecto por defecto.
    }
}
