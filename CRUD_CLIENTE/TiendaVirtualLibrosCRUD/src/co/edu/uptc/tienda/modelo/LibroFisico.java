package co.edu.uptc.tienda.modelo;

import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public class LibroFisico extends Libro {

	private static final String TIEMPO_ENTREGA_POR_DEFECTO = "72 Horas";

	private String tiempoMaximoEntrega;

	public LibroFisico(Long idLibro, String isbn, String titulo, List<String> autores, String fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible) {
		super(idLibro, isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas, precioBase,
				stockDisponible);
		this.tiempoMaximoEntrega = TIEMPO_ENTREGA_POR_DEFECTO;
	}

	public String getTiempoMaximoEntrega() {
		return tiempoMaximoEntrega;
	}

	public void setTiempoMaximoEntrega(String tiempoMaximoEntrega) {
		this.tiempoMaximoEntrega = tiempoMaximoEntrega;
	}

	@Override
	public TipoLibro getTipoLibro() {
		return TipoLibro.FISICO;
	}

}
