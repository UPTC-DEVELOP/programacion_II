package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.modelo.Libro;


public class ValidadorLibro {

	private static final int LONGITUD_MINIMA_ISBN = 8;
	private static final int LONGITUD_MAXIMA_ISBN = 12;

	public void validar(Libro libro) {
		String isbn = libro.getIsbn();
		if (isbn == null || isbn.length() < LONGITUD_MINIMA_ISBN || isbn.length() > LONGITUD_MAXIMA_ISBN) {
			throw new NegocioException("Longitud incorrecta del ISBN (Debe contener de " + LONGITUD_MINIMA_ISBN
					+ " a " + LONGITUD_MAXIMA_ISBN + " caracteres)");
		}

		if (estaVacio(libro.getTitulo()) || libro.getAutores() == null || libro.getAutores().isEmpty()
				|| estaVacio(libro.getGenero())) {
			throw new NegocioException("Por favor complete todos los campos obligatorios (*).");
		}

		if (libro.getNumeroPaginas() <= 0) {
			throw new NegocioException("El numero de páginas no puede ser Negativo o 0");
		}

		if (libro.getPrecioBase() <= 0.0) {
			throw new NegocioException("El precio del libro debe ser mayor a 0");
		}

		if (libro.getStockDisponible() < 0) {
			throw new NegocioException("El Stock no puede ser menor a 0");
		}
	}

	private boolean estaVacio(String valor) {
		return valor == null || valor.trim().isEmpty();
	}
}
