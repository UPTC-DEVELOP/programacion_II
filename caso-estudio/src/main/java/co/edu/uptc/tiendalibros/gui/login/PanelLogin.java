package co.edu.uptc.tiendalibros.gui.login;

import java.awt.GridBagLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import co.edu.uptc.tiendalibros.gui.componentes.AsociacionCamposFormulario;
import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.componentes.RestriccionesCelda;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Credencial;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un panel para el inicion de sesión de usuario.
public class PanelLogin extends PanelBase {
  
  /// Campo de texto para el correo electrónico.
  private JTextField txtCorreo;
  
  /// Campo de texto oculto para la contraseña.
  private JPasswordField txtContrasena;
  
  /// Nota de error del correo electrónico.
  private JLabel notaErrorCorreo;
  
  /// Nota de error de la contraseña.
  private JLabel notaErrorContrasena;
  
  /// Nota de error general, para los errores que no son de un campo (por ejemplo, que el
  /// correo o la contraseña no son correctos).
  private JLabel notaErrorGeneral;
  
  /// Botón para iniciar sesión.
  private JButton btnIniciarSesion;
  
  /// Botón para ir al registro de un cliente nuevo.
  private JButton btnRegistrarse;
  
  /// Asociación lógica de atributos, campos y notas de error.
  private final AsociacionCamposFormulario asociacionCampos;
  
  /// Crea un nuevo objeto [PanelLogin].
  /// 
  /// @param evento Objeto que maneja los eventos de los botones.
  public PanelLogin(Evento evento) {
    super(evento);
    this.asociacionCampos = new AsociacionCamposFormulario();
  }
  
