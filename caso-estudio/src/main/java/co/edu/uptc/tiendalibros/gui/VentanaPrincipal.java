/// @author Andres Avila
///
package co.edu.uptc.tiendalibros.gui;

import java.awt.BorderLayout;
import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.PanelAvisos;
import co.edu.uptc.tiendalibros.gui.componentes.VentanaBase;
import co.edu.uptc.tiendalibros.gui.estilos.Fuentes;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.gui.navegacion.PanelMenuNavegacion;
import co.edu.uptc.tiendalibros.interfaces.IReceptorEventos;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;
import co.edu.uptc.tiendalibros.persistencia.conexiones.ConexionBaseDatos;
import co.edu.uptc.tiendalibros.persistencia.conexiones.ConfiguracionBaseDatos;

/// Representa la ventana principal de la aplicación.
///
/// Esta ventana principal también sabe cómo manejar o atender las solicitudes de la
/// clase Evento.
public class VentanaPrincipal extends VentanaBase implements IReceptorEventos {
  
  private Evento evento;
  
  private PanelContenidoPrincipal panelContenidoPrincipal;
  
  private PanelMenuNavegacion menuLateral;
  
  private EnrutadorEventos enrutador;
  
  private PanelAvisos panelAvisos;
  
  /// Crea un nuevo objeto de [VentanaPrincipal][VentanaPrincipal].
  public VentanaPrincipal() {
    super();
    // asignar el objeto evento vinculado a esta ventana
    this.evento = new Evento(this);
    
    this.enrutador = new EnrutadorEventos();
    
    inicializarComponente();
  }
  
  @Override
  protected void inicializarComponente() {
    
    setTitle("Tienda Libros - Grupo 9");
    setSize(1000, 900);
    establecerIconoAplicacion();
    
    getContentPane().setLayout(new BorderLayout());
    
    this.panelContenidoPrincipal = crearPanelContenidoPrincipal();
    this.menuLateral = crearMenuLateral();
    
    this.panelAvisos = (PanelAvisos) new PanelAvisos().construir();
    
    JPanel zonaContenido = new JPanel();
    zonaContenido.setLayout(new BorderLayout());
    
    zonaContenido.add(this.panelAvisos, BorderLayout.NORTH);
    zonaContenido.add(this.panelContenidoPrincipal, BorderLayout.CENTER);
    
    // crear un área central formada por un panel divisor que contiene al menú lateral y el
    // contenedor de contenidos
    JSplitPane panelDivisor = new JSplitPane(
      JSplitPane.HORIZONTAL_SPLIT, this.menuLateral, zonaContenido
    );
    panelDivisor.setDividerSize(2);
    // enable = false evita que el usuario cambie el tamaño de las áreas del panel divisor
    panelDivisor.setEnabled(false);
    
    getContentPane().add(panelDivisor, BorderLayout.CENTER);
    
    setLocationRelativeTo(null);
  }
  
  /// @see co.edu.uptc.tiendalibros.interfaces.IReceptorEventos#manejarEvento(java.lang.String, java.lang.Object)
  @Override
  public void manejarEvento(String evento, Object elemento) {
    // aquí la ventana puede manejar el evento que viene de Evento mediante condicionales
    // if-else
    
    // if (evento.equals("CONSTANTE")) {
    // // aquí va el método 1
    // } else if (evento.equals("CONSTANTE")) {
    // // aquí va el método 2
    // } else {
    // y si no, preguntarle al enrutador de eventos qué hacer
    this.enrutador.manejarEvento(evento, elemento);
    // }
    
  }
  
  /// Obtiene el panel del contenido principal de la aplicación.
  public PanelContenidoPrincipal getPanelContenidoPrincipal() { return this.panelContenidoPrincipal; }
  
  private PanelContenidoPrincipal crearPanelContenidoPrincipal() {
    return (PanelContenidoPrincipal) new PanelContenidoPrincipal(this.evento)
      .construir();
  }
  
  private PanelMenuNavegacion crearMenuLateral() {
    return (PanelMenuNavegacion) new PanelMenuNavegacion(this.evento).construir();
    
  }
  
  /// Obtiene el panel del menú de navegación.
  public PanelMenuNavegacion getMenuNavegacion() { return this.menuLateral; }
  
  public EnrutadorEventos getEnrutador() { return this.enrutador; }
  
  public void mostrarAviso(Aviso aviso) {
    this.panelAvisos.mostrar(aviso);
  }
  
  public void cerrarAviso() {
    this.panelAvisos.cerrar();
  }
  
  // Establece el ícono de la aplicación.
  private void establecerIconoAplicacion() {
    Optional<ImageIcon> icono = IconoImagen.APLICACION.icono(32);
    
    if (icono.isPresent()) {
      setIconImage(
        icono.get()
          .getImage()
      );
    }
  }
  
  public static void main(String[] args) {
    
    // cargar las fuentes y los íconos antes de crear la ventana
    Fuentes.precargar();
    IconoImagen.precargar();
    
    // probar la conexión con la base de datos
    ConfiguracionBaseDatos configuracionBaseDatos = ConfiguracionBaseDatos
      .cargar(ConfiguracionBaseDatos.RUTA_PREDETERMINADA);
    try (ConexionBaseDatos conexionBaseDatos = new ConexionBaseDatos(configuracionBaseDatos)) {
      String mensajeConexion = conexionBaseDatos.probar();
      JOptionPane.showMessageDialog(
        null,
        mensajeConexion,
        "Conexión Exitosa",
        JOptionPane.INFORMATION_MESSAGE
      );
    } catch (ErrorTecnicoSistemaException excepcion) {
      // throw new ErrorTecnicoSistemaException("Error técnico", excepcion);
      // mostrar el error al usuario y terminar la ejecución del programa
      String mensajeError = String.format("Error técnico: %n%s", excepcion.getMessage());
      
      JOptionPane.showMessageDialog(
        null,
        mensajeError,
        "Conexión Fallida",
        JOptionPane.INFORMATION_MESSAGE
      );
      System.exit(1);
    }
    
    // mostrar la ventana en el hilo o proceso propio de Swing
    SwingUtilities.invokeLater(() -> {
      // crear la ventana principal
      VentanaPrincipal ventana = new VentanaPrincipal();
      // pedirle a la aplicación que inicialice componentes y que arranque la aplicación
      new Aplicacion(ventana).iniciar();
    });
    
  }
  
}
