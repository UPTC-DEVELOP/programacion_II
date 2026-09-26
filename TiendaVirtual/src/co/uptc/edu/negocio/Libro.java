package co.uptc.edu.negocio;

public class Libro {

	private int idLibro;
    private String titulo;
    private String autor;
    private String categoria;
    private String editorial;
    private int anoPublicacion;
    private String formato;
    private double precio;
    private int stock;

    public Libro() {}

    public Libro(int idLibro, String titulo, String autor, String categoria, String editorial, int anoPublicacion, String formato, double precio, int stock) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.editorial = editorial;
        this.anoPublicacion = anoPublicacion;
        this.formato = formato;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos del diagrama de negocio
    public void consultar() {
        System.out.println("Consultando información del libro: " + titulo);
    }

    public boolean esDisponible() {
        return this.stock > 0;
    }

    public void actualizarInventario() {
        System.out.println("Inventario actualizado para el libro ID: " + idLibro);
    }

    // Getters y Setters
    public int getIdLibro() { return idLibro; }
    public void setIdLibro(int idLibro) { this.idLibro = idLibro; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public int getAnoPublicacion() { return anoPublicacion; }
    public void setAnoPublicacion(int anoPublicacion) { this.anoPublicacion = anoPublicacion; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
	
}
