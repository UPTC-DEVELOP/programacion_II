package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import co.edu.uptc.tiendalibros.gui.errores.GestorErrores;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.modelo.ContrasenaProtegida;
import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.modelo.Usuario;

/// Representa el repositorio que guarda las cuentas de usuario en una base de datos
/// relacional usando la tecnología JDBC.
///
/// Una cuenta de usuario se reparte en varias tablas, y este repositorio las junta:
///
/// - `usuario`: correo, contraseña protegida, y si está habilitada.
/// - `usuario_rol`: los roles de la cuenta.
/// - `rol_permiso`: los permisos de cada rol. Los permisos de una cuenta de usuario es el
///   conjunto de todos sus roles.
/// - `empleado` o `cliente`: de donde se obtiene nombre visible y el ID del cliente.
///
/// Las operaciones que crean una cuenta junto con su cliente o su empleado, se ejecutan en
/// una sola transacción: o se guarda todo o no se guarda nada.
public class RepositorioUsuarioJDBC extends RepositorioJDBCBase<Usuario> implements IRepositorioUsuario {
  
  /// Columnas y tablas para consultar una cuenta de usuario.
  /// 
  /// Se hace LEFT JOIN entre usuario y empleado, y LEFT JOIN entre usuario y cliente. Con
  /// estos JOIN debemos obtener el nombre visible o bien desde la tabla empleado o bien
  /// desde la tabla cliente.
  /// 
  /// COALESCE construye el nombre visible dependiendo de que los atributos nombres y
  /// apellidos no sean nulos.
  /// 
  /// El ID del cliente es `NULL` si la cuenta de usuario es de un empleado.
  private static final String SQL_SELECCIONAR = "SELECT u.id_usuario, u.correo_electronico, "
    + "u.hash_contrasena, u.sal, u.habilitado, c.id_cliente, "
    + "COALESCE(CONCAT(e.nombres, ' ', e.apellidos), CONCAT(c.nombres, ' ', c.apellidos), "
    + "u.correo_electronico) AS nombre_visible "
    + "FROM usuario u "
    + "LEFT JOIN empleado e ON e.id_usuario = u.id_usuario "
    + "LEFT JOIN cliente c ON c.id_usuario = u.id_usuario ";
  
  /// Comando SQL para insertar una cuenta de usuario.
  private static final String SQL_INSERTAR = "INSERT INTO usuario "
    + "(correo_electronico, hash_contrasena, sal, habilitado) VALUES (?, ?, ?, ?)";
  
  /// Comando SQL para actualizar una cuenta de usuario.
  private static final String SQL_ACTUALIZAR = "UPDATE usuario SET correo_electronico = ?, "
    + "hash_contrasena = ?, sal = ?, habilitado = ? WHERE id_usuario = ?";
  
  /// Comando SQL para eliminar una cuenta de usuario.
  private static final String SQL_ELIMINAR = "DELETE FROM usuario WHERE id_usuario = ?";
  
  /// Comando SQL para buscar una cuenta de usuario por su ID.
  private static final String SQL_BUSCAR_POR_ID = SQL_SELECCIONAR + "WHERE u.id_usuario = ?";
  
  /// Comando SQL para buscar una cuenta de usuario por su correo electrónico.
  private static final String SQL_BUSCAR_POR_CORREO = SQL_SELECCIONAR + "WHERE u.correo_electronico = ?";
  
  /// Comando SQL para hacer la lista de cuentas de usuario.
  private static final String SQL_LISTAR = SQL_SELECCIONAR + "ORDER BY u.id_usuario";
  
  /// Comando SQL para consultar los roles de una cuenta.
  private static final String SQL_ROLES = "SELECT rol FROM usuario_rol WHERE id_usuario = ?";
  
  /// Comando SQL para consultar los permisos de una cuenta: forma el conjunto de todos sus
  /// roles. DISTINCT evita repetir un permiso que lo conceden dos roles.
  private static final String SQL_PERMISOS = "SELECT DISTINCT rp.permiso FROM usuario_rol ur "
    + "JOIN rol_permiso rp ON rp.rol = ur.rol WHERE ur.id_usuario = ?";
  
