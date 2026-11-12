//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioFormatoLibro;
import co.edu.uptc.tiendalibros.modelo.EstadoEnCatalogo;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.FormatoLibroBuilder;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.modelo.TipoIVA;

/// Representa el repositorio que guarda los formatos de libros en una base de datos
/// relacional usando la tecnología JDBC.
public class RepositorioFormatoLibroJDBC extends RepositorioJDBCBase<FormatoLibro> implements IRepositorioFormatoLibro {
  
  /// Columnas de la tabla formato_libro como parte de la cláusula SELECT.
  private static final String COLUMNAS = "id_formato_libro, id_libro, tipo_formato, precio_base, tipo_iva, "
    + "inventario, cantidad_paginas, estado_en_catalogo";
  
  /// Comando SQL para insertar un formato.
  private static final String SQL_INSERTAR = "INSERT INTO formato_libro "
    + "(id_libro, tipo_formato, precio_base, tipo_iva, inventario, cantidad_paginas, estado_en_catalogo) "
    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
  
  /// Comando SQL para actualizar un formato.
  private static final String SQL_ACTUALIZAR = "UPDATE formato_libro SET id_libro = ?, tipo_formato = ?, "
    + "precio_base = ?, tipo_iva = ?, inventario = ?, cantidad_paginas = ?, estado_en_catalogo = ? "
    + "WHERE id_formato_libro = ?";
  
  /// Comando SQL para eliminar un formato.
  private static final String SQL_ELIMINAR = "DELETE FROM formato_libro WHERE id_formato_libro = ?";
  
  /// Comando SQL para hacer la lista de formatos.
  private static final String SQL_LISTAR = "SELECT " + COLUMNAS + " FROM formato_libro ORDER BY id_formato_libro";
  
  /// Comando SQL para buscar un formato por su ID.
  private static final String SQL_BUSCAR_POR_ID = "SELECT " + COLUMNAS
    + " FROM formato_libro WHERE id_formato_libro = ?";
  
  /// Comando SQL para buscar los formatos de un libro.
  private static final String SQL_BUSCAR_POR_ID_LIBRO = "SELECT " + COLUMNAS
    + " FROM formato_libro WHERE id_libro = ? ORDER BY id_formato_libro";
  
  /// Comando SQL para eliminar todos los formatos de un libro.
  private static final String SQL_ELIMINAR_POR_ID_LIBRO = "DELETE FROM formato_libro WHERE id_libro = ?";
  
  /// Comando SQL para eliminar el formato de un tipo específico de un libro.
  private static final String SQL_ELIMINAR_POR_ID_LIBRO_Y_TIPO = "DELETE FROM formato_libro "
    + "WHERE id_libro = ? AND tipo_formato = ?";
  
  /// Crea un nuevo repositorio de formatos de libros conectado a una base de datos
  /// relacional.
  ///
  /// @param conexionBaseDatos Objeto que mantiene la conexión con la base de datos.
  public RepositorioFormatoLibroJDBC(ConexionBaseDatos conexionBaseDatos) {
    super(conexionBaseDatos);
  }
  
  /// Asigna los atributos del formato del libro en el comando SQL. Aplica el mismo orden
  /// para los comandos `INSERT` y `UPDATE`.
  private void asignarAtributos(PreparedStatement comando, FormatoLibro formato) throws SQLException {
    ValoresSQL.escribirLongNulo(comando, 1, formato.getIdLibro());
    comando.setString(
      2,
      formato.getTipoFormato()
        .getAbreviatura()
    );
    ValoresSQL.escribirDinero(comando, 3, formato.getPrecioBase());
    comando.setString(
      4,
      formato.getTipoIVA()
        .getAbreviatura()
    );
    comando.setInt(5, formato.getInventario());
    comando.setInt(6, formato.getCantidadPaginas());
    comando.setString(
      7,
      formato.getEstadoEnCatalogo()
        .getAbreviatura()
    );
  }
  
