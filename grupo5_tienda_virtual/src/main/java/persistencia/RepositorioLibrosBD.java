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
    public void cargar() {
        libros.clear(); 
        
        // SE LIMPIA LA LISTA ACTUAL PARA EVITAR DUPLICACIÓN DE DATOS
        
        String sql = "SELECT isbn, titulo, autor, anio_publicacion, categoria, editorial, " +
                     "numero_paginas, precio, porcentaje_iva, cantidad_disponible, formato FROM libros";

        try (Connection conn = obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // SE RECORRE CADA FILA RECUPERADA DESDE LA BASE DE DATOS
        	
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int anioPublicacion = rs.getInt("anio_publicacion");
                String categoria = rs.getString("categoria");
                String editorial = rs.getString("editorial");
                int numeroPaginas = rs.getInt("numero_paginas");
                double precio = rs.getDouble("precio");
                double porcentajeIva = rs.getDouble("porcentaje_iva");
                int cantidadDisponible = rs.getInt("cantidad_disponible");
                String formatoStr = rs.getString("formato");

                // SE CONVIERTE EL TEXTO DEL FORMATO A SU TIPO ENUM CORRESPONDIENTE
                
                FormatoLibro formato = null;
                if (formatoStr != null && !formatoStr.isBlank()) {
                    try {
                        formato = FormatoLibro.valueOf(formatoStr);
                    } catch (IllegalArgumentException e) {
                        formato = FormatoLibro.FISICO; 
                        
                        // EN CASO DE ERROR, SE ASIGNA EL VALOR FISICO POR DEFECTO
                    }
                }

                // SE CONSTRUYE EL OBJETO LIBRO MEDIANTE SETTERS Y SE AÑADE A LA LISTA
                
                Libro libro = new Libro();
                libro.setIsbn(isbn);
                libro.setTitulo(titulo);
                libro.setAutor(autor);
                libro.setAnioPublicacion(anioPublicacion);
                libro.setCategoria(categoria);
                libro.setEditorial(editorial);
                libro.setNumeroPaginas(numeroPaginas);
                libro.setPrecio(precio);
                libro.setPorcentajeIva(porcentajeIva);
                libro.setCantidadDisponible(cantidadDisponible);
                libro.setFormato(formato);
                libros.add(libro);
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer los libros de la BD", e);
        }
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