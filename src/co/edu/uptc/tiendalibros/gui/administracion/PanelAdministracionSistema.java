//
package co.edu.uptc.tiendalibros.gui.administracion;

import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;

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
