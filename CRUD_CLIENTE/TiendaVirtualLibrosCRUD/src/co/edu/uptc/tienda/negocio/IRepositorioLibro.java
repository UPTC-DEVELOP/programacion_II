package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Libro;

/**
 * Contrato de almacenamiento de libros. El ISBN es la clave única del libro.
 */
public interface IRepositorioLibro {

	void guardar(Libro libro);

	List<Libro> listar();

	Libro buscarPorIsbn(String isbn);

	Libro buscarPorTitulo(String titulo);

	boolean actualizar(Libro libro);

	boolean eliminar(String isbn);
}
