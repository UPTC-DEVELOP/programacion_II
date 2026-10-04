package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionable;
import co.edu.uptc.tienda.modelo.Libro;

public class GestionLibro {

	private IGestionable<Libro> gestionLibro;

	public GestionLibro(IGestionable<Libro> gestionLibro) {
		this.gestionLibro = gestionLibro;
	}

	public void agregarLibro(Libro libro) {
		gestionLibro.agregar(libro);
	}

	public List<Libro> listarLibros() {
		return gestionLibro.listar();
	}

	public Libro buscarLibro(String titulo) {
		return gestionLibro.buscar(titulo);
	}

	public boolean actualizarLibro(Libro libroActualizar) {
		return gestionLibro.actualizar(libroActualizar);
	}

	public boolean eliminarLibro(String titulo) {
		return gestionLibro.eliminar(titulo);
	}

}
