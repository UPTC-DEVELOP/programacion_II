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
import co.edu.uptc.tienda.persistencia.BaseDeDatosLibro;
import co.edu.uptc.tienda.persistencia.LocalCliente;

public class Aplicacion {

	public static void main(String[] args) {

		// Pintamos la Interfaz Gráfica de Swing con un Look And Feel moderno (Nimbus)
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.err.println("Error al implementar el LookAndFeel de NimbusLookAndFeel");
			e.printStackTrace();
		}

		// Se inyectan dependencias
		IGestionCliente gestionCliente = new GestionCliente(new LocalCliente());
		IGestionLibro gestionLibro = new GestionLibro(new BaseDeDatosLibro());

		// Se gestiona la Autenticación con el gestAutenticacion
		IGestionAutenticacion gestionAutenticacion = new GestionAutenticacion();

		// Se corre el programa en otro Hilo.
		SwingUtilities.invokeLater(() -> {

			PanelCentral panelCentral = new PanelCentral(new PanelCliente(gestionCliente),
					new PanelLibro(gestionLibro));
			VentanaPrincipal ventana = new VentanaPrincipal(gestionAutenticacion, panelCentral,
					new PanelCliente(gestionCliente));
			ventana.setVisible(Boolean.TRUE);

		});

	}
}
