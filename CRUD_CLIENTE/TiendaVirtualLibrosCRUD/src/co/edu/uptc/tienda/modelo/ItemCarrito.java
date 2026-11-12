package co.edu.uptc.tienda.modelo;


public class ItemCarrito {

	private final Libro libro;
	private int cantidad;

	public ItemCarrito(Libro libro, int cantidad) {
		this.libro = libro;
		this.cantidad = cantidad;
	}

	public Libro getLibro() {
		return libro;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	// Precio final del libro (con IVA) por la cantidad pedida
	public double getSubtotal() {
		return Math.round(libro.getPrecioFinal() * cantidad * 100.0) / 100.0;
	}
}
