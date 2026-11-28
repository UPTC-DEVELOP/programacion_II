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

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.modelo.Empleado;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa el repositorio que guarda los empleados en una base de datos relacional
/// usando la tecnología JDBC.
///
/// Este repositorio coordina a [RepositorioUsuarioJDBC] para guardar la cuenta de usuario
/// y al empleado en una sola transacción (o se guarda todo o no se guarda nada).
public class RepositorioEmpleadoJDBC implements IRepositorioEmpleado {
  
  /// Comando SQL para insertar un empleado con el ID de su cuenta.
  private static final String SQL_INSERTAR = "INSERT INTO empleado "
    + "(id_usuario, tipo_documento, numero_documento, nombres, apellidos) VALUES (?, ?, ?, ?, ?)";
  
  /// Comando SQL para eliminar un empleado. En la base de datos, se ejecuta el trigger para
  /// también eliminar la cuenta de usuario.
  private static final String SQL_ELIMINAR = "DELETE FROM empleado WHERE id_empleado = ?";
  
  /// Comando SQL para consultar los datos personales de los empleados.
  private static final String SQL_SELECCIONAR = "SELECT e.id_empleado, e.id_usuario, "
    + "e.tipo_documento, e.numero_documento, e.nombres, e.apellidos FROM empleado e ";
  
  /// Comando SQL para hacer la lista de empleados.
  private static final String SQL_LISTAR = SQL_SELECCIONAR + "ORDER BY e.id_empleado";
  
  /// Comando SQL para buscar un empleado por su ID.
  private static final String SQL_BUSCAR_POR_ID = SQL_SELECCIONAR + "WHERE e.id_empleado = ?";
  
  /// Comando SQL para buscar un empleado por su documento de identidad.
  private static final String SQL_BUSCAR_POR_DOCUMENTO = SQL_SELECCIONAR
    + "WHERE e.tipo_documento = ? AND e.numero_documento = ?";
  
  /// Comando SQL para actualizar los datos personales de un empleado.
  private static final String SQL_ACTUALIZAR = "UPDATE empleado SET tipo_documento = ?, "
    + "numero_documento = ?, nombres = ?, apellidos = ? WHERE id_empleado = ? AND id_usuario = ?";
  
  /// Objeto que mantiene la conexión con la base de datos.
  private final ConexionBaseDatos conexionBaseDatos;
  
  /// Repositorio JDBC de las cuentas de usuario.
  private final RepositorioUsuarioJDBC repositorioUsuario;
  
  /// Crea un nuevo repositorio de empleados conectado a una base de datos relacional.
  ///
  /// @param conexionBaseDatos  Objeto que mantiene la conexión con la base de datos.
  /// @param repositorioUsuario Repositorio JDBC de las cuentas de usuario.
  public RepositorioEmpleadoJDBC(
    ConexionBaseDatos conexionBaseDatos,
    RepositorioUsuarioJDBC repositorioUsuario
  ) {
    this.conexionBaseDatos = conexionBaseDatos;
    this.repositorioUsuario = repositorioUsuario;
  }
  
  @Override
  public Optional<EmpleadoConUsuario> buscarPorId(Long idEmpleado) {
    // si el ID es nulo, no hay empleado que buscar
    if (idEmpleado == null) {
      return Optional.empty();
    }
    
    try (Connection conexion = this.conexionBaseDatos.abrir()) {
      return buscarPorId(conexion, idEmpleado);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar empleado por ID", excepcion);
    }
  }
  
  @Override
  public List<EmpleadoConUsuario> listar() {
    try (
      Connection conexion = this.conexionBaseDatos.abrir();
      PreparedStatement comando = conexion.prepareStatement(SQL_LISTAR)) {
      return convertirEmpleados(conexion, comando);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("listar empleados", excepcion);
    }
  }
  
