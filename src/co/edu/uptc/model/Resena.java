package co.edu.uptc.model;

// Resena relaciona un cliente con un libro comprado y una opinión de una a cinco estrellas.
public class Resena {
    private String isbn;
    private String correoCliente;
    private int calificacion;
    private String comentario;
    private String fecha;

    public Resena(String isbn, String correoCliente, int calificacion, String comentario, String fecha) {
        this.isbn = isbn; this.correoCliente = correoCliente; this.calificacion = calificacion;
        this.comentario = comentario; this.fecha = fecha;
    }
    public String getIsbn() { return isbn; }
    public String getCorreoCliente() { return correoCliente; }
    public int getCalificacion() { return calificacion; }
    public String getComentario() { return comentario; }
    public String getFecha() { return fecha; }
}
