package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;

public interface IGestionUsuarios {
    int LONGITUD_MINIMA_CONTRASENA = 6;

    boolean registrarCliente(Cliente cliente);
    boolean iniciarSesion(CredencialDto credenciales);
    void cerrarSesion();
    Cliente getUsuarioLogueado();
    String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena);
    boolean eliminarUsuarioLogueado(String contrasena);
}