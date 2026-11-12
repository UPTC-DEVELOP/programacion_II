package co.edu.uptc.negocio;

import java.time.LocalDateTime;

public class Pedido {

    private final long id;
    private final LocalDateTime fecha;
    private final String isbn;
    private final String titulo;
    private final int cantidad;
    private final double precioUnitarioFinal;
    private final double total;

    public Pedido(long id, LocalDateTime fecha, String isbn, String titulo,
                  int cantidad, double precioUnitarioFinal) {
        this.id = id;
        this.fecha = fecha;
        this.isbn = isbn;
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.precioUnitarioFinal = precioUnitarioFinal;
        // Valor total a pagar = precio final unitario x cantidad
        this.total = Math.round(precioUnitarioFinal * cantidad * 100.0) / 100.0;
    }

    public long getId() { return id; }
    public LocalDateTime getFecha() { return fecha; }
    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitarioFinal() { return precioUnitarioFinal; }
    public double getTotal() { return total; }
}
