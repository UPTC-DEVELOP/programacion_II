package co.edu.uptc.model;

// Usuario contiene la identidad y credenciales comunes del administrador y los clientes.
public abstract class Usuario {
    private String nombreCompleto;
    private String correo;
    private String contrasenaHash;
    private Rol rol;

    protected Usuario(String nombreCompleto, String correo, String contrasenaHash, Rol rol) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasenaHash = contrasenaHash;
        this.rol = rol;
    }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasenaHash() { return contrasenaHash; }
    public void setContrasenaHash(String contrasenaHash) { this.contrasenaHash = contrasenaHash; }
    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
    @Override public String toString() { return nombreCompleto + " (" + correo + ")"; }
}