  /// Comando SQL para eliminar todos los roles de una cuenta.
  private static final String SQL_ELIMINAR_ROLES = "DELETE FROM usuario_rol WHERE id_usuario = ?";
  
  /// Comando SQL para insertar un rol de una cuenta.
  private static final String SQL_INSERTAR_ROL = "INSERT INTO usuario_rol (id_usuario, rol) VALUES (?, ?)";
  
  /// Comando SQL para insertar un cliente cuando se registra con su cuenta.
  private static final String SQL_INSERTAR_CLIENTE = "INSERT INTO cliente "
    + "(id_usuario, tipo_documento, numero_documento, nombres, apellidos, correo_electronico, "
    + "telefono, direccion_envio, tipo_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
  
  
  /// Comando SQL para contar las cuentas habilitadas que tienen un rol.
  private static final String SQL_CONTAR_CUENTAS_CON_ROL = "SELECT COUNT(*) FROM usuario u "
    + "JOIN usuario_rol ur ON ur.id_usuario = u.id_usuario WHERE ur.rol = ? AND u.habilitado = TRUE";
  
  /// Objeto que mantiene la conexión con la base de datos.
  private final ConexionBaseDatos conexionBaseDatos;
  
  /// Crea un nuevo repositorio de usuarios conectado a una base de datos relacional.
  ///
  /// @param conexionBaseDatos Objeto que mantiene la conexión con la base de datos.
  public RepositorioUsuarioJDBC(ConexionBaseDatos conexionBaseDatos) {
    super(conexionBaseDatos);
    this.conexionBaseDatos = conexionBaseDatos;
  }
  
