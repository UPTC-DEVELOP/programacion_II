package co.edu.uptc.tienda.modelo;

public class RegistroCarrito {

    private String idCliente;
    private String isbn;
    private int cantidad;

    public RegistroCarrito(String idCliente, String isbn, int cantidad) {
        this.idCliente = idCliente;
        this.isbn = isbn;
        this.cantidad = cantidad;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getCantidad() {
        return cantidad;
    }
}
