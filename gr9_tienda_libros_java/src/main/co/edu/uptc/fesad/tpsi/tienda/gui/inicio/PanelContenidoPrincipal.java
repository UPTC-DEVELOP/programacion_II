//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.inicio;

import java.awt.CardLayout;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion.PanelAdministracionSistema;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion.PanelGestionCatalogo;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion.PanelGestionClientes;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion.PanelGestionVentas;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.login.PanelLogin;

/// Representa el panel principal de la aplicación para ingresar a los módulos del sistema
/// de la tienda virtual de libros.
public class PanelContenidoPrincipal extends PanelBase {
  
  /// Nombre del panel para el inicio de sesión.
  public static final String PANEL_INICIO_SESION = "PanelInicioSesion";
  
  /// Nombre del panel para la gestión del catálogo.
  public static final String PANEL_GESTION_CATALOGO = "PanelGestionCatalogo";
  
  /// Nombre del panel para la gestión de ventas.
  public static final String PANEL_GESTION_VENTAS = "PanelGestionVentas";
  
  /// Nombre del panel para la gestión de clientes.
  public static final String PANEL_GESTION_CLIENTES = "PanelGestionClientes";
  
  /// Nombre del panel para la administración del sistema.
  public static final String PANEL_ADMINISTRACION_SISTEMA = "PanelAdministracionSistema";
  
  /// Panel de inicio de sesión de usuario.
  private PanelLogin panelLogin;
  
  /// Panel de gestión de catálogo de libros.
  private PanelGestionCatalogo panelGestionCatalogo;
  
  /// Panel de gestión de ventas.
  private PanelGestionVentas panelGestionVentas;
  
  /// Panel de gestión de clientes.
  private PanelGestionClientes panelGestionClientes;
  
  /// Panel de administración del sistema.
  private PanelAdministracionSistema panelAdministracionSistema;
  
  /// Gestor para manejar los paneles como tarjetas.
  private CardLayout layoutCartas;
  
  /// Crea un nuevo objeto [PanelContenidoPrincipal][PanelContenidoPrincipal] con el
  /// enrutador de eventos dado.
  /// 
  /// @param enrutador Enrutador de los eventos.
  public PanelContenidoPrincipal(EnrutadorEventos enrutador) {
    super(enrutador);
  }
  
  @Override
  public void inicializarComponente() {
    // el panel interno será gestionado como si fuera un pila de cartas
    this.layoutCartas = new CardLayout();
    setLayout(this.layoutCartas);
    
    // crear el panel para el inicio de sesión del usuario
    this.panelLogin = (PanelLogin) new PanelLogin().construir();
    add(this.panelLogin, PanelContenidoPrincipal.PANEL_INICIO_SESION);
    
    // crear el panel para la gestión del catálogo
    this.panelGestionCatalogo = (PanelGestionCatalogo) new PanelGestionCatalogo(getEnrutadorEventos()).construir();
    
    add(this.panelGestionCatalogo, PanelContenidoPrincipal.PANEL_GESTION_CATALOGO);
    
    // crear el panel para la gestión de ventas
    this.panelGestionVentas = (PanelGestionVentas) new PanelGestionVentas().construir();
    add(this.panelGestionVentas, PanelContenidoPrincipal.PANEL_GESTION_VENTAS);
    
    // crear el panel para la gestión de clientes
    this.panelGestionClientes = (PanelGestionClientes) new PanelGestionClientes().construir();
    add(this.panelGestionClientes, PanelContenidoPrincipal.PANEL_GESTION_CLIENTES);
    
    // crear el panel para la administración del sistema
    this.panelAdministracionSistema = (PanelAdministracionSistema) new PanelAdministracionSistema()
      .construir();
    add(this.panelAdministracionSistema, PanelContenidoPrincipal.PANEL_ADMINISTRACION_SISTEMA);
  }
  
  /// Muestra el panel de inicio de sesión de usuario.
  public void mostrarInicioSesion() {
    this.layoutCartas.show(this, PanelContenidoPrincipal.PANEL_INICIO_SESION);
  }
  
  /// Muestra el panel especificado.
  /// 
  /// @param nombrePanel Nombre del panel que se desea mostrar.
  public void mostrarPanel(String nombrePanel) {
    this.layoutCartas.show(this, nombrePanel);
  }
  
  /// Obtiene el panel de inicio de sesión de usuario
  public PanelLogin getPanelLogin() { return this.panelLogin; }
  
  /// Obtiene el panel de gestión de catálogo.
  public PanelGestionCatalogo getPanelGestionCatalogo() { return this.panelGestionCatalogo; }
  
  /// Obtiene el panel de gestión de ventas.
  public PanelGestionVentas getPanelGestionVentas() { return this.panelGestionVentas; }
  
  /// Obtiene el panel de gestión de clientes.
  public PanelGestionClientes getPanelGestionClientes() { return this.panelGestionClientes; }
  
  /// Obtiene el panel de administración del sistema.
  public PanelAdministracionSistema getPanelAdministracionSistema() { return this.panelAdministracionSistema; }
  
}
