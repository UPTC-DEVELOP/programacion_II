package co.edu.uptc.persistence;

import co.edu.uptc.model.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLibrosMySQL implements IRepositorioLibros {

    @Override
    public boolean guardarLibro(Libro l) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, anio, categoria, editorial, paginas, precio, stock, formato, porcentaje_iva) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getIsbn());
            ps.setString(2, l.getTitulo());
            ps.setString(3, l.getAutor());
            ps.setInt(4, l.getAnio());
            ps.setString(5, l.getCategoria());
            ps.setString(6, l.getEditorial());
            ps.setInt(7, l.getPaginas());
            ps.setDouble(8, l.getPrecioVenta());
            ps.setInt(9, l.getStock());
            ps.setString(10, l.getFormato() != null ? l.getFormato() : "Físico");
            ps.setDouble(11, l.getPorcentajeIva());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        String sql = "SELECT * FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearLibro(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) libros.add(mapearLibro(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    @Override
    public boolean actualizarLibro(Libro l) {
        String sql = "UPDATE libros SET titulo=?, autor=?, anio=?, categoria=?, editorial=?, paginas=?, precio=?, stock=?, formato=?, porcentaje_iva=? WHERE isbn=?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setInt(3, l.getAnio());
            ps.setString(4, l.getCategoria());
            ps.setString(5, l.getEditorial());
            ps.setInt(6, l.getPaginas());
            ps.setDouble(7, l.getPrecioVenta());
            ps.setInt(8, l.getStock());
            ps.setString(9, l.getFormato() != null ? l.getFormato() : "Físico");
            ps.setDouble(10, l.getPorcentajeIva());
            ps.setString(11, l.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setIsbn(rs.getString("isbn"));
        l.setTitulo(rs.getString("titulo"));
        l.setAutor(rs.getString("autor"));
        l.setAnio(rs.getInt("anio"));
        l.setCategoria(rs.getString("categoria"));
        l.setEditorial(rs.getString("editorial"));
        l.setPaginas(rs.getInt("paginas"));
        l.setPrecioVenta(rs.getDouble("precio"));
        l.setStock(rs.getInt("stock"));
        l.setFormato(rs.getString("formato"));
        l.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
        return l;
    }
}