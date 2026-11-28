package co.uptc.edu.caso.estudio.negocio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Compra {

    private int idCompra;
    private Date fecha;
    private double total;
    private double descuento;
    private String estado;
    private Cliente cliente;
    private Pago pago;
    private List<DetalleCompra> detalles;

    // Lista estática sincronizada para compatibilidad con LibroService.tieneVentasAsociadas()
    private static List<Compra> listaCompras = new ArrayList<>();

    public Compra() {
        this.fecha = new Date();
        this.estado = "Pendiente";
        this.detalles = new ArrayList<>();
    }

    public Compra(int idCompra, String estado) {
        this();
        this.idCompra = idCompra;
        this.estado = estado;
    }

    public void agregarDetalle(DetalleCompra detalle) {
        for (DetalleCompra d : detalles) {
            if (d.getLibro() != null && detalle.getLibro() != null
                    && d.getLibro().getIsbn().equals(detalle.getLibro().getIsbn())) {
                d.setCantidad(d.getCantidad() + detalle.getCantidad());
                d.calcularSubtotal();
                calcularTotal();
                return;
            }
        }
        this.detalles.add(detalle);
        calcularTotal();
    }

    public void eliminarDetalle(int index) {
        if (index >= 0 && index < detalles.size()) {
            detalles.remove(index);
            calcularTotal();
        }
    }

    public double calcularTotal() {
        double acum = 0.0;
        for (DetalleCompra d : detalles) {
            acum += d.calcularSubtotal();
        }
        // Descuento del 10% si el cliente es Premium (según caso de estudio)
        if (cliente != null && "Premium".equalsIgnoreCase(cliente.getTipoCliente())) {
            this.descuento = acum * 0.10;
        } else {
            this.descuento = 0.0;
        }
        this.total = acum - this.descuento;
        return this.total;
    }

    public String generarResumen() {
        String nombreCli = (cliente != null) ? cliente.getNombreCompleto() + " (" + cliente.getTipoCliente() + ")" : "General";
        return "ID: " + idCompra + " | Cliente: " + nombreCli + " | Estado: " + estado
                + " | Items: " + detalles.size() + " | Total: $" + String.format("%.2f", total) + " | Fecha: " + fecha;
    }

    public static List<Compra> obtenerTodas() {
        return listaCompras;
    }

    public static void sincronizarListaMemoria(List<Compra> compras) {
        listaCompras.clear();
        if (compras != null) {
            listaCompras.addAll(compras);
        }
    }

    // Getters y Setters
    public int getIdCompra() { return idCompra; }
    public void setIdCompra(int idCompra) { this.idCompra = idCompra; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public double getDescuento() { return descuento; }
    public void setDescuento(double descuento) { this.descuento = descuento; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public List<DetalleCompra> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleCompra> detalles) { this.detalles = detalles; }
}