package co.uptc.edu.persistencia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.uptc.edu.interfaces.IGestionLibro;
import co.uptc.edu.model.Libro;

//Inventario de libros en la tabla "libros" de MariaDB
public class JdbcLibro implements IGestionLibro {

    private static final String COLUMNAS = "isbn, titulo_libro, autor_libro, anio_publicacion, categoria, "
            + "editorial, num_paginas, precio_venta, stock, tipo_formato";

    @Override
    public void guardar(Libro libro) {
        String sql = "INSERT INTO libros (" + COLUMNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, libro.getIsbn());
            sentencia.setString(2, libro.getTituloLibro());
            sentencia.setString(3, libro.getAutorLibro());
            sentencia.setString(4, libro.getAnioPublicacion());
            sentencia.setString(5, libro.getCategoria());
            sentencia.setString(6, libro.getEditorial());
            sentencia.setInt(7, libro.getNumPaginas());
            sentencia.setBigDecimal(8, BigDecimal.valueOf(libro.getPrecioVenta()));
            sentencia.setInt(9, libro.getStock());
            sentencia.setString(10, libro.getTipoFormato());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw error("registrar el libro", e);
        }
    }

    @Override
    public void actualizarStock(String isbn, int stock) {
        String sql = "UPDATE libros SET stock = ? WHERE isbn = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, stock);
            sentencia.setString(2, isbn);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw error("actualizar el stock del libro", e);
        }
    }

    @Override
    public List<Libro> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM libros ORDER BY isbn";
        List<Libro> libros = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet r = sentencia.executeQuery()) {
            while (r.next()) {
                libros.add(new Libro(r.getString("isbn"), r.getString("titulo_libro"), r.getString("autor_libro"),
                        r.getString("anio_publicacion"), r.getString("categoria"), r.getString("editorial"),
                        r.getInt("num_paginas"), r.getDouble("precio_venta"), r.getInt("stock"),
                        r.getString("tipo_formato")));
            }
        } catch (SQLException e) {
            throw error("listar los libros", e);
        }
        return libros;
    }

    private static IllegalStateException error(String accion, SQLException causa) {
        System.err.println("Error de base de datos: " + causa.getMessage());
        return new IllegalStateException("No se pudo " + accion + " en la base de datos.", causa);
    }
}
