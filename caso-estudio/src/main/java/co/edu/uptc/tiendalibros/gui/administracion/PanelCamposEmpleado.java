//
package co.edu.uptc.tiendalibros.gui.administracion;

import java.awt.GridBagLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import co.edu.uptc.tiendalibros.gui.componentes.AsociacionCamposFormulario;
import co.edu.uptc.tiendalibros.gui.componentes.PanelCamposBase;
import co.edu.uptc.tiendalibros.gui.componentes.RestriccionesCelda;
import co.edu.uptc.tiendalibros.gui.componentes.ValidadorFormulario;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.modelo.Rol;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;

/// Representa el panel con los campos de un empleado y de su cuenta de usuario.
public class PanelCamposEmpleado extends PanelCamposBase<ElementoVistaEmpleado, DatosEmpleado> {
  
  /// Roles que se pueden asignar a un empleado.
  private static final Rol[] ROLES_EMPLEADO = {
    Rol.GESTOR_CATALOGO,
    Rol.GESTOR_VENTAS,
    Rol.ADMINISTRADOR };
  
  
  /// Combo con el tipo de documento.
  private JComboBox<String> cmbTipoDocumento;
  
  /// Campo de texto para el número de documento.
  private JTextField txtNumeroDocumento;
  
  /// Campo de texto para los nombres.
  private JTextField txtNombres;
  
  /// Campo de texto para los apellidos.
  private JTextField txtApellidos;
  
  /// Campo de texto para el correo electrónico.
  private JTextField txtCorreoElectronico;
  
  /// Combo con el rol del empleado.
  private JComboBox<String> cmbRol;
  
  /// Casilla para indicar si la cuenta está habilitada.
  private JCheckBox chkHabilitado;
  
  /// Texto que explica qué pasa con la contraseña al editar.
  private JLabel lblAyudaContrasena;
  
  /// Campo para la contraseña.
  private JPasswordField txtContrasena;
  
  /// Campo para la confirmación de la contraseña.
  private JPasswordField txtConfirmacion;
  
  /// Notas de error de cada campo.
  private JLabel notaErrorTipoDocumento;
  private JLabel notaErrorNumeroDocumento;
  private JLabel notaErrorNombres;
  private JLabel notaErrorApellidos;
  private JLabel notaErrorCorreoElectronico;
  private JLabel notaErrorRol;
  private JLabel notaErrorContrasena;
  private JLabel notaErrorConfirmacion;
  
  /// Crea un nuevo objeto [PanelCamposEmpleado].
  ///
  /// @param asociacionCampos Asociación lógica de atributos, campos y notas de error.
  public PanelCamposEmpleado(AsociacionCamposFormulario asociacionCampos) {
    super(asociacionCampos);
  }
  
  @Override
  protected void inicializarComponente() {
    FabricaEstilos.fabrica(this)
      .bordeSeccion("Datos del empleado y de su cuenta");
    setBackground(null);
    setLayout(new GridBagLayout());
    
    // crear el combo del tipo de documento
    this.cmbTipoDocumento = new JComboBox<>(new String[] { "CC", "CE" });
    FabricaEstilos.fabrica(this.cmbTipoDocumento)
      .comboBox();
    
    // crear los campos de texto
    this.txtNumeroDocumento = crearCampoTexto(20);
    this.txtNombres = crearCampoTexto(30);
    this.txtApellidos = crearCampoTexto(30);
    this.txtCorreoElectronico = crearCampoTexto(30);
    
    // crear el combo del rol: la primera opción es el texto de ayuda, y siguen los roles
    this.cmbRol = new JComboBox<>(crearOpcionesRol());
    FabricaEstilos.fabrica(this.cmbRol)
      .comboBox();
    
    // crear la casilla de cuenta habilitada
    this.chkHabilitado = new JCheckBox("Cuenta habilitada para iniciar sesión");
    
    // crear los campos de contraseña
    this.lblAyudaContrasena = new JLabel();
    this.txtContrasena = new JPasswordField();
    FabricaEstilos.fabrica(this.txtContrasena)
      .campoTexto(30);
    this.txtConfirmacion = new JPasswordField();
    FabricaEstilos.fabrica(this.txtConfirmacion)
      .campoTexto(30);
    
    // crear las notas de errores para cada campo
    this.notaErrorTipoDocumento = crearNotaError();
    this.notaErrorNumeroDocumento = crearNotaError();
    this.notaErrorNombres = crearNotaError();
    this.notaErrorApellidos = crearNotaError();
    this.notaErrorCorreoElectronico = crearNotaError();
    this.notaErrorRol = crearNotaError();
    this.notaErrorContrasena = crearNotaError();
    this.notaErrorConfirmacion = crearNotaError();
    
    // asociar de manera lógica los campos con sus notas de error.
    // Los nombres de atributo son los mismos que usa la capa de negocio para reportar los
    // errores
    asociarCampo("tipoDocumento", this.cmbTipoDocumento, this.notaErrorTipoDocumento);
    asociarCampo("numeroDocumento", this.txtNumeroDocumento, this.notaErrorNumeroDocumento);
    asociarCampo("nombres", this.txtNombres, this.notaErrorNombres);
    asociarCampo("apellidos", this.txtApellidos, this.notaErrorApellidos);
    asociarCampo("correoElectronico", this.txtCorreoElectronico, this.notaErrorCorreoElectronico);
    asociarCampo("rol", this.cmbRol, this.notaErrorRol);
    asociarCampo("contrasena", this.txtContrasena, this.notaErrorContrasena);
    asociarCampo("confirmacion", this.txtConfirmacion, this.notaErrorConfirmacion);
    
    ubicarComponentes();
  }
  
