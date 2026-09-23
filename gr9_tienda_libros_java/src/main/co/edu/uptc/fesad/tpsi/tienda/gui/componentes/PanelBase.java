/// @author Andres Avila
/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;

/// Representa un panel base para crear otros paneles.
public class PanelBase extends JPanel {
  
  /// Enrutador que asocia la ruta de eventos con las funciones que manejan los eventos.
  private EnrutadorEventos enrutadorEventos;
  
  // indica si el panel está inicializado completamente
  private boolean inicializado = false;
  
  /// Inicializa un nuevo objeto [PanelBase][PanelBase].
  public PanelBase() {
    super();
  }
  
  public PanelBase(LayoutManager layout) {
    super(layout);
  }
  
  /// Crea un nuevo objeto [PanelBase][PanelBase] con el enrutador de eventos dado.
  /// 
  /// @param enrutador Enrutador de los eventos.
  public PanelBase(EnrutadorEventos enrutador) {
    super();
    this.enrutadorEventos = enrutador;
  }
  
  public PanelBase(
    LayoutManager layout,
    EnrutadorEventos enrutador
  ) {
    super(layout);
    this.enrutadorEventos = enrutador;
  }
  
  /// Inicializa todos los componentes de este componente.
  protected void inicializarComponente() {
    // las clases derivadas deber sobreescribir este método
  }
  
  /// Obtiene el enrutador de eventos.
  public EnrutadorEventos getEnrutadorEventos() { return this.enrutadorEventos; }
  
  /// Establece el enrutador de eventos.
  /// 
  /// @param enrutador Enrutador de eventos.
  public void setEnrutadorEventos(EnrutadorEventos enrutador) { this.enrutadorEventos = enrutador; }
  
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
}
