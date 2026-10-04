package co.edu.uptc.tienda.modelo;

import java.time.LocalDate;
import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public abstract class Libro {

	private Long idLibro;
	private String isbn;
	private String titulo;
	private List<String> autores;
	private LocalDate fechaPublicacion;
	private String genero;
	private String editorial;
	private int numeroPaginas;
	private double precioBase;
	private int stockDisponible;
	private TipoLibro tipoLibro;
	private final double IVA;

	public Libro(Long idLibro, String isbn, String titulo, List<String> autores, LocalDate fechaPublicacion,
			String genero, String editorial, int numeroPaginas, double precioBase, int stockDisponible,
			TipoLibro tipoLibro, double IVA) {
		this.idLibro = idLibro;
		this.isbn = isbn;
		this.titulo = titulo;
		this.autores = autores;
		this.fechaPublicacion = fechaPublicacion;
		this.genero = genero;
		this.editorial = editorial;
		this.numeroPaginas = numeroPaginas;
		this.precioBase = precioBase;
		this.stockDisponible = stockDisponible;
		this.tipoLibro = tipoLibro;
		this.IVA = IVA;
	}

	public Long getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(Long idLibro) {
		this.idLibro = idLibro;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<String> getAutores() {
		return autores;
	}

	public void setAutores(List<String> autores) {
		this.autores = autores;
	}

	public LocalDate getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(LocalDate fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public int getNumeroPaginas() {
		return numeroPaginas;
	}

	public void setNumeroPaginas(int numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	public double getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}

	public int getStockDisponible() {
		return stockDisponible;
	}

	public void setStockDisponible(int stockDisponible) {
		this.stockDisponible = stockDisponible;
	}

	public TipoLibro getTipoLibro() {
		return tipoLibro;
	}

	public void setTipoLibro(TipoLibro tipoLibro) {
		this.tipoLibro = tipoLibro;
	}

	public double getIVA() {
		return IVA;
	}

	public abstract double calcularPrecioFinal();

}
