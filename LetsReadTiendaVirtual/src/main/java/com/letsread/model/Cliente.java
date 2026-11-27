package com.letsread.model;

public abstract class Cliente {
    private String nombreCompleto;
    private final String correo;      // identificador único (no se modifica)
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

    /** "Regular" o "Premium". Se usa para persistir y reconstruir el cliente. */
    public abstract String getTipo();

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public String getClave() { return clave; }

    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setClave(String clave) { this.clave = clave; }
}