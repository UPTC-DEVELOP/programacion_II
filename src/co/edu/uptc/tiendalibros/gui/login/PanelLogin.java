//
package co.edu.uptc.tiendalibros.gui.login;

import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;

/// Representa un panel para el inicion de sesión de usuario.
public class PanelLogin extends PanelBase {
  
  public PanelLogin() {
    super();
  }
  
  @Override
  protected void inicializarComponente() {
    JLabel lblAviso = new JLabel("Inicio de Sesión");
    add(lblAviso);
  }
}