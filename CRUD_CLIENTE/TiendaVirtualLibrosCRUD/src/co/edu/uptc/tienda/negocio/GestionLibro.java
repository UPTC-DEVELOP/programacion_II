package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionLibro;
import co.edu.uptc.tienda.modelo.Libro;

public class GestionLibro {

	private IGestionLibro gestionLibro;
	private List<Libro> librosEnMemoria;

	public GestionLibro(IGestionLibro gestionLibro) {
		this.gestionLibro = gestionLibro;

		cargarLibrosAutomaticamente();
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

	public void cargarLibrosAutomaticamente() {
		this.librosEnMemoria = gestionLibro.listar();
	}

	public List<Libro> getLibrosEnMemoria() {
		return librosEnMemoria;
	}

}
