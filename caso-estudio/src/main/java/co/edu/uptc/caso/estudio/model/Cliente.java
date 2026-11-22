package co.edu.uptc.caso.estudio.model;

public class Cliente {
    private String id;
    private String nombreCompleto;
    private String correo;
    private String direccionEnvio;
    private String departamento;
    private String ciudad;
    private String telefono;
    private String tipoCliente;
    private double porcentajeDescuento;
    private String contrasenaHash;

    public Cliente() {}

    public Cliente(String id, String nombreCompleto, String correo, String direccionEnvio, String telefono, String tipoCliente) {
        this(id, nombreCompleto, correo, direccionEnvio, "", "", telefono, tipoCliente);
    }

    public Cliente(String id, String nombreCompleto, String correo, String direccionEnvio,
                   String departamento, String ciudad, String telefono, String tipoCliente) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.direccionEnvio = direccionEnvio;
        this.departamento = departamento;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
        this.porcentajeDescuento = 0.0;
    }

    // Método polimórfico (LSP / OCP): cada subclase define su propio descuento
    public double calcularDescuento(double totalCompra) {
        return totalCompra * getPorcentajeDescuento();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public String getDepartamento() { return departamento == null ? "" : departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
    public String getCiudad() { return ciudad == null ? "" : ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(String tipoCliente) { this.tipoCliente = tipoCliente; }
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    protected void setPorcentajeDescuento(double porcentajeDescuento) { this.porcentajeDescuento = porcentajeDescuento; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
}