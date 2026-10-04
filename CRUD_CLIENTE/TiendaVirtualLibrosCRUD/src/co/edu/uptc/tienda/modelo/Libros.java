package co.edu.uptc.tienda.modelo;

import java.util.ArrayList;
import java.util.List;

public class Libros {

	private List<Libro> listaLibros;

	public Libros() {
		listaLibros = new ArrayList<Libro>();
	}

	public void agregarLibro(Libro libro) {
		listaLibros.add(libro);
	}

	public List<Libro> listarLibros() {
		return listaLibros;
	}

	public Libro buscarLibro(String titulo) {
		for (Libro libro : listaLibros) {
			if (libro.getTitulo().equals(titulo)) {
				return libro;
			}
		}
		return null;
	}

	public boolean eliminarLibro(String titulo) {
		for (Libro libro : listaLibros) {
			if (libro.getTitulo().equals(titulo)) {
				listaLibros.remove(libro);
				return true;
			}
		}
		return false;
	}

	public boolean actualizarLibro(Libro libroActualizar) {
		for (int i = 0; i < listaLibros.size(); i++) {

			Libro libroActual = listaLibros.get(i);

			if (libroActual.getTitulo().equals(libroActualizar.getTitulo())) {
				libroActualizar.setIdLibro(libroActual.getIdLibro());
				libroActualizar.setIsbn(libroActual.getIsbn());

				listaLibros.set(i, libroActualizar);
				return true;
			}

		}
		return false;
	}

}
