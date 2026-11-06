//
package co.edu.uptc.tiendalibros.gui.navegacion;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa el menú lateral para la navegación entre los módulos del
/// sistema informático.
public class PanelMenuNavegacion extends PanelBase {
  
  /// Ancho de los botones del menú, considerando el el texto más largo con su ícono.
  private static final int ANCHO_BOTON = 260;
  
  /// Alto de los botones del menú.
  private static final int ALTO_BOTON = 50;
  
  private JButton btnInicioSesion;
  private JButton btnGestionCatalogo;
  private JButton btnGestionVentas;
  private JButton btnGestionClientes;
  private JButton btnAdministracionSistema;
  
  /// Colección que asocia la ruta de un módulo, con el botón del menú de navegación.
  private Map<String, JButton> moduloBotones;
  
  public PanelMenuNavegacion(Evento evento) {
    super(evento);
    // cinco módulos
    this.moduloBotones = new HashMap<>(5);
  }
  
  @Override
  public void inicializarComponente() {
    
    // el menú lateral es un menú vertical
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    // crear los botones con un evento o ruta
    this.btnInicioSesion = crearBoton(
      "Inicio Sesión",
      IconoImagen.LOGIN,
      ControladorNavegacion.NAVEGACION_INICIO_SESION
    );
    this.btnGestionCatalogo = crearBoton(
      "Gestión de Catálogo",
      IconoImagen.CATALOGO,
      ControladorNavegacion.NAVEGACION_CATALOGO
    );
    this.btnGestionVentas = crearBoton(
      "Gestión de Ventas",
      IconoImagen.VENTAS,
      ControladorNavegacion.NAVEGACION_VENTAS
    );
    this.btnGestionClientes = crearBoton(
      "Gestión de Clientes",
      IconoImagen.CLIENTES,
      ControladorNavegacion.NAVEGACION_CLIENTES
    );
    this.btnAdministracionSistema = crearBoton(
      "Administración del Sistema",
      IconoImagen.ADMINISTRACION,
      ControladorNavegacion.NAVEGACION_ADMINISTRACION
    );
    
    // agregar los botones
    add(this.btnInicioSesion);
    add(this.btnGestionCatalogo);
    add(this.btnGestionVentas);
    add(this.btnGestionClientes);
    add(this.btnAdministracionSistema);
    
    // marcar el módulo inicial
    marcarModuloActivo(ControladorNavegacion.NAVEGACION_INICIO_SESION);
  }
  
  /// Crea un botón del menú con el texto, el ícono y el comando de evento especificados.
  ///
  /// @param texto   Texto del botón.
  /// @param icono   Ícono del módulo.
  /// @param comando Ruta del evento que se dispara al pulsar el botón.
  private JButton crearBoton(String texto, IconoImagen icono, String comando) {
    
    JButton boton = new JButton(texto);
    // aplicar el estilo del botón de navegación
    FabricaEstilos.fabrica(boton)
      .botonMenuNavegacion(icono);
    // todos los botones del menú tienen el mismo tamaño
    Dimension tamanio = new Dimension(PanelMenuNavegacion.ANCHO_BOTON, PanelMenuNavegacion.ALTO_BOTON);
    boton.setPreferredSize(tamanio);
    boton.setMaximumSize(tamanio);
    
    // establecer el comando del evento
    boton.setActionCommand(comando);
    // establecer el listener del evento
    boton.addActionListener(getEvento());
    
    // agregar la asociación módulo con botón
    this.moduloBotones.put(comando, boton);
    
    return boton;
  }
  
  /// Marca el botón del menú de navegación de acuerdo a la ruta o comando especificado.
  ///
  /// Se aplica un estilo activo al botón del módulo actual. Los otros botones quedan con un
  /// estilo predeterminado.
  ///
  /// @param comando Ruta el módulo que se quiere marcar.
  public void marcarModuloActivo(String comando) {
    // recorrer la asociación módulo-botón para marcar el módulo activo y desmarcar los otros
    for (Map.Entry<String, JButton> mbPareja : this.moduloBotones.entrySet()) {
      String comandoActual = mbPareja.getKey();
      JButton botonActual = mbPareja.getValue();
      
      if (comandoActual.equals(comando)) {
        FabricaEstilos.fabrica(botonActual)
          .moduloActivo();
      } else {
        FabricaEstilos.fabrica(botonActual)
          .quitarModuloActivo();
      }
      
    }
  }
}
