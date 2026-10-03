//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.catalogo;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import main.co.edu.uptc.fesad.tpsi.tienda.categoria.PanelCategorias;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.autor.PanelAutores;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.PanelEditoriales;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

/// Representa un panel para permitir la gestión del catálogo de libros.
public class PanelGestionCatalogo extends PanelBase {
  
  private JTabbedPane pestanas;
  
  /// Panel para la gestión de editoriales.
  private PanelEditoriales panelEditoriales;
  
  private PanelAutores panelAutores;
  
  private PanelCategorias panelCategorias;
  
  /// Crea un nuevo objeto [PanelGestionCatalogo][PanelGestionCatalogo]
  public PanelGestionCatalogo(Evento evento) {
    super(evento);
  }
  
  @Override
  protected void inicializarComponente() {
    setLayout(new BorderLayout());
    
    this.pestanas = new JTabbedPane(SwingConstants.TOP);
    
    // crear los paneles
    this.panelEditoriales = (PanelEditoriales) new PanelEditoriales(getEvento()).construir();
    
    this.panelAutores = (PanelAutores) new PanelAutores(getEvento()).construir();
    
    this.panelCategorias = (PanelCategorias) new PanelCategorias(getEvento()).construir();
    
    // agregar los paneles en forma de pestañas
    this.pestanas.add("Editoriales", this.panelEditoriales);
    this.pestanas.add("Autores", this.panelAutores);
    this.pestanas.add("Categorias", this.panelCategorias);
    
    // agregar las pestañas al panel
    add(this.pestanas, BorderLayout.CENTER);
  }
  
  /// Obtiene el panel para la gestión de editoriales.
  public PanelEditoriales getPanelEditoriales() { return this.panelEditoriales; }
  
  public PanelAutores getPanelAutores() { return this.panelAutores; }
  
}

