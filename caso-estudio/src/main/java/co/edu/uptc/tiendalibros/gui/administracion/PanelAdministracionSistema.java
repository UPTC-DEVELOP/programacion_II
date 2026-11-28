//
package co.edu.uptc.tiendalibros.gui.administracion;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.componentes.PanelConPestanas;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa un panel para la administración del sistema.
public class PanelAdministracionSistema extends PanelBase implements PanelConPestanas {
  
  /// Panel en forma de pestañas.
  private JTabbedPane pestanas;
  
  /// Panel para la gestión de empleados.
  private PanelEmpleados panelEmpleados;
  
  /// Crea un nuevo objeto [PanelAdministracionSistema].
  ///
  /// @param evento Objeto que maneja eventos.
  public PanelAdministracionSistema(Evento evento) {
    super(evento);
  }
  
  @Override
  protected void inicializarComponente() {
    setLayout(new BorderLayout());
    
    this.pestanas = new JTabbedPane(SwingConstants.TOP);
    FabricaEstilos.fabrica(this.pestanas)
      .pestanas();
    
    this.panelEmpleados = (PanelEmpleados) new PanelEmpleados(getEvento()).construir();
    agregarPestana("Empleados", IconoImagen.EMPLEADOS, this.pestanas, this.panelEmpleados);
    
    add(this.pestanas, BorderLayout.CENTER);
  }
  
  /// Obtiene el panel de gestión de empleados.
  public PanelEmpleados getPanelEmpleados() { return this.panelEmpleados; }
}