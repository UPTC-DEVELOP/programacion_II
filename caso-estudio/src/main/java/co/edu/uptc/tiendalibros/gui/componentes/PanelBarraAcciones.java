///
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa una barra que contiene botones de acción para un panel de gestión
/// de entidades.
public class PanelBarraAcciones extends PanelBase {
  
  /// Representa una barra que contiene los botones de acción.
  private JPanel barraBotones;
  
  public PanelBarraAcciones(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout(2, 4));
    
    this.barraBotones = new JPanel();
    this.barraBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
    
    add(this.barraBotones, BorderLayout.NORTH);
    
  }
  
  /// Agrega el botón dado a la barra de acciones.
  ///
  /// @param boton Botón que quiere agregar a la barra de acciones.
  public void agregarBotonAccion(JComponent boton) {
    this.barraBotones.add(boton);
  }
}
