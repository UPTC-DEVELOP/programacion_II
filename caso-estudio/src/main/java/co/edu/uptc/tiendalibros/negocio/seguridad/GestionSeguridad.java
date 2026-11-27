/// 
package co.edu.uptc.tiendalibros.negocio.seguridad;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.errores.GestorErrores;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.modelo.ContrasenaProtegida;
import co.edu.uptc.tiendalibros.modelo.Credencial;
import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaConMayuscula;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaConMinuscula;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaConNumero;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaConSimbolo;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaLongitudMaxima;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaLongitudMinima;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaContrasenaSinEspacios;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaCredencialContrasenaNoVacia;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaCredencialCorreoNoVacio;

/// Representa un gestor de la lógica de negocio de la seguridad:
/// 
/// - autenticación (iniciar y cerrar sesión)
/// - requisitos de las contraseñas
/// - consulta de permisos del usuario con la sesión de usuario iniciada.
public class GestionSeguridad {
  
  /// Mensaje que se muestra cuando el correo electrónico o la contraseña no corresponde a
  /// una cuenta de usuario.
  private static final String MENSAJE_CREDENCIALES_INCORRECTAS = "El correo electrónico o la contraseña no son correctos. Revise los datos e intente de nuevo.";
  
  /// Mensaje cuando la cuenta de usuario existe y su contraseña es correcta, pero la cuenta
  /// está deshabilitada.
  private static final String MENSAJE_CUENTA_DESHABILITADA = "La cuenta está deshabilitada. Comuníquese con el administrador del sistema.";
  
  /// Mensaje cuando se intenta iniciar sesión estando ya trabajando dentro una sesión.
  private static final String MENSAJE_SESION_YA_INICIADA = "Ya hay una sesión iniciada. Cierre la sesión antes de iniciar otra.";
  
  /// Contraseña con un valor de relleno que se usa para imitar el tiempo de encriptación de
  /// una contraseña que sí es válida.
  private static final ContrasenaProtegida CONTRASENA_FALSA = new ContrasenaProtegida(
    "A".repeat(43) + "=", "A".repeat(22) + "=="
  );
  
  /// Repositorio de datos de las cuentas de usuario.
  private final IRepositorioUsuario repositorioUsuario;
  
  /// Estrategia para proteger y verificar las contraseñas.
  private final IProtectorContrasena protectorContrasena;
  
  /// Sesión de la aplicación.
  private final SesionUsuario sesionUsuario;
  
  /// Reglas que se validan al iniciar sesión.
  private final List<IReglaValidacion<Credencial>> reglasCredencial;
  
  /// Reglas de los requisitos de las contraseñas que se validan al crear o cambiar
  /// una contraseña.
  private final List<IReglaValidacion<char[]>> reglasContrasena;
  
  /// Crea un nuevo objeto [GestionSeguridad].
  ///
  /// @param repositorioUsuario  Repositorio de datos de las cuentas de usuario.
  /// @param protectorContrasena Estrategia para proteger y verificar contraseñas.
  /// @param sesionUsuario       Sesión de la aplicación, donde se guarda el usuario que
  ///                            inicia sesión.
  public GestionSeguridad(
    IRepositorioUsuario repositorioUsuario,
    IProtectorContrasena protectorContrasena,
    SesionUsuario sesionUsuario
  ) {
    this.repositorioUsuario = repositorioUsuario;
    this.protectorContrasena = protectorContrasena;
    this.sesionUsuario = sesionUsuario;
    
    // reglas para iniciar la sesión
    this.reglasCredencial = new ArrayList<>(
      List.of(
        new ReglaCredencialCorreoNoVacio(),
        new ReglaCredencialContrasenaNoVacia()
      )
    );
    
    // reglas de los requisitos de las contraseña
    this.reglasContrasena = new ArrayList<>(
      List.of(
        new ReglaContrasenaLongitudMinima(),
        new ReglaContrasenaLongitudMaxima(),
        new ReglaContrasenaSinEspacios(),
        new ReglaContrasenaConMayuscula(),
        new ReglaContrasenaConMinuscula(),
        new ReglaContrasenaConNumero(),
        new ReglaContrasenaConSimbolo()
      )
    );
  }
  
