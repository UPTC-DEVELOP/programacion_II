/// @author Andres Avila

package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/// Representa la ventana base para crear ventanas.
public abstract class VentanaPrincipalBase extends JFrame {
  
  /// Crea un nuevo objeto [VentanaPrincipalBase][VentanaPrincipalBase]
  public VentanaPrincipalBase() {
    this("");
  }
  
  /// Crea un nuevo objeto [VentanaPrincipalBase][VentanaPrincipalBase] con el título dado.
  /// 
  /// @param titulo Título que se desea poner en la ventana.
  public VentanaPrincipalBase(String titulo) {
    super(titulo);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }
  
  /// Inicializa todos los componentes de este componente.
  protected abstract void inicializarComponente();
}
