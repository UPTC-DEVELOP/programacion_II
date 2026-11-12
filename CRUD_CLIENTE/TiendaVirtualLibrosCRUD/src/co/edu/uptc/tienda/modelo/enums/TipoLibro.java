package co.edu.uptc.tienda.modelo.enums;

import java.util.List;

import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.LibroDigital;
import co.edu.uptc.tienda.modelo.LibroFisico;


public enum TipoLibro {

	FISICO {
		@Override
		public Libro crear(String isbn, String titulo, List<String> autores, String fechaPublicacion, String genero,
				String editorial, int numeroPaginas, double precioBase, int stockDisponible) {
			return new LibroFisico(null, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas,
					precioBase, stockDisponible);
		}
	},
	DIGITAL {
		@Override
		public Libro crear(String isbn, String titulo, List<String> autores, String fechaPublicacion, String genero,
				String editorial, int numeroPaginas, double precioBase, int stockDisponible) {
			return new LibroDigital(null, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas,
					precioBase, stockDisponible);
		}
	};

	public abstract Libro crear(String isbn, String titulo, List<String> autores, String fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible);

}
