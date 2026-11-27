package co.edu.uptc.tiendalibros.gui.navegacion;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextPane;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.gui.login.ControladorLogin;

/// Representa el menú lateral para la navegación entre los módulos del sistema
/// informático.
///
/// Este menú tiene dos estados:
/// 
/// - Estado sin sesión: muestra el botón de inicio de sesión.
/// - Estado con sesión: muestra el nombre del usuario, los módulos a los que puede acceder
///   y el botón para cerrar la sesión.
///   
/// Nota: este panel solo muestra u oculta botones. [ControladorNavegacion] decide qué
/// módulos corresponden a cada usuario.
public class PanelMenuNavegacion extends PanelBase {
  
  /// Ancho de los botones del menú, considerando el el texto más largo con su ícono.
  private static final int ANCHO_BOTON = 260;
  
  /// Alto de los botones del menú.
  private static final int ALTO_BOTON = 50;
  
  private JTextPane txtUsuario;
  
  private JButton btnInicioSesion;
  private JButton btnGestionCatalogo;
  private JButton btnGestionVentas;
  private JButton btnGestionClientes;
  private JButton btnAdministracionSistema;
  
  private JButton btnExploracionCatalogo;
  
  /// Botón para cerrar la sesión. No es un módulo: no se marca como módulo activo.
  private JButton btnCerrarSesion;
  
  /// Colección que asocia la ruta de un módulo, con el botón del menú de navegación.
  private Map<String, JButton> moduloBotones;
  
  public PanelMenuNavegacion(Evento evento) {
    super(evento);
    // seis módulos
    this.moduloBotones = new HashMap<>(6);
  }
  
  @Override
  public void inicializarComponente() {
    
    // el menú lateral es un menú vertical
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    
    // texto con el nombre del usuario que inició sesión y su rol
    this.txtUsuario = new JTextPane();
    FabricaEstilos.fabrica(this.txtUsuario)
      .textoMultilinea();
    
    // crear los botones con un evento o ruta
    this.btnInicioSesion = crearBoton(
      "Inicio Sesión",
      IconoImagen.LOGIN,
      ControladorNavegacion.NAVEGACION_INICIO_SESION,
      true
    );
    this.btnGestionCatalogo = crearBoton(
      "Gestión de Catálogo",
      IconoImagen.CATALOGO,
      ControladorNavegacion.NAVEGACION_CATALOGO,
      true
    );
    this.btnGestionVentas = crearBoton(
      "Gestión de Ventas",
      IconoImagen.VENTAS,
      ControladorNavegacion.NAVEGACION_VENTAS,
      true
    );
    this.btnGestionClientes = crearBoton(
      "Gestión de Clientes",
      IconoImagen.CLIENTES,
      ControladorNavegacion.NAVEGACION_CLIENTES,
      true
    );
    this.btnAdministracionSistema = crearBoton(
      "Administración del Sistema",
      IconoImagen.ADMINISTRACION,
      ControladorNavegacion.NAVEGACION_ADMINISTRACION,
      true
    );
    
    this.btnExploracionCatalogo = crearBoton(
      "Explorar Catálogo",
      IconoImagen.EXPLORAR_CATALOGO,
      ControladorNavegacion.NAVEGACION_EXPLORACION_CATALOGO,
      true
    );
    
    // el botón de cerrar sesión no es un módulo: no se asocia en la colección de módulos
    this.btnCerrarSesion = crearBoton(
      "Cerrar Sesión",
      IconoImagen.CERRAR_SESION,
      ControladorLogin.LOGIN_CERRAR_SESION,
      false
    );
    
    // ahora que tenemos el último botón, se ajusta la alineación horizontal para ubicar el
    // texto de usuario de manera simular a los botones
    this.txtUsuario.setAlignmentX(this.btnCerrarSesion.getAlignmentX());
    
    // agregar los componentes
    add(this.txtUsuario);
    add(this.btnInicioSesion);
    add(this.btnExploracionCatalogo);
    add(this.btnGestionCatalogo);
    add(this.btnGestionVentas);
    add(this.btnGestionClientes);
    add(this.btnAdministracionSistema);
    add(this.btnCerrarSesion);
    
    // marcar el módulo inicial
    marcarModuloActivo(ControladorNavegacion.NAVEGACION_INICIO_SESION);
    
    // al iniciar la aplicación no hay sesión y se ocultan los módulos
    mostrarSinSesion();
  }
  
