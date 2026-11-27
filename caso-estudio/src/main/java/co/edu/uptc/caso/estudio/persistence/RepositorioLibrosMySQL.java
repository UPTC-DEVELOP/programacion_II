package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.FabricaLibros;
import co.edu.uptc.caso.estudio.model.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLibrosMySQL implements IRepositorioLibros {

    @Override
    public boolean guardarLibro(Libro l) {
        if (l == null || l.getIsbn() == null) {
            return false;
        }
        String sql = "INSERT INTO libros (titulo, autor, anio, categoria, editorial, paginas, precio_venta, stock, formato, porcentaje_iva, isbn) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            asignarParametrosComunes(ps, l);
            ps.setString(11, l.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error guardando libro en MySQL: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        String sql = "SELECT * FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearLibro(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error buscando libro por ISBN: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Libro> obtenerTodos() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando libros de MySQL: " + e.getMessage());
        }
        return libros;
    }

    @Override
    public boolean actualizarLibro(Libro l) {
        if (l == null || l.getIsbn() == null) {
            return false;
        }
        String sql = "UPDATE libros SET titulo=?, autor=?, anio=?, categoria=?, editorial=?, paginas=?, precio_venta=?, stock=?, formato=?, porcentaje_iva=? WHERE isbn=?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            asignarParametrosComunes(ps, l);
            ps.setString(11, l.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando libro en MySQL: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        if (isbn == null) {
            return false;
        }
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando libro en MySQL: " + e.getMessage());
            return false;
        }
    }

    private void asignarParametrosComunes(PreparedStatement ps, Libro l) throws SQLException {
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
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return FabricaLibros.crear(
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("anio"),
                rs.getString("categoria"),
                rs.getString("editorial"),
                rs.getInt("paginas"),
                rs.getDouble("precio_venta"),
                rs.getInt("stock"),
                rs.getString("formato"),
                rs.getDouble("porcentaje_iva")
        );
    }
}