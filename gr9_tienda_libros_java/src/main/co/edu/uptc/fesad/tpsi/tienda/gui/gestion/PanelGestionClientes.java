//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion;

import javax.swing.JLabel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;

//
public class PanelGestionClientes extends PanelBase {
  
  public PanelGestionClientes() {
    super();
  }
  
  @Override
  protected void inicializarComponente() {
    JLabel lblAviso = new JLabel("Gestión de Clientes");
    add(lblAviso);
  }
}
