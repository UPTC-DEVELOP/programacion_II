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

/**
 * Punto de entrada. Es el único lugar donde se eligen las implementaciones
 * concretas de cada capa: para cambiar de persistencia solo se modifica aquí.
 */
public class Aplicacion {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		IGestionCliente gestionCliente = new GestionCliente(new LocalCliente());
		IGestionLibro gestionLibro = new GestionLibro(new BaseDeDatosLibro());
		IGestionAutenticacion gestionAutenticacion = new GestionAutenticacion();

		SwingUtilities.invokeLater(() -> {
			PanelCentral panelCentral = new PanelCentral(new PanelCliente(gestionCliente),
					new PanelLibro(gestionLibro));
			VentanaPrincipal ventana = new VentanaPrincipal(gestionAutenticacion, panelCentral);
			ventana.setVisible(Boolean.TRUE);
		});
	}
}
