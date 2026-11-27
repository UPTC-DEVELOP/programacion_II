package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.modelo.ItemCarrito;
import java.util.List;

public interface IPersistenciaCarrito {

	void guardarCarritoCliente(String claveCliente, List<ItemCarrito> items);

	List<ItemGuardadoCarrito> cargarCarritoCliente(String claveCliente);

	void eliminarCarritoCliente(String claveCliente);

	class ItemGuardadoCarrito {
		private String isbn;
		private int cantidad;

		public ItemGuardadoCarrito() {
		}

		public ItemGuardadoCarrito(String isbn, int cantidad) {
			this.isbn = isbn;
			this.cantidad = cantidad;
		}

		public String getIsbn() {
			return isbn;
		}

		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}

		public int getCantidad() {
			return cantidad;
		}

		public void setCantidad(int cantidad) {
			this.cantidad = cantidad;
		}
	}
}
