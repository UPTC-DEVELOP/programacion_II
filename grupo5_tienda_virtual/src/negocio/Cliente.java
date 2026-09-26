package negocio;

/**
 * Cliente de la tienda virtual. Clase abstracta: todo cliente es Regular o
 * Premium (herencia).
 */
public abstract class Cliente {

    private String nombreCompleto;
    private String correo;
    private String direccion;
    private String telefono;
    private String contrasena;

    protected Cliente() {
    }

    protected Cliente(String nombreCompleto, String correo, String contrasena) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public abstract double calcularDescuento();

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Cliente otro = (Cliente) obj;
        return correo != null && correo.equals(otro.correo);
    }

    @Override
    public int hashCode() {
        return correo == null ? 0 : correo.hashCode();
    }

    @Override
    public String toString() {
        return nombreCompleto + " <" + correo + ">";
    }
}
