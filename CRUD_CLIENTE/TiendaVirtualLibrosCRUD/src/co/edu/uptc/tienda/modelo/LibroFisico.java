package co.edu.uptc.tienda.modelo;

import java.time.LocalDate;
import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public class LibroFisico extends Libro {

	private String tiempoMaximoEntrega;

	public LibroFisico(Long idLibro, String isbn, String titulo, List<String> autores, LocalDate fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible,
			TipoLibro tipoLibro, double IVA, String tiempoMaximoEntrega) {
		super(idLibro, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas, precioBase,
				stockDisponible, tipoLibro, IVA);
		this.tiempoMaximoEntrega = tiempoMaximoEntrega;
	}

	public String getTiempoMaximoEntrega() {
		return tiempoMaximoEntrega;
	}

	public void setTiempoMaximoEntrega(String tiempoMaximoEntrega) {
		this.tiempoMaximoEntrega = tiempoMaximoEntrega;
	}

	public double calcularPrecioFinal() {
		return super.getPrecioBase() + (super.getPrecioBase() * super.getIVA());
	}

}
