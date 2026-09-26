package modelo;

public class Libro {
    private String titulo;
    private String autor;
    private String formato;
    private int precio;

    public Libro(String titulo, String autor, String formato, int precio) {
        this.titulo = titulo;
        this.autor = autor;
        this.formato = formato;
        this.precio = precio;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getFormato() {
        return formato;
    }

    public int getPrecio() {
        return precio;
    }
}
