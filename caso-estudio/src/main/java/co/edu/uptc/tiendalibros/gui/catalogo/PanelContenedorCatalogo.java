/// 
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa un panel contenedor para mostrar el catálogo de libros.
public class PanelContenedorCatalogo extends PanelBase {
  
  private PanelExploradorCatalogo panelExploradorCatalogo;
  private PanelDerecho panelDerecho;
  
  public PanelContenedorCatalogo(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente();
    
    setLayout(new BorderLayout());
    
    this.panelExploradorCatalogo = (PanelExploradorCatalogo) new PanelExploradorCatalogo(getEvento()).construir();
    this.panelDerecho = (PanelDerecho) new PanelDerecho().construir();
    
    // el panel que muestra el catálogo de libros se agrega dentro de un scroll para tener la
    // barra de desplazamiento
    JScrollPane scrPnCatalogoLibros = new JScrollPane(
      this.panelExploradorCatalogo,
      ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
      ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    
    // ajustar el desplazamiento vertical del scroll para avanzar por fichas cómodamente
    scrPnCatalogoLibros.getVerticalScrollBar()
      .setUnitIncrement(16);
    
    add(scrPnCatalogoLibros, BorderLayout.CENTER);
    add(this.panelDerecho, BorderLayout.EAST);
  }
  
  public PanelExploradorCatalogo getPanelExploradorCatalogo() { return this.panelExploradorCatalogo; }
}
