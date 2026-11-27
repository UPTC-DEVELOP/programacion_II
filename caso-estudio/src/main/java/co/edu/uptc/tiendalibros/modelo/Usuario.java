package co.edu.uptc.tiendalibros.modelo;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/// Representa la cuenta de un usuario que inicia sesión en el sistema.
/// 
/// Al cargar una cuenta de usuario se cargan sus roles, sus permisos y un nombre visible.
public class Usuario extends ElementoBase {
  
  /// Correo electrónico del usuario. Es su nombre de usuario.
  private final String correoElectronico;
  
  /// Contraseña protegida (con hash y sal) del usuario.
  private final ContrasenaProtegida contrasena;
  
  /// Indica si la cuenta está habilitada para iniciar sesión.
  private final boolean habilitado;
  
  /// ID del cliente de esta cuenta. Es `null` si la cuenta es de un empleado.
  private final Long idCliente;
  
  /// Roles del usuario.
  private final Set<Rol> roles;
  
  /// Permisos del usuario: los de todos sus roles juntos.
  private final Set<Permiso> permisos;
  
  /// Nombre que se muestra en la interfaz gráfica.
  private final String nombreVisible;
  
  /// Crea un objeto [Usuario] nuevo sin ID, sin permisos y sin nombre visible.
  ///
  /// @param correoElectronico Correo electrónico del usuario.
  /// @param contrasena        Contraseña protegida del usuario.
  /// @param habilitado        `true` si la cuenta puede iniciar sesión.
  /// @param roles             Roles del usuario.
  /// @throws IllegalArgumentException Si algún argumento no es válido.
  public Usuario(
    String correoElectronico,
    ContrasenaProtegida contrasena,
    boolean habilitado,
    Set<Rol> roles
  ) {
    this(
      null,
      correoElectronico,
      contrasena,
      habilitado,
      roles,
      EnumSet.noneOf(Permiso.class),
      "",
      null
    );
  }
  
  /// Crea un usuario con todos sus datos especificados.
  ///
  /// @param id                ID del usuario.
  /// @param correoElectronico Correo electrónico del usuario. No es nulo ni está en blanco.
  /// @param contrasena        Contraseña protegida del usuario. No es nula.
  /// @param habilitado        `true` si la cuenta puede iniciar sesión.
  /// @param roles             Roles del usuario. No es nulo.
  /// @param permisos          Permisos del usuario. No es nulo.
  /// @param nombreVisible     Nombre que se muestra en la interfaz. No es nulo.
  /// @param idCliente         ID del cliente de esta cuenta O, `null` si la cuenta es de un
  ///                          empleado.
  /// @throws IllegalArgumentException Si algún argumento no cumple lo indicado.
  public Usuario(
    Long id,
    String correoElectronico,
    ContrasenaProtegida contrasena,
    boolean habilitado,
    Set<Rol> roles,
    Set<Permiso> permisos,
    String nombreVisible, 
    Long idCliente
  ) {
    super(id);
    // el correo electrónico no puede ser vacío
    if ((correoElectronico == null) || correoElectronico.isBlank()) {
      throw new IllegalArgumentException("El correo electrónico del usuario no puede estar vacío.");
    }
    // la contraseña no puede ser vacía
    if (contrasena == null) {
      throw new IllegalArgumentException("El usuario debe tener una contraseña protegida.");
    }
    // los atributos roles, permisos y nombreVisible tampoco pueden ser vacíos
    if ((roles == null) || (permisos == null) || (nombreVisible == null)) {
      throw new IllegalArgumentException("Los roles, los permisos y el nombre visible no pueden ser nulos.");
    }
    this.correoElectronico = correoElectronico;
    this.contrasena = contrasena;
    this.habilitado = habilitado;
    this.idCliente = idCliente;
    // usar EnumSet.copyOf para crear un conjunto vacío que guardará los roles del usuario
    Set<Rol> copiaRoles = EnumSet.noneOf(Rol.class);
    copiaRoles.addAll(roles);
    // como el atributo roles es final (solo lectura) su valor se asigna como un conjunto
    // inmodificable
    this.roles = Collections.unmodifiableSet(copiaRoles);
    
    // usar EnumSet.copyOf para crear un conjunto vacío que guardará los permisos del usuario
    Set<Permiso> copiaPermisos = EnumSet.noneOf(Permiso.class);
    copiaPermisos.addAll(permisos);
    // como el atributo roles es final (solo lectura) su valor se asigna como un conjunto
    // inmodificable
    this.permisos = Collections.unmodifiableSet(copiaPermisos);
    
    this.nombreVisible = nombreVisible;
  }
  
  /// Obtiene el correo electrónico del usuario.
  ///
  /// @return Correo electrónico del usuario.
  public String getCorreoElectronico() { return this.correoElectronico; }
  
  /// Obtiene la contraseña protegida del usuario.
  ///
  /// @return Contraseña protegida del usuario.
  public ContrasenaProtegida getContrasena() { return this.contrasena; }
  
  /// Indica si la cuenta está habilitada para iniciar sesión.
  ///
  /// @return `true` si la cuenta está habilitada.
  public boolean isHabilitado() { return this.habilitado; }
  
  /// Obtiene el ID del cliente de esta cuenta.
  ///
  /// @return ID del cliente. O, `null` si la cuenta es de un empleado.
  public Long getIdCliente() { return this.idCliente; }
  
  /// Obtiene los roles del usuario.
  ///
  /// @return Roles del usuario.
  public Set<Rol> getRoles() { return this.roles; }
  
  /// Obtiene los permisos del usuario.
  ///
  /// @return Permisos del usuario.
  public Set<Permiso> getPermisos() { return this.permisos; }
  
  /// Obtiene el nombre que se muestra en la interfaz.
  ///
  /// @return Nombres y apellidos del usuario, o un texto vacío si no se cargó.
  public String getNombreVisible() { return this.nombreVisible; }
  
  /// Indica si el usuario tiene el rol dado.
  ///
  /// @param rol Rol que se quiere consultar.
  /// @return `true` si el usuario tiene ese rol.
  public boolean tieneRol(Rol rol) {
    return (rol != null) && this.roles.contains(rol);
  }
  
  /// Indica si el usuario puede hacer lo que dice el permiso dado.
  ///
  /// Si el permiso es nulo se deniega.
  ///
  /// @param permiso Permiso que se quiere consultar.
  /// @return `true` si alguno de los roles del usuario concede ese permiso.
  public boolean tienePermiso(Permiso permiso) {
    return (permiso != null) && this.permisos.contains(permiso);
  }
  
  @Override
  public String toString() {
    return String.format("Usuario[id=%d]", getId());
  }
}