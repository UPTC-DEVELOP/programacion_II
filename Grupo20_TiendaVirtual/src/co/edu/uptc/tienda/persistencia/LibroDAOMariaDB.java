package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.modelo.Libro;

public class LibroDAOMariaDB implements LibroDAO {

    @Override
    public boolean registrar(Libro libro) throws LibroExcepcion {

        String sql = "INSERT INTO libros "
                + "(isbn, titulo, autor, anio_publicacion, editorial, "
                + "num_paginas, precio, iva_porcentaje, stock, formato) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
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
            throw new LibroExcepcion("No se pudo registrar el libro en la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) throws LibroExcepcion {

        String sql = "SELECT * FROM libros WHERE isbn = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirLibro(rs);
                }
            }

        } catch (SQLException e) {
            throw new LibroExcepcion("No se pudo buscar el libro en la base de datos: "
                    + ConexionBD.explicarError(e));
        }

        return null;
    }

    @Override
    public List<Libro> listar() throws LibroExcepcion {

        List<Libro> lista = new ArrayList<>();

        String sql = "SELECT * FROM libros";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(convertirLibro(rs));
            }

        } catch (SQLException e) {
            throw new LibroExcepcion("No se pudieron leer los libros de la base de datos: "
                    + ConexionBD.explicarError(e));
        }

        return lista;
    }

    @Override
    public boolean actualizar(Libro libro) throws LibroExcepcion {

        String sql = "UPDATE libros SET titulo = ?, autor = ?, "
                + "anio_publicacion = ?, editorial = ?, num_paginas = ?, "
                + "precio = ?, iva_porcentaje = ?, stock = ?, formato = ? "
                + "WHERE isbn = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
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
            throw new LibroExcepcion("No se pudo actualizar el libro en la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    @Override
    public boolean eliminar(String isbn) throws LibroExcepcion {

        String sql = "DELETE FROM libros WHERE isbn = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, isbn);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new LibroExcepcion("No se pudo eliminar el libro de la base de datos: "
                    + ConexionBD.explicarError(e));
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
