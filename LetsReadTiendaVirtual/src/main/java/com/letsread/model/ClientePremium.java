package com.letsread.model;

public class ClientePremium extends Cliente {
    private static final double PORCENTAJE_DESCUENTO = 0.10; // 10% de descuento

    public ClientePremium(String nombre, String correo, String direccion, String telefono, String clave) {
        super(nombre, correo, direccion, telefono, clave);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return totalCompra * PORCENTAJE_DESCUENTO;
    }

    @Override
    public String getTipo() { return "Premium"; }
}