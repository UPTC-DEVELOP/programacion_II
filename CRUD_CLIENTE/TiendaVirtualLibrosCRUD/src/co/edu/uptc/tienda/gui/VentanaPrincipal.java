package co.edu.uptc.tienda.gui;

import javax.swing.*;

import co.edu.uptc.tienda.gui.clientes.DialogoCliente;
import co.edu.uptc.tienda.gui.clientes.PanelCliente;
import co.edu.uptc.tienda.gui.compras.PanelCarritoCompras;
import co.edu.uptc.tienda.gui.compras.PanelCatalogo;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.negocio.IGestionAutenticacion;
import co.edu.uptc.tienda.negocio.IGestionCarrito;
import co.edu.uptc.tienda.negocio.IGestionCompra;
import co.edu.uptc.tienda.negocio.IGestionLibro;
import co.edu.uptc.tienda.negocio.NegocioException;

public class VentanaPrincipal extends JFrame {

	private static final long serialVersionUID = 1L;

	private final IGestionAutenticacion gestionAutenticacion;
	private final IGestionLibro gestionLibro;
	private final IGestionCarrito gestionCarrito;

	private final PanelCentral panelCentral;
	private final PanelCatalogo panelCatalogo;
	private final PanelCarritoCompras panelCarrito;

	// Pestañas que ve el cliente después del login: Catálogo | Carrito
	private final JTabbedPane pestanasCliente;
	private final PanelCliente panelCliente;
	private final PanelLogin panelLogin;
	private final Evento evento;

	// Cliente que inició sesión (null mientras no haya sesión)
	private Cliente clienteAutenticado;

	public VentanaPrincipal(IGestionAutenticacion gestionAutenticacion, PanelCentral panelCentral,
			PanelCliente panelCliente, IGestionLibro gestionLibro, IGestionCarrito gestionCarrito,
			IGestionCompra gestionCompra) {
		this.gestionAutenticacion = gestionAutenticacion;
		this.gestionLibro = gestionLibro;
		this.gestionCarrito = gestionCarrito;

		this.panelCentral = panelCentral;
		this.panelCliente = panelCliente;

		setTitle("Tienda Virtual de Libros");
		setSize(380, 180);
		setResizable(Boolean.FALSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		evento = new Evento();
		evento.registrarAccion(Evento.LOGIN, this::loguear);
		evento.registrarAccion(Evento.REGISTRARSE, this::registrarseCliente);
		panelLogin = new PanelLogin(evento);

		evento.registrarAccion(Evento.CERRAR_SESION, this::cerrarSesion);
		panelCatalogo = new PanelCatalogo(gestionLibro, gestionCarrito, evento);
		panelCarrito = new PanelCarritoCompras(gestionCarrito, gestionCompra);

		pestanasCliente = new JTabbedPane();
		pestanasCliente.addTab("Catálogo", panelCatalogo);
		pestanasCliente.addTab("Carrito de Compras", panelCarrito);
		// Al abrir la pestaña del carrito se muestran los libros agregados desde el
		// catálogo
		pestanasCliente.addChangeListener(e -> panelCarrito.actualizar());
		// Desde el carrito, el botón "Agregar libros del catálogo" abre la pestaña Catálogo
		panelCarrito.setAccionVerCatalogo(() -> pestanasCliente.setSelectedComponent(panelCatalogo));

		add(panelLogin);
		getRootPane().setDefaultButton(panelLogin.getBotonIngresar());
	}

	private void loguear() {
		try {
			clienteAutenticado = gestionAutenticacion.autenticar(panelLogin.getUsuario(), panelLogin.getContrasena());
		} catch (NegocioException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
			panelLogin.limpiarContrasena();
			return;
		}

		JOptionPane.showMessageDialog(
				this, "Bienvenido(a), " + clienteAutenticado.getPrimerNombre() + " "
						+ clienteAutenticado.getPrimerApellido() + ".",
				"Inicio de sesión exitoso", JOptionPane.INFORMATION_MESSAGE);

		if (clienteAutenticado.getTipoCliente().name().equals("ADMIN")) {

			setTitle("Tienda Virtual de Libros - " + clienteAutenticado.getPrimerNombre() + " ("
					+ clienteAutenticado.getTipoCliente() + ")");

			getRootPane().setDefaultButton(null);
			this.panelLogin.setVisible(Boolean.FALSE);
			setResizable(Boolean.TRUE);
			setSize(1100, 650);
			setLocationRelativeTo(null);
			add(this.panelCentral);
			this.panelCentral.setVisible(Boolean.TRUE);

		} else {

			setTitle("Tienda Virtual de Libros - " + clienteAutenticado.getPrimerNombre() + " ("
					+ clienteAutenticado.getTipoCliente() + ")");

			getRootPane().setDefaultButton(null);
			this.panelLogin.setVisible(Boolean.FALSE);
			this.panelCarrito.setCliente(clienteAutenticado);
			this.pestanasCliente.setSelectedIndex(0);
			add(this.pestanasCliente);
			setResizable(Boolean.TRUE);
			setSize(1100, 650);
			setLocationRelativeTo(null);
			this.pestanasCliente.setVisible(Boolean.TRUE);
		}

	}

	private void cerrarSesion() {
		// El carrito pertenece a la sesión: el siguiente cliente empieza vacío
		this.gestionCarrito.vaciar();
		this.panelCarrito.setCliente(null);

		this.pestanasCliente.setVisible(Boolean.FALSE);
		this.panelLogin.setVisible(Boolean.TRUE);

		setTitle("Tienda Virtual de Libros");
		setSize(380, 180);
		setLocationRelativeTo(null);
	}

	private void registrarseCliente() {
		panelCliente.abrirDialogoCliente(null);
	}

	public Cliente getClienteAutenticado() {
		return clienteAutenticado;
	}

}
