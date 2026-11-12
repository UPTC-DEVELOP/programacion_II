package co.edu.uptc.model;

// Cliente define los datos de contacto y el contrato de descuento por tipo de cliente.
public abstract class Cliente extends Usuario implements Descontable {
    private String direccionEnvio;
    private String telefono;

    protected Cliente(String nombre, String correo, String hash, String direccion,
                      String telefono) {
        super(nombre, correo, hash, Rol.CLIENTE);
        this.direccionEnvio = direccion;
        this.telefono = telefono;
    }
    public String getDireccionEnvio() { return direccionEnvio; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
