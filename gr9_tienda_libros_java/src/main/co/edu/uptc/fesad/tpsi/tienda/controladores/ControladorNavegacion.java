//
package main.co.edu.uptc.fesad.tpsi.tienda.controladores;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.inicio.PanelContenidoPrincipal;

//
public class ControladorNavegacion extends ControladorBase {
  
  private PanelContenidoPrincipal panelContenidoPrincipal;
  
  public ControladorNavegacion(
    EnrutadorEventos enrutador,
    PanelContenidoPrincipal panel
  ) {
    super(enrutador);
    this.panelContenidoPrincipal = panel;
    
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.controladores.ControladorBase#inicializar()
  @Override
  public void inicializar() {
    // TODO
    
  }
  
  public void mostrarInicioSesion() {
    this.panelContenidoPrincipal.mostrarInicioSesion();
  }
  
  public void mostrarPanel(String nombrePanel) {
    this.panelContenidoPrincipal.mostrarPanel(nombrePanel);
  }
  
}
