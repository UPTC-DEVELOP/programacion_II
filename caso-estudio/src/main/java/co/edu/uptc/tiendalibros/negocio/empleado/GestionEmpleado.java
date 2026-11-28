//
package co.edu.uptc.tiendalibros.negocio.empleado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.errores.GestorErrores;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.ContrasenaProtegida;
import co.edu.uptc.tiendalibros.modelo.Empleado;
import co.edu.uptc.tiendalibros.modelo.EmpleadoConUsuario;
import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoApellidosValidos;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoCorreoUnico;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoCorreoValido;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoDocumentoUnico;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoExiste;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoMantieneUnAdministrador;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoNoCambiaSuPropioAcceso;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoNoEsCuentaPropia;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoNoEsUltimoAdministrador;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoNombresValidos;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoNumeroDocumentoValido;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoRolValido;
import co.edu.uptc.tiendalibros.negocio.empleado.reglas.ReglaEmpleadoTipoDocumentoValido;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;
import co.edu.uptc.tiendalibros.negocio.seguridad.GestionSeguridad;
import co.edu.uptc.tiendalibros.negocio.seguridad.SesionUsuario;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaUsuarioAutorizado;

/// Representa un gestor de la lógica de negocio para los empleados y sus cuentas de
/// usuario.
///
/// Solo el administrador puede crear, editar y eliminar empleados.
/// 
/// Un empleado se guarda junto con su cuenta de usuario.
public class GestionEmpleado {
  
  /// Repositorio de datos de las cuentas de usuario.
  private final IRepositorioUsuario repositorioUsuario;
  
  /// Repositorio de los empleados.
  private final IRepositorioEmpleado repositorioEmpleado;
  
  /// Gestor de seguridad. Valida y protege la contraseña del empleado.
  private final GestionSeguridad gestorSeguridad;
  
  /// Sesión del usuario que está usando el programa.
  private final SesionUsuario sesionUsuario;
  
  /// Lista de reglas de validación para guardar un empleado.
  private final List<IReglaValidacion<DatosEmpleado>> reglasValidacionGuardar;
  
  /// Lista de reglas de validación para eliminar un empleado.
  private final List<IReglaValidacion<EmpleadoConUsuario>> reglasValidacionEliminar;
  
  

  /// Crea un nuevo objeto [GestionEmpleado].
  /// 
  /// @param repositorioEmpleado Repositorio de los empleados.
  /// @param repositorioUsuario  Repositorio de datos de las cuentas de usuario.
  /// @param gestorSeguridad     Gestor de seguridad para validar y proteger contraseñas.
  /// @param sesionUsuario       Sesión del usuario.
  public GestionEmpleado(
    IRepositorioEmpleado repositorioEmpleado,
    IRepositorioUsuario repositorioUsuario,
    GestionSeguridad gestorSeguridad,
    SesionUsuario sesionUsuario
  ) {
    this.repositorioEmpleado = repositorioEmpleado;
    this.repositorioUsuario = repositorioUsuario;
    this.gestorSeguridad = gestorSeguridad;
    this.sesionUsuario = sesionUsuario;
    
    // agregar las reglas de validación para guardar un empleado
    this.reglasValidacionGuardar = new ArrayList<>(
      List.of(
        new ReglaEmpleadoTipoDocumentoValido(),
        new ReglaEmpleadoNumeroDocumentoValido(),
        new ReglaEmpleadoNombresValidos(),
        new ReglaEmpleadoApellidosValidos(),
        new ReglaEmpleadoCorreoValido(),
        new ReglaEmpleadoRolValido(),
        new ReglaEmpleadoDocumentoUnico(this.repositorioEmpleado),
        new ReglaEmpleadoCorreoUnico(this.repositorioUsuario, this.repositorioEmpleado)
      )
    );
    
    // agregar las reglas de validación para eliminar un empleado
    this.reglasValidacionEliminar = new ArrayList<>(
      List.of(
        new ReglaEmpleadoNoEsCuentaPropia(this.sesionUsuario),
        new ReglaEmpleadoNoEsUltimoAdministrador(this.repositorioUsuario)
      )
    );
  }
  
