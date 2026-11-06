package co.edu.uptc.tienda.negocio;

import java.util.List;

import co.edu.uptc.tienda.modelo.Libro;

public class GestionLibro implements IGestionLibro {

	private final IRepositorioLibro repositorio;
	private final ValidadorLibro validador;

	public GestionLibro(IRepositorioLibro repositorio) {
		this.repositorio = repositorio;
		this.validador = new ValidadorLibro();
	}

	@Override
	public void agregarLibro(Libro libro) {
		validador.validar(libro);

		if (repositorio.buscarPorIsbn(libro.getIsbn()) != null) {
			throw new NegocioException("Ya existe un libro registrado con este ISBN");
		}

		repositorio.guardar(libro);
	}

	@Override
	public List<Libro> listarLibros() {
		return repositorio.listar();
	}

	@Override
	public Libro buscarLibroPorIsbn(String isbn) {
		return repositorio.buscarPorIsbn(isbn);
	}

	@Override
	public Libro buscarLibroPorTitulo(String titulo) {
		return repositorio.buscarPorTitulo(titulo);
	}

	@Override
	public void actualizarLibro(Libro libroActualizar) {
		validador.validar(libroActualizar);

		Libro libroActual = repositorio.buscarPorIsbn(libroActualizar.getIsbn());
		if (libroActual == null) {
			throw new NegocioException("No existe un libro con el ISBN: " + libroActualizar.getIsbn());
		}

		libroActualizar.setIdLibro(libroActual.getIdLibro());
		if (!repositorio.actualizar(libroActualizar)) {
			throw new NegocioException("No se pudo actualizar la información del Libro.");
		}
	}

	@Override
	public void eliminarLibro(String isbn) {
		if (!repositorio.eliminar(isbn)) {
			throw new NegocioException("No se pudo eliminar el libro con ISBN: " + isbn);
		}
	}
}
