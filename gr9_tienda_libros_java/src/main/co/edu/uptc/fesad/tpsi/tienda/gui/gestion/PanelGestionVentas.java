//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion;

import javax.swing.JLabel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;

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
