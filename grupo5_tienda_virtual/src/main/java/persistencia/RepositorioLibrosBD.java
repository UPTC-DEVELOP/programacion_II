package persistencia;

import model.FormatoLibro;
import model.Libro;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// CLASE QUE GESTIONA LA PERSISTENCIA DE LIBROS EN LA BASE DE DATOS SQLITE

public class RepositorioLibrosBD implements IRepositorio<Libro> {

    // SE DEFINE LA RUTA DE LA BASE DE DATOS Y LA LISTA DE LIBROS EN MEMORIA
    
    private final String rutaBd;
    private final List<Libro> libros;

    // CONSTRUCTOR POR DEFECTO: SE UTILIZA LA RUTA CENTRALIZADA DE CONFIGURACIONBD
    
    public RepositorioLibrosBD() {
        this(ConfiguracionBD.RUTA_BD);
    }

    // CONSTRUCTOR PRINCIPAL: SE PREPARA LA TABLA Y SE CARGAN LOS DATOS AL INICIAR
    
    public RepositorioLibrosBD(String rutaBd) {
        this.rutaBd = rutaBd;
        this.libros = new ArrayList<>();
        ConfiguracionBD.crearCarpeta(rutaBd);
        crearTablaSiNoExiste();
        cargar();
    }

    // SE ESTABLECE LA CONEXIÓN CON LA BASE DE DATOS SQLITE
    
