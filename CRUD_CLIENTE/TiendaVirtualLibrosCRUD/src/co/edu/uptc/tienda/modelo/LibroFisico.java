package co.edu.uptc.tienda.modelo;

import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public class LibroFisico extends Libro {

	private String tiempoMaximoEntrega;

	public LibroFisico(Long idLibro, String isbn, String titulo, List<String> autores, String fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible,
			TipoLibro tipoLibro) {
		super(idLibro, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas, precioBase,
				stockDisponible, tipoLibro);
		this.tiempoMaximoEntrega = "72 Horas";
	}

	public String getTiempoMaximoEntrega() {
		return tiempoMaximoEntrega;
	}

	public void setTiempoMaximoEntrega(String tiempoMaximoEntrega) {
		this.tiempoMaximoEntrega = tiempoMaximoEntrega;
	}

	public double calcularPrecioFinal() {
		double valorFinal = super.getPrecioBase() + (super.getPrecioBase() * super.getIVA());
		setPrecioFinal(valorFinal);

		return Math.round(valorFinal * 100.0) / 100.0;
	}

}
