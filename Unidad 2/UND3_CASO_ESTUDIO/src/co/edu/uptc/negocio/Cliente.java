package co.edu.uptc.negocio;

public class Cliente {

    public static final double DESCUENTO_PREMIUM = 10.0;

    private String nombreCompleto;
    private String correoElectronico;
    private String direccionEnvio;
    private String telefono;
    private TipoCliente tipoCliente;
    private String contrasenaHash;

    public Cliente(String nombreCompleto, String correoElectronico, String direccionEnvio,
                   String telefono, TipoCliente tipoCliente, String contrasenaHash) {
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.direccionEnvio = direccionEnvio;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
        this.contrasenaHash = contrasenaHash;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDireccionEnvio() {
        return direccionEnvio;
    }

    public void setDireccionEnvio(String direccionEnvio) {
        this.direccionEnvio = direccionEnvio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public boolean esPremium() {
        return tipoCliente == TipoCliente.PREMIUM;
    }

    @Override
    public String toString() {
        return nombreCompleto + " (" + correoElectronico + ")";
    }
}
