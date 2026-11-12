package co.edu.uptc.tienda;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import co.edu.uptc.tienda.gui.PanelCentral;
import co.edu.uptc.tienda.gui.VentanaPrincipal;
import co.edu.uptc.tienda.gui.clientes.PanelCliente;
import co.edu.uptc.tienda.gui.libros.PanelLibro;
import co.edu.uptc.tienda.negocio.GestionAutenticacion;
import co.edu.uptc.tienda.negocio.GestionCliente;
import co.edu.uptc.tienda.negocio.GestionLibro;
import co.edu.uptc.tienda.negocio.IGestionAutenticacion;
import co.edu.uptc.tienda.negocio.IGestionCliente;
import co.edu.uptc.tienda.negocio.IGestionLibro;
import co.edu.uptc.tienda.negocio.IRepositorioCliente;
import co.edu.uptc.tienda.persistencia.BaseDeDatosLibro;
import co.edu.uptc.tienda.persistencia.ClienteJSON;

public class Aplicacion {

	public static void main(String[] args) {

		// Pintamos la Interfaz Gráfica de Swing con un Look And Feel moderno (Nimbus)
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.err.println("Error al implementar el LookAndFeel de NimbusLookAndFeel");
			e.printStackTrace();
		}

		// Se inyectan dependencias.
		// Un único repositorio de clientes: el login debe ver a los clientes
		// registrados
		// Los clientes se guardan en data/clientes.json (antes: new LocalCliente(), en memoria)
		IRepositorioCliente repositorioCliente = new ClienteJSON();
		IGestionCliente gestionCliente = new GestionCliente(repositorioCliente);
		IGestionLibro gestionLibro = new GestionLibro(new BaseDeDatosLibro());

		// Se gestiona la Autenticación con el gestAutenticacion
		IGestionAutenticacion gestionAutenticacion = new GestionAutenticacion(repositorioCliente);

		// Se corre el programa en otro Hilo.
		SwingUtilities.invokeLater(() -> {

			PanelCentral panelCentral = new PanelCentral(new PanelCliente(gestionCliente),
					new PanelLibro(gestionLibro));
			VentanaPrincipal ventana = new VentanaPrincipal(gestionAutenticacion, panelCentral,
					new PanelCliente(gestionCliente), gestionLibro);
			ventana.setVisible(Boolean.TRUE);

		});

	}
}