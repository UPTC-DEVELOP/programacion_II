package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;

public interface IGestionCarrito {

	void agregarLibro(Libro libro, int cantidad);

	void quitarLibro(String isbn);

	void vaciar();

	List<ItemCarrito> listarItems();

	boolean estaVacio();

	double calcularSubtotal();

	double calcularDescuento(Cliente cliente);

	double calcularTotal(Cliente cliente);
}
