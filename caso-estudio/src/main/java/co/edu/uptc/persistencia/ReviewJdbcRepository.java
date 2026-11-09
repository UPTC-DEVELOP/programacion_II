package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.IReviewRepository;
import co.edu.uptc.persistencia.conexion.ConexionDB;

/**
 * IMPLEMENTA IReviewRepository Y GESTIONA LA PERSISTENCIA DE RESEÑAS EN POSTGRESQL
 *  @author: Equipo de desarrollo grupo 7 UPTC
 *  @modificacion:Brayan Panqueva
 */
public class ReviewJdbcRepository implements IReviewRepository {

    @Override
    public void guardar(Review r) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("INSERT INTO product_reviews(isbn_libro,id_cliente,calificacion,comentario) VALUES(?,?,?,?)")) {
            p.setString(1, r.isbn());
            p.setInt(2, r.idCliente());
            p.setInt(3, r.calificacion());
            p.setString(4, r.comentario());
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "guardar la reseña");
        }
    }

    @Override
    public List<Review> listarPorLibro(String isbn) {
        List<Review> out = new ArrayList<>();
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("SELECT id_resena,isbn_libro,id_cliente,calificacion,comentario FROM product_reviews WHERE isbn_libro=? ORDER BY fecha DESC")) {
            p.setString(1, isbn);
            try (ResultSet r = p.executeQuery()) {
                while (r.next()) {
                    out.add(new Review(r.getInt(1), r.getString(2), r.getInt(3), r.getInt(4), r.getString(5)));
                
                }return out;
            }
        } catch (SQLException e) {
            throw error(e, "consultar las reseñas");
        }
    }

    @Override
    public void actualizar(Review r) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("UPDATE product_reviews SET calificacion=?,comentario=? WHERE id_resena=?")) {
            p.setInt(1, r.calificacion());
            p.setString(2, r.comentario());
            p.setInt(3, r.id());
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "actualizar la reseña");
        }
    }
 //METODO SOBRESCRITO PARA ELIMINAR LAS REVIEWS DE UN LIBRO, PERO SOLO SI EL CLIENTE ES EL DUEÑO DE LA RESEÑA O SI ES ADMINISTRADOR
    @Override
    public void eliminar(int id, int idCliente) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("DELETE FROM product_reviews WHERE id_resena=? AND (id_cliente=? OR EXISTS(SELECT 1 FROM clients c JOIN profiles_rols r ON r.id_rol=c.rol_id WHERE c.id_cliente=? AND r.codigo='ADMIN'))")) {
            p.setInt(1, id);
            p.setInt(2, idCliente);
            p.setInt(3, idCliente);
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "eliminar la reseña");
        }
    }

    private PersistenciaException error(SQLException e, String op) {
        return new PersistenciaException("No se pudo " + op + " en PostgreSQL: " + e.getMessage(), e);
    }
}
