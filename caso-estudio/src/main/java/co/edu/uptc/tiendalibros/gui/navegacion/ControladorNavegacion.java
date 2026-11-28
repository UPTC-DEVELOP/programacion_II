package co.edu.uptc.tiendalibros.gui.navegacion;

import co.edu.uptc.tiendalibros.gui.ControladorVentanaPrincipal;
import co.edu.uptc.tiendalibros.gui.PanelContenidoPrincipal;
import co.edu.uptc.tiendalibros.gui.administracion.ControladorEmpleado;
import co.edu.uptc.tiendalibros.gui.catalogo.ControladorExploradorCatalogo;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.editorial.ControladorEditorial;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.modelo.Usuario;
import co.edu.uptc.tiendalibros.negocio.seguridad.GestionSeguridad;

/// Representa un controlador para la navegación entre los paneles de los módulos.
///
/// También ajusta el menú de navegación cuando se inicia o se cierra una sesión y muestra
/// solo los módulos a los que el usuario tiene permiso de acceso.
public class ControladorNavegacion extends ControladorBase {
  
  /// Ruta para el evento de abrir el panel de inicio de sesión.
  public static final String NAVEGACION_INICIO_SESION = "navegacion.inicioSesion";
  
  /// Ruta para el evento de abrir el panel de gestión del catálogo.
  public static final String NAVEGACION_CATALOGO = "navegacion.gestionCatalogo";
  
  /// Ruta para el evento de abrir el panel de gestión de ventas.
  public static final String NAVEGACION_VENTAS = "navegacion.gestionVentas";
  
  /// Ruta para el evento de abrir el panel de gestión de clientes.
  public static final String NAVEGACION_CLIENTES = "navegacion.gestionClientes";
  
  /// Ruta para el evento de abrir el panel de administración del sistema.
  public static final String NAVEGACION_ADMINISTRACION = "navegacion.administracionSistema";
  
  /// Ruta para el evento de abrir el panel de exploración del catálogo.
  public static final String NAVEGACION_EXPLORACION_CATALOGO = "navegacion.exploracionCatalogo";
  
  /// Ruta para el evento que avisa que se inició una sesión.
  public static final String NAVEGACION_SESION_INICIADA = "navegacion.sesionIniciada";
  
  /// Ruta para el evento que avisa que se cerró la sesión.
  public static final String NAVEGACION_SESION_CERRADA = "navegacion.sesionCerrada";
  
  /// Panel del contenido principal de la ventana de la tienda.
  private final PanelContenidoPrincipal panelContenidoPrincipal;
  
  /// Panel del menú de navegación.
  private final PanelMenuNavegacion panelNavegacion;
  
  /// Gestor de la seguridad: informa quién tiene la sesión y qué permisos tiene.
  private final GestionSeguridad gestorSeguridad;
  
  /// Crea un nuevo objeto [ControladorNavegacion][ControladorNavegacion].
  ///
  /// @param enrutador       Enrutador para comunicar eventos.
  /// @param panel           Panel del contenido principal de la ventana.
  /// @param panelNavegacion Panel del menú de navegación.
  /// @param gestor          Gestor de la seguridad.
  public ControladorNavegacion(
    EnrutadorEventos enrutador,
    PanelContenidoPrincipal panel,
    PanelMenuNavegacion panelNavegacion,
    GestionSeguridad gestor
  ) {
    super(enrutador);
    this.panelContenidoPrincipal = panel;
    this.panelNavegacion = panelNavegacion;
    this.gestorSeguridad = gestor;
  }
  
  /// Muestra el panel que tenga el nombre especificado.
  ///
  /// @param nombrePanel    Nombre del panel que se desea mostrar.
  /// @param rutaNavegacion Ruta de de la navegación. Útil para marcar el módulo acivo.
  public void mostrarPanel(String nombrePanel, String rutaNavegacion) {
    // cuando el usuario seleccione otro botón menú, se cierra el aviso que se está mostrando.
    getEnrutador().manejarEvento(ControladorVentanaPrincipal.CERRAR_AVISO);
    // mostrar el panel especificado.
    this.panelContenidoPrincipal.mostrarPanel(nombrePanel);
    // marcar el botón para indicar el módulo activo
    this.panelNavegacion.marcarModuloActivo(rutaNavegacion);
  }
  
