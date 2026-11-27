package co.edu.uptc.tienda.negocio;

import java.util.List;
import co.edu.uptc.tienda.modelo.Factura;

public interface IGestionFactura {

	boolean procesarYGuardarFactura(Factura factura);

	List<Factura> listarFacturas();

	Factura busquedaPorFiltro(String filtro);

	int obtenerSiguienteNumeroFactura();
	

}
