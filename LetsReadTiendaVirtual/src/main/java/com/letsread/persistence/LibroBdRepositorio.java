package com.letsread.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.letsread.model.Libro;

/**
 * Guarda los libros en la tabla libro de MariaDB usando JDBC.
 */
public class LibroBdRepositorio implements LibroRepositorio {

    private static final String SELECT_TODOS =
        "SELECT isbn, titulo, autor, anio_publicacion, categoria, editorial, "
        + "numero_paginas, precio_venta, porcentaje_iva, cantidad_inventario, "
        + "formato, tiene_ventas FROM libro";

    private static final String INSERT =
        "INSERT INTO libro (isbn, titulo, autor, anio_publicacion, categoria, editorial, "
        + "numero_paginas, precio_venta, porcentaje_iva, cantidad_inventario, "
        + "formato, tiene_ventas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
        "UPDATE libro SET titulo = ?, autor = ?, anio_publicacion = ?, categoria = ?, "
        + "editorial = ?, numero_paginas = ?, precio_venta = ?, porcentaje_iva = ?, "
        + "cantidad_inventario = ?, formato = ?, tiene_ventas = ? WHERE isbn = ?";

    private static final String DELETE = "DELETE FROM libro WHERE isbn = ?";

    @Override
    public List<Libro> listar() {
        List<Libro> libros = new ArrayList<>();
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                libros.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar libros: " + e.getMessage(), e);
        }
        return libros;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT_TODOS + " WHERE isbn = ?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar el libro " + isbn + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void guardar(Libro libro) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setInt(4, libro.getAnioPublicacion());
            ps.setString(5, libro.getCategoria());
            ps.setString(6, libro.getEditorial());
            ps.setInt(7, libro.getNumeroPaginas());
            ps.setDouble(8, libro.getPrecioVenta());
            ps.setDouble(9, libro.getPorcentajeIva());
            ps.setInt(10, libro.getCantidadInventario());
            ps.setString(11, libro.getFormato());
            ps.setBoolean(12, libro.isTieneVentas());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el libro " + libro.getIsbn() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setString(4, libro.getCategoria());
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getNumeroPaginas());
            ps.setDouble(7, libro.getPrecioVenta());
            ps.setDouble(8, libro.getPorcentajeIva());
            ps.setInt(9, libro.getCantidadInventario());
            ps.setString(10, libro.getFormato());
            ps.setBoolean(11, libro.isTieneVentas());
            ps.setString(12, libro.getIsbn());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe un libro con ISBN " + libro.getIsbn());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el libro " + libro.getIsbn() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String isbn) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setString(1, isbn);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el libro " + isbn + ": " + e.getMessage(), e);
        }
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        Libro libro = new Libro(
            rs.getString("isbn"),
            rs.getString("titulo"),
            rs.getString("autor"),
            rs.getInt("anio_publicacion"),
            rs.getString("categoria"),
            rs.getString("editorial"),
            rs.getInt("numero_paginas"),
            rs.getDouble("precio_venta"),
            rs.getDouble("porcentaje_iva"),
            rs.getInt("cantidad_inventario"),
            rs.getString("formato"));
        libro.setTieneVentas(rs.getBoolean("tiene_ventas"));
        return libro;
    }
}