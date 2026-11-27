package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.persistencia.BaseDeDatosFactura;

public class GestorFactura implements IGestionFactura {

	private IGestionFactura facturaBD;

	public GestorFactura(IGestionFactura gestionFactura) {
		this.facturaBD = gestionFactura;
	}

	@Override
	public boolean procesarYGuardarFactura(Factura factura) {
		if (factura == null || factura.getItems() == null || factura.getItems().isEmpty()) {
			System.out.println("La factura está vacía o no tiene ítems.");
			return false;
		}

		return facturaBD.procesarYGuardarFactura(factura);
	}

	@Override
	public List<Factura> listarFacturas() {
		return facturaBD.listarFacturas();
	}

	@Override
	public Factura busquedaPorFiltro(String filtro) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int obtenerSiguienteNumeroFactura() {
		return facturaBD.obtenerSiguienteNumeroFactura();
	}

}