  /// Crea un botón del menú con el texto, el ícono y el comando de evento especificados.
  ///
  /// @param texto    Texto del botón.
  /// @param icono    Ícono del módulo.
  /// @param comando  Ruta del evento que se dispara al pulsar el botón.
  /// @param esModulo `true` si el botón abre un módulo. Si es `true` el botón recibe la
  ///                 lógica de marcar un módulo activo. Si es `false` el botón no hace parte
  ///                 de la lógica de marcar el módulo activo.
  private JButton crearBoton(String texto, IconoImagen icono, String comando, boolean esModulo) {
    // pues el botón de iniciar comparte una parte de la presentación con los otros botones.
    
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
    if (esModulo) {
      this.moduloBotones.put(comando, boton);
    }
    
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
  
  /// Muestra u oculta el botón del módulo con la ruta especificada.
  ///
  /// @param comando Ruta del módulo.
  /// @param visible `true` para mostrar el botón. `false` para ocultar el botón.
  public void setModuloVisible(String comando, boolean visible) {
    JButton boton = this.moduloBotones.get(comando);
    if (boton != null) {
      boton.setVisible(visible);
    }
  }
  
  /// Deja el menú en el estado sin sesión: solo se ve el botón de inicio de sesión.
  public void mostrarSinSesion() {
    for (JButton boton : this.moduloBotones.values()) {
      boton.setVisible(false);
    }
    this.btnInicioSesion.setVisible(true);
    this.btnCerrarSesion.setVisible(false);
    this.txtUsuario.setText("");
    this.txtUsuario.setToolTipText(null);
    this.txtUsuario.setVisible(false);
    revalidate();
    repaint();
  }
  
  /// Deja el menú en el estado con sesión.
  /// 
  /// Muestra el nombre del usuario y el botón de cerrar sesión, y oculta el botón de inicio
  /// de sesión.
  ///
  /// Los botones de los módulos se muestran con el método [#setModuloVisible].
  ///
  /// @param nombreVisible Nombre del usuario que inició sesión.
  public void mostrarSesion(String nombreVisible) {
    // mostrar los datos del usuario y los botones respectivo
    this.txtUsuario.setText(nombreVisible);
    this.txtUsuario.setToolTipText(nombreVisible);
    // antes de hace visible el panel, se ajusta el tamaño del panel de los datos de usuario
    ajustarAltoTextoUsuario();
    this.txtUsuario.setVisible(true);
    this.btnInicioSesion.setVisible(false);
    this.btnCerrarSesion.setVisible(true);
    revalidate();
    repaint();
  }
  
  /// Ajusta el alto del texto del usuario para mostrar todo el texto y teniendo en cuenta
  /// el ancho del menú de navegación.
  private void ajustarAltoTextoUsuario() {
    // Siempre que se inicie sesión el tamaño de se debe volver a calcular
    this.txtUsuario.setPreferredSize(null);
    this.txtUsuario.setMaximumSize(null);
    // se establece que el ancho sea el mismo del panel de navegación
    this.txtUsuario.setSize(PanelMenuNavegacion.ANCHO_BOTON, Short.MAX_VALUE);
    // ahora, calcular el alto preferido
    int alto = this.txtUsuario.getPreferredSize().height;
    Dimension tamanio = new Dimension(PanelMenuNavegacion.ANCHO_BOTON, alto);
    // establecer el tamaño final
    this.txtUsuario.setPreferredSize(tamanio);
    this.txtUsuario.setMaximumSize(tamanio);
  }
}