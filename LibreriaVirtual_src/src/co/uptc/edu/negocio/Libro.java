package co.uptc.edu.negocio;


public class Libro {

	private final String isbn ;
	private final String tituloLibro;
	private final String autorLibro;
	private final String anioPublicacion;
	private final String categoria;
	private final String editorial;
	private final int numPaginas;
	private final double precioVenta;
	private int stock;
	private final String tipoFormato;
	public Libro(String isbn, String tituloLibro, String autorLibro, String anioPublicacion, String categoria,
			String editorial, int numPaginas, double precioVenta, int stock, String tipoFormato) {
		super();
		this.isbn = isbn;
		this.tituloLibro = tituloLibro;
		this.autorLibro = autorLibro;
		this.anioPublicacion = anioPublicacion;
		this.categoria = categoria;
		this.editorial = editorial;
		this.numPaginas = numPaginas;
		this.precioVenta = precioVenta;
		this.stock = stock;
		this.tipoFormato = tipoFormato;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public String getIsbn() {
		return isbn;
	}
	public String getTituloLibro() {
		return tituloLibro;
	}
	public String getAutorLibro() {
		return autorLibro;
	}
	public String getAnioPublicacion() {
		return anioPublicacion;
	}
	public String getCategoria() {
		return categoria;
	}
	public String getEditorial() {
		return editorial;
	}
	public int getNumPaginas() {
		return numPaginas;
	}
	public double getPrecioVenta() {
		return precioVenta;
	}
	public String getTipoFormato() {
		return tipoFormato;
	}

}
