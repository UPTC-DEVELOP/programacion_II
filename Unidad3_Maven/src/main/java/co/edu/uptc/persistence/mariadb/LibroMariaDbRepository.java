package co.edu.uptc.persistence.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Categoria;
import co.edu.uptc.model.FormatoLibro;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.LibroFactory;
import co.edu.uptc.persistence.Conexion;
import co.edu.uptc.persistence.LibroRepository;

/**
 * Implementación MariaDB del repositorio de libros.
 *
 * Las consultas utilizan PreparedStatement para no concatenar datos externos
 * dentro de SQL y prevenir inyección SQL.
 */
public class LibroMariaDbRepository implements LibroRepository {

    private static final String SELECT_COLUMNAS =
            "SELECT isbn, titulo, autor, anio_publicacion, categoria, editorial, "
                    + "numero_paginas, precio, stock, formato, porcentaje_descuento, impuesto_iva "
                    + "FROM libro";

    @Override
    public List<Libro> listar() {
        List<Libro> libros = new ArrayList<>();
        String sql = SELECT_COLUMNAS + " ORDER BY titulo";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                libros.add(mapearLibro(resultado));
            }
            return libros;
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo listar el catálogo de libros", e);
        }
    }

    @Override
    public Optional<Libro> buscarPorIsbn(String isbn) {
        String sql = SELECT_COLUMNAS + " WHERE isbn = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, isbn);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearLibro(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo buscar el libro " + isbn, e);
        }
    }

    @Override
    public void guardar(Libro libro) {
        String sql = "INSERT INTO libro (isbn, titulo, autor, anio_publicacion, categoria, "
                + "editorial, numero_paginas, precio, stock, formato, porcentaje_descuento, impuesto_iva) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE titulo = VALUES(titulo), autor = VALUES(autor), "
                + "anio_publicacion = VALUES(anio_publicacion), categoria = VALUES(categoria), "
                + "editorial = VALUES(editorial), numero_paginas = VALUES(numero_paginas), "
                + "precio = VALUES(precio), stock = VALUES(stock), formato = VALUES(formato), "
                + "porcentaje_descuento = VALUES(porcentaje_descuento), impuesto_iva = VALUES(impuesto_iva)";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignarParametros(sentencia, libro);
            sentencia.executeUpdate();
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo guardar el libro " + libro.getIsbn(), e);
        }
    }

    @Override
    public void eliminar(String isbn) {
        String sql = "DELETE FROM libro WHERE isbn = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, isbn);
            sentencia.executeUpdate();
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo eliminar el libro " + isbn, e);
        }
    }

    @Override
    public boolean existe(String isbn) {
        String sql = "SELECT 1 FROM libro WHERE isbn = ? LIMIT 1";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, isbn);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo comprobar el libro " + isbn, e);
        }
    }

    private Connection obtenerConexion() throws SQLException {
        Connection conexion = Conexion.obtenerConexion();
        if (conexion == null) {
            throw new SQLException("Conexion.obtenerConexion() devolvió null");
        }
        return conexion;
    }

    private static void asignarParametros(PreparedStatement sentencia, Libro libro) throws SQLException {
        sentencia.setString(1, libro.getIsbn());
        sentencia.setString(2, libro.getTitulo());
        sentencia.setString(3, String.join(";", libro.getAutores()));
        sentencia.setInt(4, libro.getAnioPublicacion());
        sentencia.setString(5, libro.getCategoria().getTexto());
        sentencia.setString(6, libro.getEditorial());
        sentencia.setInt(7, libro.getNumeroPaginas());
        sentencia.setDouble(8, libro.getPrecioBase());
        sentencia.setInt(9, libro.getStock());
        sentencia.setString(10, libro.getFormato().getTexto());
        sentencia.setDouble(11, libro.getPorcentajeDescuento());
        sentencia.setDouble(12, libro.getImpuestoIVA());
    }

    private static Libro mapearLibro(ResultSet resultado) throws SQLException {
        ArrayList<String> autores = new ArrayList<>();
        String autoresTexto = resultado.getString("autor");
        if (autoresTexto != null && !autoresTexto.isBlank()) {
            for (String autor : autoresTexto.split(";")) {
                autores.add(autor.trim());
            }
        }

        return LibroFactory.crear(
                convertirFormato(resultado.getString("formato")),
                resultado.getString("isbn"),
                resultado.getString("titulo"),
                autores,
                resultado.getInt("anio_publicacion"),
                convertirCategoria(resultado.getString("categoria")),
                resultado.getString("editorial"),
                resultado.getInt("numero_paginas"),
                resultado.getDouble("precio"),
                resultado.getInt("stock"),
                resultado.getDouble("porcentaje_descuento"),
                resultado.getDouble("impuesto_iva"));
    }

    private static Categoria convertirCategoria(String texto) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.name().equalsIgnoreCase(texto)
                    || categoria.getTexto().equalsIgnoreCase(texto)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoría no reconocida: " + texto);
    }

    private static FormatoLibro convertirFormato(String texto) {
        for (FormatoLibro formato : FormatoLibro.values()) {
            if (formato.name().equalsIgnoreCase(texto)
                    || formato.getTexto().equalsIgnoreCase(texto)) {
                return formato;
            }
        }
        throw new IllegalArgumentException("Formato no reconocido: " + texto);
    }

    private static PersistenciaException error(String mensaje, Exception causa) {
        return new PersistenciaException(mensaje, causa);
    }
}