  /// Guarda un empleado y su cuenta de usuario.
  ///
  /// Si la respuesta es exitosa, el empleado y su cuenta quedaron guardados, y la respuesta
  /// contiene el empleado con su ID y su cuenta.
  ///
  /// Si es fallida, el repositorio no se modificó.
  ///
  /// @param datos Datos del empleado y de su cuenta que se quieren guardar.
  /// @return Respuesta con el resultado de la operación de guardar.
  public Respuesta<EmpleadoConUsuario> guardar(DatosEmpleado datos) {
    try {
      return guardarDatos(datos);
    } finally {
      // las contraseñas en claro siempre se borran de la memoria RAM
      datos.borrarContrasenas();
    }
  }
  
  /// Elimina el empleado con el ID especificado junto con su cuenta de usuario.
  ///
  /// @param id ID del empleado que se quiere eliminar. No es el ID de su cuenta.
  /// @return Respuesta con el resultado de la operación de eliminar.
  public Respuesta<EmpleadoConUsuario> eliminar(Long id) {
    // resultado de la validación de errores
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // primero, verificar que el usuario tiene permiso. Si no lo tiene, no se busca nada
    new ReglaUsuarioAutorizado<Long>(
      this.sesionUsuario, "eliminar empleados",
      Permiso.CUENTA_ELIMINAR_EMPLEADO
    )
      .validar(errores, id);
    
    if (errores.hayErrores()) {
      return new RespuestaFallida<EmpleadoConUsuario>(Optional.empty(), "No se eliminó el empleado.", errores);
    }
    
    // buscar el empleado con el ID dado
    Optional<EmpleadoConUsuario> empleado = this.repositorioEmpleado.buscarPorId(id);
    
    // devolver una respuesta fallida si no se encontró el empleado
    if (empleado.isEmpty()) {
      errores.agregarViolacion(
        new ViolacionElementoNoExisteException("El empleado ya no existe en el repositorio de datos.")
      );
      
      return new RespuestaFallida<EmpleadoConUsuario>(
        Optional.empty(), "No se pudo eliminar el empleado.", errores
      );
    }
    
    // verificar si no hubo errores de validación de reglas de negocio
    for (IReglaValidacion<EmpleadoConUsuario> regla : this.reglasValidacionEliminar) {
      regla.validar(errores, empleado.get());
    }
    
    if (errores.hayErrores()) {
      return new RespuestaFallida<EmpleadoConUsuario>(
        empleado, "No se puede eliminar el empleado.", errores
      );
    }
    
    // aquí, procede a eliminar el empleado. La base de datos elimina también su cuenta
    this.repositorioEmpleado.eliminar(id);
    
    // en el archivo de log solo se registran los IDs, nunca datos personales
    GestorErrores.registrarInfo(
      GestionEmpleado.class,
      String.format(
        "El usuario con ID %d eliminó al empleado con ID %d.",
        this.sesionUsuario.getUsuario()
          .getId(),
        id
      )
    );
    
    return new RespuestaExitosa<EmpleadoConUsuario>(empleado, "Se eliminó el empleado.");
  }
  
  /// Carga los empleados con sus cuentas.
  ///
  /// Solo el personal con rol de Administrador puede ver los datos de los empleados.
  ///
  /// @return Lista de empleados con su cuenta.
  public List<EmpleadoConUsuario> listar() {
    if (this.sesionUsuario.tienePermiso(Permiso.ACCEDER_ADMINISTRACION) == false) {
      return List.of();
    }
    return this.repositorioEmpleado.listar();
  }
  
  /// Busca el empleado con el ID especificado, junto con su cuenta.
  ///
  /// Si el usuario no tiene permito administrativo no se devuelve nada.
  ///
  /// @param id ID del empleado que se quiere buscar. No es el ID de su cuenta.
  /// @return El empleado con su cuenta. O, un valor vacío si no existe, o si el usuario no
  ///         tiene acceso a la administración.
  public Optional<EmpleadoConUsuario> buscarPorId(Long id) {
    if (this.sesionUsuario.tienePermiso(Permiso.ACCEDER_ADMINISTRACION) == false) {
      return Optional.empty();
    }
    return this.repositorioEmpleado.buscarPorId(id);
  }
  
