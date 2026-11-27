package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.FabricaLibros;
import co.edu.uptc.caso.estudio.model.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Persistencia MySQL para el catálogo e inventario de libros
public class RepositorioLibrosMySQL implements IRepositorioLibros {

    // Inserta un nuevo libro en la tabla libros
    @Override
    public boolean guardarLibro(Libro l) {
        if (l == null || esVacio(l.getIsbn())) {
            return false;
        }
        String sql = "INSERT INTO libros (titulo, autor, anio, categoria, editorial, paginas, "
                + "precio, stock, formato, porcentaje_iva, isbn) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            asignarParametrosComunes(ps, l);
            ps.setString(11, l.getIsbn().trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error guardando libro en MySQL: " + e.getMessage());
            return false;
        }
    }

    // Busca un libro por su ISBN exacto
    @Override
    public Libro buscarPorIsbn(String isbn) {
        if (esVacio(isbn)) {
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

    // Obtiene todos los libros registrados en MySQL
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

    // Actualiza un libro existente identificado por ISBN
    @Override
    public boolean actualizarLibro(Libro l) {
        if (l == null || esVacio(l.getIsbn())) {
            return false;
        }
        String sql = "UPDATE libros SET titulo=?, autor=?, anio=?, categoria=?, editorial=?, "
                + "paginas=?, precio=?, stock=?, formato=?, porcentaje_iva=? WHERE isbn=?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            asignarParametrosComunes(ps, l);
            ps.setString(11, l.getIsbn().trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando libro en MySQL: " + e.getMessage());
            return false;
        }
    }

    // Elimina un libro por ISBN
    @Override
    public boolean eliminarLibro(String isbn) {
        if (esVacio(isbn)) {
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

    // Asigna los parámetros compartidos entre INSERT y UPDATE
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

    // Construye una instancia de dominio Libro desde un ResultSet
    private Libro mapearLibro(ResultSet rs) throws SQLException {
        return FabricaLibros.crear(
                rs.getString("isbn"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getInt("anio"),
                rs.getString("categoria"),
                rs.getString("editorial"),
                rs.getInt("paginas"),
                rs.getDouble("precio"),
                rs.getInt("stock"),
                rs.getString("formato"),
                rs.getDouble("porcentaje_iva"));
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}