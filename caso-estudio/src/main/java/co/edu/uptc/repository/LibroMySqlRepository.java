package co.edu.uptc.repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.config.ConexionBD;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.model.Categoria;
import co.edu.uptc.model.Formato;
import co.edu.uptc.model.Libro;

public class LibroMySqlRepository implements ILibroRepositorio {

    @Override
    public void guardar(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo_libro, autor, "
                + "fecha_publicacion, categoria, editorial, paginas, "
                + "precio_venta, cantidad_disponible, formato) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTituloLibro());
            ps.setString(3, libro.getAutor());
            ps.setInt(4, libro.getFechaPublicacion());
            ps.setString(5, libro.getCategoria().name());
            ps.setString(6, libro.getEditorial());
            ps.setInt(7, libro.getPaginas());
            ps.setDouble(8, libro.getPrecioVenta());
            ps.setInt(9, libro.getCantidadDisponible());
            ps.setString(10, libro.getFormato().name());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el libro: " + e.getMessage(), e);
        }
    }

    @Override
    public Libro buscar(String isbn) {
        String sql = "SELECT * FROM libros WHERE isbn = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return construirLibro(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el libro: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el libro: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        String sql = "UPDATE libros SET titulo_libro = ?, autor = ?, "
                + "fecha_publicacion = ?, categoria = ?, editorial = ?, "
                + "paginas = ?, precio_venta = ?, cantidad_disponible = ?, "
                + "formato = ? WHERE isbn = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTituloLibro());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getFechaPublicacion());
            ps.setString(4, libro.getCategoria().name());
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getPaginas());
            ps.setDouble(7, libro.getPrecioVenta());
            ps.setInt(8, libro.getCantidadDisponible());
            ps.setString(9, libro.getFormato().name());
            ps.setString(10, libro.getIsbn());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el libro: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Libro> listar() {
        String sql = "SELECT * FROM libros";
        List<Libro> libros = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(construirLibro(rs));
            }
            return libros;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los libros: " + e.getMessage(), e);
        }
    }

    private Libro construirLibro(ResultSet rs) throws SQLException {
        return new Libro(
            rs.getString("isbn"),
            rs.getString("titulo_libro"),
            rs.getString("autor"),
            rs.getInt("fecha_publicacion"),
            Categoria.valueOf(rs.getString("categoria")),
            rs.getString("editorial"),
            rs.getInt("paginas"),
            rs.getDouble("precio_venta"),
            rs.getInt("cantidad_disponible"),
            Formato.valueOf(rs.getString("formato")));
    }
}