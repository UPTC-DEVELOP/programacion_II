package co.edu.uptc.gui.cliente;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import co.edu.uptc.gui.eventos.cliente.EventoCliente;
import co.edu.uptc.negocio.cliente.GestionCliente;
import co.edu.uptc.negocio.admin.ReglaNegocioException;
import co.edu.uptc.negocio.modelo.Cliente;
import co.edu.uptc.negocio.modelo.ClientePremium;
import co.edu.uptc.negocio.modelo.ClienteRegular;
import co.edu.uptc.negocio.modelo.TipoCliente;

/**
 * Formulario de cliente. Se usa para:
 *  - Crear un cliente / registrarse desde el login (clienteEditar = null)
 *  - Actualizar un cliente existente (clienteEditar != null): la identificacion
 *    y el tipo de cliente quedan bloqueados y la contrasenia es opcional.
 */
public class DialogoCliente extends JDialog {

    private static final long serialVersionUID = 1L;

    private static final String[] TIPOS_IDENTIFICACION = { "CC", "CE", "TI", "PA" };

    private JTextField txtPrimerNombre;
    private JTextField txtOtrosNombres;
    private JTextField txtPrimerApellido;
    private JTextField txtOtrosApellidos;
    private JComboBox<String> comboTipoIdentificacion;
    private JTextField txtIdentificacion;
    private JTextField txtCorreoElectronico;
    private JTextField txtCelular;
    private JTextField txtDireccion;
    private JPasswordField txtContrasenia;

    private JComboBox<TipoCliente> comboTipoCliente;

    private JButton btnGuardar;
    private JButton btnCancelar;

    private final GestionCliente gestionCliente;
    private final EventoCliente evento;
    private final Cliente clienteEditar;

    //Constructor
    public DialogoCliente(GestionCliente gestionCliente, EventoCliente evento, Cliente clienteEditar) {

    	this.gestionCliente = gestionCliente;
    	this.evento = evento;
    	this.clienteEditar = clienteEditar;

        setTitle(clienteEditar == null ? "Creación Cliente" : "Actualizar Cliente");
        setSize(500, 520);
        setLocationRelativeTo(null);
        //interaccion constante no puede devolverse a vp
        setModal(true);

        crearFormulario();
        if (clienteEditar != null) {
            cargarDatos(clienteEditar);
        }
    }

    private void crearFormulario() {

        JPanel panelFormulario =
                new JPanel(new GridLayout(11, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        txtPrimerNombre = new JTextField();
        txtOtrosNombres = new JTextField();
        txtPrimerApellido = new JTextField();
        txtOtrosApellidos = new JTextField();
        comboTipoIdentificacion = new JComboBox<>(TIPOS_IDENTIFICACION);
        txtIdentificacion = new JTextField();
        txtCorreoElectronico = new JTextField();
        txtCelular = new JTextField();
        txtDireccion = new JTextField();
        txtContrasenia = new JPasswordField();

        comboTipoCliente =
                new JComboBox<>(TipoCliente.values());

        //Botones
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");

        //Campos del formulario

        panelFormulario.add(new JLabel("Primer nombre: *"));
        panelFormulario.add(txtPrimerNombre);

        panelFormulario.add(new JLabel("Otros nombres:"));
        panelFormulario.add(txtOtrosNombres);

        panelFormulario.add(new JLabel("Primer apellido: *"));
        panelFormulario.add(txtPrimerApellido);

        panelFormulario.add(new JLabel("Otros apellidos:"));
        panelFormulario.add(txtOtrosApellidos);

        panelFormulario.add(new JLabel("Tipo identificación: *"));
        panelFormulario.add(comboTipoIdentificacion);

        panelFormulario.add(new JLabel("Identificación: *"));
        panelFormulario.add(txtIdentificacion);

        panelFormulario.add(new JLabel("Correo electrónico: *"));
        panelFormulario.add(txtCorreoElectronico);

        panelFormulario.add(new JLabel("Celular:"));
        panelFormulario.add(txtCelular);

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);

        panelFormulario.add(new JLabel(clienteEditar == null
                ? "Contraseña: *" : "Nueva contraseña (opcional):"));
        panelFormulario.add(txtContrasenia);


        panelFormulario.add(new JLabel("Tipo cliente:"));
        panelFormulario.add(comboTipoCliente);

        add(panelFormulario, BorderLayout.CENTER);

        //Botones finales
        JPanel panelBotones = new JPanel();

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        //Accion al click CANCELAR
        btnCancelar.addActionListener(e -> dispose());

        //ACCION AL CLIK GUARDAR

        btnGuardar.addActionListener(e -> guardarCliente());

    }

    //Modo edicion: se llenan los campos y se bloquea lo que no se puede cambiar
    private void cargarDatos(Cliente cliente) {
        txtPrimerNombre.setText(cliente.getPrimerNombre());
        txtOtrosNombres.setText(cliente.getOtrosNombres());
        txtPrimerApellido.setText(cliente.getPrimerApellido());
        txtOtrosApellidos.setText(cliente.getOtrosApellidos());
        comboTipoIdentificacion.setSelectedItem(cliente.getTipoIdentificacion());
        txtIdentificacion.setText(cliente.getIdentificacion());
        txtCorreoElectronico.setText(cliente.getCorreoElectronico());
        txtCelular.setText(cliente.getCelular());
        txtDireccion.setText(cliente.getDireccion());
        comboTipoCliente.setSelectedItem(cliente.getTipoCliente());

        txtIdentificacion.setEditable(false);
        comboTipoCliente.setEnabled(false);
    }

	    private void guardarCliente() {

	    	    String primerNombre = txtPrimerNombre.getText().trim();
	    	    String otrosNombres = txtOtrosNombres.getText().trim();
	    	    String primerApellido = txtPrimerApellido.getText().trim();
	    	    String otrosApellidos = txtOtrosApellidos.getText().trim();
	    	    String tipoIdentificacion = (String) comboTipoIdentificacion.getSelectedItem();
	    	    String identificacion = txtIdentificacion.getText().trim();
	    	    String correoElectronico = txtCorreoElectronico.getText().trim();
	    	    String celular = txtCelular.getText().trim();
	    	    String direccion = txtDireccion.getText().trim();
	    	    String contrasenia = new String(txtContrasenia.getPassword());
	    	    TipoCliente tipoCliente = (TipoCliente) comboTipoCliente.getSelectedItem();

	    	    Cliente cliente;

	    	    if (tipoCliente == TipoCliente.PREMIUM) {

	    	        cliente = new ClientePremium( primerNombre,otrosNombres, primerApellido, otrosApellidos,
	    	                tipoIdentificacion, identificacion, correoElectronico, celular, direccion, 0,tipoCliente,
	    	                contrasenia, null, 0);
	    	    } else {
	    	    	cliente = new ClienteRegular( primerNombre, otrosNombres, primerApellido, otrosApellidos,
	    	                tipoIdentificacion,identificacion,correoElectronico,celular, direccion,
	    	                0, tipoCliente,contrasenia, null,0);
	    	    }

	    	    try {
	    	        //GUARDARlo (el negocio valida y lanza el error con el mensaje para el usuario)
	    	        if (clienteEditar == null) {
	    	            gestionCliente.agregarCliente(cliente);
	    	        } else {
	    	            gestionCliente.actualizarCliente(cliente);
	    	        }
	    	    } catch (ReglaNegocioException ex) {
	    	        //Se deja el formulario abierto para que el usuario corrija
	    	        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    	        return;
	    	    }

	    	    evento.ejecutarEvento("GUARDAR");
	    	    //cerrrar
	    	    dispose();
	    	}
	    }
