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
}