  @Override
  public Optional<Empleado> buscarPorDocumento(String tipoDocumento, String numeroDocumento) {
    // sin los dos datos del documento no hay empleado que buscar
    if ((tipoDocumento == null) || (numeroDocumento == null)) {
      return Optional.empty();
    }
    
    try (
      Connection conexion = this.conexionBaseDatos.abrir();
      PreparedStatement comando = conexion.prepareStatement(SQL_BUSCAR_POR_DOCUMENTO)) {
      comando.setString(1, tipoDocumento);
      comando.setString(2, numeroDocumento);
      
      // el documento es único, así que hay un empleado o ninguno
      try (ResultSet fila = comando.executeQuery()) {
        if (fila.next()) {
          return Optional.of(convertirEmpleado(fila));
        }
        return Optional.empty();
      }
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar empleado por documento", excepcion);
    }
  }
  
  @Override
  public Empleado crear(Usuario usuario, Empleado empleado) {
    // la cuenta y el empleado deben ser nuevos
    if ((usuario == null) || (empleado == null) || (usuario.getId() != null) || (empleado.getId() != null)) {
      throw new IllegalArgumentException("Para crear un empleado se necesita una cuenta y un empleado nuevos.");
    }
    
    // guardar todo en una transacción: cuenta, roles y empleado
    return this.conexionBaseDatos.ejecutarEnTransaccion((conexion) -> {
      // no hay ID, así que el repositorio de usuarios inserta la cuenta y después sus roles
      // orElseThrow() devuelve NoSuchElementException si el optional no tiene un valor y ahorra
      // un isEmpty() throw new
      Usuario guardado = this.repositorioUsuario.guardar(conexion, usuario)
        .orElseThrow();
      return insertar(conexion, guardado, empleado);
    });
  }
  
  @Override
  public Optional<EmpleadoConUsuario> actualizar(Usuario usuario, Empleado empleado) {
    // la cuenta y el empleado deben existir, y por eso tener ID
    if ((usuario == null) || (empleado == null) || (usuario.getId() == null) || (empleado.getId() == null)) {
      throw new IllegalArgumentException("Para actualizar un empleado se necesita una cuenta y un empleado con ID.");
    }
    
    // guardar todo en una transacción: datos del empleado, cuenta y roles
    return this.conexionBaseDatos.ejecutarEnTransaccion((conexion) -> {
      // primero los datos del empleado. si ya no existe el empleado, no hacer nada
      try (PreparedStatement comando = conexion.prepareStatement(SQL_ACTUALIZAR)) {
        comando.setString(1, empleado.getTipoDocumento());
        comando.setString(2, empleado.getNumeroDocumento());
        comando.setString(3, empleado.getNombres());
        comando.setString(4, empleado.getApellidos());
        comando.setLong(5, empleado.getId());
        comando.setLong(6, usuario.getId());
        
        if (comando.executeUpdate() == 0) {
          return Optional.<EmpleadoConUsuario>empty();
        }
      }
      
      // después, la cuenta y sus roles. La cuenta existe porque el empleado apunta a ella
      this.repositorioUsuario.guardar(conexion, usuario)
        .orElseThrow();
      
      // devolver lo que quedó guardado, con los permisos del rol nuevo
      return buscarPorId(conexion, empleado.getId());
    });
  }
  
  @Override
  public boolean eliminar(Long idEmpleado) {
    // si el ID es nulo, no se elimina nada
    if (idEmpleado == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar un empleado con ID nulo");
    }
    
    // eliminar el empleado en una transacción
    return this.conexionBaseDatos.ejecutarEnTransaccion((conexion) -> {
      try (PreparedStatement comando = conexion.prepareStatement(SQL_ELIMINAR)) {
        comando.setLong(1, idEmpleado);
        return (comando.executeUpdate() > 0);
      }
    });
  }
  
