package co.edu.uptc.caso.estudio.negocio.dto;

public class CredencialDto {
    private final String correo;
    private final String contrasena;

    public CredencialDto(String correo, String contrasena) {
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public String getCorreo() { return correo; }
    public String getContrasena() { return contrasena; }
}
