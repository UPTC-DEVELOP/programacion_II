package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

public class ClienteDto {
    private int idCliente;
    private String primerNombre;
    private String otrosNombres;
    private String primerApellido;
    private String otrosApellidos;
    private String tipoIdentificacion;
    private String identificacion;
    private String correoElectronico;
    private String celular;
    private String direccion;
    private TipoCliente tipoCliente;
    private String contrasenia;
    private Rol rol = Rol.CLIENTE;

    // Constructor vacío para que la GUI pueda instanciarlo y usar setters
    public ClienteDto() {}

    // --- GETTERS Y SETTERS -
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getOtrosNombres() { return otrosNombres; }
    public void setOtrosNombres(String otrosNombres) { this.otrosNombres = otrosNombres; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getOtrosApellidos() { return otrosApellidos; }
    public void setOtrosApellidos(String otrosApellidos) { this.otrosApellidos = otrosApellidos; }
    public String getTipoIdentificacion() { return tipoIdentificacion; }
    public void setTipoIdentificacion(String tipoIdentificacion) { this.tipoIdentificacion = tipoIdentificacion; }
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public void setTipoCliente(TipoCliente tipoCliente) { this.tipoCliente = tipoCliente; }
    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }
    public Rol getRol() { return rol == null ? Rol.CLIENTE : rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    // Método de utilidad para la GUI
    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        if (primerNombre != null) sb.append(primerNombre).append(" ");
        if (otrosNombres != null && !otrosNombres.isEmpty()) sb.append(otrosNombres).append(" ");
        if (primerApellido != null) sb.append(primerApellido).append(" ");
        if (otrosApellidos != null && !otrosApellidos.isEmpty()) sb.append(otrosApellidos);
        return sb.toString().trim();
    }
}