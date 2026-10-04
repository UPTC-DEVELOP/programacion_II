package com.letsread.model;

public abstract class Cliente {
    private String nombreCompleto;
    private String correo;
    private String direccion;
    private String telefono;
    private String clave;
    

    public Cliente(String nombreCompleto, String correo, String direccion, String telefono, String clave) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.clave = clave;
    }

    // Método abstracto que implementará cada tipo de cliente
    public abstract double calcularDescuento(double totalCompra);

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
    public String getClave() { return clave; }
}