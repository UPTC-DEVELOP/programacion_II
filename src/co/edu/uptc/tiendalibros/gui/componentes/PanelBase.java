/// @author Andres Avila
///
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa un panel base para crear otros paneles.
public class PanelBase extends JPanel {
  
  private Evento evento;
  
  // indica si el panel está inicializado completamente
  private boolean inicializado = false;
  
  /// Inicializa un nuevo objeto [PanelBase][PanelBase].
  public PanelBase() {
    super();
  }
  
  /// Crea un nuevo objeto [PanelBase][PanelBase] con el layout especificado.
  ///
  /// @param layout Layout que el panel tendrá.
  public PanelBase(LayoutManager layout) {
    super(layout);
  }
  
  /// Crea un nuevo objeto [PanelBase][PanelBase].
  ///
  /// @param evento Objeto que maneja eventos.
  public PanelBase(Evento evento) {
    super();
    this.evento = evento;
  }
  
  /// Crea un nuevo objeto [PanelBase][PanelBase] con el layout especificado.
  ///
  /// @param layout Layout que el panel tendrá.
  /// @param evento Objeto que maneja eventos.
  public PanelBase(
    LayoutManager layout,
    Evento evento
  ) {
    super(layout);
    this.evento = evento;
  }
  
  /// Inicializa todos los componentes de este componente. Las clases derivadas deber
  /// sobreescribir este método
  protected void inicializarComponente() {
    // las clases derivadas deber sobreescribir este método
  }
  
  /// Termina la inicialización del componente. Invoca al método inicializarComponente().
  public PanelBase construir() {
    // este método surgió de ensayo y error
    // las clases derivadas pueden inicializar el componente creando elementos como JButton,
    // JLabel, JTable, pero siempre se debe garantizar que el panel ya terminó su
    // inicialización
    if (!this.inicializado) {
      this.inicializado = true;
      inicializarComponente();
    }
    return this;
  }
  
  /// Obtiene el objeto que maneja eventos.
  ///
  /// @return Objeto que maneja eventos.
  public Evento getEvento() { return this.evento; }
}