  /// Busca un empleado con su cuenta usando una conexión compartida.
  ///
  /// @param conexion   Conexión abierta y compartida.
  /// @param idEmpleado ID del empleado.
  /// @return El empleado con su cuenta. O, un valor vacío si no existe.
  /// @throws SQLException Si ocurre un error al consultar.
  private Optional<EmpleadoConUsuario> buscarPorId(Connection conexion, Long idEmpleado) throws SQLException {
    try (PreparedStatement comando = conexion.prepareStatement(SQL_BUSCAR_POR_ID)) {
      comando.setLong(1, idEmpleado);
      List<EmpleadoConUsuario> encontrados = convertirEmpleados(conexion, comando);
      
      if (encontrados.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(encontrados.get(0));
    }
  }
  
  /// Ejecuta una consulta de empleados y completa cada uno con su cuenta de usuario.
  ///
  /// @param conexion Conexión abierta y compartida.
  /// @param comando  Comando SQL preparado con la consulta de empleados.
  /// @return Lista de empleados con su cuenta. Vacía si no hubo filas.
  /// @throws SQLException Si ocurre un error al consultar.
  private List<EmpleadoConUsuario> convertirEmpleados(Connection conexion, PreparedStatement comando)
    throws SQLException {
    // paso 1: leer los datos personales de un empleado cada fila
    List<Empleado> empleados = new ArrayList<>();
    try (ResultSet filas = comando.executeQuery()) {
      while (filas.next()) {
        empleados.add(convertirEmpleado(filas));
      }
    }
    
    // paso 2: completar cada empleado con su cuenta, que la busca el repositorio de usuarios
    List<EmpleadoConUsuario> completos = new ArrayList<>();
    for (Empleado empleado : empleados) {
      Usuario usuario = this.repositorioUsuario.buscarPorId(conexion, empleado.getIdUsuario())
        .orElseThrow();
      completos.add(new EmpleadoConUsuario(empleado, usuario));
    }
    return completos;
  }
  
  /// Convierte la fila actual en un empleado, sin su cuenta.
  ///
  /// @param fila Fila de la consulta de empleados.
  /// @return El empleado de la fila.
  /// @throws SQLException Si ocurre un error al leer la fila.
  private Empleado convertirEmpleado(ResultSet fila) throws SQLException {
    return new Empleado(
      fila.getLong("id_empleado"),
      fila.getLong("id_usuario"),
      fila.getString("tipo_documento"),
      fila.getString("numero_documento"),
      fila.getString("nombres"),
      fila.getString("apellidos")
    );
  }
  
  /// Inserta el empleado de una cuenta recién guardada usando una conexión compartida.
  ///
  /// @param conexion Conexión con la transacción abierta.
  /// @param usuario  Cuenta ya guardada con ID.
  /// @param empleado Datos del empleado.
  /// @return El empleado guardado, con su ID.
  /// @throws SQLException Si ocurre un error al insertar.
  private Empleado insertar(Connection conexion, Usuario usuario, Empleado empleado) throws SQLException {
    try (PreparedStatement comando = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
      comando.setLong(1, usuario.getId());
      comando.setString(2, empleado.getTipoDocumento());
      comando.setString(3, empleado.getNumeroDocumento());
      comando.setString(4, empleado.getNombres());
      comando.setString(5, empleado.getApellidos());
      comando.executeUpdate();
      
      try (ResultSet ids = comando.getGeneratedKeys()) {
        if (ids.next()) {
          return new Empleado(
            ids.getLong(1), usuario.getId(), empleado.getTipoDocumento(),
            empleado.getNumeroDocumento(),
            empleado.getNombres(), empleado.getApellidos()
          );
        }
      }
      throw new ErrorTecnicoSistemaException("La base de datos no devolvió el ID del empleado guardado.", null);
    }
  }
  
  /// Traduce un error de SQL en un error técnico del sistema.
  ///
  /// @param operacion Nombre de la operación que tuvo el error.
  /// @param excepcion Excepción de SQL que se generó.
  /// @return Un objeto [ErrorTecnicoSistemaException] con el error traducido.
  private ErrorTecnicoSistemaException traducirExcepcion(String operacion, SQLException excepcion) {
    return new ErrorTecnicoSistemaException(
      String.format("Error de la base de datos al ejecutar la operación de %s.", operacion), excepcion
    );
  }
}