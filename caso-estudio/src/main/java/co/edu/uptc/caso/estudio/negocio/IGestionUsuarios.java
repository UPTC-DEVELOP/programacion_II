package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;

// Principio I (SOLID): define únicamente las operaciones de usuario que necesita la interfaz.
public interface IGestionUsuarios {

    int LONGITUD_MINIMA_CONTRASENA = 6;

    String registrarCliente(Cliente cliente);

    boolean iniciarSesion(CredencialDto credenciales);

    void cerrarSesion();

    Cliente getUsuarioLogueado();

    String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena);

    String restablecerContrasena(String id, String correo, String nuevaContrasena);

    boolean eliminarUsuarioLogueado(String contrasena);
}