  /// Muestra el panel de exploración del catálogo y le pide al explorador que cargue la
  /// lista de libros disponibles.
  private void mostrarExploracionCatalogo() {
    // primero, mostrar el panel de exploración
    mostrarPanel(
      PanelContenidoPrincipal.PANEL_EXPLORACION_CATALOGO,
      ControladorNavegacion.NAVEGACION_EXPLORACION_CATALOGO
    );
    
    // ahora, provocar el evento de inicio del explorador, que carga las fichas
    getEnrutador().manejarEvento(ControladorExploradorCatalogo.CATALOGO_INICIO);
  }
  
  /// Ajusta el menú al usuario que acaba de iniciar sesión y abre su módulo inicial.
  ///
  /// Ocultar los botones es solo una ayuda visual. La autorización real se verifica en la
  /// capa de negocio.
  private void atenderSesionIniciada() {
    Usuario usuario = this.gestorSeguridad.getUsuarioActual();
    
    // no debería suceder: este evento se dispara después de autenticar con éxito
    if (usuario == null) {
      return;
    }
    
    // mostar en el panel de usuario el texto de usuario
    this.panelNavegacion.mostrarSesion(crearTextoUsuario(usuario));
    
    // tener una lista de las rutas de navegación de cada botón que abre un módulo
    String[] rutas = {
      ControladorNavegacion.NAVEGACION_EXPLORACION_CATALOGO,
      ControladorNavegacion.NAVEGACION_CATALOGO,
      ControladorNavegacion.NAVEGACION_VENTAS,
      ControladorNavegacion.NAVEGACION_CLIENTES,
      ControladorNavegacion.NAVEGACION_ADMINISTRACION
    };
    
    // tener una lista de los permisos que se usan para acceder a un módulo
    Permiso[] permisos = {
      Permiso.ACCEDER_EXPLORADOR_CATALOGO,
      Permiso.ACCEDER_GESTION_CATALOGO,
      Permiso.ACCEDER_GESTION_VENTAS,
      Permiso.ACCEDER_GESTION_CLIENTES,
      Permiso.ACCEDER_ADMINISTRACION
    };
    
    // recorrer la lista de rutas, preguntar si el usuario tiene permiso para acceder a esa
    // ruta, y mostrar solo los módulos permitidos
    for (int i = 0; i < rutas.length; i++) {
      this.panelNavegacion.setModuloVisible(
        rutas[i],
        this.gestorSeguridad.tienePermiso(permisos[i])
      );
    }
    
    // abrir el módulo inicial del usuario
    getEnrutador().manejarEvento(obtenerRutaInicial());
  }
  
  /// Obtiene la ruta del módulo con el que empieza cada usuario después de iniciar sesión.
  ///
  /// Todos los roles de usuario pueden explorar el catálogo.
  ///
  /// @return Ruta del módulo inicial.
  private String obtenerRutaInicial() {
    if (this.gestorSeguridad.tienePermiso(Permiso.ACCEDER_ADMINISTRACION)) {
      return NAVEGACION_ADMINISTRACION;
    }
    if (this.gestorSeguridad.tienePermiso(Permiso.ACCEDER_GESTION_VENTAS)) {
      return NAVEGACION_VENTAS;
    }
    if (this.gestorSeguridad.tienePermiso(Permiso.ACCEDER_GESTION_CATALOGO)) {
      return NAVEGACION_CATALOGO;
    }
    if (this.gestorSeguridad.tienePermiso(Permiso.ACCEDER_GESTION_CLIENTES)) {
      return NAVEGACION_CLIENTES;
    }
    return NAVEGACION_EXPLORACION_CATALOGO;
  }
  
