//
package main.co.edu.uptc.fesad.tpsi.tienda.gui;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.ControladorEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador;

//
public class ControladorNavegacion implements IControlador {
  
  public static final String NAVEGACION_INICIO_SESION = "navegacion.inicioSesion";
  
  public static final String NAVEGACION_CATALOGO = "navegacion.gestionCatalogo";
  
  public static final String NAVEGACION_VENTAS = "navegacion.gestionVentas";
  
  public static final String NAVEGACION_CLIENTES = "navegacion.gestionClientes";
  
  public static final String NAVEGACION_ADMINISTRACION = "navegacion.administracionSistema";
  
  private final PanelContenidoPrincipal panelContenidoPrincipal;
  
  
  public ControladorNavegacion(
    PanelContenidoPrincipal panel,
    EnrutadorEventos enrutador
  ) {
    this.panelContenidoPrincipal = panel;
  }


  public void mostrarPanel(String nombrePanel) {
    this.panelContenidoPrincipal.mostrarPanel(nombrePanel);
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador#registrarEventos(main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos)
  @Override
  public void registrarEventos(EnrutadorEventos enrutador) {
    
    // asociar el evento para mostrar el panel de inicio de sesión de usuario
    enrutador
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_INICIO_SESION,
        (elemento) -> mostrarPanel(PanelContenidoPrincipal.PANEL_INICIO_SESION)
      );
    // asociar el evento de mostrar el panel de gestión del catálogo de libros
    enrutador
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_CATALOGO,
        (elemento) -> {
          
          // primero, mostrar el panel de catálogo
          mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_CATALOGO);
          
          // ahora, provocar el evento de pantalla de inicio de editorial
          enrutador.manejarEvento(ControladorEditorial.EDITORIAL_INICIO);
        }
      );
    // asociar el evento de mostrar el panel de gestión de ventas
    enrutador
      .registrarEvento(
        NAVEGACION_VENTAS,
        (elemento) -> mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_VENTAS)
      );
    
    // asociar el evento de mostrar el panel de gestión de clientes
    enrutador
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_CLIENTES,
        (elemento) -> mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_CLIENTES)
      );
    
    // asociar el evento de mostrar el panel de administración del sistema
    enrutador
      .registrarEvento(
        ControladorNavegacion.NAVEGACION_ADMINISTRACION,
        (elemento) -> mostrarPanel(PanelContenidoPrincipal.PANEL_ADMINISTRACION_SISTEMA)
      );
  }
  
}