  /// Inicia la sesión de un usuario con su correo electrónico y su contraseña.
  ///
  /// Si la respuesta es exitosa, el usuario queda asignado en la sesión y la respuesta trae
  /// el usuario con sus roles y permisos.
  /// 
  /// Si la respuesta es fallida, no hay sesión.
  ///
  /// Este método con éxito o con falla siempre borra la contraseña de la credencial.
  ///
  /// @param credencial Correo electrónico y contraseña que escribió el usuario.
  /// @return Respuesta con el resultado de iniciar sesión.
  /// @throws IllegalArgumentException Si `credencial` es nula.
  public Respuesta<Usuario> autenticar(Credencial credencial) {
    // no se puede autenticar si la credencial es nula
    if (credencial == null) {
      throw new IllegalArgumentException("Error: No se puede iniciar sesión con una credencial nula.");
    }
    
    // intentar autenticar al usuario con la credencial
    try {
      return autenticarCredencial(credencial);
    } finally {
      // la contraseña en claro sin proteger siemre se borra de la memoria RAM
      credencial.borrarContrasena();
    }
  }
  
  /// Cierra la sesión del usuario.
  /// 
  /// Si no hay una sesión iniciada, no hace nada.
  public void cerrarSesion() {
    // no hay sesión de usuario, no hacer nada
    if (this.sesionUsuario.haySesion() == false) {
      return;
    }
    // en archivo de log solo se registra el id del usuario
    GestorErrores.registrarInfo(
      GestionSeguridad.class,
      String.format(
        "Cerró sesión el usuario con el ID: %d.",
        this.sesionUsuario.getUsuario()
          .getId()
      )
    );
    this.sesionUsuario.cerrar();
  }
  
  /// Valida una contraseña nueva según los requisitos de las contraseñas: entre 8 y 64
  /// caracteres, sin espacios, con almenos una mayúscula, una minúscula, un número y un
  /// símbolo.
  ///
  /// Los errores quedan asociados al atributo `contrasena`.
  ///
  /// @param contrasena Contraseña en claro (sin proteger).
  /// @return Los errores de validación. No hay errores si la contraseña cumple
  ///         los requisitos.
  public ResultadoValidacion validarContrasena(char[] contrasena) {
    ResultadoValidacion errores = new ResultadoValidacion();
    // objetes la contraseña como un array de bytes
    char[] aValidar = (contrasena == null) ? new char[0] : contrasena;
    
    // verificar que la contraseña cumpla los requisitos
    for (IReglaValidacion<char[]> regla : this.reglasContrasena) {
      regla.validar(errores, aValidar);
    }
    return errores;
  }
  
  /// Protege una contraseña para guardarla en la base de datos.
  ///
  /// Antes de proteger la contraseá, se debe validar la contraseña con
  /// [#validarContrasena(char[])].
  /// 
  /// @param contrasena Contraseña en claro, sin proteger.
  /// @return La contraseña protegida, con su hash y su sal.
  public ContrasenaProtegida protegerContrasena(char[] contrasena) {
    return this.protectorContrasena.proteger(contrasena);
  }
  
  /// Indica si hay una sesión iniciada.
  ///
  /// @return `true` si hay un usuario con sesión iniciada.
  public boolean haySesion() {
    return this.sesionUsuario.haySesion();
  }
  
  /// Obtiene el usuario que tiene la sesión iniciada.
  ///
  /// @return El usuario de la sesión. O, `null` si no hay sesión iniciada.
  public Usuario getUsuarioActual() { return this.sesionUsuario.getUsuario(); }
  
  /// Indica si el usuario con sesión iniciada tiene el permiso especificado.
  ///
  /// @param permiso Permiso que se quiere consultar.
  /// @return `true` si hay sesión y el usuario tiene ese permiso. Sin sesión se
  ///         retorna `false`.
  public boolean tienePermiso(Permiso permiso) {
    return this.sesionUsuario.tienePermiso(permiso);
  }
  
