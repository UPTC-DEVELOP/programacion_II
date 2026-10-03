package co.edu.uptc.libreria.modelo;

public class Libro {

    private String codigo;
    private String titulo;
    private String autor;
    private String editorial;
    private String anioPublicacion;
    private String genero;
    private String precio;
    private String stock;

    private final double porcentajeIva = 0.19;


    public Libro() {
    }

    public Libro(String codigo, String titulo, String autor,
                 String editorial, String anioPublicacion,
                 String genero, String precio, String stock) {

        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(String anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
    

    
    
    public double calcularIva(double subtotal, double descuento) {
    		double baseGravable = subtotal - descuento;
    		return baseGravable * porcentajeIva;
    	}

    public double calcularTotalconIva(double subtotal, double descuento, double iva) {
		return subtotal - descuento + iva;
	}

}