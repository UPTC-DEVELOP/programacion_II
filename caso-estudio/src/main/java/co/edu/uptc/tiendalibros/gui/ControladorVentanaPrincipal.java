//
package co.edu.uptc.tiendalibros.gui;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;

/// Representa un controlador para la ventana principal.
public class ControladorVentanaPrincipal extends ControladorBase {
  
  /// Ruta del evento para mostrar un aviso.
  public static final String MOSTRAR_AVISO = "ventanaPrincipal.mostrarAviso";
  
  /// Ruta del evento para cerrar un aviso.
  public static final String CERRAR_AVISO = "ventanaPrincipal.cerrarAviso";
  
  /// Ventana principal de la aplicación.
  private VentanaPrincipal ventanaPrincipal;
  
  /// Crea un nuevo objeto [ControladorVentanaPrincipal][ ControladorVentanaPrincipal].
  ///
  /// @param enrutador Enrutador para comunicar eventos.
  /// @param ventana   Ventana principal de la aplicación.
  public ControladorVentanaPrincipal(
    EnrutadorEventos enrutador,
    VentanaPrincipal ventana
  ) {
    super(enrutador);
    this.ventanaPrincipal = ventana;
  }
  
  /// Muestra un aviso al usuario.
  ///
  /// @param aviso Aviso que se quiere mostrar al usuario.
  public void mostrarAviso(Object aviso) {
    if (!(aviso instanceof Aviso elAviso)) {
      return;
    }
    
    this.ventanaPrincipal.mostrarAviso(elAviso);
  }
  
  /// Cierra el aviso del usuario.
  @Override
  public void cerrarAviso() {
    this.ventanaPrincipal.cerrarAviso();
  }
  
  @Override
  public void registrarEventos() {
    // asociar el evento de mostrar un aviso
    getEnrutador().registrarEvento(ControladorVentanaPrincipal.MOSTRAR_AVISO, (aviso) -> mostrarAviso(aviso));
    
    // asociar el evento de cerrar un aviso
    getEnrutador().registrarEvento(ControladorVentanaPrincipal.CERRAR_AVISO, (evento) -> cerrarAviso());
  }
}
