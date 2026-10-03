/// @author Andres Avila
/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

/// Representa un panel base para crear otros paneles.
public class PanelBase extends JPanel {
  
  private Evento evento;
  
  // indica si el panel está inicializado completamente
  private boolean inicializado = false;
  
  /// Inicializa un nuevo objeto [PanelBase][PanelBase].
  public PanelBase() {
    super();
  }
  
  public PanelBase(LayoutManager layout) {
    super(layout);
  }
  
  /// Crea un nuevo objeto [PanelBase][PanelBase].
  public PanelBase(Evento evento) {
    super();
    this.evento = evento;
  }
  
  public PanelBase(
    LayoutManager layout,
    Evento evento
  ) {
    super(layout);
    this.evento = evento;
  }
  
  /// Inicializa todos los componentes de este componente.
  protected void inicializarComponente() {
    // las clases derivadas deber sobreescribir este método
  }
  
  
  /// Termina la inicialización del componente. Invoca al método inicializarComponente().
  public final PanelBase construir() {
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
  
  public Evento getEvento() { return this.evento; }
}
