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
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroBuilder;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa el repositorio que guarda los libros en una base de datos relacional usando
/// la tecnología JDBC.
/// 
/// Al leer desde la base de datos, todo libro construido por este repositorio sale con su
/// categoría y su editorial completas.
/// 
/// El método [RepositorioJDBCBase#convertirFilas(PreparedStatement)] se asegura de
/// convertir las filas [ResultSet] en libros, y de que luego cada libro carge sus objetos
/// dependientes categoría y editorial.
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
  
  /// Comando SQL para saber si exite algún libro de una editorial. Devuelve una sola fila
  /// con 1 si existe, o con 0 si no existe.
  private static final String SQL_EXISTE_LIBRO_POR_EDITORIAL = "SELECT EXISTS (SELECT 1 FROM libro WHERE id_editorial = ?)";
  
  /// Comando SQL para saber si existe algún libro de una categoría. Devuelve una sola fila
  /// con 1 si existe, o con 0 si no existe.
  private static final String SQL_EXISTE_LIBRO_POR_CATEGORIA = "SELECT EXISTS (SELECT 1 FROM libro WHERE id_categoria = ?)";
  
  /// Repositorio que se usa para cargar la categoría de cada libro.
  private final RepositorioCategoriaJDBC repositorioCategoria;
  
  /// Repositorio que se usa para cargar la editorial de cada libro.
  private final RepositorioEditorialJDBC repositorioEditorial;
  
  /// Crea un nuevo repositorio de libros conectado a una base de datos relacional.
  /// 
  /// Utiliza repositorios JDBC para compartir la conexión que lee los libros, y permitir
  /// leer y asociar la categoría y la editorial de cada libro.
  ///
  /// @param conexionBaseDatos    Objeto que mantiene la conexión con la base de datos.
  /// @param repositorioCategoria Repositorio JDBC de las categorías.
  /// @param repositorioEditorial Repositorio JDBC de las editoriales.
  public RepositorioLibroJDBC(
    ConexionBaseDatos conexionBaseDatos,
    RepositorioCategoriaJDBC repositorioCategoria,
    RepositorioEditorialJDBC repositorioEditorial
  ) {
    super(conexionBaseDatos);
    this.repositorioCategoria = repositorioCategoria;
    this.repositorioEditorial = repositorioEditorial;
  }
  
  /// Asigna los atributos del libro en el comando SQL. Aplica el mismo orden para los
  /// comandos `INSERT` y `UPDATE`.
  private void asignarAtributos(PreparedStatement comando, Libro libro) throws SQLException {
    // asignar cada parámetro del comando SQL
    comando.setString(1, libro.getIsbn());
    comando.setString(2, libro.getTitulo());
    comando.setInt(3, libro.getAnioPublicacion());
    comando.setString(4, libro.getImagenLibro());
    // recordemos que la tabla libro solo almacena las claves foráneas y, por eso se necesitan
    // los IDs de categoría y editorial
    ValoresSQL.escribirLongNulo(comando, 5, obtenerIdCategoriaDe(libro));
    ValoresSQL.escribirLongNulo(comando, 6, obtenerIdEditorialDe(libro));
  }
  
  /// Obtiene el ID de la categoría del libro.
  /// 
  /// @param libro Libro del que quiere obtener el ID de la categoría.
  /// @return El ID de la categoría del libro. O, `null` si el libro no tiene una
  ///         categoría asociada.
  private Long obtenerIdCategoriaDe(Libro libro) {
    // o sea, se obtiene el ID de la categoría, pero si la categoría no es nula
    if (libro.getCategoria() == null) {
      return null;
    } else {
      return libro.getCategoria()
        .getId();
    }
  }
  
  /// Obtiene el ID de la editorial del libro.
  /// 
  /// @param libro Libro del que quiere obtener el ID de la editorial.
  /// @return El ID de la editorial del libro. O, `null` si el libro no tiene una
  ///         editorial asociada.
  private Long obtenerIdEditorialDe(Libro libro) {
    // o sea, se obtiene el ID de la editorial, pero si la editorial no es nula
    if (libro.getEditorial() == null) {
      return null;
    } else {
      return libro.getEditorial()
        .getId();
    }
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
      .categoria(libro.getCategoria())
      .editorial(libro.getEditorial())
      .listaAutoria(libro.getListaAutoria())
      .imagenLibro(libro.getImagenLibro())
      .construir();
  }
  
  @Override
  protected Libro convertirFila(ResultSet fila) throws SQLException {
    // se crean objetos categoría y editorial con solo su ID quedando como objetos
    // provisionales. El método cargarElementosDependientes() cargará
    // completamente los objetos categoría y editorial
    Categoria categoria = new Categoria(fila.getLong("id_categoria"), null, null, null);
    Editorial editorial = new Editorial(fila.getLong("id_editorial"), null);
    
    // convertir la fila SQL a un objeto libro
    return new LibroBuilder()
      .id(fila.getLong("id_libro"))
      .isbn(fila.getString("isbn"))
      .titulo(fila.getString("titulo"))
      .anioPublicacion(fila.getInt("anio_publicacion"))
      .categoria(categoria)
      .editorial(editorial)
      .listaAutoria(new ArrayList<>())
      .imagenLibro(fila.getString("imagen_libro"))
      .construir();
  }
  
  @Override
  protected Libro cargarElementosDependientes(Connection conexion, Libro libro) throws SQLException {
    // aquí, se construyen los componentes u objetos asociados de un libro.
    // en el método convertirFilas() se crearon objetos categoría y editorial provisionales
    // que solo tienen el ID respectivo.
    // ahora, usando la misma conexión que lee el libro, se leerán y se construirán los
    // objetos categoría y editorial
    
    Long idCategoria = libro.getCategoria()
      .getId();
    Optional<Categoria> categoria = this.repositorioCategoria.buscarPorId(conexion, idCategoria);
    
    Long idEditorial = libro.getEditorial()
      .getId();
    Optional<Editorial> editorial = this.repositorioEditorial.buscarPorId(conexion, idEditorial);
    
    // en la base de datos, la clave foránea garantiza que el libro tenga una categoría y una
    // editorial. Si la consulta de categoría o editorial produjeron objetos vacíos, significa
    // que la base de datos está inconsistente.
    // Ni el programa ni el usuario pueden corregir esta inconsistencia, y por eso se lanza un
    // error técnico
    
    if (categoria.isEmpty()) {
      throw new ErrorTecnicoSistemaException(
        String.format(
          "El libro %d referencia la categoría %d, que no existe en la base de datos.",
          libro.getId(),
          idCategoria
        ), null
      );
    }
    
    if (editorial.isEmpty()) {
      throw new ErrorTecnicoSistemaException(
        String.format(
          "El libro %d referencia la editorial %d, que no existe en la base de datos.",
          libro.getId(),
          idEditorial
        ), null
      );
    }
    
    // aquí, construir la categoría y la editorial fue exitoso, y ahora se asocian al libro
    // que se devolverá
    libro.setCategoria(categoria.get());
    libro.setEditorial(editorial.get());
    return libro;
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
  
  @Override
  public boolean existeLibroConEditorial(Editorial editorial) {
    // no se puede buscar una editorial nula
    if (editorial == null) {
      throw new IllegalArgumentException("Error: La editorial no puede ser nula.");
    }
    
    // una editorial sin ID no se ha guardado y, por tanto, no puede tener libros
    if (editorial.getId() == null) {
      return false;
    }
    
    // hacer la consulta a la base de datos
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion
        .prepareStatement(RepositorioLibroJDBC.SQL_EXISTE_LIBRO_POR_EDITORIAL)) {
      comando.setLong(1, editorial.getId());
      
      try (ResultSet resultado = comando.executeQuery()) {
        // SELECT EXISTS siempre devuelve una fila: 1 (existe) o 0 (no existe)
        return resultado.next() && resultado.getBoolean(1);
      }
    } catch (SQLException excepcion) {
      throw traducirExcepcion("verificar si la editorial tiene libros", excepcion);
    }
  }
  
  @Override
  public boolean existeLibroConCategoria(Categoria categoria) {
    // no se puede buscar una categoría nula
    if (categoria == null) {
      throw new IllegalArgumentException("Error: La categoría no puede ser nula.");
    }
    
    // una categoría sin ID no se ha guardado y, por tanto, no puede tener libros
    if (categoria.getId() == null) {
      return false;
    }
    
    // hacer la consulta a la base de datos
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion
        .prepareStatement(RepositorioLibroJDBC.SQL_EXISTE_LIBRO_POR_CATEGORIA)) {
      comando.setLong(1, categoria.getId());
      
      try (ResultSet resultado = comando.executeQuery()) {
        // SELECT EXISTS siempre devuelve una fila: 1 (existe) o 0 (no existe)
        return resultado.next() && resultado.getBoolean(1);
      }
    } catch (SQLException excepcion) {
      throw traducirExcepcion("verificar si la categoría tiene libros", excepcion);
    }
  }
}
