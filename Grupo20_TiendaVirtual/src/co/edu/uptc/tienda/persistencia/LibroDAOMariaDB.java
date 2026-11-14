
package persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import negocio.Libro;

public class LibroDAOMariaDB implements LibroDAO {

    @Override
    public boolean registrar(Libro libro) {

        String sql = "INSERT INTO libros "
                + "(isbn, titulo, autor, anio_publicacion, editorial, "
                + "num_paginas, precio, iva_porcentaje, stock, formato) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setInt(4, libro.getAnioPublicacion());
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getNumPaginas());
            ps.setDouble(7, libro.getPrecio());
            ps.setDouble(8, libro.getIvaPorcentaje());
            ps.setInt(9, libro.getStock());
            ps.setString(10, libro.getFormato());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar libro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {

        String sql = "SELECT * FROM libros WHERE isbn = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirLibro(rs);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libro: " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Libro> listar() {

        List<Libro> lista = new ArrayList<>();

        String sql = "SELECT * FROM libros";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(convertirLibro(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }

        return lista;
    }

    @Override
    public boolean actualizar(Libro libro) {

        String sql = "UPDATE libros SET titulo = ?, autor = ?, "
                + "anio_publicacion = ?, editorial = ?, num_paginas = ?, "
                + "precio = ?, iva_porcentaje = ?, stock = ?, formato = ? "
                + "WHERE isbn = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getNumPaginas());
            ps.setDouble(6, libro.getPrecio());
            ps.setDouble(7, libro.getIvaPorcentaje());
            ps.setInt(8, libro.getStock());
            ps.setString(9, libro.getFormato());
            ps.setString(10, libro.getIsbn());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminar(String isbn) {

        String sql = "DELETE FROM libros WHERE isbn = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, isbn);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
            return false;
        }
    }

    private Libro convertirLibro(ResultSet rs) throws SQLException {

        return new Libro(
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("anio_publicacion"),
                rs.getString("editorial"),
                rs.getInt("num_paginas"),
                rs.getDouble("precio"),
                rs.getDouble("iva_porcentaje"),
                rs.getInt("stock"),
                rs.getString("formato")
        );
    }
}
