package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public class GestionCarrito implements IGestionCarrito {

	private final List<ItemCarrito> items;

	public GestionCarrito() {
		this.items = new ArrayList<>();
	}

	@Override
	public void agregarLibro(Libro libro, int cantidad) {
		if (libro == null) {
			throw new NegocioException("Seleccione un libro del catálogo.");
		}

		if (cantidad <= 0) {
			throw new NegocioException("La cantidad debe ser mayor a 0.");
		}

		ItemCarrito itemExistente = buscarItem(libro.getIsbn());
		int cantidadEnCarrito = itemExistente != null ? itemExistente.getCantidad() : 0;

		if (cantidadEnCarrito + cantidad > libro.getStockDisponible()) {
			throw new NegocioException("Stock insuficiente para '" + libro.getTitulo() + "'. Disponibles: "
					+ libro.getStockDisponible() + ", en el carrito: " + cantidadEnCarrito + ".");
		}

		if (itemExistente != null) {
			itemExistente.setCantidad(cantidadEnCarrito + cantidad);
		} else {
			items.add(new ItemCarrito(libro, cantidad));
		}
	}

	@Override
	public void quitarLibro(String isbn) {
		if (!items.removeIf(item -> item.getLibro().getIsbn().equals(isbn))) {
			throw new NegocioException("El libro con ISBN " + isbn + " no está en el carrito.");
		}
	}

	@Override
	public void vaciar() {
		items.clear();
	}

	@Override
	public List<ItemCarrito> listarItems() {
		return new ArrayList<>(items);
	}

	@Override
	public boolean estaVacio() {
		return items.isEmpty();
	}

	@Override
	public double calcularSubtotal() {
		double subtotal = 0;
		for (ItemCarrito item : items) {
			subtotal += item.getSubtotal();
		}
		return redondear(subtotal);
	}

	@Override
	public double calcularDescuento(Cliente cliente) {
		if (cliente == null) {
			return 0;
		}
		return redondear(cliente.calcularDescuento(calcularSubtotal()));
	}

	@Override
	public double calcularTotal(Cliente cliente) {
		return redondear(calcularSubtotal() - calcularDescuento(cliente));
	}

	private ItemCarrito buscarItem(String isbn) {
		for (ItemCarrito item : items) {
			if (item.getLibro().getIsbn().equals(isbn)) {
				return item;
			}
		}
		return null;
	}

	private double redondear(double valor) {
		return Math.round(valor * 100.0) / 100.0;
	}
}
