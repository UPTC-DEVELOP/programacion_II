package co.edu.uptc.tienda.modelo;

import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public class LibroDigital extends Libro {

	public LibroDigital(Long idLibro, String isbn, String titulo, List<String> autores, String fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible) {

		super(idLibro, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas, precioBase,
				stockDisponible);
	}

	@Override
	public TipoLibro getTipoLibro() {
		return TipoLibro.DIGITAL;
	}

}
