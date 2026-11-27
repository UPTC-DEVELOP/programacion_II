package com.tiendavirtual.repositorio;

import com.tiendavirtual.modelo.Libro;
import com.tiendavirtual.util.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroRepositoryMySQL implements LibroRepository {

    @Override
    public void guardar(Libro l) {
        String sql = "INSERT INTO libros "
                + "(isbn, titulo, autor, anio, categoria, editorial, paginas, precio, stock, formato, iva) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getIsbn());
            ps.setString(2, l.getTitulo());
            ps.setString(3, l.getAutor());
            ps.setInt(4, l.getAnio());
            ps.setString(5, l.getCategoria());
            ps.setString(6, l.getEditorial());
            ps.setInt(7, l.getPaginas());
            ps.setDouble(8, l.getPrecio());
            ps.setInt(9, l.getStock());
            ps.setString(10, l.getFormato());
            ps.setDouble(11, l.getIva());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar libro: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Libro l) {
        String sql = "UPDATE libros SET titulo=?, autor=?, anio=?, categoria=?, "
                + "editorial=?, paginas=?, precio=?, stock=?, formato=?, iva=? WHERE isbn=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, l.getTitulo());
            ps.setString(2, l.getAutor());
            ps.setInt(3, l.getAnio());
            ps.setString(4, l.getCategoria());
            ps.setString(5, l.getEditorial());
            ps.setInt(6, l.getPaginas());
            ps.setDouble(7, l.getPrecio());
            ps.setInt(8, l.getStock());
            ps.setString(9, l.getFormato());
            ps.setDouble(10, l.getIva());
            ps.setString(11, l.getIsbn());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar libro: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar libro: " + e.getMessage(), e);
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        String sql = "SELECT l.*, EXISTS (SELECT 1 FROM ventas v WHERE v.isbn = l.isbn) AS tiene_ventas "
                + "FROM libros l WHERE l.isbn=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirLibro(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libro: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Libro> listar() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, EXISTS (SELECT 1 FROM ventas v WHERE v.isbn = l.isbn) AS tiene_ventas "
                + "FROM libros l";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                libros.add(convertirLibro(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar libros: " + e.getMessage(), e);
        }

        return libros;
    }

    private Libro convertirLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro(
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
                rs.getDouble("iva")
        );
        libro.setTieneVentasAsociadas(rs.getBoolean("tiene_ventas"));
        return libro;
    }
}