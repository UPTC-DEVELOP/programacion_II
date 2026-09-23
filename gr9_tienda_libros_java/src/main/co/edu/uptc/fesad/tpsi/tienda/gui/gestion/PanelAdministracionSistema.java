//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion;

import javax.swing.JLabel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;

//
public class PanelAdministracionSistema extends PanelBase {
  
  public PanelAdministracionSistema() {
    super();
  }
  
  @Override
  protected void inicializarComponente() {
    JLabel lblAviso = new JLabel("Administración Sistema");
    add(lblAviso);
  }
}