  @Override
  public void prepararCampos(ElementoVistaEmpleado elementoVista) {
    setElementoVista(elementoVista);
    
    DatosEmpleado datos = elementoVista.getElemento();
    
    this.cmbTipoDocumento.setSelectedItem(datos.tipoDocumento());
    this.txtNumeroDocumento.setText(datos.numeroDocumento());
    this.txtNombres.setText(datos.nombres());
    this.txtApellidos.setText(datos.apellidos());
    this.txtCorreoElectronico.setText(datos.correoElectronico());
    this.cmbRol.setSelectedIndex(obtenerIndiceRol(datos.rol()));
    this.chkHabilitado.setSelected(datos.habilitado());
    
    // las contraseñas nunca se vuelven a mostrar
    this.txtContrasena.setText("");
    this.txtConfirmacion.setText("");
    
    // al editar, la contraseña es opcional
    if (datos.id() == null) {
      this.lblAyudaContrasena.setText("Escribe la contraseña inicial del empleado.");
    } else {
      this.lblAyudaContrasena.setText("Dejarla en blanco para conservar la contraseña actual.");
    }
  }
  
  @Override
  public ResultadoValidacion validarCampos() {
    // las reglas de validación están en la capa de negocio
    return new ValidadorFormulario().getErrores();
  }
  
  @Override
  public DatosEmpleado capturarDatos() {
    // el ID viene del empleado que se muestra. Es nulo si el empleado es nuevo
    Long id = getElementoVista().getElemento()
      .id();
    
    // obtener los datos que el usuario ingresó
    DatosEmpleado datos = new DatosEmpleado(
      id,
      (String) this.cmbTipoDocumento.getSelectedItem(),
      this.txtNumeroDocumento.getText(),
      this.txtNombres.getText(),
      this.txtApellidos.getText(),
      this.txtCorreoElectronico.getText(),
      obtenerRolSeleccionado(),
      this.chkHabilitado.isSelected(),
      this.txtContrasena.getPassword(),
      this.txtConfirmacion.getPassword()
    );
    
    // los campos de contraseña se vacían apenas se capturan los datos
    this.txtContrasena.setText("");
    this.txtConfirmacion.setText("");
    
    return datos;
  }
  
  /// Crea las opciones del combo de roles: el texto de ayuda y el nombre de cada rol.
  ///
  /// @return Opciones del combo de roles.
  private DefaultComboBoxModel<String> crearOpcionesRol() {
    DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
    // este es la opción vacía o nula
    modelo.addElement("Selecciona un rol");
    // agregar los roles que un empleado puede tener
    for (Rol rol : ROLES_EMPLEADO) {
      modelo.addElement(rol.getNombreVisible());
    }
    return modelo;
  }
  
