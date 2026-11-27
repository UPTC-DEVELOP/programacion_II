package co.edu.uptc.tiendalibros.gui.login;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.gui.navegacion.ControladorNavegacion;
import co.edu.uptc.tiendalibros.modelo.Credencial;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.seguridad.GestionSeguridad;

/// Representa un controlador para los eventos de iniciar y cerrar sesión.
public class ControladorLogin extends ControladorBase {
  
  /// Ruta para el evento de iniciar sesión.
  public static final String LOGIN_INICIAR = "login.iniciar";
  
  /// Ruta para el evento de ir al registro de un cliente nuevo.
  public static final String LOGIN_REGISTRAR = "login.registrar";
  
  /// Ruta para el evento de cerrar sesión.
  public static final String LOGIN_CERRAR_SESION = "login.cerrarSesion";
  
  /// Panel de inicio de sesión que este controlador controla.
  private final PanelLogin panelLogin;
  
  /// Gestor de la seguridad: autentica y cierra la sesión.
  private final GestionSeguridad gestorSeguridad;
  
  /// Crea un nuevo objeto [ControladorLogin].
  ///
  /// @param enrutador Enrutador para comunicar eventos.
  /// @param panel     Panel de inicio de sesión.
  /// @param gestor    Gestor de la seguridad.
  public ControladorLogin(
    EnrutadorEventos enrutador,
    PanelLogin panel,
    GestionSeguridad gestor
  ) {
    super(enrutador);
    this.panelLogin = panel;
    this.gestorSeguridad = gestor;
  }
  
  /// Inicia la sesión con el correo y la contraseña que escribió el usuario.
  public void iniciarSesion() {
    // obtener la credencial: correo electrónico y la contraseña en un arreglo
    Credencial credencial = this.panelLogin.capturarCredencial();
    
    Respuesta<Usuario> respuesta = this.gestorSeguridad.autenticar(credencial);
    
    switch (respuesta) {
      case RespuestaExitosa<Usuario> exitosa -> {
        // la contraseña no debe quedar en el campo
        this.panelLogin.limpiarCampos();
        
        // avisar a la navegación que hay un usuario nuevo: ajusta el menú y abre su módulo
        getEnrutador().manejarEvento(ControladorNavegacion.NAVEGACION_SESION_INICIADA);
        
        // el aviso va después de navegar, porque al cambiar de módulo se cierran los avisos
        mostrarAviso(Aviso.exito(exitosa.getMensaje()));
      }
      case RespuestaFallida<Usuario> fallida -> {
        // mostrar los errores en los campos, y vaciar la contraseña para que se escriba de nuevo
        this.panelLogin.mostrarErrores(fallida.getErrores());
        this.panelLogin.limpiarContrasena();
      }
    }
  }
  
  /// Cierra la sesión del usuario y vuelve al inicio de sesión.
  public void cerrarSesion() {
    this.gestorSeguridad.cerrarSesion();
    
    // la navegación deja el menú sin sesión y muestra el panel de inicio de sesión
    getEnrutador().manejarEvento(ControladorNavegacion.NAVEGACION_SESION_CERRADA);
    
    this.panelLogin.enfocarCorreo();
    mostrarAviso(Aviso.informacion("Se cerró la sesión."));
  }
  
  @Override
  public void registrarEventos() {
    // asociar el evento de iniciar sesión
    getEnrutador().registrarEvento(ControladorLogin.LOGIN_INICIAR, (elemento) -> iniciarSesion());
    
    // asociar el evento de cerrar sesión
    getEnrutador().registrarEvento(ControladorLogin.LOGIN_CERRAR_SESION, (elemento) -> cerrarSesion());
  }
}