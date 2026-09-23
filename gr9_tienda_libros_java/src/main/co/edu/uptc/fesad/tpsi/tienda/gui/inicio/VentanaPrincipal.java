/// @author Andres Avila
/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.inicio;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelMenuLateral;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.VentanaPrincipalBase;

/// Representa la ventana principal de la aplicación.
public class VentanaPrincipal extends VentanaPrincipalBase {
  
  private PanelContenidoPrincipal panelContenidoPrincipal;
  
  private PanelMenuLateral menuLateral;
  
  private EnrutadorEventos enrutadorEventos;
  
  /// Crea un nuevo objeto de [VentanaPrincipal][VentanaPrincipal] con el enrutador de
  /// eventos dado.
  /// 
  /// @param enrutador Enrutador de eventos.
  public VentanaPrincipal(EnrutadorEventos enrutador) {
    super();
    this.enrutadorEventos = enrutador;
    inicializarComponente();
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.VentanaPrincipalBase#inicializarComponente()
  @Override
  protected void inicializarComponente() {
    setTitle("Tienda Libros - Grupo 9");
    setSize(1000, 600);
    
    getContentPane().setLayout(new BorderLayout());
    
    crearPanelesContenido();
    crearMenuLateral();
    
    // crear un área central formada por un panel divisor que contiene al menú lateral y el
    // contenedor de contenidos
    JSplitPane panelDivisor = new JSplitPane(
      JSplitPane.HORIZONTAL_SPLIT, this.menuLateral, this.panelContenidoPrincipal
    );
    panelDivisor.setDividerLocation(200);
    panelDivisor.setDividerSize(2);
    // enable = false evita que el usuario cambie el tamaño de las áreas del panel divisor
    panelDivisor.setEnabled(false);
    
    getContentPane().add(panelDivisor, BorderLayout.CENTER);
  }
  
  /// Obtiene el panel del contenido principal de la aplicación.
  public PanelContenidoPrincipal getPanelContenidoPrincipal() { return this.panelContenidoPrincipal; }
  
  public EnrutadorEventos getEnrutadorEventos() { return this.enrutadorEventos; }
  
  public void setEnrutadorEventos(EnrutadorEventos enrutador) { this.enrutadorEventos = enrutador; }
  
  private void crearPanelesContenido() {
    this.panelContenidoPrincipal = (PanelContenidoPrincipal) new PanelContenidoPrincipal(getEnrutadorEventos())
      .construir();
  }
  
  private void crearMenuLateral() {
    this.menuLateral = (PanelMenuLateral) new PanelMenuLateral(this.enrutadorEventos).construir();
    
  }
  
}
