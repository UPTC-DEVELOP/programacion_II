//
package main.co.edu.uptc.fesad.tpsi.tienda.main;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.ControladorEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.controladores.ControladorNavegacion;
import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.PanelEditoriales;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.inicio.PanelContenidoPrincipal;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.inicio.VentanaPrincipal;

/// Representa la aplicación.
public class Aplicacion {
  
  private EnrutadorEventos enrutadorEventos;
  
  private VentanaPrincipal ventanaPrincipal;
  
  /// Crea un nuevo objeto de controlador de aplicación.
  public Aplicacion() {
    // crear un nuevo objeto enrutador.
    this.enrutadorEventos = new EnrutadorEventos();
    
    // crear la ventana principal
    this.ventanaPrincipal = new VentanaPrincipal(this.enrutadorEventos);
    
    // continuar creando los otros componentes de la aplicación
    inicializarAplicacion();
  }
  
  public EnrutadorEventos getEnrutadorEventos() { return this.enrutadorEventos; }
  
  public VentanaPrincipal getVentanaPrincipal() { return this.ventanaPrincipal; }
  
  public void inicializarAplicacion() {
    crearControladorNavegacion();
    crearControlEditorial();
  }
  
  /// Crea o inicializa el controlador que controla la navegacion.
  public ControladorNavegacion crearControladorNavegacion() {
    // primero, obtener la vista que este controlador debe manejar
    PanelContenidoPrincipal panelContenidoPrincipal = this.ventanaPrincipal.getPanelContenidoPrincipal();
    
    // segundo, crear el controlador pasando la vista y el enrutador de eventos
    ControladorNavegacion controladorNavegacion = new ControladorNavegacion(
      this.enrutadorEventos, panelContenidoPrincipal
    );
    // tercero, asociar los eventos de navegación con sus manejadores
    
    // asociar el evento para mostrar el panel de inicio de sesión de usuario
    this.enrutadorEventos
      .registrarEvento(
        "navegacion.inicio_sesion",
        (elemento) -> controladorNavegacion.mostrarInicioSesion()
      );
    // asociar el evento de mostrar el panel de gestión del catálogo de libros
    this.enrutadorEventos
      .registrarEvento(
        "navegacion.gestion_catalogo",
        (elemento) -> {
          
          // primero, mostrar el panel de catálogo
          controladorNavegacion.mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_CATALOGO);
          
          // ahora, provocar el evento de pantalla de inicio de editorial
          this.enrutadorEventos.manejarEvento(ControladorEditorial.EDITORIAL_INICIO);
        }
      );
    // asociar el evento de mostrar el panel de gestión de ventas
    this.enrutadorEventos
      .registrarEvento(
        "navegacion.gestion_ventas",
        (elemento) -> controladorNavegacion.mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_VENTAS)
      );
    
    // asociar el evento de mostrar el panel de gestión de clientes
    this.enrutadorEventos
      .registrarEvento(
        "navegacion.gestion_clientes",
        (elemento) -> controladorNavegacion.mostrarPanel(PanelContenidoPrincipal.PANEL_GESTION_CLIENTES)
      );
    
    // asociar el evento de mostrar el panel de administración del sistema
    this.enrutadorEventos
      .registrarEvento(
        "navegacion.administracion_sistema",
        (elemento) -> controladorNavegacion.mostrarPanel(PanelContenidoPrincipal.PANEL_ADMINISTRACION_SISTEMA)
      );
    
    return controladorNavegacion;
  }
  
  public ControladorEditorial crearControlEditorial() {
    // primero, obtener la vista que este controlador debe manejar
    PanelEditoriales panel = this.ventanaPrincipal.getPanelContenidoPrincipal()
      .getPanelGestionCatalogo()
      .getPanelEditoriales();
    
    // segundo, crear el controlador pasando la vista y el enrutador de eventos
    ControladorEditorial controladorEditorial = new ControladorEditorial(this.enrutadorEventos, panel);
    
    // tercero, asociar los eventos de navegación con sus manejadores
    
    this.enrutadorEventos.registrarEvento(
      ControladorEditorial.EDITORIAL_INICIO,
      (elemento) -> controladorEditorial.mostrarInicio()
    );
    
    this.enrutadorEventos.registrarEvento(
      ControladorEditorial.EDITORIAL_LISTAR,
      (elemento) -> controladorEditorial.listar()
    );
    
    this.enrutadorEventos
      .registrarEvento(ControladorEditorial.EDITORIAL_NUEVO, (elemento) -> controladorEditorial.nuevo());
    
    this.enrutadorEventos
      .registrarEvento(ControladorEditorial.EDITORIAL_EDITAR, (elemento) -> controladorEditorial.editar(elemento));
    
    this.enrutadorEventos
      .registrarEvento(ControladorEditorial.EDITORIAL_GUARDAR, (elemento) -> controladorEditorial.guardar(elemento));
    
    return controladorEditorial;
  }
}
