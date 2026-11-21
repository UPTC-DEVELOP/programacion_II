/// 
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.awt.Dimension;

import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;

///
public class PanelDerecho extends PanelBase {
  public PanelDerecho() {
    
  }
  
  @Override
  protected void inicializarComponente() {
    super.inicializarComponente();
    
    setPreferredSize(new Dimension(280, 0));
    
    add(new JLabel("panel derecho"));
  }
}
