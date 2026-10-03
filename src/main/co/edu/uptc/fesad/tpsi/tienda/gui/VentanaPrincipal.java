/// @author Andres Avila
/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui;

import java.awt.BorderLayout;

import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.VentanaBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IReceptorEventos;

/// Representa la ventana principal de la aplicación.
/// 
/// Esta ventana principal también sabe cómo manejar o atender las solicitudes de la
/// clase Evento.
public class VentanaPrincipal extends VentanaBase implements IReceptorEventos {
  
  private Evento evento;
  
  private PanelContenidoPrincipal panelContenidoPrincipal;
  
  private PanelMenuLateral menuLateral;
  
  private EnrutadorEventos enrutador;
  
  /// Crea un nuevo objeto de [VentanaPrincipal][VentanaPrincipal].
  public VentanaPrincipal() {
    super();
    // asignar el objeto evento vinculado a esta ventana
    this.evento = new Evento(this);
    
    this.enrutador = new EnrutadorEventos();

    inicializarComponente();
  }
  
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.VentanaBase#inicializarComponente()
  @Override
  protected void inicializarComponente() {
    
    setTitle("Tienda Libros - Grupo 9");
    setSize(1000, 600);
    
    getContentPane().setLayout(new BorderLayout());
    
    this.panelContenidoPrincipal = crearPanelContenidoPrincipal();
    this.menuLateral = crearMenuLateral();
    
    // crear un área central formada por un panel divisor que contiene al menú lateral y el
    // contenedor de contenidos
    JSplitPane panelDivisor = new JSplitPane(
      JSplitPane.HORIZONTAL_SPLIT, this.menuLateral, this.panelContenidoPrincipal
    );
    panelDivisor.setDividerLocation(200);
    panelDivisor.setDividerSize(2);
    // enable = false evita que el usuario cambie el tamaño de las áreas del panel divisor
    panelDivisor.setEnabled(false);
    
    getContentPane().add(panelDivisor, BorderLayout.CENTER);
    
    setLocationRelativeTo(null);
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IReceptorEventos#manejarEvento(java.lang.String, java.lang.Object)
  @Override
  public void manejarEvento(String evento, Object elemento) {
    // aquí la ventana puede manejar el evento que viene de Evento mediante condicionales
    // if-else
    
    if (evento.equals("CONSTANTE")) {
      // aquí va el método 1
    } else if (evento.equals("CONSTANTE")) {
      // aquí va el método 2
    } else {
      // y si no, preguntarle al enrutador de eventos qué hacer
      this.enrutador.manejarEvento(evento, elemento);
    }
    
  }
  

  

  
  /// Obtiene el panel del contenido principal de la aplicación.
  public PanelContenidoPrincipal getPanelContenidoPrincipal() { return this.panelContenidoPrincipal; }
  

  
  private PanelContenidoPrincipal crearPanelContenidoPrincipal() {
    return (PanelContenidoPrincipal) new PanelContenidoPrincipal(this.evento)
      .construir();
  }
  
  private PanelMenuLateral crearMenuLateral() {
    return (PanelMenuLateral) new PanelMenuLateral(this.evento).construir();
    
  }
  
  public EnrutadorEventos getEnrutador() { return this.enrutador; }
  
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      // crear la ventana principal
      VentanaPrincipal ventana = new VentanaPrincipal();
      // pedirle a la aplicación que inicialice componentes y que arranque la aplicación
      new Aplicacion(ventana).iniciar();
    });
  }
  
  

}
