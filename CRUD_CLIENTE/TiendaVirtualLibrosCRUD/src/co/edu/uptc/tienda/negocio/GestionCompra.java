package co.edu.uptc.tienda.negocio;

import java.time.LocalDateTime;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Factura;

public class GestionCompra implements IGestionCompra {

	private final IGestionCarrito gestionCarrito;

	private int siguienteNumeroFactura;

	public GestionCompra(IGestionCarrito gestionCarrito) {
		this.gestionCarrito = gestionCarrito;
		this.siguienteNumeroFactura = 1;
	}

	@Override
	public Factura finalizarCompra(Cliente cliente) {
		if (cliente == null) {
			throw new NegocioException("Debe iniciar sesión para finalizar la compra.");
		}

		if (gestionCarrito.estaVacio()) {
			throw new NegocioException("El carrito está vacío. Agregue libros del catálogo para comprar.");
		}

		Factura factura = new Factura(siguienteNumeroFactura, LocalDateTime.now(), cliente,
				gestionCarrito.listarItems(), gestionCarrito.calcularSubtotal(),
				gestionCarrito.calcularDescuento(cliente), gestionCarrito.calcularTotal(cliente));

		siguienteNumeroFactura++;
		gestionCarrito.vaciar();

		return factura;
	}
}
