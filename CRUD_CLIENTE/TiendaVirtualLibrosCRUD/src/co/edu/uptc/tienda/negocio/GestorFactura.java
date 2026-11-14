package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.persistencia.BaseDeDatosFactura;

public class GestorFactura {

	private BaseDeDatosFactura facturaBD;

	public GestorFactura() {
		this.facturaBD = new BaseDeDatosFactura();
	}

	/**
	 * Recibe la factura completa desde la interfaz o la lógica de compras y
	 * solicita su registro en la base de datos a través del DAO.
	 */
	public boolean procesarYGuardarFactura(Factura factura) {
		if (factura == null || factura.getItems() == null || factura.getItems().isEmpty()) {
			System.out.println("La factura está vacía o no tiene ítems.");
			return false;
		}

		return facturaBD.registrarFactura(factura);
	}
}
