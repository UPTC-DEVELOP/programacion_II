
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCliente;
import co.edu.uptc.tiendalibros.modelo.Cliente;

/// Implementa el repositorio JDBC encargado de almacenar y consultar clientes.
public class RepositorioClienteJDBC extends RepositorioJDBCBase<Cliente>
    implements IRepositorioCliente {

  /// Crea un repositorio de clientes utilizando la conexión indicada.
  ///
  /// @param conexionBaseDatos Conexión con la base de datos.
  public RepositorioClienteJDBC(ConexionBaseDatos conexionBaseDatos) {
    super(conexionBaseDatos);
  }

  /// Prepara el comando SQL para insertar un cliente.
  ///
  /// @param conexion Conexión con la base de datos.
  /// @param cliente Cliente que se desea insertar.
  /// @return Comando SQL preparado.
  /// @throws SQLException Si ocurre un error al preparar el comando.
  @Override
  protected PreparedStatement crearComandoSQLInsertar(
      Connection conexion, Cliente cliente) throws SQLException {

    String sql = "INSERT INTO cliente "
        + "(tipo_documento, numero_documento, nombres, apellidos, "
        + "correo_electronico, telefono, direccion_envio, tipo_cliente) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    PreparedStatement comando = conexion.prepareStatement(
        sql, Statement.RETURN_GENERATED_KEYS);

    comando.setString(1, cliente.getTipoDocumento());
    comando.setString(2, cliente.getNumeroDocumento());
    comando.setString(3, cliente.getNombres());
    comando.setString(4, cliente.getApellidos());
    comando.setString(5, cliente.getCorreoElectronico());
    comando.setString(6, cliente.getTelefono());
    comando.setString(7, cliente.getDireccionEnvio());
    comando.setString(8, cliente.getTipoCliente());

    return comando;
  }

  /// Prepara el comando SQL para actualizar los datos de un cliente.
  ///
  /// @param conexion Conexión con la base de datos.
  /// @param cliente Cliente que se desea actualizar.
  /// @return Comando SQL preparado.
  /// @throws SQLException Si ocurre un error al preparar el comando.
  @Override
  protected PreparedStatement crearComandoSQLActualizar(
      Connection conexion, Cliente cliente) throws SQLException {

    // se actualiza el correo electrónico en la tabla usuario
    String sql = "UPDATE cliente c JOIN usuario u ON u.id_usuario = c.id_usuario SET "
      + "c.tipo_documento = ?, c.numero_documento = ?, c.nombres = ?, "
      + "c.apellidos = ?, u.correo_electronico = ?, c.telefono = ?, "
      + "c.direccion_envio = ?, c.tipo_cliente = ? "
      + "WHERE c.id_cliente = ?";

    PreparedStatement comando = conexion.prepareStatement(sql);

    comando.setString(1, cliente.getTipoDocumento());
    comando.setString(2, cliente.getNumeroDocumento());
    comando.setString(3, cliente.getNombres());
    comando.setString(4, cliente.getApellidos());
    comando.setString(5, cliente.getCorreoElectronico());
    comando.setString(6, cliente.getTelefono());
    comando.setString(7, cliente.getDireccionEnvio());
    comando.setString(8, cliente.getTipoCliente());
    comando.setLong(9, cliente.getId());

    return comando;
  }

  /// Prepara el comando SQL para eliminar un cliente.
  ///
  /// @param conexion Conexión con la base de datos.
  /// @param id Identificador del cliente.
  /// @return Comando SQL preparado.
  /// @throws SQLException Si ocurre un error al preparar el comando.
  @Override
  protected PreparedStatement crearComandoSQLEliminar(
      Connection conexion, Long id) throws SQLException {

    String sql = "DELETE FROM cliente WHERE id_cliente = ?";

    PreparedStatement comando = conexion.prepareStatement(sql);
    comando.setLong(1, id);

    return comando;
  }

  /// Prepara el comando SQL para buscar un cliente por su identificador.
  ///
  /// @param conexion Conexión con la base de datos.
  /// @param id Identificador del cliente.
  /// @return Comando SQL preparado.
  /// @throws SQLException Si ocurre un error al preparar el comando.
  @Override
  protected PreparedStatement crearComandoSQLBuscarPorId(
      Connection conexion, Long id) throws SQLException {

    String sql = "SELECT id_cliente, tipo_documento, numero_documento, "
        + "nombres, apellidos, correo_electronico, telefono, "
        + "direccion_envio, tipo_cliente "
        + "FROM cliente WHERE id_cliente = ?";

    PreparedStatement comando = conexion.prepareStatement(sql);
    comando.setLong(1, id);

    return comando;
  }

  /// Prepara el comando SQL para listar todos los clientes.
  ///
  /// @param conexion Conexión con la base de datos.
  /// @return Comando SQL preparado.
  /// @throws SQLException Si ocurre un error al preparar el comando.
  @Override
  protected PreparedStatement crearComandoSQLListar(
      Connection conexion) throws SQLException {

    String sql = "SELECT id_cliente, tipo_documento, numero_documento, "
        + "nombres, apellidos, correo_electronico, telefono, "
        + "direccion_envio, tipo_cliente "
        + "FROM cliente ORDER BY id_cliente";

    return conexion.prepareStatement(sql);
  }

  /// Asigna el identificador generado al cliente guardado.
  ///
  /// @param cliente Cliente guardado.
  /// @param id Identificador generado por la base de datos.
  /// @return Nuevo objeto cliente con su identificador.
  @Override
  protected Cliente asignarId(Cliente cliente, Long id) {
    return new Cliente(
        id,
        cliente.getTipoDocumento(),
        cliente.getNumeroDocumento(),
        cliente.getNombres(),
        cliente.getApellidos(),
        cliente.getCorreoElectronico(),
        cliente.getTelefono(),
        cliente.getDireccionEnvio(),
        cliente.getTipoCliente()
    );
  }

  /// Convierte una fila de la base de datos en un objeto Cliente.
  ///
  /// @param fila Fila obtenida de la consulta SQL.
  /// @return Objeto cliente construido con los datos de la fila.
  /// @throws SQLException Si ocurre un error al leer los datos.
  @Override
  protected Cliente convertirFila(ResultSet fila) throws SQLException {
    return new Cliente(
        fila.getLong("id_cliente"),
        fila.getString("tipo_documento"),
        fila.getString("numero_documento"),
        fila.getString("nombres"),
        fila.getString("apellidos"),
        fila.getString("correo_electronico"),
        fila.getString("telefono"),
        fila.getString("direccion_envio"),
        fila.getString("tipo_cliente")
    );
  }
}
