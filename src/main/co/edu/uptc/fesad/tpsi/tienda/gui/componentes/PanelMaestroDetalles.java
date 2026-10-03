//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Dimension;

/// Representa una panel que se comporta como un Panel Maestro y ver Detalles.
public class PanelMaestroDetalles extends PanelBase {
  
  private PanelLista panelLista;
  private PanelBase panelFormulario;
  
  public PanelMaestroDetalles(
    ModeloTablaLista modelo,
    PanelBase formulario
  ) {
    super();
    this.panelLista = (PanelLista) new PanelLista(modelo).construir();
    this.panelFormulario = formulario;
    
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout(4, 4));
    
    add(this.panelLista, BorderLayout.CENTER);
    
    // dar al formulario un tamaño fijo
    this.panelFormulario.setPreferredSize(new Dimension(280, 0));
    add(this.panelFormulario, BorderLayout.EAST);
    
  }
  
  public PanelLista getPanelLista() { return this.panelLista; }
  
  public PanelBase getPanelFormulario() { return this.panelFormulario; }
  
  /// Actualiza la tabla que le muestra la lista de objetos de negocio al usuario.
  /// 
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.panelLista.listar(datos);
  }
  
}
