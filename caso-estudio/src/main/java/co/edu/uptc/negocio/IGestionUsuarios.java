package co.edu.uptc.negocio;

import co.edu.uptc.model.Cliente;
import co.edu.uptc.negocio.dto.CredencialDto;

public interface IGestionUsuarios {
    boolean registrarCliente(Cliente cliente);
    boolean iniciarSesion(CredencialDto credenciales);
    void cerrarSesion();
    Cliente getUsuarioLogueado();
    String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena);
    boolean eliminarUsuarioLogueado(String contrasena);
}
