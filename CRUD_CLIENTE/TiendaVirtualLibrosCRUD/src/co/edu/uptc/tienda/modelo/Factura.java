package co.edu.uptc.tienda.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Factura {

	private int numero;
	private LocalDateTime fecha;
	private Cliente cliente;
	private List<ItemCarrito> items;
	private double subtotal;
	private double descuento;
	private double total;

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

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setItems(List<ItemCarrito> items) {
		this.items = items;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Factura() {

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