  /// Obtiene el índice del combo que le corresponde a un rol.
  ///
  /// @param rol Rol que se quiere mostrar. Puede ser nulo.
  /// @return Índice de la opción del rol. Es 0 (texto de ayuda) si el rol es nulo.
  private int obtenerIndiceRol(Rol rol) {
    for (int i = 0; i < ROLES_EMPLEADO.length; i++) {
      if (ROLES_EMPLEADO[i] == rol) {
        // la opción 0 es el texto de ayuda
        return i + 1;
      }
    }
    return 0;
  }
  
  /// Obtiene el rol que el usuario eligió en el combo.
  ///
  /// @return Rol elegido. O `null` si no eligió ninguno.
  private Rol obtenerRolSeleccionado() {
    int indice = this.cmbRol.getSelectedIndex();
    // la opción 0 es el texto de ayuda
    if (indice <= 0) {
      return null;
    }
    return ROLES_EMPLEADO[indice - 1];
  }
  
  /// Crea un campo de texto con el estilo de los formularios.
  ///
  /// @param columnas Cantidad de columnas del campo.
  /// @return Campo de texto con estilo.
  private JTextField crearCampoTexto(int columnas) {
    JTextField campo = new JTextField();
    FabricaEstilos.fabrica(campo)
      .campoTexto(columnas);
    return campo;
  }
  
  /// Crea una nota de error con el estilo de los formularios.
  ///
  /// @return Nota de error sin texto.
  private JLabel crearNotaError() {
    JLabel nota = new JLabel();
    FabricaEstilos.fabrica(nota)
      .notaError();
    return nota;
  }
  
  /// Ubica las etiquetas, los campos y las notas de error en el panel.
  private void ubicarComponentes() {
    // crear una restricción base para todos los componentes del formulario
    RestriccionesCelda base = new RestriccionesCelda().insets(4, 4, 0, 4)
      .anclarIzquierda();
    
    // en esta oportunidad, vamos a usar un método para ubicar cada componente del campo a la
    // vez.
    // cada campo ocupa dos filas: la fila del campo y la fila de su nota de error
    agregarFila(0, "Tipo de documento:", this.cmbTipoDocumento, this.notaErrorTipoDocumento, base);
    agregarFila(2, "Número de documento:", this.txtNumeroDocumento, this.notaErrorNumeroDocumento, base);
    agregarFila(4, "Nombres:", this.txtNombres, this.notaErrorNombres, base);
    agregarFila(6, "Apellidos:", this.txtApellidos, this.notaErrorApellidos, base);
    agregarFila(8, "Correo electrónico:", this.txtCorreoElectronico, this.notaErrorCorreoElectronico, base);
    agregarFila(10, "Rol:", this.cmbRol, this.notaErrorRol, base);
    
    // la casilla de cuenta habilitada no tiene nota de error
    agregar(
      this.chkHabilitado,
      base.clonar()
        .gridy(12)
        .gridx(1)
    );
    
    // el texto de ayuda de la contraseña
    agregar(
      this.lblAyudaContrasena,
      base.clonar()
        .gridy(13)
        .gridx(1)
    );
    
    agregarFila(14, "Contraseña:", this.txtContrasena, this.notaErrorContrasena, base);
    agregarFila(16, "Confirmar contraseña:", this.txtConfirmacion, this.notaErrorConfirmacion, base);
  }
  
  /// Agrega una etiqueta, su campo y la nota de error del campo.
  ///
  /// @param fila      Fila donde se ubica el campo. La nota de error va en la fila
  ///                  siguiente.
  /// @param etiqueta  Texto de la etiqueta del campo.
  /// @param campo     Campo de datos.
  /// @param notaError Nota de error del campo.
  /// @param base      Restricción base para la celda.
  private void agregarFila(
    int fila,
    String etiqueta,
    javax.swing.JComponent campo,
    JLabel notaError,
    RestriccionesCelda base
  ) {
    // la x (columna) es la primera o la última columna
    // agregar la etiqueta del campo
    agregar(
      new JLabel(etiqueta),
      base.clonar()
        .gridy(fila)
        .gridx(0)
    );
    // agregar el campo
    agregar(
      campo,
      base.clonar()
        .gridy(fila)
        .gridx(1)
        .expandirHorizontal()
    );
    // agregar la nota de error del campo
    agregar(
      notaError,
      base.clonar()
        .gridy(fila + 1)
        .gridx(1)
        .expandirHorizontal()
    );
  }
}