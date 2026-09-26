package co.uptc.edu.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compra {

	private int idCompra;
    private Date fecha;
    private double total;
    private String estado;
    private List<DetalleCompra> detalles;

    public Compra() {
        this.fecha = new Date();
        this.estado = "Pendiente";
        this.detalles = new ArrayList<>();
    }

    public Compra(int idCompra) {
        this();
        this.idCompra = idCompra;
    }

    public void agregarDetalle(DetalleCompra detalle) {
        this.detalles.add(detalle);
        calcularTotal();
    }

    // Métodos del diagrama de negocio
    public double calcularTotal() {
        double acum = 0.0;
        for (DetalleCompra d : detalles) {
            acum += d.calcularSubtotal();
        }
        this.total = acum;
        return this.total;
    }

    public String generarResumen() {
        return "Pedido #" + idCompra + " | Total ítems: " + detalles.size() + " | Total a pagar: $" + total;
    }

    public void confirmarCompra() {
        this.estado = "Confirmada";
        System.out.println("La compra #" + idCompra + " fue confirmada en el sistema.");
    }

    // Getters y Setters
    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public Date getFecha() { return fecha; }

    public double getTotal() { return total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public List<DetalleCompra> getDetalles() { return detalles; }
	
}
