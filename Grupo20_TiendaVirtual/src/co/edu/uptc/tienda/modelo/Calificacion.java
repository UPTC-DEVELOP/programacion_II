package co.edu.uptc.tienda.modelo;

public class Calificacion {

    private String isbn;
    private String idCliente;
    private int puntaje;
    private String comentario;
    private String fecha;

    public Calificacion(String isbn, String idCliente, int puntaje, String comentario, String fecha) {
        this.isbn = isbn;
        this.idCliente = idCliente;
        this.puntaje = puntaje;
        this.comentario = comentario;
        this.fecha = fecha;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public int getPuntaje() {
        return puntaje;
    }

    public String getComentario() {
        return comentario == null ? "" : comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public boolean esDe(String isbn, String idCliente) {
        return this.isbn.equals(isbn) && this.idCliente.equals(idCliente);
    }
}
