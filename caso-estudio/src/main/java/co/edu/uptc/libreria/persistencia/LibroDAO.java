package co.edu.uptc.libreria.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.libreria.modelo.Libro;

public class LibroDAO {

    public boolean insertar(Libro libro) {
        String sql = "INSERT INTO libros (id, titulo, autor, precio, editorial, aniopublicacion, stock, genero) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, libro.getCodigo());
                ps.setString(2, libro.getTitulo());
                ps.setString(3, libro.getAutor());
                ps.setDouble(4, libro.getPrecio());
                ps.setString(5, libro.getEditorial());
                ps.setString(6, libro.getAnioPublicacion());
                ps.setString(7, libro.getStock());
                ps.setString(8, libro.getGenero());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public List<Libro> listar() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT* FROM libros";

        try (Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    libros.add(Construirlibros(rs));
                        
                }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return libros;
    }

    private Libro Construirlibros(ResultSet rs) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, precio = ? WHERE id = ?";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, libro.getTitulo());
                ps.setString(2, libro.getAutor());
                ps.setDouble(3, libro.getPrecio());
                ps.setInt(4, libro.getCodigo());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection con = ConexionBD.getConnection()) {
            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, id);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }
}