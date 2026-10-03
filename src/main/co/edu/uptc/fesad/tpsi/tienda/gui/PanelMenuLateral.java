//
package main.co.edu.uptc.fesad.tpsi.tienda.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

//
public class PanelMenuLateral extends PanelBase {
  
  private JButton btnInicioSesion;
  private JButton btnGestionCatalogo;
  private JButton btnGestionVentas;
  private JButton btnGestionClientes;
  private JButton btnAdministracionSistema;
  
  public PanelMenuLateral(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    
    // el menú lateral es un menú vertical
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    // crear los botones con un evento o ruta
    this.btnInicioSesion = crearBoton("Inicio Sesión", ControladorNavegacion.NAVEGACION_INICIO_SESION);
    this.btnGestionCatalogo = crearBoton("Gestión de Catálogo", ControladorNavegacion.NAVEGACION_CATALOGO);
    this.btnGestionVentas = crearBoton("Gestión de Ventas", ControladorNavegacion.NAVEGACION_VENTAS);
    this.btnGestionClientes = crearBoton("Gestión de Clientes", ControladorNavegacion.NAVEGACION_CLIENTES);
    this.btnAdministracionSistema = crearBoton(
      "Administración del Sistema",
      ControladorNavegacion.NAVEGACION_ADMINISTRACION
    );
    
    // agregar los botones
    add(this.btnInicioSesion);
    add(this.btnGestionCatalogo);
    add(this.btnGestionVentas);
    add(this.btnGestionClientes);
    add(this.btnAdministracionSistema);
  }
  
  /// Crea un botón que con el texto y el comando de evento especificados.
  private JButton crearBoton(String texto, String comando) {
    int anchoBoton = 200;
    int altoBoton = 50;
    
    JButton boton = new JButton(texto);
    boton.setMaximumSize(new Dimension(anchoBoton, altoBoton));
    // establecer el comando del evento
    boton.setActionCommand(comando);
    // establecer el listener del evento
    boton.addActionListener(getEvento());
    return boton;
  }
}
