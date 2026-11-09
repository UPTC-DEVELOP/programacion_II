//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.modelo.Editorial;

/// Representa el repositorio que guarda las editoriales en una base de datos relacional
/// usando la tecnología JDBC.
public class RepositorioEditorialJDBC extends RepositorioJDBCBase<Editorial> implements IRepositorioEditorial {
  
  /// Comando SQL para insertar una editorial.
  private static final String SQL_INSERTAR = "INSERT INTO editorial (nombre) VALUES (?)";
  
  /// Comando SQL para actualizar una editorial.
  private static final String SQL_ACTUALIZAR = "UPDATE editorial SET nombre = ? WHERE id_editorial = ?";
  
  /// Comando SQL para eliminar una editorial.
  private static final String SQL_ELIMINAR = "DELETE FROM editorial WHERE id_editorial = ?";
  
  /// Comando SQL para hacer la lista de editoriales.
  private static final String SQL_LISTAR = "SELECT id_editorial, nombre FROM editorial ORDER BY id_editorial";
  
  /// Comando SQL para buscar una editorial por su ID.
  private static final String SQL_BUSCAR_POR_ID = "SELECT id_editorial, nombre FROM editorial WHERE id_editorial = ?";
  
  /// Comando SQL para buscar una editorial por su nombre;
  private static final String SQL_BUSCAR_POR_NOMBRE = "SELECT id_editorial, nombre FROM editorial WHERE nombre = ?";
  
  /// Crea un nuevo repositorio de editoriales conectado a una base de datos relacional.
  /// 
  /// @param conexionBaseDatos Objeto que mantiene la conexión con la base de datos.
  public RepositorioEditorialJDBC(ConexionBaseDatos conexionBaseDatos) {
    super(conexionBaseDatos);
  }
  
  @Override
  protected PreparedStatement crearComandoSQLInsertar(Connection conexion, Editorial editorial)
    throws SQLException {
    // crear el comando SQL INSERT pidiendo devolver el ID generado
    PreparedStatement comando = conexion
      .prepareStatement(RepositorioEditorialJDBC.SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
    // asignar cada parámetro `?` del SQL
    comando.setString(1, editorial.getNombre());
    
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLActualizar(Connection conexion, Editorial editorial) throws SQLException {
    // crear el comando SQL UPDATE
    PreparedStatement comando = conexion.prepareStatement(RepositorioEditorialJDBC.SQL_ACTUALIZAR);
    // asignar cada parámetro `?` del SQL
    comando.setString(1, editorial.getNombre());
    comando.setLong(2, editorial.getId());
    
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLEliminar(Connection conexion, Long id) throws SQLException {
    // crear el comando SQL DELETE
    PreparedStatement comando = conexion.prepareStatement(RepositorioEditorialJDBC.SQL_ELIMINAR);
    
    // asignar el parámetro `?`del SQL
    comando.setLong(1, id);
    
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLBuscarPorId(Connection conexion, Long id) throws SQLException {
    // crear el comando SQL SELECT
    PreparedStatement comando = conexion.prepareStatement(RepositorioEditorialJDBC.SQL_BUSCAR_POR_ID);
    
    // asignar el parámetro `?`del SQL
    comando.setLong(1, id);
    
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLListar(Connection conexion) throws SQLException {
    // crear el comando SQL SELECT
    PreparedStatement comando = conexion.prepareStatement(RepositorioEditorialJDBC.SQL_LISTAR);
    
    return comando;
  }
  
  @Override
  protected Editorial asignarId(Editorial editorial, Long id) {
    return new Editorial(
      id,
      editorial.getNombre()
    );
  }
  
  @Override
  protected Editorial convertirFila(ResultSet fila) throws SQLException {
    return new Editorial(
      fila.getLong("id_editorial"),
      fila.getString("nombre")
    );
  }
  
  @Override
  public List<Editorial> buscarPorNombre(String nombre) {
    // sin nombre no hay resultados
    if (nombre == null) {
      return new ArrayList<>();
    }
    
    // intentar ejecutar el comando de buscar
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion.prepareStatement(SQL_BUSCAR_POR_NOMBRE)) {
      comando.setString(1, nombre.trim());
      return convertirFilas(comando);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar por nombre", excepcion);
    }
  }
  
}
