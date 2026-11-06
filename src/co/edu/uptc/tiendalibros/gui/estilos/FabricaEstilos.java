//
package co.edu.uptc.tiendalibros.gui.estilos;

import javax.swing.JComponent;

/// Representa un fabricador que aplica estilos personalizados a los componentes Swing.
public class FabricaEstilos {
  
  public static AplicadorEstilos fabrica(JComponent componente) {
    return new AplicadorEstilos(componente);
  }
  
}
