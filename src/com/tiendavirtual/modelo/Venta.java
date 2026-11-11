package com.tiendavirtual.modelo;

public class Venta {

    private String id;
    private String fecha;
    private double montoTotal;
    private int idCliente;

    public Venta(String id, String fecha, double montoTotal, int idCliente) {
        this.id = id;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
        this.idCliente = idCliente;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}