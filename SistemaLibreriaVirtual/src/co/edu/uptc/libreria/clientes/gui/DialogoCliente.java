package co.edu.uptc.libreria.clientes.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;

public class DialogoCliente extends JDialog {

	private boolean isCrear;
	private JTextField txNombre;
	private JTextField txCorreo;
	private JTextField txDireccion;
	private JTextField txTelefono;
	private JComboBox<TipoCliente> cbxTipo;
	private JButton btnGuardar;
	private JButton btnCancelar;

	public DialogoCliente(Frame propietario, Evento evento, String titulo, boolean isCrear) {
		super(propietario, titulo, true);
		this.isCrear = isCrear;
		setSize(400, 280);
		setLayout(new BorderLayout());

		txNombre = new JTextField();
		txCorreo = new JTextField();
		txDireccion = new JTextField();
		txTelefono = new JTextField();
		cbxTipo = new JComboBox<TipoCliente>(TipoCliente.values());

		JPanel pCampos = new JPanel(new GridLayout(5, 2, 6, 8));
		pCampos.setBorder(BorderFactory.createEmptyBorder(12, 12, 4, 12));
		pCampos.add(new JLabel("Nombre completo"));
		pCampos.add(txNombre);
		pCampos.add(new JLabel("Correo electrónico"));
		pCampos.add(txCorreo);
		pCampos.add(new JLabel("Dirección de envío"));
		pCampos.add(txDireccion);
		pCampos.add(new JLabel("Teléfono de contacto"));
		pCampos.add(txTelefono);
		pCampos.add(new JLabel("Tipo de cliente"));
		pCampos.add(cbxTipo);

		btnGuardar = new JButton("Guardar");
		btnCancelar = new JButton("Cancelar");
		btnGuardar.addActionListener(evento);
		btnCancelar.addActionListener(evento);
		asignarComandoBotones();

		JPanel pBotones = new JPanel();
		pBotones.add(btnCancelar);
		pBotones.add(btnGuardar);

		add(pCampos, BorderLayout.CENTER);
		add(pBotones, BorderLayout.SOUTH);
		setLocationRelativeTo(propietario);
	}

	private void asignarComandoBotones() {
		btnCancelar.setActionCommand(Evento.CANCELAR_CLI);
		if (isCrear) {
			btnGuardar.setActionCommand(Evento.GUARDAR_CLI);
		} else {
			btnGuardar.setActionCommand(Evento.EDITAR_CLI);
		}
	}

	/** Arma un Cliente con lo digitado; las reglas de negocio se validan en GestionCliente. */
	public Cliente capturarDatos() {
		Cliente nuevo = new Cliente();
		nuevo.setNombreCompleto(txNombre.getText());
		nuevo.setCorreo(txCorreo.getText());
		nuevo.setDireccionEnvio(txDireccion.getText());
		nuevo.setTelefono(txTelefono.getText());
		nuevo.setTipo((TipoCliente) cbxTipo.getSelectedItem());
		return nuevo;
	}

	public void actualizarCampos(Cliente cliente) {
		txNombre.setText(cliente.getNombreCompleto());
		txCorreo.setText(cliente.getCorreo());
		txDireccion.setText(cliente.getDireccionEnvio());
		txTelefono.setText(cliente.getTelefono());
		cbxTipo.setSelectedItem(cliente.getTipo());
		// el correo es la llave del cliente, no se modifica al editar
		txCorreo.setEditable(false);
	}

	public void modoSoloLectura() {
		txNombre.setEditable(false);
		txCorreo.setEditable(false);
		txDireccion.setEditable(false);
		txTelefono.setEditable(false);
		cbxTipo.setEnabled(false);
		btnGuardar.setVisible(false);
		btnCancelar.setText("Cerrar");
	}

}