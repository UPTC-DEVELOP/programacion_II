package co.edu.uptc.modelo.dto;

/**
 * DTO para transportar credenciales de autenticación.
 *
 * PRINCIPIO SOLID aplicado:
 * - SRP: Esta clase SOLO transporta datos de credenciales, no contiene lógica.
 * - Separación de concerns: La GUI no conoce la estructura interna del modelo Cliente.
 *
 * @author Grupo 7
 * @version 1.0
 */
public class CredencialDto {

    private String correoElectronico;
    private String contrasena;

    public CredencialDto(String correoElectronico, String contrasena) {
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
    }

    public CredencialDto() {}

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}