package co.edu.uptc.tienda.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Factura {

	private final int numero;
	private final LocalDateTime fecha;
	private final Cliente cliente;
	private final List<ItemCarrito> items;
	private final double subtotal;
	private final double descuento;
	private final double total;

	// Pendiente agregar los datos de la factura que faltan, por ejemplo:

	public Factura(int numero, LocalDateTime fecha, Cliente cliente, List<ItemCarrito> items, double subtotal,
			double descuento, double total) {
		this.numero = numero;
		this.fecha = fecha;
		this.cliente = cliente;
		this.items = new ArrayList<>(items);
		this.subtotal = subtotal;
		this.descuento = descuento;
		this.total = total;
	}

	public int getNumero() {
		return numero;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public List<ItemCarrito> getItems() {
		return new ArrayList<>(items);
	}

	public double getSubtotal() {
		return subtotal;
	}

	public double getDescuento() {
		return descuento;
	}

	public double getTotal() {
		return total;
	}
}
