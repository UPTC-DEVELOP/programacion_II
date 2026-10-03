package co.edu.uptc.interfaces;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.Rol;

/**
 * INTERFAZ IGestionAcceso  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de NEGOCIO para el inicio de sesión y el registro de
 * usuarios nuevos. EventoLogin (GUI) solo conoce esta interfaz: no sabe dónde
 * ni cómo se guardan los usuarios (SRP / DIP).
 */
public interface IGestionAcceso {

    /**
     * Verifica correo, contraseña y que el perfil elegido corresponda a la cuenta.
     * @param perfil el perfil con el que el usuario intenta ingresar
     * @return el rol autorizado
     * @throws ReglaNegocioException con un mensaje listo para mostrar si se niega el acceso
     */
    Rol autenticar(String correo, String contrasenia, Rol perfil) throws ReglaNegocioException;

    void registrarCliente(Cliente cliente) throws ReglaNegocioException;

    void registrarAdministrador(Administrador administrador) throws ReglaNegocioException;
}
