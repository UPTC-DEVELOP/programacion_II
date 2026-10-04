package co.edu.uptc.tienda.modelo;

import java.time.LocalDate;
import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public class LibroDigital extends Libro {

	public LibroDigital(Long idLibro, String isbn, String titulo, List<String> autores, LocalDate fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible,
			TipoLibro tipoLibro, double IVA) {
		super(idLibro, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas, precioBase,
				stockDisponible, tipoLibro, IVA);
	}

	public double calcularPrecioFinal() {
		return super.getPrecioBase() + (super.getPrecioBase() * super.getIVA());
	}

}
