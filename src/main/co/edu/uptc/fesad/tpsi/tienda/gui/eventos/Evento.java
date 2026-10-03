/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IReceptorEventos;

/// Representa un escuchador de eventos de la interfaz gráfica.
public class Evento implements ActionListener {
  
  /// El objeto interfaz que sabe cómo manejar el evento
  private final IReceptorEventos receptor;
  

  
  public Evento(IReceptorEventos receptor) {
    // se asigna el objeto que sabe cómo manejar el evento.
    // pero se asocia con una interfaz en vez de la clase concreta VentanaPrincipal
    // es para practicar algo de SOLID
    this.receptor = receptor;
  }
  
  @Override
  public void actionPerformed(ActionEvent e) {
    // Obtener el evento (ruta)
    // ActionCommand: el comando de acción del componente es el nombre o ruta del evento
    String evento = e.getActionCommand();
    // aquí Evento puede manejar el evento que viene mediante condicionales if-else
    
    if (evento.equals("CONSTANTE")) {
      // aquí va el método 1
    } else if (evento.equals("CONSTANTE")) {
      // aquí va el método 2
    } else {
      // o bien, preguntarle al receptor de eventos qué hacer
      this.receptor.manejarEvento(evento, null);
    }
  }
  
  /// Invoca un manejador de evento enviando el evento o ruta, y un elemento con datos.
  public void manejar(String evento, Object elemento) {
    this.receptor
      .manejarEvento(evento, elemento);
  }
  
  /// Invoca un manejador de evento enviando el evento o ruta, pero sin elemento
  /// (sin datos).
  public void manejar(String evento) {
    this.receptor
      .manejarEvento(evento, null);
  }
  
}
