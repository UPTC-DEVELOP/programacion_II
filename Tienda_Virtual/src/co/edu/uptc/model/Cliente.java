package co.edu.uptc.model;

public class Cliente {
    private String nombreCompleto;
    private String correo;
    private String direccionEnvio;
    private String telefono;

    public Cliente(String nombreCompleto, String correo, String direccionEnvio, String telefono) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
    }

    public double calcularDescuento(double totalCompra) {
        return 0.0;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getCorreo() { return correo; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public String getTelefono() { return telefono; }
}