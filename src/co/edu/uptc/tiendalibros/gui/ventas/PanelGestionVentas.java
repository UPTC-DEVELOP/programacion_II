//
package co.edu.uptc.tiendalibros.gui.ventas;

import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;

//
public class PanelGestionVentas extends PanelBase {
  
  public PanelGestionVentas() {
    super();
  }
  
  @Override
  protected void inicializarComponente() {
    JLabel lblAviso = new JLabel("Gestión de Ventas");
    add(lblAviso);
  }
}
