//
package co.edu.uptc.fesad.tpsi.tienda.main;

import javax.swing.SwingUtilities;

import co.edu.uptc.fesad.tpsi.tienda.gui.VentanaPrincipal;

/// Representa el punto de inicio de la aplicación
public class Main {
  
  /// Inicializa la aplicación
  void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
      ventanaPrincipal.setVisible(true);
    });
  }
}
