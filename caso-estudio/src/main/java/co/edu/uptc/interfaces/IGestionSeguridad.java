package co.edu.uptc.interfaces;

import co.edu.uptc.modelo.dto.CredencialDto;

/**
 * Interfaz que define el contrato para la gestión de seguridad y autenticación.
 *
 * PRINCIPIO SOLID aplicado:
 * - DIP (Dependency Inversion): La capa de negocio depende de esta abstracción.
 * - ISP (Interface Segregation): Solo los métodos necesarios para autenticación.
 *
 * @author Grupo 7
 * @version 1.0
 */
public interface IGestionSeguridad {

    /**
     * Inicia sesión con las credenciales proporcionadas.
     *
     * @param credencial DTO con correo y contraseña
     * @return true si la autenticación es exitosa
     */
    boolean iniciarSesion(CredencialDto credencial);

    /**
     * Cierra la sesión actual.
     */
    void cerrarSesion();

    /**
     * Verifica si hay una sesión activa.
     *
     * @return true si hay un usuario autenticado
     */
    boolean haySesionActiva();
}