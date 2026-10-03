package co.edu.uptc.modelo;

/**
 * ENTIDAD DE DOMINIO Administrador  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * Usuario con acceso al módulo de administración (libros, reportes, clientes).
 * Solo guarda datos: las reglas de acceso viven en la capa de negocio.
 */
public class Administrador extends Persona {

    private String contrasenia;

    public Administrador(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
            String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
            String direccion, String contrasenia) {
        super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion,
                correoElectronico, celular, direccion);
        this.contrasenia = contrasenia;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
