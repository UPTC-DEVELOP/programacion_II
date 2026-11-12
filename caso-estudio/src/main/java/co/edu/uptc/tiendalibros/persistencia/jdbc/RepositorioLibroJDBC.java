//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroBuilder;

/// Representa el repositorio que guarda los libros en una base de datos relacional usando
/// la tecnología JDBC.
///
/// Por ahora no guarda las asociaciones autor-libro (`Libro.listaAutoria`): al cargar un
/// libro su lista de autoría queda vacía.
public class RepositorioLibroJDBC extends RepositorioJDBCBase<Libro> implements IRepositorioLibro {
  
  /// Columnas de la tabla libro como parte de la cláusula SELECT.
  private static final String COLUMNAS_SELECT = "id_libro, isbn, titulo, anio_publicacion, "
    + "imagen_libro, id_categoria, id_editorial";
  
  /// Comando SQL para insertar un libro.
  private static final String SQL_INSERTAR = "INSERT INTO libro "
    + "(isbn, titulo, anio_publicacion, imagen_libro, id_categoria, id_editorial) "
    + "VALUES (?, ?, ?, ?, ?, ?)";
  
  /// Comando SQL para actualizar un libro.
  private static final String SQL_ACTUALIZAR = "UPDATE libro SET isbn = ?, titulo = ?, anio_publicacion = ?, "
    + "imagen_libro = ?, id_categoria = ?, id_editorial = ? WHERE id_libro = ?";
  
  /// Comando SQL para eliminar un libro.
  private static final String SQL_ELIMINAR = "DELETE FROM libro WHERE id_libro = ?";
  
  /// Comando SQL para hacer la lista de libros.
  private static final String SQL_LISTAR = "SELECT " + COLUMNAS_SELECT + " FROM libro ORDER BY id_libro";
  
  /// Comando SQL para buscar un libro por su ID.
  private static final String SQL_BUSCAR_POR_ID = "SELECT " + COLUMNAS_SELECT + " FROM libro WHERE id_libro = ?";
  
  /// Comando SQL para buscar un libro por su ISBN.
  private static final String SQL_BUSCAR_POR_ISBN = "SELECT " + COLUMNAS_SELECT + " FROM libro WHERE isbn = ?";
  
  /// Comando SQL para buscar libros cuyo título contiene un fragmento de texto.
  private static final String SQL_BUSCAR_POR_TITULO = "SELECT " + COLUMNAS_SELECT
    + " FROM libro WHERE titulo LIKE ? ORDER BY titulo";
  
  /// Crea un nuevo repositorio de libros conectado a una base de datos relacional.
  ///
  /// @param conexionBaseDatos Objeto que mantiene la conexión con la base de datos.
  public RepositorioLibroJDBC(ConexionBaseDatos conexionBaseDatos) {
    super(conexionBaseDatos);
  }
  
  /// Asigna los atributos del libro en el comando SQL. Aplica el mismo orden para los
  /// comandos `INSERT` y `UPDATE`.
  private void asignarAtributos(PreparedStatement comando, Libro libro) throws SQLException {
    // asignar cada parámetro del comando SQL
    comando.setString(1, libro.getIsbn());
    comando.setString(2, libro.getTitulo());
    comando.setInt(3, libro.getAnioPublicacion());
    comando.setString(4, libro.getImagenLibro());
    ValoresSQL.escribirLongNulo(comando, 5, libro.getIdCategoria());
    ValoresSQL.escribirLongNulo(comando, 6, libro.getIdEditorial());
  }
  
  @Override
  protected PreparedStatement crearComandoSQLInsertar(Connection conexion, Libro libro) throws SQLException {
    // crear el comando SQL INSERT pidiendo devolver el ID generado
    PreparedStatement comando = conexion
      .prepareStatement(RepositorioLibroJDBC.SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
    asignarAtributos(comando, libro);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLActualizar(Connection conexion, Libro libro) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(RepositorioLibroJDBC.SQL_ACTUALIZAR);
    asignarAtributos(comando, libro);
    // el parámetro 7 es el ID de la cláusula WHERE
    comando.setLong(7, libro.getId());
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLEliminar(Connection conexion, Long id) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(RepositorioLibroJDBC.SQL_ELIMINAR);
    comando.setLong(1, id);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLBuscarPorId(Connection conexion, Long id) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(RepositorioLibroJDBC.SQL_BUSCAR_POR_ID);
    comando.setLong(1, id);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLListar(Connection conexion) throws SQLException {
    return conexion.prepareStatement(RepositorioLibroJDBC.SQL_LISTAR);
  }
  
  @Override
  protected Libro asignarId(Libro libro, Long id) {
    // crear un nuevo libro con el ID
    return new LibroBuilder()
      .id(id)
      .isbn(libro.getIsbn())
      .titulo(libro.getTitulo())
      .anioPublicacion(libro.getAnioPublicacion())
      .idCategoria(libro.getIdCategoria())
      .idEditorial(libro.getIdEditorial())
      .listaAutoria(libro.getListaAutoria())
      .imagenLibro(libro.getImagenLibro())
      .construir();
  }
  
  @Override
  protected Libro convertirFila(ResultSet fila) throws SQLException {
    // convertir la fila SQL a un objeto libro
    return new LibroBuilder()
      .id(fila.getLong("id_libro"))
      .isbn(fila.getString("isbn"))
      .titulo(fila.getString("titulo"))
      .anioPublicacion(fila.getInt("anio_publicacion"))
      .idCategoria(ValoresSQL.leerLongNulo(fila, "id_categoria"))
      .idEditorial(ValoresSQL.leerLongNulo(fila, "id_editorial"))
      .listaAutoria(new ArrayList<>())
      .imagenLibro(fila.getString("imagen_libro"))
      .construir();
  }
  
  @Override
  public Optional<Libro> buscarPorIsbn(String isbn) {
    // sin ISBN no hay resultados
    if (isbn == null || isbn.isBlank()) {
      return Optional.empty();
    }
    
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion.prepareStatement(RepositorioLibroJDBC.SQL_BUSCAR_POR_ISBN)) {
      comando.setString(1, isbn.trim());
      List<Libro> encontrados = convertirFilas(comando);
      // el ISBN es único: se supone que solo hay un libro con ese ISBN
      return encontrados.isEmpty() ? Optional.empty() : Optional.of(encontrados.get(0));
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar por ISBN", excepcion);
    }
  }
  
  @Override
  public List<Libro> buscarPorTitulo(String titulo) {
    // sin título no hay resultados
    if (titulo == null || titulo.isBlank()) {
      return new ArrayList<>();
    }
    
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion.prepareStatement(RepositorioLibroJDBC.SQL_BUSCAR_POR_TITULO)) {
      // búsqueda aproximada LIKE : el título contiene el fragmento. La comparación ignora
      // mayúsculas y tildes por la collation de la base de datos.
      comando.setString(1, "%" + titulo.trim() + "%");
      return convertirFilas(comando);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar por título", excepcion);
    }
  }
  
}
