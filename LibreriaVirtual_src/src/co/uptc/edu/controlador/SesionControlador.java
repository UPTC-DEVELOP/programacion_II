package co.uptc.edu.controlador;

import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.model.Usuario;

// Controlador de la sesión (RF-01): las pantallas inician y cierran sesión solo a través de esta clase
public class SesionControlador {

    private final Tienda tienda;

    public SesionControlador(Tienda tienda) {
        this.tienda = tienda;
    }

    // Devuelve true si las credenciales son correctas y deja al usuario en sesión
    public boolean iniciarSesion(String correo, String contrasena) {
        return tienda.iniciarSesion(correo, contrasena);
    }

    public void cerrarSesion() {
        tienda.cerrarSesion();
    }

    public boolean haySesion() {
        return tienda.getUsuarioSesion() != null;
    }

    public boolean esAdministrador() {
        return tienda.getRolActual() == Rol.ADMIN;
    }

    public Usuario getUsuarioSesion() {
        return tienda.getUsuarioSesion();
    }

    public String getNombreUsuarioActual() {
        return tienda.getUsuarioActual();
    }

    // Lanza AccesoDenegadoException si quien está en sesión no es administrador
    void exigirAdministrador(String accion) {
        if (!esAdministrador()) {
            throw new AccesoDenegadoException("Solo un administrador puede " + accion + ".");
        }
    }

    // Lanza AccesoDenegadoException si no hay nadie en sesión
    Usuario exigirSesion() {
        Usuario u = tienda.getUsuarioSesion();
        if (u == null) {
            throw new AccesoDenegadoException("No hay una sesión activa.");
        }
        return u;
    }

    // Mantiene al día los datos del usuario en sesión después de editarlos
    void actualizarUsuarioSesion(Usuario usuario) {
        tienda.actualizarUsuarioSesion(usuario);
    }
}
