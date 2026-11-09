/// @author Andres Avila

package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.GraphicsConfiguration;
import java.awt.MouseInfo;

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
    GraphicsConfiguration gc = MouseInfo.getPointerInfo()
      .getDevice()
      .getDefaultConfiguration();
    super(titulo, gc);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
  }
  
  /// Inicializa todos los componentes de este componente.
  protected abstract void inicializarComponente();
}
