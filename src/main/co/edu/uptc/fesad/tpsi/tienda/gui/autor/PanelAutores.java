/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.autor;

import javax.swing.JLabel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

///
public class PanelAutores extends PanelBase {
  
  //
  public PanelAutores(Evento evento) {
    super(evento);
    
    add(new JLabel("aquí van los componentes para autores"));
  }
}
