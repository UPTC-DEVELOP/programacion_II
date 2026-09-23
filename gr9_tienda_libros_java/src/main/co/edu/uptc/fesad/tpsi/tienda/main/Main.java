//
package main.co.edu.uptc.fesad.tpsi.tienda.main;

import javax.swing.SwingUtilities;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.inicio.VentanaPrincipal;

/// Representa el punto de inicio de la aplicación
public class Main {
  
  /// Inicializa la aplicación
  void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Aplicacion aplicacion = new Aplicacion();
      VentanaPrincipal ventanaPrincipal = aplicacion.getVentanaPrincipal();
      
      ventanaPrincipal.setVisible(true);
    });
  }
}
