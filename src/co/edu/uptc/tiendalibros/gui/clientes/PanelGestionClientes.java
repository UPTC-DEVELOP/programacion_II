//
package co.edu.uptc.tiendalibros.gui.clientes;

import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;

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
