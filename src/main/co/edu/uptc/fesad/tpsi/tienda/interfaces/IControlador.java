/// 
package main.co.edu.uptc.fesad.tpsi.tienda.interfaces;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;

/// Representa un controlador que interactúa entre la interfaz gráfica y los gestores. O
/// sea, entre la capa de vista y la capa de lógica.
public interface IControlador {
  /// Registra en el enrutador los eventos que este controlador manejará.
  public void registrarEventos(EnrutadorEventos enrutador);
}
