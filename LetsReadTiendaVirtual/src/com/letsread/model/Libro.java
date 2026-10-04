package com.letsread.model;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anioPublicacion;
    private String categoria;
    private String editorial;
    private int numeroPaginas;
    private double precioVenta; // Incluye IVA
    private double porcentajeIva; // 19.0 o 5.0
    private int cantidadInventario;
    private String formato; // "Físico" o "Digital"
    private boolean tieneVentas;

    
    
    
    
    
    public Libro(String isbn, String titulo, String autor, int anioPublicacion, 
                 String categoria, String editorial, int numeroPaginas, 
                 double precioVenta, double porcentajeIva, int cantidadInventario, String formato) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.editorial = editorial;
        this.numeroPaginas = numeroPaginas;
        this.precioVenta = precioVenta;
        this.porcentajeIva = porcentajeIva;
        this.cantidadInventario = cantidadInventario;
        this.formato = formato;
        this.tieneVentas = false;
    }

	public String getIsbn() {
		return isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getAutor() {
		return autor;
	}

	public int getAnioPublicacion() {
		return anioPublicacion;
	}

	public String getCategoria() {
		return categoria;
	}

	public String getEditorial() {
		return editorial;
	}

	public int getNumeroPaginas() {
		return numeroPaginas;
	}

	public double getPrecioVenta() {
		return precioVenta;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public int getCantidadInventario() {
		return cantidadInventario;
	}

	public String getFormato() {
		return formato;
	}

	public boolean isTieneVentas() {
		return tieneVentas;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public void setAnioPublicacion(int anioPublicacion) {
		this.anioPublicacion = anioPublicacion;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public void setNumeroPaginas(int numeroPaginas) {
		this.numeroPaginas = numeroPaginas;
	}

	public void setPrecioVenta(double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public void setCantidadInventario(int cantidadInventario) {
		this.cantidadInventario = cantidadInventario;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public void setTieneVentas(boolean tieneVentas) {
		this.tieneVentas = tieneVentas;
	}}

    // Métodos Getters y Setters
    
    
    
    
    
//    public String getIsbn() { return isbn; }
//    public String getTitulo() { return titulo; }
//    public String getAutor() { return autor; }
//    public double getPrecioVenta() { return precioVenta; }
//    public double getPorcentajeIva() { return porcentajeIva; }
//    public int getCantidadInventario() { return cantidadInventario; }
//    public void setCantidadInventario(int cantidad) { this.cantidadInventario = cantidad; }
//    public boolean isTieneVentas() { return tieneVentas; }
//    public void setTieneVentas(boolean tieneVentas) { this.tieneVentas = tieneVentas; }
//}
