//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;

//
public class PanelMenuLateral extends PanelBase {
  
  private EnrutadorEventos enrutadorEventos;
  
  private JButton btnInicioSesion;
  private JButton btnGestionCatalogo;
  private JButton btnGestionVentas;
  private JButton btnGestionClientes;
  private JButton btnAdministracionSistema;
  
  public PanelMenuLateral(EnrutadorEventos enrutador) {
    super();
    this.enrutadorEventos = enrutador;
  }
  
  @Override
  public void inicializarComponente() {
    
    // el menú lateral es un menú vertical
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    int anchoBoton = 200;
    int altoBoton = 50;
    
    setPreferredSize(new Dimension(anchoBoton, 0));
    
    // crear el botón para mostrar el panel de inicio de sesión
    this.btnInicioSesion = new JButton("Inicio Sesión");
    this.btnInicioSesion.setMaximumSize(new Dimension(anchoBoton, altoBoton));
    // asignar el manejador de evento del botón para navegar al panel de inicio de sesión.
    this.btnInicioSesion.addActionListener((e) -> {
      this.enrutadorEventos.manejarEvento("navegacion.inicio_sesion");
    });
    // agregar el botón al panel de menú
    add(this.btnInicioSesion);
    
    // crear el botón para mostrar el panel de gestión del catálogo de libros
    this.btnGestionCatalogo = new JButton("Gestión de Catálogo");
    this.btnGestionCatalogo.setMaximumSize(new Dimension(anchoBoton, altoBoton));
    // asignar el manejador de evento del botón para navegar al panel de gestión del catálogo
    this.btnGestionCatalogo.addActionListener((e) -> {
      this.enrutadorEventos.manejarEvento("navegacion.gestion_catalogo");
    });
    // agregar el botón al panel de menú
    add(this.btnGestionCatalogo);
    
    // crear el botón para mostrar el panel de gestión de ventas
    this.btnGestionVentas = new JButton("Gestión de Ventas");
    this.btnGestionVentas.setMaximumSize(new Dimension(anchoBoton, altoBoton));
    // asignar el manejador de evento del botón para navegar al panel de gestión de ventas
    this.btnGestionVentas.addActionListener((e) -> {
      this.enrutadorEventos.manejarEvento("navegacion.gestion_ventas");
    });
    // agregar el botón al panel de menú
    add(this.btnGestionVentas);
    
    // crear el botón para mostrar el panel de gestión de clientes
    this.btnGestionClientes = new JButton("Gestión de Clientes");
    this.btnGestionClientes.setMaximumSize(new Dimension(anchoBoton, altoBoton));
    // asignar el manejador de evento del botón para navegar al panel de gestión de clientes
    this.btnGestionClientes.addActionListener((e) -> {
      this.enrutadorEventos.manejarEvento("navegacion.gestion_clientes");
    });
    // agregar el botón al panel de menú
    add(this.btnGestionClientes);
    
    // crear el botón para mostrar el panel de administración del sistema
    this.btnAdministracionSistema = new JButton("Administración del Sistema");
    this.btnAdministracionSistema.setMaximumSize(new Dimension(anchoBoton, altoBoton));
    // asignar el manejador de evento del botón para navegar al panel de adminitración del
    // sistema
    this.btnAdministracionSistema.addActionListener((e) -> {
      this.enrutadorEventos.manejarEvento("navegacion.administracion_sistema");
    });
    // agregar el botón al panel de menú
    add(this.btnAdministracionSistema);
  }
}