  /// Deja el menú sin sesión y muestra el panel de inicio de sesión.
  private void atenderSesionCerrada() {
    this.panelNavegacion.mostrarSinSesion();
    mostrarPanel(PanelContenidoPrincipal.PANEL_INICIO_SESION, ControladorNavegacion.NAVEGACION_INICIO_SESION);
  }
  
  /// Crea el texto que identifica al usuario en el menú de navegación.
  /// 
  /// Este texto muestra: nombre visible y sus roles. Ejemplo "Rocío Gómez (Administrador)".
  ///
  /// @param usuario Usuario que tiene la sesión iniciada.
  /// @return Texto con el nombre y los roles del usuario.
  private String crearTextoUsuario(Usuario usuario) {
    StringBuilder roles = new StringBuilder();
    
    // el usuario tiene el atributo roles, y se busca en el enum Roles el nombre visible de
    // cada rol
    for (Rol rol : Rol.values()) {
      if (usuario.getRoles()
        .contains(rol)) {
        if (roles.length() > 0) {
          roles.append(", ");
        }
        roles.append(rol.getNombreVisible());
      }
    }
    
    // se supone que una cuenta sin roles no debería existir, pero si sucede mostrar el texto
    // sin rol
    if (roles.length() == 0) {
      return String.format("%s (sin rol)", usuario.getNombreVisible());
    }
    
    return String.format("%s (%s)", usuario.getNombreVisible(), roles);
  }
  
  @Override
  public void registrarEventos() {
    
    // asociar el evento para mostrar el panel de inicio de sesión de usuario
    getEnrutador()
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_INICIO_SESION,
        (elemento) -> mostrarPanel(PanelContenidoPrincipal.PANEL_INICIO_SESION,
          ControladorNavegacion.NAVEGACION_INICIO_SESION
        )
      );
    
    // asociar el evento de mostrar el panel de gestión del catálogo de libros
    getEnrutador()
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_CATALOGO,
        (elemento) -> {
          
          // primero, mostrar el panel de catálogo
          mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_CATALOGO, ControladorNavegacion.NAVEGACION_CATALOGO);
          
          // ahora, provocar el evento de pantalla de inicio de editorial
          getEnrutador().manejarEvento(ControladorEditorial.EDITORIAL_INICIO);
        }
      );
    
    // asociar el evento de mostrar el panel de gestión de ventas
    getEnrutador()
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_VENTAS,
        (elemento) -> mostrarPanel(
          PanelContenidoPrincipal.PANEL_GESTION_VENTAS,
          ControladorNavegacion.NAVEGACION_VENTAS
        )
      );
    
    // asociar el evento de mostrar el panel de gestión de clientes
    getEnrutador()
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_CLIENTES,
        (elemento) -> mostrarPanel(
          PanelContenidoPrincipal.PANEL_GESTION_CLIENTES,
          ControladorNavegacion.NAVEGACION_CLIENTES
        )
      );
    
    // asociar el evento de mostrar el panel de administración del sistema
    getEnrutador()
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_ADMINISTRACION,
        (elemento) -> {
          mostrarPanel(
            PanelContenidoPrincipal.PANEL_ADMINISTRACION_SISTEMA,
            ControladorNavegacion.NAVEGACION_ADMINISTRACION
          );
          // cargar la lista de empleados
          getEnrutador().manejarEvento(ControladorEmpleado.EMPLEADO_INICIO);
        }
      );
    
    // asociar el evento de mostrar el panel de exploración del catálogo
    getEnrutador().registrarEvento(
      ControladorNavegacion.NAVEGACION_EXPLORACION_CATALOGO,
      (elemento) -> mostrarExploracionCatalogo()
    );
    
    // asociar el evento de sesión iniciada
    getEnrutador().registrarEvento(
      ControladorNavegacion.NAVEGACION_SESION_INICIADA,
      (elemento) -> atenderSesionIniciada()
    );
    
    // asociar el evento de sesión cerrada
    getEnrutador().registrarEvento(
      ControladorNavegacion.NAVEGACION_SESION_CERRADA,
      (elemento) -> atenderSesionCerrada()
    );
  }
  
}