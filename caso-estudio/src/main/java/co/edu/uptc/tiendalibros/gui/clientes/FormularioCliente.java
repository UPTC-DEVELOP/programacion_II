
package co.edu.uptc.tiendalibros.gui.clientes;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.ValidadorFormulario;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Cliente;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa el formulario para registrar, consultar y editar clientes.
public class FormularioCliente
  extends FormularioElementoBase<ElementoVistaCliente, Object> {

  /// Campos utilizados para capturar los datos del cliente.
  private JComboBox<String> cmbTipoDocumento;
  private JTextField txtNumeroDocumento;
  private JTextField txtNombres;
  private JTextField txtApellidos;
  private JTextField txtCorreoElectronico;
  private JTextField txtTelefono;
  private JTextField txtDireccionEnvio;
  private JComboBox<String> cmbTipoCliente;
  
  private JPasswordField txtContrasena;
  private JPasswordField txtConfirmacionContrasena;
  
  private JPanel panelCampos;

  /// Crea un formulario de clientes asociado al manejador de eventos.
  ///
  /// @param evento Objeto encargado de manejar los eventos del formulario.
  public FormularioCliente(Evento evento) {
    super(evento);
  }

  /// Inicializa los componentes visuales del formulario.
  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Cliente");

    Border sinBorde = BorderFactory.createEmptyBorder(3, 3, 3, 3);

    this.panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
    this.panelCampos.setBorder(sinBorde);

    // Campo para seleccionar el tipo de documento.
    this.cmbTipoDocumento = new JComboBox<>(
        new String[] {"CC", "TI", "CE", "PA", "RC"}
    );
    agregarCampo(this.panelCampos, "Tipo de documento:", this.cmbTipoDocumento);

    // Campo para ingresar el número de documento.
    this.txtNumeroDocumento = new JTextField(30);
    agregarCampo(this.panelCampos, "Número de documento:", this.txtNumeroDocumento);

    // Campo para ingresar los nombres.
    this.txtNombres = new JTextField(30);
    agregarCampo(this.panelCampos, "Nombres:", this.txtNombres);

    // Campo para ingresar los apellidos.
    this.txtApellidos = new JTextField(30);
    agregarCampo(this.panelCampos, "Apellidos:", this.txtApellidos);

    // Campo para ingresar el correo electrónico.
    this.txtCorreoElectronico = new JTextField(30);
    agregarCampo(this.panelCampos, "Correo electrónico:", this.txtCorreoElectronico);

    // Campo para ingresar el teléfono.
    this.txtTelefono = new JTextField(30);
    agregarCampo(this.panelCampos, "Teléfono:", this.txtTelefono);

    // Campo para ingresar la dirección de envío.
    this.txtDireccionEnvio = new JTextField(30);
    agregarCampo(this.panelCampos, "Dirección de envío:", this.txtDireccionEnvio);

    // Campo para seleccionar el tipo de cliente.
    this.cmbTipoCliente = new JComboBox<>(
        new String[] {"REGULAR", "PREMIUM"}
    );
    agregarCampo(this.panelCampos, "Tipo de cliente:", this.cmbTipoCliente);

    this.txtContrasena = new JPasswordField(30);
    this.txtConfirmacionContrasena = new JPasswordField(30);
    
    getPanelCampos().setLayout(new BorderLayout());
    getPanelCampos().add(this.panelCampos, BorderLayout.NORTH);
  }

  /// Agrega una etiqueta y un campo al panel del formulario.
  ///
  /// @param panel Panel que contiene los campos.
  /// @param texto Texto que identifica el campo.
  /// @param campo Componente utilizado para ingresar el dato.
  private void agregarCampo(
      JPanel panel,
      String texto,
      java.awt.Component campo
  ) {
    JLabel etiqueta = new JLabel(texto);
    etiqueta.setLabelFor(campo);
    panel.add(etiqueta);
    panel.add(campo);
  }

  /// Prepara los campos con la información del cliente seleccionado.
  ///
  /// @param elementoVista Elemento vista que contiene el cliente.
  @Override
  public void prepararCampos(ElementoVistaCliente elementoVista) {
    Cliente cliente = elementoVista.getElemento();

    this.cmbTipoDocumento.setSelectedItem(cliente.getTipoDocumento());
    this.txtNumeroDocumento.setText(cliente.getNumeroDocumento());
    this.txtNombres.setText(cliente.getNombres());
    this.txtApellidos.setText(cliente.getApellidos());
    this.txtCorreoElectronico.setText(cliente.getCorreoElectronico());
    this.txtTelefono.setText(cliente.getTelefono());
    this.txtDireccionEnvio.setText(cliente.getDireccionEnvio());
    this.cmbTipoCliente.setSelectedItem(cliente.getTipoCliente());
    
    agregarCampo(this.panelCampos, "Contraseña:", this.txtContrasena);
    agregarCampo(this.panelCampos, "Confirmar contraseña:", this.txtConfirmacionContrasena);
    this.txtContrasena.setText("");
    this.txtConfirmacionContrasena.setText("");
  }

  /// Obtiene el resultado de la validación del formulario.
  ///
  /// @return Resultado de la validación.
  @Override
  public ResultadoValidacion validarFormulario() {
    return new ValidadorFormulario().getErrores();
  }

  /// Captura los datos ingresados y construye un objeto Cliente.
  ///
  /// @return Cliente construido con los datos del formulario.
  @Override
  public Object capturarDatos() {
    Cliente clienteActual = getElementoVista().getElemento();

    Long id = clienteActual != null ? clienteActual.getId() : null;

    Cliente cliente = new Cliente(
        id,
        (String) this.cmbTipoDocumento.getSelectedItem(),
        this.txtNumeroDocumento.getText(),
        this.txtNombres.getText(),
        this.txtApellidos.getText(),
        this.txtCorreoElectronico.getText(),
        this.txtTelefono.getText(),
        this.txtDireccionEnvio.getText(),
        (String) this.cmbTipoCliente.getSelectedItem()
    );
    
    // al editar, solo se envían los datos del cliente
    if (id != null) {
      return cliente;
    }
    
    // al registrar, se envían también las contraseñas
    return new DatosRegistroCliente(
      cliente,
      this.txtContrasena.getPassword(),
      this.txtConfirmacionContrasena.getPassword()
    );
  }

  /// Describe el cliente que se está mostrando en el formulario.
  ///
  /// @return Descripción del cliente.
  @Override
  protected String describirElemento() {
    Cliente cliente = getElementoVista().getElemento();

    return String.format(
        "el cliente '%s %s'",
        cliente.getNombres(),
        cliente.getApellidos()
    );
  }

  /// Obtiene la ruta del evento encargado de guardar un cliente.
  @Override
  protected String getRutaEventoGuardar() {
    // return ControladorCliente.CLIENTE_GUARDAR;
    boolean nuevo = getElementoVista().getElemento()
      .getId() == null;
    
    return nuevo ? ControladorCliente.CLIENTE_REGISTRAR : ControladorCliente.CLIENTE_GUARDAR;
  }

  /// Obtiene la ruta del evento encargado de eliminar un cliente.
  @Override
  protected String getRutaEventoEliminar() {
    return ControladorCliente.CLIENTE_ELIMINAR;
  }
}
