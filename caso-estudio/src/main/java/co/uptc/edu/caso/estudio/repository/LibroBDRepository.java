package co.uptc.edu.caso.estudio.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import co.uptc.edu.caso.estudio.negocio.Libro;
import co.uptc.edu.caso.estudio.persistencia.ConexionBD;

public class LibroBDRepository implements LibroRepository {

    private Connection conexion;
    private String tabla;

    public LibroBDRepository(String tabla) {
        try {
            this.conexion = ConexionBD.conectar();
            this.tabla = tabla;
        } catch (Exception e) {
            System.out.println("Error al conectar con la base de datos: "
                    + e.getMessage());
        }
    }

    @Override
    public void guardar(Libro libro) {

        String sql = "INSERT INTO " + tabla
                + " (isbn, titulo, autores, anioPublicacion, categoria, "
                + "editorial, numeroPaginas, precioVenta, "
                + "cantidadInventario, formato) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutores());
            ps.setInt(4, libro.getAnioPublicacion());
            ps.setString(5, libro.getCategoria());
            ps.setString(6, libro.getEditorial());
            ps.setInt(7, libro.getNumeroPaginas());
            ps.setDouble(8, libro.getPrecioVenta());
            ps.setInt(9, libro.getCantidadInventario());
            ps.setString(10, libro.getFormato());

            ps.executeUpdate();

            System.out.println("Libro guardado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al guardar el libro: "
                    + e.getMessage());
        }
    }

    @Override
    public List<Libro> listar() {

        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT * FROM " + tabla;

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Libro libro = new Libro(
                        rs.getString("isbn"),
                        rs.getString("titulo"),
                        rs.getString("autores"),
                        rs.getInt("anioPublicacion"),
                        rs.getString("categoria"),
                        rs.getString("editorial"),
                        rs.getInt("numeroPaginas"),
                        rs.getDouble("precioVenta"),
                        rs.getInt("cantidadInventario"),
                        rs.getString("formato")
                );

                libros.add(libro);
            }

        } catch (Exception e) {
            System.out.println("Error al listar los libros: "
                    + e.getMessage());
        }

        return libros;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {

        String sql = "SELECT * FROM " + tabla
                + " WHERE isbn = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, isbn);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Libro(
                            rs.getString("isbn"),
                            rs.getString("titulo"),
                            rs.getString("autores"),
                            rs.getInt("anioPublicacion"),
                            rs.getString("categoria"),
                            rs.getString("editorial"),
                            rs.getInt("numeroPaginas"),
                            rs.getDouble("precioVenta"),
                            rs.getInt("cantidadInventario"),
                            rs.getString("formato")
                    );
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar el libro: "
                    + e.getMessage());
        }

        return null;
    }

    @Override
    public void actualizar(Libro libro) {

        String sql = "UPDATE " + tabla
                + " SET titulo = ?, "
                + "autores = ?, "
                + "anioPublicacion = ?, "
                + "categoria = ?, "
                + "editorial = ?, "
                + "numeroPaginas = ?, "
                + "precioVenta = ?, "
                + "cantidadInventario = ?, "
                + "formato = ? "
                + "WHERE isbn = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutores());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setString(4, libro.getCategoria());
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getNumeroPaginas());
            ps.setDouble(7, libro.getPrecioVenta());
            ps.setInt(8, libro.getCantidadInventario());
            ps.setString(9, libro.getFormato());
            ps.setString(10, libro.getIsbn());

            ps.executeUpdate();

            System.out.println("Libro actualizado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al actualizar el libro: "
                    + e.getMessage());
        }
    }

    @Override
    public void eliminar(String isbn) {

        String sql = "DELETE FROM " + tabla
                + " WHERE isbn = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, isbn);

            ps.executeUpdate();

            System.out.println("Libro eliminado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al eliminar el libro: "
                    + e.getMessage());
        }
    }
}