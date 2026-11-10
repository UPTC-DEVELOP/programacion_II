package co.edu.uptc.tienda.gui;

import javax.swing.*;

import co.edu.uptc.tienda.gui.clientes.DialogoCliente;
import co.edu.uptc.tienda.gui.clientes.PanelCliente;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.negocio.IGestionAutenticacion;
import co.edu.uptc.tienda.negocio.NegocioException;

/**
 * Ventana principal de la aplicación Tienda Virtual de Libros.
 */
public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;
	private final IGestionAutenticacion gestionAutenticacion;
	private final PanelCentral panelCentral;
	private final PanelCliente panelCliente;
	private final PanelLogin panelLogin;
	private final Evento evento;

	// Cliente que inició sesión (null mientras no haya sesión)
	private Cliente clienteAutenticado;

	public VentanaPrincipal(IGestionAutenticacion gestionAutenticacion, PanelCentral panelCentral,
			PanelCliente panelCliente) {
		this.gestionAutenticacion = gestionAutenticacion;
		this.panelCentral = panelCentral;
		this.panelCliente = panelCliente;

		setTitle("Tienda Virtual de Libros");
		setSize(380, 180);
		setResizable(Boolean.FALSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		// Relaciones o Asociaciones
		evento = new Evento();
		evento.registrarAccion(Evento.LOGIN, this::loguear);
		evento.registrarAccion(Evento.REGISTRARSE, this::registrarseCliente);
		panelLogin = new PanelLogin(evento);

		add(panelLogin);
		getRootPane().setDefaultButton(panelLogin.getBotonIngresar());
	}

	private void loguear() {
		try {
			clienteAutenticado = gestionAutenticacion.autenticar(panelLogin.getUsuario(),
					panelLogin.getContrasena());
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
			panelLogin.limpiarContrasena();
			return;
		}

		JOptionPane.showMessageDialog(this, "Bienvenido(a), " + clienteAutenticado.getPrimerNombre() + " "
				+ clienteAutenticado.getPrimerApellido() + ".", "Inicio de sesión exitoso",
				JOptionPane.INFORMATION_MESSAGE);

		setTitle("Tienda Virtual de Libros - " + clienteAutenticado.getPrimerNombre() + " ("
				+ clienteAutenticado.getTipoCliente() + ")");
		getRootPane().setDefaultButton(null);
		this.panelLogin.setVisible(Boolean.FALSE);
		add(this.panelCentral);
		setResizable(Boolean.TRUE);
		setSize(850, 570);
		setLocationRelativeTo(null);
		this.panelCentral.setVisible(Boolean.TRUE);
	}

	private void registrarseCliente() {
		panelCliente.abrirDialogoCliente(null);
	}

	public Cliente getClienteAutenticado() {
		return clienteAutenticado;
	}

}
