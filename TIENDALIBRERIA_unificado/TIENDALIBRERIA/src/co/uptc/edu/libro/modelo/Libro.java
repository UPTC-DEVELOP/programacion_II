package co.uptc.edu.libro.modelo;


public class Libro {
    private String isbn;
    protected String tituloLibro;
    protected String autor;
    protected int fechaPublicacion;   // solo año
    protected Categoria categoria;// enum
    protected String editorial;
    protected int paginas;
    protected double precioVenta;
    protected int cantidadDisponible;
    protected Formato formato; //enun

    public Libro(String isbn, String tituloLibro, String autor, int fechaPublicacion,
                 Categoria categoria, String editorial, int paginas,
                 double precioVenta, int cantidadDisponible, Formato formato) {
        this.isbn = isbn;
        this.tituloLibro = tituloLibro;
        this.autor = autor;
        this.fechaPublicacion = fechaPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.paginas = paginas;
        this.precioVenta = precioVenta;
        this.cantidadDisponible = cantidadDisponible;
        this.formato = formato;
    }

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTituloLibro() {
		return tituloLibro;
	}

	public void setTituloLibro(String tituloLibro) {
		this.tituloLibro = tituloLibro;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public int getFechaPublicacion() {
		return fechaPublicacion;
	}

	public void setFechaPublicacion(int fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public int getPaginas() {
		return paginas;
	}

	public void setPaginas(int paginas) {
		this.paginas = paginas;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public int getCantidadDisponible() {
		return cantidadDisponible;
	}

	public void setCantidadDisponible(int cantidadDisponible) {
		this.cantidadDisponible = cantidadDisponible;
	}

	public Formato getFormato() {
		return formato;
	}

	public void setFormato(Formato formato) {
		this.formato = formato;
	}

	@Override
	public String toString() {
		return "Libro [isbn=" + isbn + ", tituloLibro=" + tituloLibro + ", autor=" + autor + ", fechaPublicacion="
				+ fechaPublicacion + ", categoria=" + categoria + ", editorial=" + editorial + ", paginas=" + paginas
				+ ", precioVenta=" + precioVenta + ", cantidadDisponible=" + cantidadDisponible + ", formato=" + formato
				+ "]";
	}
}