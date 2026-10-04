package com.letsread.model;

public class ClientePremium extends Cliente {
    private double porcentajeDescuento = 0.10; // 10% de descuento

    public ClientePremium(String nombre, String correo, String direccion, String telefono, String clave) {
        super(nombre, correo, direccion, telefono, clave);
    }

    
    public double calcularDescuento(double totalCompra) {
        return totalCompra * porcentajeDescuento;
    }
}