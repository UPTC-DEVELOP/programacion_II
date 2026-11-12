package co.edu.uptc.model;

import java.util.ArrayList;

// Pedido representa una compra confirmada y conserva los valores que se cobraron.
public class Pedido {
    private long id;
    private String fecha;
    private String correoCliente;
    private ArrayList<LineaPedido> lineas;
    private double subtotal;
    private double impuestos;
    private double descuento;
    private double total;
    private MetodoPago metodoPago;

    public Pedido(long id, String fecha, String correoCliente, ArrayList<LineaPedido> lineas,
                  double subtotal, double impuestos, double descuento, double total,
                  MetodoPago metodoPago) {
        this.id = id; this.fecha = fecha; this.correoCliente = correoCliente;
        this.lineas = lineas; this.subtotal = subtotal; this.impuestos = impuestos;
        this.descuento = descuento; this.total = total; this.metodoPago = metodoPago;
    }
    public long getId() { return id; }
    public String getFecha() { return fecha; }
    public String getCorreoCliente() { return correoCliente; }
    public ArrayList<LineaPedido> getLineas() { return lineas; }
    public double getSubtotal() { return subtotal; }
    public double getImpuestos() { return impuestos; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public MetodoPago getMetodoPago() { return metodoPago; }
}
