package co.edu.uptc.tienda.gui.clientes;

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

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;
import co.edu.uptc.tienda.negocio.IGestionCliente;
import co.edu.uptc.tienda.negocio.NegocioException;

/**
 * Diálogo modal para la creación y actualización de clientes. Solo captura y
 * muestra datos: las reglas de negocio las aplica {@link IGestionCliente}.
 */
public class DialogoCliente extends JDialog {

	private static final long serialVersionUID = 1L;

	private JTextField txtPrimerNombre;
	private JTextField txtOtrosNombres;
	private JTextField txtPrimerApellido;
	private JTextField txtOtrosApellidos;
	private JTextField txtTipoIdentificacion;
	private JTextField txtIdentificacion;
	private JTextField txtCorreoElectronico;
	private JTextField txtCelular;
	private JTextField txtDireccion;
	private JPasswordField txtContrasenia;
	private JComboBox<TipoCliente> comboTipoCliente;

	private final IGestionCliente gestionCliente;

	// null cuando se registra un cliente nuevo
	private final Cliente clienteEdicion;

	private boolean guardadoExitoso = false;

	public DialogoCliente(IGestionCliente gestionCliente, Cliente clienteAEditar) {
		this.gestionCliente = gestionCliente;
		this.clienteEdicion = clienteAEditar;

		if (clienteAEditar == null) {
			setTitle("Registro de Nuevo Cliente");
		} else {
			setTitle("Actualizar Cliente - " + clienteAEditar.getIdentificacion());
		}

		setSize(520, 520);
		setLocationRelativeTo(null);
		setModal(true);

		crearFormulario();

		if (clienteAEditar != null) {
			cargarDatosCliente(clienteAEditar);
		}
	}

	private void crearFormulario() {
		JPanel panelFormulario = new JPanel(new GridLayout(11, 2, 8, 8));

		txtPrimerNombre = new JTextField();
		txtOtrosNombres = new JTextField();
		txtPrimerApellido = new JTextField();
		txtOtrosApellidos = new JTextField();
		txtTipoIdentificacion = new JTextField();
		txtIdentificacion = new JTextField();
		txtCorreoElectronico = new JTextField();
		txtCelular = new JTextField();
		txtDireccion = new JTextField();
		txtContrasenia = new JPasswordField();
		comboTipoCliente = new JComboBox<>(TipoCliente.values());

		JButton btnGuardar = new JButton(clienteEdicion == null ? "Guardar" : "Actualizar");
		JButton btnCancelar = new JButton("Cancelar");

		panelFormulario.add(new JLabel("Primer nombre (*):"));
		panelFormulario.add(txtPrimerNombre);

		panelFormulario.add(new JLabel("Otros nombres:"));
		panelFormulario.add(txtOtrosNombres);

		panelFormulario.add(new JLabel("Primer apellido (*):"));
		panelFormulario.add(txtPrimerApellido);

		panelFormulario.add(new JLabel("Otros apellidos:"));
		panelFormulario.add(txtOtrosApellidos);

		panelFormulario.add(new JLabel("Tipo identificación (*):"));
		panelFormulario.add(txtTipoIdentificacion);

		panelFormulario.add(new JLabel("Identificación (*):"));
		panelFormulario.add(txtIdentificacion);

		panelFormulario.add(new JLabel("Correo electrónico (*):"));
		panelFormulario.add(txtCorreoElectronico);

		panelFormulario.add(new JLabel("Celular:"));
		panelFormulario.add(txtCelular);

		panelFormulario.add(new JLabel("Dirección:"));
		panelFormulario.add(txtDireccion);

		panelFormulario.add(new JLabel("Contraseña:"));
		panelFormulario.add(txtContrasenia);

		panelFormulario.add(new JLabel("Tipo cliente:"));
		panelFormulario.add(comboTipoCliente);

		JPanel contenedorFormulario = new JPanel(new BorderLayout());
		contenedorFormulario.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		contenedorFormulario.add(panelFormulario, BorderLayout.CENTER);
		add(contenedorFormulario, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel();
		panelBotones.add(btnGuardar);
		panelBotones.add(btnCancelar);
		add(panelBotones, BorderLayout.SOUTH);

		btnCancelar.addActionListener(e -> dispose());
		btnGuardar.addActionListener(e -> procesarGuardado());
	}

	private void cargarDatosCliente(Cliente cliente) {
		txtPrimerNombre.setText(cliente.getPrimerNombre());
		txtOtrosNombres.setText(cliente.getOtrosNombres() != null ? cliente.getOtrosNombres() : "");
		txtPrimerApellido.setText(cliente.getPrimerApellido());
		txtOtrosApellidos.setText(cliente.getOtrosApellidos() != null ? cliente.getOtrosApellidos() : "");
		txtTipoIdentificacion.setText(cliente.getTipoIdentificacion());
		txtIdentificacion.setText(cliente.getIdentificacion());
		// La identificación es la clave del cliente, no se puede modificar
		txtIdentificacion.setEditable(false);

		txtCorreoElectronico.setText(cliente.getCorreoElectronico());
		txtCelular.setText(cliente.getCelular() != null ? cliente.getCelular() : "");
		txtDireccion.setText(cliente.getDireccion() != null ? cliente.getDireccion() : "");
		// Si se deja vacía, el negocio conserva la contraseña actual
		txtContrasenia.setText("");

		comboTipoCliente.setSelectedItem(cliente.getTipoCliente());
	}

	private void procesarGuardado() {
		TipoCliente tipoCliente = (TipoCliente) comboTipoCliente.getSelectedItem();

		Cliente clienteProcesado = tipoCliente.crear(txtPrimerNombre.getText().trim(),
				txtOtrosNombres.getText().trim(), txtPrimerApellido.getText().trim(),
				txtOtrosApellidos.getText().trim(), txtTipoIdentificacion.getText().trim(),
				txtIdentificacion.getText().trim(), txtCorreoElectronico.getText().trim(),
				txtCelular.getText().trim(), txtDireccion.getText().trim(),
				new String(txtContrasenia.getPassword()).trim());

		try {
			if (clienteEdicion == null) {
				gestionCliente.agregarCliente(clienteProcesado);
				JOptionPane.showMessageDialog(this, "Cliente registrado exitosamente.", "Registro Exitoso",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				gestionCliente.actualizarCliente(clienteProcesado);
				JOptionPane.showMessageDialog(this, "Cliente actualizado exitosamente.", "Actualización Exitosa",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		guardadoExitoso = true;
		dispose();
	}

	public boolean isGuardadoExitoso() {
		return guardadoExitoso;
	}
}
