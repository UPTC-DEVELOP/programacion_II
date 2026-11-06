//
package co.edu.uptc.tiendalibros.gui.navegacion;

import co.edu.uptc.tiendalibros.gui.ControladorVentanaPrincipal;
import co.edu.uptc.tiendalibros.gui.PanelContenidoPrincipal;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.editorial.ControladorEditorial;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;

/// Representa un controlador para la navegación entre los paneles de los módulos.
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
  
  /// Panel del contenido principal de la ventana de la tienda.
  private final PanelContenidoPrincipal panelContenidoPrincipal;
  
  /// Panel del menú de navegación.
  private final PanelMenuNavegacion panelNavegacion;
  
  /// Crea un nuevo objeto [ControladorNavegacion][ControladorNavegacion].
  ///
  /// @param enrutador       Enrutador para comunicar eventos.
  /// @param panel           Panel del contenido principal de la ventana.
  /// @param panelNavegacion Panel del menú de navegación.
  public ControladorNavegacion(
    EnrutadorEventos enrutador,
    PanelContenidoPrincipal panel,
    PanelMenuNavegacion panelNavegacion
  ) {
    super(enrutador);
    this.panelContenidoPrincipal = panel;
    this.panelNavegacion = panelNavegacion;
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
        (elemento) -> mostrarPanel(
          PanelContenidoPrincipal.PANEL_ADMINISTRACION_SISTEMA,
          ControladorNavegacion.NAVEGACION_ADMINISTRACION
        )
      );
  }
  
}
