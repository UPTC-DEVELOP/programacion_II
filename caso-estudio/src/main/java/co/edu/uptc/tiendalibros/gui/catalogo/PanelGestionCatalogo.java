//
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import co.edu.uptc.tiendalibros.gui.autor.ControladorAutor;
import co.edu.uptc.tiendalibros.gui.autor.PanelAutores;
import co.edu.uptc.tiendalibros.gui.categoria.ControladorCategoria;
import co.edu.uptc.tiendalibros.gui.categoria.PanelCategorias;
import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.componentes.PanelConPestanas;
import co.edu.uptc.tiendalibros.gui.editorial.ControladorEditorial;
import co.edu.uptc.tiendalibros.gui.editorial.PanelEditoriales;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.gui.libro.ControladorLibro;
import co.edu.uptc.tiendalibros.gui.libro.PanelLibros;

/// Representa un panel para permitir la gestión del catálogo de libros.
public class PanelGestionCatalogo extends PanelBase implements PanelConPestanas {
  
  /// Panel en forma de pestañas.
  private JTabbedPane pestanas;
  
  /// Panel para la gestión de editoriales.
  private PanelEditoriales panelEditoriales;
  
  /// Panel para la gestión de autores.
  private PanelAutores panelAutores;
  
  /// Panel para la gestión de categorías.
  private PanelCategorias panelCategorias;
  
  /// Panel para la gestión de libros.
  private PanelLibros panelLibros;
  
  /// Crea un nuevo objeto [PanelGestionCatalogo][PanelGestionCatalogo]
  public PanelGestionCatalogo(Evento evento) {
    super(evento);
  }
  
  @Override
  protected void inicializarComponente() {
    setLayout(new BorderLayout());
    
    // crear el panel tipo pestañas
    this.pestanas = new JTabbedPane(SwingConstants.TOP);
    FabricaEstilos.fabrica(this.pestanas)
      .pestanas();
    
    // crear los paneles de cada pestaña
    this.panelEditoriales = (PanelEditoriales) new PanelEditoriales(getEvento()).construir();
    
    this.panelAutores = (PanelAutores) new PanelAutores(getEvento()).construir();
    
    this.panelCategorias = (PanelCategorias) new PanelCategorias(getEvento()).construir();
    
    this.panelLibros = (PanelLibros) new PanelLibros(getEvento()).construir();
    
    // agregar los paneles en forma de pestañas
    
    agregarPestana("Editoriales", IconoImagen.EDITORIAL, this.pestanas, this.panelEditoriales);
    agregarPestana("Autores", IconoImagen.AUTOR, this.pestanas, this.panelAutores);
    agregarPestana("Categorías", IconoImagen.CATEGORIA, this.pestanas, this.panelCategorias);
    agregarPestana("Libros", IconoImagen.LIBRO, this.pestanas, this.panelLibros);
    
    // agregar las pestañas al panel
    add(this.pestanas, BorderLayout.CENTER);
    
    // establecer el evento cuando el usuario activa una pestaña
    this.pestanas.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JTabbedPane lasPestanas = (JTabbedPane) e.getSource();
        PanelBase panelSeleccionado = (PanelBase) lasPestanas.getSelectedComponent();
        
        // listar las editoriales
        if (panelSeleccionado instanceof PanelEditoriales) {
          getEvento().manejar(ControladorEditorial.EDITORIAL_LISTAR);
        }
        
        // listar los autores
        if (panelSeleccionado instanceof PanelAutores) {
          getEvento().manejar(ControladorAutor.AUTOR_LISTAR);
        }
        
        // listar las categorías
        if (panelSeleccionado instanceof PanelCategorias) {
          getEvento().manejar(ControladorCategoria.CATEGORIA_LISTAR);
        }
        
        // listar los libros
        if (panelSeleccionado instanceof PanelLibros) {
          getEvento().manejar(ControladorLibro.LIBRO_LISTAR);
        }
        
      }
    });
    

  }
  
  /// Obtiene el panel para la gestión de editoriales.
  public PanelEditoriales getPanelEditoriales() { return this.panelEditoriales; }
  
  /// Obtiene el panel para la gestión de autores.
  public PanelAutores getPanelAutores() { return this.panelAutores; }
  
  /// Obtiene el panel para la gestión de categorías.
  public PanelCategorias getPanelCategorias() { return this.panelCategorias; }
  
  /// Obtiene el panel para la gestión de libros.
  public PanelLibros getPanelLibros() { return this.panelLibros; }
  
}
