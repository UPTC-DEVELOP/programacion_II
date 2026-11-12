package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Libro;


public interface IGestionLibro {

	void agregarLibro(Libro libro);

	List<Libro> listarLibros();

	Libro buscarLibroPorIsbn(String isbn);

	Libro buscarLibroPorTitulo(String titulo);

	void actualizarLibro(Libro libro);

	void eliminarLibro(String isbn);
}
