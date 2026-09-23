//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.gestion;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.PanelEditoriales;

/// Representa un panel para permitir la gestión del catálogo de libros.
public class PanelGestionCatalogo extends PanelBase {
  
  private JTabbedPane pestanas;
  
  /// Panel para la gestión de editoriales.
  private PanelEditoriales panelEditoriales;
  
  /// Crea un nuevo objeto [PanelGestionCatalogo][PanelGestionCatalogo] con el enrutador de
  /// eventos dado.
  /// 
  /// @param enrutador Enrutador de los eventos.
  public PanelGestionCatalogo(EnrutadorEventos enrutador) {
    super(enrutador);
  }
  
  @Override
  protected void inicializarComponente() {
    setLayout(new BorderLayout());
    
    this.pestanas = new JTabbedPane(SwingConstants.TOP);
    
    // crear los paneles
    this.panelEditoriales = (PanelEditoriales) new PanelEditoriales(getEnrutadorEventos()).construir();
    
    // agregar los paneles en forma de pestañas
    this.pestanas.add("Editoriales", this.panelEditoriales);
    
    // agregar las pestañas al panel
    add(this.pestanas, BorderLayout.CENTER);
  }
  
  /// Obtiene el panel para la gestión de editoriales.
  public PanelEditoriales getPanelEditoriales() { return this.panelEditoriales; }
}
