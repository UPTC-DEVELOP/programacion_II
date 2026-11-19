//
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.componentes.PanelConPestanas;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa el panel contenedor para la funcionalidad de explorar el catálogo
/// de libros.
public class PanelExploracionCatalogo extends PanelBase implements PanelConPestanas {
  
  /// Panel en forma de pestañas.
  private JTabbedPane pestanas;
  
  /// Panel para explorar el catálogo
  private PanelCatalogo panelCatalogo;
  
  public PanelExploracionCatalogo(Evento evento) {
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
    this.panelCatalogo = (PanelCatalogo) new PanelCatalogo(getEvento()).construir();
    
    // agregar los paneles en forma de pestaña
    agregarPestana("Explorar Catálogo", IconoImagen.PESTANA_CATALOGO, this.pestanas, this.panelCatalogo);
    
    // agregar las pestañas al panel
    add(this.pestanas, BorderLayout.CENTER);
  }
  
}
