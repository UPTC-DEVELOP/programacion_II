//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Dimension;

/// Representa una panel que se comporta como un Panel Maestro y ver Detalles.
public class PanelMaestroDetalles extends PanelBase {
  
  private PanelTablaLista panelLista;
  private PanelBase panelFormulario;
  
  public PanelMaestroDetalles(
    ModeloTablaLista modelo,
    PanelBase formulario
  ) {
    super();
    this.panelLista = new PanelTablaLista(modelo);
    this.panelFormulario = formulario;
    
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout(4, 4));
    
    this.panelLista = (PanelTablaLista) this.panelLista.construir();
    
    add(this.panelLista, BorderLayout.CENTER);
    
    // dar al formulario un tamaño fijo
    this.panelFormulario.setPreferredSize(new Dimension(280, 0));
    add(this.panelFormulario, BorderLayout.EAST);
    
  }
  
  public PanelTablaLista getPanelLista() { return this.panelLista; }
  
  public PanelBase getPanelFormulario() { return this.panelFormulario; }
  
  /// Actualiza la tabla que le muestra la lista de objetos de negocio al usuario.
  /// 
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.panelLista.listar(datos);
  }
  
}
