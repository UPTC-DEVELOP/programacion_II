package co.edu.uptc.tienda.modelo;

import java.util.ArrayList;
import java.util.List;

public class Compra {

    private int numero;
    private String idCliente;
    private String nombreCliente;
    private String fecha;
    private String metodoPago;
    private double subtotal;
    private double impuestos;
    private double porcentajeDescuento;
    private double descuento;
    private double total;
    private List<DetalleCompra> detalles;

    public Compra(int numero, String idCliente, String nombreCliente, String fecha, String metodoPago,
            double porcentajeDescuento, List<DetalleCompra> detalles) {
        this.numero = numero;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.porcentajeDescuento = porcentajeDescuento;
        this.detalles = new ArrayList<>(detalles);
        for (DetalleCompra detalle : detalles) {
            subtotal += detalle.obtenerSubtotal();
            impuestos += detalle.obtenerImpuesto();
        }
        descuento = (subtotal + impuestos) * porcentajeDescuento / 100.0;
        total = subtotal + impuestos - descuento;
    }

    public int getNumero() {
        return numero;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getImpuestos() {
        return impuestos;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return total;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles == null ? new ArrayList<>() : detalles;
    }

    public boolean tieneLibro(String isbn) {
        for (DetalleCompra detalle : getDetalles()) {
            if (detalle.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public int obtenerCantidadLibros() {
        int cantidad = 0;
        for (DetalleCompra detalle : getDetalles()) {
            cantidad += detalle.getCantidad();
        }
        return cantidad;
    }
}
