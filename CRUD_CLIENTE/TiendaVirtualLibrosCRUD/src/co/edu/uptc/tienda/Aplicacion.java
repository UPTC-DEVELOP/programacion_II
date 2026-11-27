package co.edu.uptc.tienda;

import co.edu.uptc.tienda.gui.PanelCentral;
import co.edu.uptc.tienda.gui.clientes.PanelCliente;
import co.edu.uptc.tienda.gui.facturas.PanelFactura;
import co.edu.uptc.tienda.gui.libros.PanelLibro;
import co.edu.uptc.tienda.gui.VentanaPrincipal;
import co.edu.uptc.tienda.negocio.GestionAutenticacion;
import co.edu.uptc.tienda.negocio.GestionCarrito;
import co.edu.uptc.tienda.negocio.GestionCliente;
import co.edu.uptc.tienda.negocio.GestionCompra;
import co.edu.uptc.tienda.negocio.GestionLibro;
import co.edu.uptc.tienda.negocio.GestorFactura;
import co.edu.uptc.tienda.negocio.IGestionAutenticacion;
import co.edu.uptc.tienda.negocio.IGestionCliente;
import co.edu.uptc.tienda.negocio.IGestionCompra;
import co.edu.uptc.tienda.negocio.IGestionFactura;
import co.edu.uptc.tienda.negocio.IGestionLibro;
import co.edu.uptc.tienda.negocio.IPersistenciaCarrito;
import co.edu.uptc.tienda.negocio.IRepositorioCliente;
import co.edu.uptc.tienda.negocio.IRepositorioLibro;
import co.edu.uptc.tienda.persistencia.BaseDeDatosFactura;
import co.edu.uptc.tienda.persistencia.BaseDeDatosLibro;
import co.edu.uptc.tienda.persistencia.ClienteJSON;
import co.edu.uptc.tienda.persistencia.PersistenciaCarritoJSON;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Aplicacion {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			System.err.println("Error al implementar el LookAndFeel de NimbusLookAndFeel");
			e.printStackTrace();
		}

		// Los clientes se guardan en data/clientes.json
		IRepositorioCliente repositorioCliente = new ClienteJSON();
		IGestionCliente gestionCliente = new GestionCliente(repositorioCliente);

		// Libros y facturas en base de datos
		IRepositorioLibro repositorioLibro = new BaseDeDatosLibro();
		IGestionLibro gestionLibro = new GestionLibro(repositorioLibro);
		IGestionFactura gestionFactura = new GestorFactura(new BaseDeDatosFactura());

		// Se gestiona la Autenticación con el gestor de autenticación
		IGestionAutenticacion gestionAutenticacion = new GestionAutenticacion(repositorioCliente);

		// Carrito de compras de la sesión con persistencia en JSON
		IPersistenciaCarrito persistenciaCarrito = new PersistenciaCarritoJSON();
		GestionCarrito gestionCarrito = new GestionCarrito(persistenciaCarrito, repositorioLibro);

		// Compra: convierte el carrito en factura
		IGestionCompra gestionCompra = new GestionCompra(gestionCarrito, gestionFactura);

		// Se corre el programa en otro Hilo.
		SwingUtilities.invokeLater(() -> {
			PanelCentral panelCentral = new PanelCentral(new PanelCliente(gestionCliente), new PanelLibro(gestionLibro),
					new PanelFactura(gestionFactura));
			VentanaPrincipal ventana = new VentanaPrincipal(gestionAutenticacion, panelCentral,
					new PanelCliente(gestionCliente), gestionLibro, gestionCarrito, gestionCompra, gestionFactura);
			ventana.setVisible(Boolean.TRUE);
		});
	}
}