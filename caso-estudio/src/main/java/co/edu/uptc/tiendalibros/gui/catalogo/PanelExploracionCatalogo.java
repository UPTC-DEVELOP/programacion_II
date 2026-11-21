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
  private PanelContenedorCatalogo panelContenedorCatalogo;
  
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
    this.panelContenedorCatalogo = (PanelContenedorCatalogo) new PanelContenedorCatalogo(getEvento()).construir();
    
    // agregar los paneles en forma de pestaña
    agregarPestana("Explorar Catálogo", IconoImagen.PESTANA_CATALOGO, this.pestanas, this.panelContenedorCatalogo);
    
    // agregar las pestañas al panel
    add(this.pestanas, BorderLayout.CENTER);
    
    // si llegamos a necesitar otra pestaña, usar este código para el listener de las pestañas
    /*
     * // establecer el evento cuando el usuario activa una pestaña
     * this.pestanas.addChangeListener(new ChangeListener() {
     * 
     * @Override public void stateChanged(ChangeEvent e) { JTabbedPane lasPestanas =
     * (JTabbedPane) e.getSource(); PanelBase panelSeleccionado = (PanelBase)
     * lasPestanas.getSelectedComponent();
     * 
     * // listar los libros y sus formatos en el explorador del catálogo if
     * (panelSeleccionado.getClass() == PanelContenedorCatalogo.class) {
     * getEvento().manejar(ControladorExploradorCatalogo.CATALOGO_LISTAR); }
     * 
     * } });
     */
  }
  
  /// Obtiene el panel que muestra las fichas de los libros del catálogo.
  public PanelExploradorCatalogo getPanelExploradorCatalogo() { return this.panelContenedorCatalogo.getPanelExploradorCatalogo(); }
  
}
