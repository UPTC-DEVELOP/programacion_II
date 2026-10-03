/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.Dialog;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

///
public abstract class DialogoBase extends JDialog {
  
  private Evento evento;
  
  private boolean inicializado = false;
  
  protected DialogoBase(
    Window propietario,
    String titulo,
    Evento evento
  ) {
    super(propietario, titulo, Dialog.ModalityType.APPLICATION_MODAL);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.evento = evento;
  }
  

  
  protected abstract void inicializarComponente();
  
  protected abstract void asignarComandoBotones();
  
  

  public void mostrar() {
    setVisible(true);
  }
  
  public void cerrar() {
    dispose();
  }
  
  public final DialogoBase construir() {
    if (!this.inicializado) {
      this.inicializado = true;
      inicializarComponente();
      pack();
      setLocationRelativeTo(getOwner());
    }
    return this;
  }
  
  public Evento getEvento() { return this.evento; }
  
}
