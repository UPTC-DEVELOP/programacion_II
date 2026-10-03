/// 
package main.co.edu.uptc.fesad.tpsi.tienda.categoria;

import javax.swing.JLabel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

///
public class PanelCategorias extends PanelBase {
  
  public PanelCategorias(Evento evento) {
    super(evento);
    add(new JLabel("componentes para categoría"));
  }
}
