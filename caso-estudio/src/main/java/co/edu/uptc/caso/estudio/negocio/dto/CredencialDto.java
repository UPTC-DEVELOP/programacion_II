package co.edu.uptc.caso.estudio.negocio.dto;

// Objeto de transferencia inmutable con las credenciales de acceso de un usuario
public class CredencialDto {

    private final String correo;
    private final String contrasena;

    public CredencialDto(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContrasena() {
        return contrasena;
    }
}