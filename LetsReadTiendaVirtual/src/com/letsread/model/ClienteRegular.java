package com.letsread.model;

public class ClienteRegular extends Cliente {
    public ClienteRegular(String nombre, String correo, String direccion, String telefono, String clave) {
        super(nombre, correo, direccion, telefono, clave);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return 0.0; // Los clientes regulares no tienen descuento
    }
}