  /// Ejecuta los pasos para guardar un empleado: autorizar, validar y guardar.
  ///
  /// @param datos Datos del empleado y de su cuenta.
  /// @return Respuesta con el resultado de la operación de guardar.
  private Respuesta<EmpleadoConUsuario> guardarDatos(DatosEmpleado datos) {
    // objeto para notificar los errores
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // primero, verificar que el usuario tiene el permiso necesario.
    // si el usuario no tiene el permiso, no se continúa validando más
    
    // determinar si el empleado que se quiere guardar es nuevo en el repositorio (ID = null).
    boolean esElementoNuevo = (datos.id() == null);
    
    // si el elemento es nuevo, se pide el permiso para crear
    // si el elemento es existente, se pide el permiso para editar
    IReglaValidacion<DatosEmpleado> reglaAutorizacion = null;
    if (esElementoNuevo) {
      reglaAutorizacion = new ReglaUsuarioAutorizado<DatosEmpleado>(
        this.sesionUsuario, "crear empleados", Permiso.CUENTA_CREAR_EMPLEADO
      );
    } else {
      reglaAutorizacion = new ReglaUsuarioAutorizado<DatosEmpleado>(
        this.sesionUsuario, "editar empleados", Permiso.CUENTA_EDITAR_EMPLEADO
      );
    }
    
    reglaAutorizacion.validar(errores, datos);
    
    if (errores.hayErrores()) {
      return new RespuestaFallida<EmpleadoConUsuario>(Optional.empty(), "No se guardó el empleado.", errores);
    }
    
    // limpiar los espacios de los textos antes de validar y guardar
    DatosEmpleado limpios = datos.conTextosLimpios();
    
    // copiar la lista de reglas de validación para guardar
    List<IReglaValidacion<DatosEmpleado>> reglasParaValidar = new ArrayList<>(this.reglasValidacionGuardar);
    
    // si se quiere editar un empleado ya existente (ID != null), se agrega la precondición de
    // que el empleado existe, y las reglas que protegen al sistema
    if (esElementoNuevo == false) {
      reglasParaValidar.addFirst(new ReglaEmpleadoExiste(this.repositorioEmpleado));
      reglasParaValidar.add(
        new ReglaEmpleadoNoCambiaSuPropioAcceso(this.sesionUsuario, this.repositorioEmpleado)
      );
      reglasParaValidar.add(
        new ReglaEmpleadoMantieneUnAdministrador(this.repositorioEmpleado, this.repositorioUsuario)
      );
    }
    
    // verificar si no hubo errores de validación de datos
    for (IReglaValidacion<DatosEmpleado> regla : reglasParaValidar) {
      regla.validar(errores, limpios);
    }
    
    // validar la contraseña y su confirmación
    validarContrasena(errores, limpios, esElementoNuevo);
    
    if (errores.hayErrores()) {
      return new RespuestaFallida<EmpleadoConUsuario>(
        Optional.empty(), "Hay errores en los datos ingresados para el empleado.", errores
      );
    }
    
    if (esElementoNuevo) {
      return crearEmpleado(limpios);
    }
    return actualizarEmpleado(limpios);
  }
  
  /// Valida la contraseña y su confirmación, y reporta los errores.
  ///
  /// Un empleado nuevo debe tener contraseña. Al editar, si la contraseña y la confirmación
  /// están vacías se conserva la contraseña actual y no hay nada que validar.
  ///
  /// @param errores         Objeto en el que se reportan los errores.
  /// @param datos           Datos del empleado.
  /// @param esElementoNuevo `true` si el empleado es nuevo.
  private void validarContrasena(ResultadoValidacion errores, DatosEmpleado datos, boolean esElementoNuevo) {
    // al editar, dejar vacíos los dos campos conserva la contraseña actual
    if ((esElementoNuevo == false) && estaVacia(datos.contrasena()) && estaVacia(datos.confirmacion())) {
      return;
    }
    
    // la contraseña debe cumplir los requisitos de las contraseñas
    errores.union(this.gestorSeguridad.validarContrasena(datos.contrasena()));
    
    // la confirmación debe ser igual a la contraseña
    if (Arrays.equals(datos.contrasena(), datos.confirmacion()) == false) {
      errores.agregarAtributo("confirmacion", "La confirmación no coincide con la contraseña.");
    }
  }
  
