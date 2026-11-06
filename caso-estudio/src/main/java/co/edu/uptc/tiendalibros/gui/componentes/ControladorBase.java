//
package co.edu.uptc.tiendalibros.gui.componentes;

import co.edu.uptc.tiendalibros.gui.ControladorVentanaPrincipal;
import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.interfaces.IControlador;

/// Representa un controlador base.
public abstract class ControladorBase implements IControlador {
  
  /// Enrutador de eventos.
  private final EnrutadorEventos enrutador;
  
  /// Crea un nuevo objeto [ControladorBase][ControladorBase].
  ///
  /// @param enrutador Enrutador de eventos.
  protected ControladorBase(EnrutadorEventos enrutador) {
    this.enrutador = enrutador;
  }
  
  @Override
  public abstract void registrarEventos();
  
  /// Obtiene el enrutador de eventos asignado al controlador.
  ///
  /// @return Enrutador de eventos asignado al controlador.
  public EnrutadorEventos getEnrutador() { return this.enrutador; }
  
  /// Muestra el aviso al usuario.
  ///
  /// @param aviso Aviso que se quiere mostrar al usuario.
  public void mostrarAviso(Aviso aviso) {
    this.enrutador.manejarEvento(ControladorVentanaPrincipal.MOSTRAR_AVISO, aviso);
  }
  
  /// Cierra el aviso mostrado al usuario.
  public void cerrarAviso() {
    this.enrutador.manejarEvento(ControladorVentanaPrincipal.CERRAR_AVISO);
  }
  
  
}