  /// Ejecuta los pasos para iniciar una sesión de usuario.
  ///
  /// @param credencial Correo electrónico y contraseña que el usuario ingresó.
  /// @return Respuesta con el resultado de iniciar sesión.
  private Respuesta<Usuario> autenticarCredencial(Credencial credencial) {
    // paso 1: no abrir una sesión de usuario si ya existe una sesión
    if (this.sesionUsuario.haySesion()) {
      return crearRespuestaFallida(GestionSeguridad.MENSAJE_SESION_YA_INICIADA);
    }
    
    // paso 2: verificar que los datos de la credencial son correctos
    ResultadoValidacion errores = new ResultadoValidacion();
    for (IReglaValidacion<Credencial> regla : this.reglasCredencial) {
      regla.validar(errores, credencial);
    }
    if (errores.hayErrores()) {
      return new RespuestaFallida<Usuario>(Optional.empty(), "No se pudo iniciar sesión.", errores);
    }
    
    // paso 3: buscar la cuenta de usuario
    String correoElectronico = credencial.getCorreoElectronico()
      .trim();
    Optional<Usuario> encontrado = this.repositorioUsuario.buscarPorCorreo(correoElectronico);
    
    if (encontrado.isEmpty()) {
      // la cuenta de usuario no existe. Leí que es una buena práctica de seguridad hacer que
      // las contraseñas incorrectas imiten el mismo tiempo de encriptación de una contraseña
      // correcta para evitar que los atacantes adivinen cuentas de usuario que existen y no
      // existen
      this.protectorContrasena.verificar(credencial.getContrasena(), GestionSeguridad.CONTRASENA_FALSA);
      GestorErrores.registrarAdvertencia(
        GestionSeguridad.class,
        "Intento de iniciar sesión con un correo que no tiene cuenta."
      );
      return crearRespuestaFallida(GestionSeguridad.MENSAJE_CREDENCIALES_INCORRECTAS);
    }
    
    // se encontró la cuenta de usuario
    Usuario usuario = encontrado.get();
    
    // paso 4: verificar si la contraseña ingresada coinncide con la contraseña de la cuenta
    // de usuario
    if (this.protectorContrasena.verificar(credencial.getContrasena(), usuario.getContrasena()) == false) {
      GestorErrores.registrarAdvertencia(
        GestionSeguridad.class,
        String.format("Contraseña incorrecta para el usuario con ID %d.", usuario.getId())
      );
      return crearRespuestaFallida(GestionSeguridad.MENSAJE_CREDENCIALES_INCORRECTAS);
    }
    
    // paso 5: si la cuenta de usuario está deshabilitada, no se puede continuar con el inicio
    // de sesión
    if (usuario.isHabilitado() == false) {
      GestorErrores.registrarAdvertencia(
        GestionSeguridad.class,
        String.format("Intento de iniciar sesión en la cuenta deshabilitada con ID %d.", usuario.getId())
      );
      return crearRespuestaFallida(GestionSeguridad.MENSAJE_CUENTA_DESHABILITADA);
    }
    
    // paso 6: aquí todo está correcto: el usuario está autenticado. El usuario se asigna a la
    // sesión
    this.sesionUsuario.iniciar(usuario);
    GestorErrores.registrarInfo(
      GestionSeguridad.class,
      String.format("Inició sesión el usuario con ID %d.", usuario.getId())
    );
    // devolver un mensaje de bienvenida al usuario autenticado
    return new RespuestaExitosa<Usuario>(Optional.of(usuario), String.format("Hola, %s.", usuario.getNombreVisible()));
  }
  
  /// Crea una respuesta fallida de iniciar sesión con un error general.
  ///
  /// @param mensajeError Mensaje que se le muestra al usuario.
  /// @return Respuesta fallida con el mensaje como error general.
  private Respuesta<Usuario> crearRespuestaFallida(String mensajeError) {
    ResultadoValidacion errores = new ResultadoValidacion();
    errores.agregarGeneral(mensajeError);
    return new RespuestaFallida<Usuario>(Optional.empty(), "No se pudo iniciar sesión.", errores);
  }
  
}