  /// Crea un empleado nuevo con su cuenta de usuario.
  ///
  /// @param datos Datos ya limpios y validados del empleado nuevo.
  /// @return Respuesta exitosa con el empleado guardado.
  private Respuesta<EmpleadoConUsuario> crearEmpleado(DatosEmpleado datos) {
    ContrasenaProtegida contrasenaProtegida = this.gestorSeguridad.protegerContrasena(datos.contrasena());
    
    Usuario cuenta = new Usuario(
      datos.correoElectronico(), contrasenaProtegida, datos.habilitado(), EnumSet.of(datos.rol())
    );
    
    Empleado empleado = new Empleado(
      null, datos.tipoDocumento(), datos.numeroDocumento(), datos.nombres(), datos.apellidos()
    );
    
    Empleado guardado = this.repositorioEmpleado.crear(cuenta, empleado);
    
    // consultar el empleado con su cuenta como quedó guardado
    EmpleadoConUsuario resultado = this.repositorioEmpleado.buscarPorId(guardado.getId())
      .orElseThrow();
    
    // en el archivo de log solo se registran los IDs, nunca datos personales
    GestorErrores.registrarInfo(
      GestionEmpleado.class,
      String.format(
        "El usuario con ID %d creó al empleado con ID %d.",
        this.sesionUsuario.getUsuario()
          .getId(),
        guardado.getId()
      )
    );
    
    return new RespuestaExitosa<EmpleadoConUsuario>(Optional.of(resultado), "Se guardó el empleado.");
  }
  
  /// Actualiza un empleado que ya existe y su cuenta de usuario.
  ///
  /// @param datos Datos ya limpios y validados del empleado que se edita.
  /// @return Respuesta con el empleado actualizado. Es fallida si el empleado ya no existe.
  private Respuesta<EmpleadoConUsuario> actualizarEmpleado(DatosEmpleado datos) {
    Optional<EmpleadoConUsuario> almacenado = this.repositorioEmpleado.buscarPorId(datos.id());
    
    // el empleado pudo eliminarse justo antes de guardar
    if (almacenado.isEmpty()) {
      ResultadoValidacion errores = new ResultadoValidacion();
      errores.agregarViolacion(
        new ViolacionElementoNoExisteException("El empleado ya no existe en el repositorio de datos.")
      );
      
      return new RespuestaFallida<EmpleadoConUsuario>(
        Optional.empty(), "No se pudo guardar el empleado.", errores
      );
    }
    
    Long idUsuario = almacenado.get()
      .getUsuario()
      .getId();
    
    // si no escribió una contraseña nueva, se conserva la contraseña protegida que ya estaba
    ContrasenaProtegida contrasenaProtegida = almacenado.get()
      .getUsuario()
      .getContrasena();
    if (estaVacia(datos.contrasena()) == false) {
      contrasenaProtegida = this.gestorSeguridad.protegerContrasena(datos.contrasena());
    }
    
    Usuario cuenta = new Usuario(
      idUsuario, datos.correoElectronico(), contrasenaProtegida, datos.habilitado(),
      EnumSet.of(datos.rol()), EnumSet.noneOf(Permiso.class), "", null
    );
    
    Empleado empleado = new Empleado(
      datos.id(), idUsuario, datos.tipoDocumento(), datos.numeroDocumento(), datos.nombres(), datos.apellidos()
    );
    
    Optional<EmpleadoConUsuario> actualizado = this.repositorioEmpleado.actualizar(cuenta, empleado);
    
    // el empleado pudo eliminarse justo mientras se guardaba
    if (actualizado.isEmpty()) {
      ResultadoValidacion errores = new ResultadoValidacion();
      errores.agregarViolacion(
        new ViolacionElementoNoExisteException("El empleado ya no existe en el repositorio de datos.")
      );
      
      return new RespuestaFallida<EmpleadoConUsuario>(
        Optional.empty(), "No se pudo guardar el empleado.", errores
      );
    }
    
    // en el archivo de log solo se registran los IDs, nunca datos personales
    GestorErrores.registrarInfo(
      GestionEmpleado.class,
      String.format(
        "El usuario con ID %d editó al empleado con ID %d.",
        this.sesionUsuario.getUsuario()
          .getId(),
        datos.id()
      )
    );
    
    return new RespuestaExitosa<EmpleadoConUsuario>(actualizado, "Se guardó el empleado.");
  }
  
  /// Indica si una contraseña en claro (sin protección) es nula o no tiene caracteres.
  ///
  /// @param contrasena Contraseña en claro. Puede ser nula.
  /// @return `true` si es nula o está vacía.
  private boolean estaVacia(char[] contrasena) {
    return (contrasena == null) || (contrasena.length == 0);
  }
}