    private Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(ConfiguracionBD.JDBC_PREFIJO + rutaBd);
    }

    // SE CREA LA TABLA "LIBROS" EN LA BASE DE DATOS EN CASO DE QUE AÚN NO EXISTA
    
    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS libros (" +
                "isbn TEXT PRIMARY KEY, " +
                "titulo TEXT, " +
                "autor TEXT, " +
                "anio_publicacion INTEGER, " +
                "categoria TEXT, " +
                "editorial TEXT, " +
                "numero_paginas INTEGER, " +
                "precio REAL, " +
                "porcentaje_iva REAL, " +
                "cantidad_disponible INTEGER, " +
                "formato TEXT" +
                ");";

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo crear la tabla libros", e);
        }
    }

    // SE DEVUELVE LA LISTA COMPLETA DE LIBROS ALMACENADA EN MEMORIA
    
    @Override
    public List<Libro> listar() {
        return libros;
    }

    // SE AGREGA UN LIBRO A LA LISTA TEMPORAL (AÚN NO SE GUARDA EN BASE DE DATOS)
    
    @Override
    public void agregar(Libro elemento) {
        if (elemento != null && !libros.contains(elemento)) {
            libros.add(elemento);
        }
    }

    // SE ELIMINA UN LIBRO DE LA LISTA TEMPORAL
    
    @Override
    public boolean eliminar(Libro elemento) {
        return libros.remove(elemento);
    }

    // SE CONSULTA LA BASE DE DATOS Y SE LEEN TODOS LOS LIBROS ALMACENADOS
    
    @Override
    public void insertar(Libro libro) {
        if (libro == null) {
            return;
        }
        String sql = "INSERT OR REPLACE INTO libros (isbn, titulo, autor, anio_publicacion, "
                + "categoria, editorial, numero_paginas, precio, porcentaje_iva, "
                + "cantidad_disponible, formato) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());
            ps.setString(3, libro.getAutor());
            ps.setInt(4, libro.getAnioPublicacion());
            ps.setString(5, libro.getCategoria());
            ps.setString(6, libro.getEditorial());
            ps.setInt(7, libro.getNumeroPaginas());
            ps.setDouble(8, libro.getPrecio());
            ps.setDouble(9, libro.getPorcentajeIva());
            ps.setInt(10, libro.getCantidadDisponible());
            ps.setString(11, libro.getFormato() != null ? libro.getFormato().name() : null);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo insertar el libro en la BD", e);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        if (libro == null) {
            return;
        }
        String sql = "UPDATE libros SET titulo = ?, autor = ?, anio_publicacion = ?, "
                + "categoria = ?, editorial = ?, numero_paginas = ?, precio = ?, "
                + "porcentaje_iva = ?, cantidad_disponible = ?, formato = ? "
                + "WHERE isbn = ?";
        try (Connection conn = obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setInt(3, libro.getAnioPublicacion());
            ps.setString(4, libro.getCategoria());
            ps.setString(5, libro.getEditorial());
            ps.setInt(6, libro.getNumeroPaginas());
            ps.setDouble(7, libro.getPrecio());
            ps.setDouble(8, libro.getPorcentajeIva());
            ps.setInt(9, libro.getCantidadDisponible());
            ps.setString(10, libro.getFormato() != null ? libro.getFormato().name() : null);
            ps.setString(11, libro.getIsbn());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo actualizar el libro en la BD", e);
        }
    }

    @Override
    public boolean eliminarPersistente(Libro libro) {
        if (libro == null || libro.getIsbn() == null) {
            return false;
        }
        String sql = "DELETE FROM libros WHERE isbn = ?";
        try (Connection conn = obtenerConexion();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libro.getIsbn());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar el libro de la BD", e);
        }
    }

    @Override
    public void cargar() {
        libros.clear();
        String sql = "SELECT isbn, titulo, autor, anio_publicacion, categoria, editorial, "
                + "numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato "
                + "FROM libros ORDER BY rowid ASC";

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                libros.add(mapearLibro(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer los libros de la BD", e);
        }
    }

    @Override
    public List<Libro> listarUltimos(int limite) {
        if (limite <= 0) {
            return new ArrayList<Libro>();
        }
        String sql = "SELECT isbn, titulo, autor, anio_publicacion, categoria, editorial, "
                + "numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato "
                + "FROM libros ORDER BY rowid DESC LIMIT ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                List<Libro> ultimos = new ArrayList<Libro>();
                while (rs.next()) {
                    ultimos.add(mapearLibro(rs));
                }
                return ultimos;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer los ultimos libros de la BD", e);
        }
    }

    @Override
    public List<Libro> listarPagina(int inicio, int cantidad) {
        if (inicio < 0 || cantidad <= 0) {
            return new ArrayList<Libro>();
        }
        String sql = "SELECT isbn, titulo, autor, anio_publicacion, categoria, editorial, "
                + "numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato "
                + "FROM libros ORDER BY rowid ASC LIMIT ? OFFSET ?";
        try (Connection conn = obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, inicio);
            try (ResultSet rs = ps.executeQuery()) {
                List<Libro> pagina = new ArrayList<Libro>();
                while (rs.next()) {
                    pagina.add(mapearLibro(rs));
                }
                return pagina;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo leer la pagina de libros de la BD", e);
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM libros";
        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo contar los libros de la BD", e);
        }
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        String isbn = rs.getString("isbn");
        String formatoStr = rs.getString("formato");
        if (formatoStr == null || formatoStr.isBlank()) {
            throw new PersistenciaException(
                    "El libro " + isbn + " no tiene formato en la BD");
        }

        FormatoLibro formato;
        try {
            formato = FormatoLibro.valueOf(formatoStr.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException(
                    "Formato invalido para el libro " + isbn + ": " + formatoStr, e);
        }

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setAnioPublicacion(rs.getInt("anio_publicacion"));
        libro.setCategoria(rs.getString("categoria"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setNumeroPaginas(rs.getInt("numero_paginas"));
        libro.setPrecio(rs.getDouble("precio"));
        libro.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
        libro.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        libro.setFormato(formato);
        return libro;
    }

    // SE PERSISTEN DEFINITIVAMENTE TODOS LOS LIBROS EN LA BASE DE DATOS SQLITE
    
    @Override
    public void guardar() {
        String sqlDelete = "DELETE FROM libros";
        String sqlInsert = "INSERT INTO libros (isbn, titulo, autor, anio_publicacion, categoria, " +
                           "editorial, numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato) " +
                           "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = obtenerConexion()) {
            conn.setAutoCommit(false);

            // SE DESACTIVA EL AUTO-COMMIT PARA EJECUTAR LAS OPERACIONES EN LOTE

            try {
                // SE LIMPIA LA TABLA PARA INSERTAR LA INFORMACIÓN ACTUALIZADA

                try (Statement deleteStmt = conn.createStatement()) {
                    deleteStmt.executeUpdate(sqlDelete);
                }

                // SE INSERTAN TODOS LOS LIBROS EN UN SOLO LOTE (OPERACIÓN OPTIMIZADA)

                try (PreparedStatement insertStmt = conn.prepareStatement(sqlInsert)) {
                    for (Libro libro : libros) {
                        insertStmt.setString(1, libro.getIsbn());
                        insertStmt.setString(2, libro.getTitulo());
                        insertStmt.setString(3, libro.getAutor());
                        insertStmt.setInt(4, libro.getAnioPublicacion());
                        insertStmt.setString(5, libro.getCategoria());
                        insertStmt.setString(6, libro.getEditorial());
                        insertStmt.setInt(7, libro.getNumeroPaginas());
                        insertStmt.setDouble(8, libro.getPrecio());
                        insertStmt.setDouble(9, libro.getPorcentajeIva());
                        insertStmt.setInt(10, libro.getCantidadDisponible());
                        insertStmt.setString(11, libro.getFormato() != null ? libro.getFormato().name() : null);

                        insertStmt.addBatch();
                    }
                    insertStmt.executeBatch();
                }

                conn.commit();

                // SE CONFIRMAN Y APLICAN LOS CAMBIOS EN LA BASE DE DATOS

            } catch (SQLException e) {
                try {
                    conn.rollback();
                } catch (SQLException ignored) {
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron guardar los libros en la BD", e);
        }
    }
}