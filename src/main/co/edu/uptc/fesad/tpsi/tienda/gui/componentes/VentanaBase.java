/// @author Andres Avila

package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/// Representa la ventana base para crear ventanas.
public abstract class VentanaBase extends JFrame {
  
  /// Crea un nuevo objeto [VentanaBase][VentanaBase]
  public VentanaBase() {
    this("");
  }
  
  /// Crea un nuevo objeto [VentanaBase][VentanaBase] con el título dado.
  /// 
  /// @param titulo Título que se desea poner en la ventana.
  public VentanaBase(String titulo) {
    super(titulo);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }
  
  /// Inicializa todos los componentes de este componente.
  protected abstract void inicializarComponente();
}