  @Override
  protected PreparedStatement crearComandoSQLInsertar(Connection conexion, FormatoLibro formato)
    throws SQLException {
    PreparedStatement comando = conexion
      .prepareStatement(RepositorioFormatoLibroJDBC.SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
    asignarAtributos(comando, formato);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLActualizar(Connection conexion, FormatoLibro formato)
    throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(RepositorioFormatoLibroJDBC.SQL_ACTUALIZAR);
    asignarAtributos(comando, formato);
    // el parámetro 8 es el ID de la cláusula WHERE
    comando.setLong(8, formato.getId());
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLEliminar(Connection conexion, Long id) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(RepositorioFormatoLibroJDBC.SQL_ELIMINAR);
    comando.setLong(1, id);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLBuscarPorId(Connection conexion, Long id) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(RepositorioFormatoLibroJDBC.SQL_BUSCAR_POR_ID);
    comando.setLong(1, id);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLListar(Connection conexion) throws SQLException {
    return conexion.prepareStatement(RepositorioFormatoLibroJDBC.SQL_LISTAR);
  }
  
  @Override
  protected FormatoLibro asignarId(FormatoLibro formato, Long id) {
    // crear un formato libro con el ID
    return new FormatoLibroBuilder()
      .id(id)
      .idLibro(formato.getIdLibro())
      .tipoFormatoLibro(formato.getTipoFormato())
      .precioBase(formato.getPrecioBase())
      .tipoIVA(formato.getTipoIVA())
      .inventario(formato.getInventario())
      .cantidadPaginas(formato.getCantidadPaginas())
      .estadoEnCatalogo(formato.getEstadoEnCatalogo())
      .construir();
  }
  
  @Override
  protected FormatoLibro convertirFila(ResultSet fila) throws SQLException {
    // convertir la fila SQL a un objeto formato libro
    return new FormatoLibroBuilder()
      .id(fila.getLong("id_formato_libro"))
      .idLibro(fila.getLong("id_libro"))
      .tipoFormatoLibro(TipoFormatoLibro.convertirAbreviatura(fila.getString("tipo_formato")))
      .precioBase(ValoresSQL.leerDinero(fila, "precio_base"))
      .tipoIVA(TipoIVA.convertirAbreviatura(fila.getString("tipo_iva")))
      .inventario(fila.getInt("inventario"))
      .cantidadPaginas(fila.getInt("cantidad_paginas"))
      .estadoEnCatalogo(EstadoEnCatalogo.convertirAbreviatura(fila.getString("estado_en_catalogo")))
      .construir();
  }
  
  @Override
  public List<FormatoLibro> buscarPorIdLibro(Long idLibro) {
    // sin ID no hay resultados
    if (idLibro == null) {
      return new ArrayList<>();
    }
    
    try (Connection conexion = abrirConexion()) {
      // invocar a buscar por id usando la conexión
      return buscarPorIdLibro(conexion, idLibro);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar por ID del libro", excepcion);
    }
  }
  
  /// Busca los formatos de un libro usando una conexión que ya está abierta. Sirve para
  /// consultar dentro de la misma conexión (o transacción) de otro repositorio.
  ///
  /// @param conexion Conexión abierta con la base de datos.
  /// @param idLibro  ID del libro. No es nulo.
  /// @return Lista con los formatos del libro. Vacía si no tiene.
  /// @throws SQLException Si ocurre un error técnico al consultar.
  public List<FormatoLibro> buscarPorIdLibro(Connection conexion, Long idLibro) throws SQLException {
    try (PreparedStatement comando = conexion.prepareStatement(RepositorioFormatoLibroJDBC.SQL_BUSCAR_POR_ID_LIBRO)) {
      comando.setLong(1, idLibro);
      return convertirFilas(comando);
    }
  }
  
  /// Elimina todos los formatos de un libro usando una conexión que ya está abierta.
  ///
  /// @param conexion Conexión abierta con la base de datos.
  /// @param idLibro  ID del libro. No es nulo.
  /// @throws SQLException Si ocurre un error técnico al eliminar.
  void eliminarPorIdLibro(Connection conexion, Long idLibro) throws SQLException {
    try (PreparedStatement comando = conexion.prepareStatement(RepositorioFormatoLibroJDBC.SQL_ELIMINAR_POR_ID_LIBRO)) {
      comando.setLong(1, idLibro);
      comando.executeUpdate();
    }
  }
  
  /// Elimina el formato de un tipo específico de un libro usando una conexión que ya está
  /// abierta. Si el libro no tiene ese formato, no hace nada.
  /// 
  /// @param conexion    Conexión abierta con la base de datos.
  /// @param idLibro     ID del libro. No es nulo.
  /// @param tipoFormato Tipo del formato que se quiere eliminar. No es nulo.
  /// @throws SQLException Si ocurre un error técnico al eliminar.
  void eliminarPorIdLibroYTipo(Connection conexion, Long idLibro, TipoFormatoLibro tipoFormato) throws SQLException {
    try (PreparedStatement comando = conexion
      .prepareStatement(RepositorioFormatoLibroJDBC.SQL_ELIMINAR_POR_ID_LIBRO_Y_TIPO)) {
      comando.setLong(1, idLibro);
      comando.setString(2, tipoFormato.getAbreviatura());
      comando.executeUpdate();
    }
  }
}
