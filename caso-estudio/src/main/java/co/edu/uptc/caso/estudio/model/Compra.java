package co.edu.uptc.caso.estudio.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private String idCompra;
    private String clienteId;
    private String nombreCliente;
    private LocalDateTime fecha;
    private List<ItemCarrito> items;
    private double subtotalSinIva;
    private double totalImpuestos;
    private double descuentoAplicado;
    private double totalPagado;
    private String metodoPago;

    public Compra() {
        this.items = new ArrayList<>();
        this.fecha = LocalDateTime.now();
    }

    public Compra(String idCompra, Cliente cliente, Carrito carrito, String metodoPago) {
        this();
        this.idCompra = idCompra;
        this.metodoPago = metodoPago;

        if (cliente != null) {
            this.clienteId = cliente.getId();
            this.nombreCliente = cliente.getNombreCompleto();
        }

        if (carrito != null) {
            this.items = copiarItems(carrito.getItems());
            this.subtotalSinIva = carrito.calcularSubtotalSinIva();
            this.totalImpuestos = carrito.calcularTotalImpuestos();
            this.descuentoAplicado = carrito.calcularDescuento(cliente);
            this.totalPagado = carrito.calcularTotalFinal(cliente);
        }
    }

    private List<ItemCarrito> copiarItems(List<ItemCarrito> origen) {
        List<ItemCarrito> copia = new ArrayList<>();
        if (origen == null) {
            return copia;
        }
        for (ItemCarrito item : origen) {
            if (item != null) {
                copia.add(item);
            }
        }
        return copia;
    }

    public String getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items != null ? items : new ArrayList<>();
    }

    public double getSubtotalSinIva() {
        return subtotalSinIva;
    }

    public void setSubtotalSinIva(double subtotalSinIva) {
        this.subtotalSinIva = subtotalSinIva;
    }

    public double getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(double totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public double getDescuentoAplicado() {
        return descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
}