  @Override
  public Optional<Usuario> buscarPorCorreo(String correoElectronico) {
    // si el correo es nulo, no hay cuenta que buscar
    if (correoElectronico == null) {
      return Optional.empty();
    }
    
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion.prepareStatement(SQL_BUSCAR_POR_CORREO)) {
      comando.setString(1, correoElectronico);
      
      // el correo es único, así que hay una cuenta o ninguna
      List<Usuario> encontrados = convertirFilas(comando);
      if (encontrados.isEmpty()) {
        return Optional.empty();
      }
      return Optional.of(encontrados.get(0));
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar usuario por correo", excepcion);
    }
  }
  
  @Override
  public Usuario registrarCliente(Usuario usuario, Cliente cliente) {
    // la cuenta y el cliente deben ser nuevos
    if ((usuario == null) || (cliente == null) || (usuario.getId() != null) || (cliente.getId() != null)) {
      throw new IllegalArgumentException("Para registrar un cliente se necesita pasar una cuenta de usuario y un cliente nuevos.");
    }
    
    // guardar todo en una transacción: cuenta, roles y cliente
    return this.conexionBaseDatos.ejecutarEnTransaccion((conexion) -> {
      Usuario guardado = guardarCuentaNueva(conexion, usuario);
      insertarCliente(conexion, guardado, cliente);
      // buscar la cuenta de usuario
      // orElseThrow() devuelve NoSuchElementException si el optional no tiene un valor y ahorra
      // un isEmpty() throw new
      return buscarPorId(conexion, guardado.getId()).orElseThrow();
    });
  }
  
  
  @Override
  public int contarCuentasHabilitadasConRol(Rol rol) {
    if (rol == null) {
      throw new IllegalArgumentException("Error: No se pueden contar las cuentas de un rol nulo");
    }
    
    try (
      Connection conexion = abrirConexion();
      PreparedStatement comando = conexion.prepareStatement(SQL_CONTAR_CUENTAS_CON_ROL)) {
      comando.setString(1, rol.getAbreviatura());
      
      try (ResultSet fila = comando.executeQuery()) {
        fila.next();
        return fila.getInt(1);
      }
    } catch (SQLException excepcion) {
      throw traducirExcepcion("contar cuentas habilitadas por rol", excepcion);
    }
  }
  
  /// Guarda una cuenta nueva y sus roles usando una conexión compartida.
  ///
  /// @param conexion Conexión con la transacción abierta.
  /// @param usuario  Cuenta nueva sin ID.
  /// @return La cuenta guardada con su ID asignado.
  /// @throws SQLException Si ocurre un error al guardar.
  private Usuario guardarCuentaNueva(Connection conexion, Usuario usuario) throws SQLException {
    // no hay ID, así que guardar() inserta la cuenta y después sus roles
    // orElseThrow() devuelve NoSuchElementException si el optional no tiene un valor y ahorra
    // un isEmpty() throw new
    return guardar(conexion, usuario).orElseThrow();
  }
  
  /// Inserta el cliente de una cuenta recién guardada usando una conexión compartida.
  ///
  /// El correo que se guarda es el de la cuenta de usuario.
  ///
  /// @param conexion Conexión con la transacción abierta.
  /// @param usuario  Cuenta ya guardada con ID.
  /// @param cliente  Datos del cliente.
  /// @throws SQLException Si ocurre un error al insertar.
  private void insertarCliente(Connection conexion, Usuario usuario, Cliente cliente) throws SQLException {
    try (PreparedStatement comando = conexion.prepareStatement(SQL_INSERTAR_CLIENTE)) {
      comando.setLong(1, usuario.getId());
      comando.setString(2, cliente.getTipoDocumento());
      comando.setString(3, cliente.getNumeroDocumento());
      comando.setString(4, cliente.getNombres());
      comando.setString(5, cliente.getApellidos());
      comando.setString(6, usuario.getCorreoElectronico());
      comando.setString(7, cliente.getTelefono());
      comando.setString(8, cliente.getDireccionEnvio());
      comando.setString(9, cliente.getTipoCliente());
      comando.executeUpdate();
    }
  }
  
  @Override
  protected PreparedStatement crearComandoSQLInsertar(Connection conexion, Usuario usuario) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS);
    comando.setString(1, usuario.getCorreoElectronico());
    comando.setString(
      2,
      usuario.getContrasena()
        .getHash()
    );
    comando.setString(
      3,
      usuario.getContrasena()
        .getSal()
    );
    comando.setBoolean(4, usuario.isHabilitado());
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLActualizar(Connection conexion, Usuario usuario) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(SQL_ACTUALIZAR);
    comando.setString(1, usuario.getCorreoElectronico());
    comando.setString(
      2,
      usuario.getContrasena()
        .getHash()
    );
    comando.setString(
      3,
      usuario.getContrasena()
        .getSal()
    );
    comando.setBoolean(4, usuario.isHabilitado());
    comando.setLong(5, usuario.getId());
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLEliminar(Connection conexion, Long id) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(SQL_ELIMINAR);
    comando.setLong(1, id);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLBuscarPorId(Connection conexion, Long id) throws SQLException {
    PreparedStatement comando = conexion.prepareStatement(SQL_BUSCAR_POR_ID);
    comando.setLong(1, id);
    return comando;
  }
  
  @Override
  protected PreparedStatement crearComandoSQLListar(Connection conexion) throws SQLException {
    return conexion.prepareStatement(SQL_LISTAR);
  }
  
  @Override
  protected Usuario asignarId(Usuario usuario, Long id) {
    return new Usuario(
      id, usuario.getCorreoElectronico(), usuario.getContrasena(), usuario.isHabilitado(),
      usuario.getRoles(), usuario.getPermisos(), usuario.getNombreVisible(), usuario.getIdCliente()
    );
  }
  
  /// Convierte la fila actual en una cuenta, pero sin roles **sin roles ni permisos**.
  /// 
  /// Los roles y los permisos se se consultan invocando
  /// [#cargarElementosDependientes(Connection, Usuario)].
  @Override
  protected Usuario convertirFila(ResultSet fila) throws SQLException {
    return new Usuario(
      fila.getLong("id_usuario"),
      fila.getString("correo_electronico"),
      new ContrasenaProtegida(fila.getString("hash_contrasena"), fila.getString("sal")),
      fila.getBoolean("habilitado"),
      EnumSet.noneOf(Rol.class),
      EnumSet.noneOf(Permiso.class),
      fila.getString("nombre_visible"),
      ValoresSQL.leerLongNulo(fila, "id_cliente")
    );
  }
  
  /// Completa la cuenta con sus roles y sus permisos.
  @Override
  protected Usuario cargarElementosDependientes(Connection conexion, Usuario usuario) throws SQLException {
    Set<Rol> roles = consultarRoles(conexion, usuario.getId());
    Set<Permiso> permisos = consultarPermisos(conexion, usuario.getId());
    
    // Usuario es un objeto inmutable, y toca crear otro objeto usuario con los roles y
    // permisos
    return new Usuario(
      usuario.getId(), usuario.getCorreoElectronico(), usuario.getContrasena(), usuario.isHabilitado(),
      roles, permisos, usuario.getNombreVisible(), usuario.getIdCliente()
    );
  }
  
  /// Guarda los roles de la cuenta: borra los que tenía y guarda los actuales.
  @Override
  protected Usuario guardarElementosDependientes(Connection conexion, Usuario usuario) throws SQLException {
    try (PreparedStatement borrar = conexion.prepareStatement(SQL_ELIMINAR_ROLES)) {
      borrar.setLong(1, usuario.getId());
      borrar.executeUpdate();
    }
    
    try (PreparedStatement insertar = conexion.prepareStatement(SQL_INSERTAR_ROL)) {
      for (Rol rol : usuario.getRoles()) {
        insertar.setLong(1, usuario.getId());
        insertar.setString(2, rol.getAbreviatura());
        insertar.executeUpdate();
      }
    }
    return usuario;
  }
  
  /// Consulta los roles de una cuenta usando una conexión compartida.
  ///
  /// @param conexion  Conexión abierta y compartida.
  /// @param idUsuario ID de la cuenta.
  /// @return Los roles de la cuenta. Vacío si no tiene.
  /// @throws SQLException Si ocurre un error al consultar.
  private Set<Rol> consultarRoles(Connection conexion, Long idUsuario) throws SQLException {
    // los roles se van recuperando en un conjunto
    Set<Rol> roles = EnumSet.noneOf(Rol.class);
    try (PreparedStatement comando = conexion.prepareStatement(SQL_ROLES)) {
      comando.setLong(1, idUsuario);
      try (ResultSet filas = comando.executeQuery()) {
        while (filas.next()) {
          roles.add(Rol.convertirAbreviatura(filas.getString("rol")));
        }
      }
    }
    return roles;
  }
  
  /// Consulta los permisos de una cuenta usando una conexión compartida.
  ///
  /// Si la base de datos tiene un permiso que el enum [Permiso] ya no conoce, no se concede
  /// (se deniega por defecto) y se deja una advertencia en el registro de eventos.
  ///
  /// @param conexion  Conexión abierta y compartida.
  /// @param idUsuario ID de la cuenta.
  /// @return Los permisos de la cuenta. Vacío si no tiene.
  /// @throws SQLException Si ocurre un error al consultar.
  private Set<Permiso> consultarPermisos(Connection conexion, Long idUsuario) throws SQLException {
    // los nombres de la base de datos se leen primero, y después se convierten
    List<String> nombres = new ArrayList<>();
    try (PreparedStatement comando = conexion.prepareStatement(SQL_PERMISOS)) {
      comando.setLong(1, idUsuario);
      try (ResultSet filas = comando.executeQuery()) {
        while (filas.next()) {
          nombres.add(filas.getString("permiso"));
        }
      }
    }
    
    // se cargaron los permisos desde la base de datos.
    // ahora se verifica que el permiso cargado lo recononozca el enum Permiso
    // si el permiso cargado no es un valor registrado en el enum Permiso, el permiso se
    // decarta
    Set<Permiso> permisos = EnumSet.noneOf(Permiso.class);
    for (String nombre : nombres) {
      Optional<Permiso> permiso = Permiso.buscarPorNombre(nombre);
      if (permiso.isPresent()) {
        permisos.add(permiso.get());
      } else {
        // informar del error al cargar el permiso, pero no se regitra datos del usuario
        GestorErrores.registrarAdvertencia(
          String.format(
            "El permiso '%s' de la base de datos no existe en el código Java. No se concede.",
            nombre
          )
        );
      }
    }
    return permisos;
  }
}