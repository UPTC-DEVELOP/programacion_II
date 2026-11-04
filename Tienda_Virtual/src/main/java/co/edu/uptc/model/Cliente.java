package co.edu.uptc.model;

public class Cliente {
    private String id;
    private String nombreCompleto;
    private String correo;
    private String direccionEnvio;
    private String telefono;
    private String tipoCliente;
    private double porcentajeDescuento;

    public Cliente() {}

    public Cliente(String id, String nombreCompleto, String correo, String direccionEnvio, String telefono, String tipoCliente) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
        this.porcentajeDescuento = tipoCliente.equalsIgnoreCase("PREMIUM") ? 0.10 : 0.0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { 
        this.tipoCliente = tipoCliente;
        this.porcentajeDescuento = tipoCliente.equalsIgnoreCase("PREMIUM") ? 0.10 : 0.0;
    }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
}