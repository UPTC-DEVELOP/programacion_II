package usuariosjdbc.modelo;
/*
 * Clase que representa un usuario del sistema.
 */
public class Usuario {
    private String nombre;
    private String correo;
    private String direccion;
    private String telefono;
    private String tipoCliente;
    private String contrasena;
    public Usuario(String nombre, String correo,
            String direccion, String telefono,
            String tipoCliente, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoCliente = tipoCliente;
        this.contrasena = contrasena;
    }
    public String getNombre() {
        return nombre;
    }
    public String getCorreo() {
        return correo;
    }
    public String getDireccion() {
        return direccion;
    }
    public String getTelefono() {
        return telefono;
    }
    public String getTipoCliente() {
        return tipoCliente;
    }
    public String getContrasena() {
        return contrasena;
    }
}