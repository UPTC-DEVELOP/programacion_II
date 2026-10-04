package co.edu.uptc.libreria.controladores;

import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.negocio.GestionCarrito;
import co.edu.uptc.libreria.modelo.Libro;

import java.util.List;

public class ControladorCarrito {
	
	private GestionCarrito gestionCarrito;

	public ControladorCarrito(GestionCarrito gestionCarrito) {
		this.gestionCarrito = gestionCarrito;
	}
	
	public void agregarAlCarrito(Libro libro, double precio, int cantidad) {
		gestionCarrito.agregarLibro(libro, precio, cantidad);
	}
	
	public List<ItemCarrito> listarCarrito() {
		return gestionCarrito.obtenerItems();
	}
	
	public void modificarCantidad(String isbn, int nuevaCantidad) {
		gestionCarrito.actualizarCantidad(isbn, nuevaCantidad);
	}
	
	public void eliminarElemento(String isbn) {
		gestionCarrito.eliminarLibro(isbn);
	}
	
	public double obtenerSubtotal() {
		return gestionCarrito.calcularSubtotal();
	}
	
	public double obtenerDescuento() {
		return gestionCarrito.calcularDescuento();
	}
	
	public double obtenerIva() {
		return gestionCarrito.calcularIva();
	}
	
	public double obtenerTotal() {
		return gestionCarrito.calcularTotal();
	}
	
	public void vaciarCarrito() {
		gestionCarrito.vaciarCarrito();
	}

}
