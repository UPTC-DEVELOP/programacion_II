package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Factura;

/**
 * Servicio de compras que consume la GUI. Las operaciones que incumplen una
 * regla lanzan {@link NegocioException}.
 */
public interface IGestionCompra {

	/**
	 * Convierte el carrito del cliente en una factura y deja el carrito vacío.
	 *
	 * @param cliente cliente que inició sesión
	 * @return la factura generada
	 */
	Factura finalizarCompra(Cliente cliente);
}
