/// @author Andres Avila
/// 
package co.edu.uptc.fesad.tpsi.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import co.edu.uptc.fesad.tpsi.tienda.gui.base.PanelBase;
import co.edu.uptc.fesad.tpsi.tienda.gui.base.VentanaPrincipalBase;

/// Representa la ventana principal de la aplicación.
public class VentanaPrincipal extends VentanaPrincipalBase {
  
  private PanelBase panelPrincipal;
  
  /// @see co.edu.uptc.fesad.tpsi.tienda.gui.base.VentanaPrincipalBase#inicializarComponente()
  @Override
  protected void inicializarComponente() {
    setTitle("Tienda Libros - Grupo 9");
    setSize(500, 400);
    
    this.panelPrincipal = new PanelBase();
    this.panelPrincipal.add(new JLabel("inicio"));
    
    getContentPane().setLayout(new BorderLayout());
    
    getContentPane().add(this.panelPrincipal, BorderLayout.CENTER);
  }
  
}