  @Override
  protected void inicializarComponente() {
    setLayout(new GridBagLayout());
    
    JPanel tarjeta = new JPanel(new GridBagLayout());
    FabricaEstilos.fabrica(tarjeta)
      .bordeSeccion("Inicio de sesión");
    
    // crear los campos
    this.txtCorreo = new JTextField();
    FabricaEstilos.fabrica(this.txtCorreo)
      .campoTexto(26);
    
    this.txtContrasena = new JPasswordField();
    FabricaEstilos.fabrica(this.txtContrasena)
      .campoTexto(26);
    // al pulsar Enter en la contraseña se inicia sesión, igual que con el botón
    this.txtContrasena.setActionCommand(ControladorLogin.LOGIN_INICIAR);
    this.txtContrasena.addActionListener(getEvento());
    
    // crear las notas de error
    this.notaErrorCorreo = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorCorreo)
      .notaError();
    this.notaErrorContrasena = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorContrasena)
      .notaError();
    this.notaErrorGeneral = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorGeneral)
      .notaError();
    
    // asociar de manera lógica los campos con sus notas de error
    this.asociacionCampos.asociar("correoElectronico", this.txtCorreo, this.notaErrorCorreo);
    this.asociacionCampos.asociar("contrasena", this.txtContrasena, this.notaErrorContrasena);
    
    // crear los botones
    this.btnIniciarSesion = new JButton("Iniciar Sesión");
    this.btnIniciarSesion.setActionCommand(ControladorLogin.LOGIN_INICIAR);
    this.btnIniciarSesion.addActionListener(getEvento());
    FabricaEstilos.fabrica(this.btnIniciarSesion)
      .botonIniciarSesion()
      .tamanioPreferido();
    
    this.btnRegistrarse = new JButton("Registrarse");
    this.btnRegistrarse.setActionCommand(ControladorLogin.LOGIN_REGISTRAR);
    this.btnRegistrarse.addActionListener(getEvento());
    FabricaEstilos.fabrica(this.btnRegistrarse)
      .botonRegistrarse()
      .tamanioPreferido();
    
    ubicarComponentes(tarjeta);
    add(tarjeta);
  }
  
  /// Ubica los componentes en la tarjeta.
  ///
  /// @param tarjeta Panel que contiene los componentes del formulario.
  private void ubicarComponentes(JPanel tarjeta) {
    // crear la etiquetas de los campos
    JLabel lblCorreo = new JLabel("Correo electrónico:");
    JLabel lblContrasena = new JLabel("Contraseña:");
    // restricción base para los componentes
    RestriccionesCelda base = new RestriccionesCelda().insets(4, 8, 0, 8)
      .anclarIzquierda();
    
    // fila 0 y 1: correo y su nota de error
    tarjeta.add(
      lblCorreo,
      base.clonar()
        .gridy(0)
        .gridx(0)        
        .aGBC()
    );
    tarjeta.add(
      this.txtCorreo,
      base.clonar()
        .gridy(0)
        .gridx(1)
        .expandirHorizontal()
        .aGBC()
    );
    tarjeta.add(
      this.notaErrorCorreo,
      base.clonar()
        .gridy(1)
        .gridx(1)
        .expandirHorizontal()
        .aGBC()
    );
    
    // fila 2 y 3: contraseña y su nota de error
    tarjeta.add(
      lblContrasena,
      base.clonar()
        .gridy(2)
        .gridx(0)
        .aGBC()
    );
    tarjeta.add(
      this.txtContrasena,
      base.clonar()
        .gridy(2)
        .gridx(1)
        .expandirHorizontal()
        .aGBC()
    );
    tarjeta.add(
      this.notaErrorContrasena,
      base.clonar()
        .gridy(3)
        .gridx(1)
        .expandirHorizontal()
        .aGBC()
    );
    
    // fila 4: nota de error general
    tarjeta.add(
      this.notaErrorGeneral,
      base.clonar()
        .gridy(4)
        .gridx(0)
        .ocuparFila()
        .expandirHorizontal()
        .aGBC()
    );
    
    // fila 5: botones
    JPanel panelBotones = new JPanel();
    panelBotones.setOpaque(false);
    panelBotones.add(this.btnIniciarSesion);
    panelBotones.add(this.btnRegistrarse);
    tarjeta.add(
      panelBotones,
      base.clonar()
        .insets(12, 8, 8, 8)
        .gridy(5)
        .gridx(0)
        .ocuparFila()
        .expandirHorizontal()
        .aGBC()
    );
  }
  
  /// Crea una credencial a partir de lo que el usuario ingresó en los campos.
  ///
  /// La contraseña se obtiene como un arreglo (array) de caracteres.
  ///
  /// @return La credencial con el correo y la contraseña escritos.
  public Credencial capturarCredencial() {
    return new Credencial(this.txtCorreo.getText(), this.txtContrasena.getPassword());
  }
  
  /// Muestra los errores de validación en las notas de error de cada campo.
  ///
  /// @param errores Errores de validación del inicio de sesión.
  public void mostrarErrores(ResultadoValidacion errores) {
    // borrar los errores anteriores
    limpiarErrores();
    
    // errores de los campos
    for (Map.Entry<String, List<String>> atributoConErrores : errores.getPorAtributo()
      .entrySet()) {
      AsociacionCamposFormulario.CamposAsociados asociados = this.asociacionCampos.getAsociacionCampos()
        .get(atributoConErrores.getKey());
      
      // si la capa de negocio reporta un atributo que este panel no tiene, se ignora
      if (asociados == null) {
        continue;
      }
      
      FabricaEstilos.fabrica(asociados.campo())
        .error();
      asociados.notaError()
        .setText(aTextoHtml(atributoConErrores.getValue()));
    }
    
    // errores generales
    if (errores.hayErroresGenerales()) {
      this.notaErrorGeneral.setText(aTextoHtml(errores.getGenerales()));
    }
  }
  
  /// Limpia los errores mostrados en los campos del panel.
  public void limpiarErrores() {
    for (AsociacionCamposFormulario.CamposAsociados asociados : this.asociacionCampos.getAsociacionCampos()
      .values()) {
      FabricaEstilos.fabrica(asociados.campo())
        .quitarError();
      asociados.notaError()
        .setText("");
    }
    this.notaErrorGeneral.setText("");
  }
  
  /// Limpia los campos y quita los errores mostrados.
  public void limpiarCampos() {
    this.txtCorreo.setText("");
    this.txtContrasena.setText("");
    limpiarErrores();
  }
  
  /// Limpia solo el campo de la contraseña.
  public void limpiarContrasena() {
    this.txtContrasena.setText("");
  }
  
  /// Pone el cursor en el campo del correo electrónico.
  public void enfocarCorreo() {
    this.txtCorreo.requestFocusInWindow();
  }
  
  /// Convierte los mensajes de error en un texto.
  /// 
  /// @param mensajes Mensajes de error.
  /// @return Texto HTML con los mensajes, uno por línea.
  private String aTextoHtml(List<String> mensajes) {
    StringBuilder texto = new StringBuilder();
    texto.append("<html><div style='width:")
      .append(300)
      .append("px;font-family:SansSerif'>");
    
    for (int i = 0; i < mensajes.size(); i++) {
      texto.append(mensajes.get(i));
      if (i < mensajes.size() - 1) {
        texto.append("<br>");
      }
    }
    
    texto.append("</div></html>");
    return texto.toString();
  }
}