package co.edu.uptc.tienda.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.negocio.IRepositorioLibro;


public class LocalLibro implements IRepositorioLibro {

	private final List<Libro> libros;
	private long siguienteIdLibro;

	public LocalLibro() {
		this.libros = new ArrayList<>();
		this.siguienteIdLibro = 1L;
	}

	@Override
	public void guardar(Libro libro) {
		libro.setIdLibro(this.siguienteIdLibro);
		this.siguienteIdLibro++;
		libros.add(libro);
	}

	@Override
	public List<Libro> listar() {
		return new ArrayList<>(libros);
	}

	@Override
	public Libro buscarPorIsbn(String isbn) {
		for (Libro libro : libros) {
			if (libro.getIsbn().equals(isbn)) {
				return libro;
			}
		}
		return null;
	}

	@Override
	public Libro buscarPorTitulo(String titulo) {
		for (Libro libro : libros) {
			if (libro.getTitulo().equalsIgnoreCase(titulo)) {
				return libro;
			}
		}
		return null;
	}

	@Override
	public boolean actualizar(Libro libroActualizar) {
		for (int i = 0; i < libros.size(); i++) {
			if (libros.get(i).getIsbn().equals(libroActualizar.getIsbn())) {
				libros.set(i, libroActualizar);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean eliminar(String isbn) {
		return libros.removeIf(libro -> libro.getIsbn().equals(isbn));
	}
}
