//
package main.co.edu.uptc.fesad.tpsi.tienda.controladores;

/// Representa un controlador base
public abstract class ControladorBase {
  
  /// El enrutador que asocia eventos y sus manejadores.
  private EnrutadorEventos enrutadorEventos;
  
  /// Crea un nuevo objeto [ControladorBase][ControladorBase].
  public ControladorBase(EnrutadorEventos enrutador) {
    this.enrutadorEventos = enrutador;
  }
  
  public EnrutadorEventos getEnrutadorEventos() { return this.enrutadorEventos; }
  
  /// Inicializa la lógica de control del controlador.
  public abstract void inicializar();
}
