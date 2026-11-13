package co.edu.uptc.tienda.modelo;

import java.util.List;

import co.edu.uptc.tienda.modelo.enums.TipoLibro;

public abstract class Libro {

	public static final double IVA = 0.15;

	private Long idLibro;
	private String isbn;
	private String titulo;
	private List<String> autores;
	private String fechaPublicacion;
	private String genero;
	private String editorial;
	private int numeroPaginas;
	private double precioBase;
	private int stockDisponible;

	public Libro(Long idLibro, String isbn, String titulo, List<String> autores, String fechaPublicacion, String genero,
			String editorial, int numeroPaginas, double precioBase, int stockDisponible) {
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

	public String getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(String fechaPublicacion) {
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

	public double getPrecioFinal() {
		return Math.round(calcularPrecioFinal() * 100.0) / 100.0;
	}

	protected double calcularPrecioFinal() {
		return precioBase + (precioBase * IVA);
	}

	public abstract TipoLibro getTipoLibro();

}
