package co.edu.uptc.service;

import co.edu.uptc.exception.SesionNoIniciadaException;
import co.edu.uptc.model.Usuario;

// Sesion conserva solamente el usuario actual. Main crea una instancia y la inyecta donde se necesita.
public class Sesion {
    private Usuario usuarioActual;
    public void iniciar(Usuario usuario) { this.usuarioActual = usuario; }
    public void cerrar() { this.usuarioActual = null; }
    public boolean estaLogueado() { return usuarioActual != null; }
    public Usuario getUsuarioActual() { return usuarioActual; }
    public Usuario exigirUsuario() {
        if (!estaLogueado()) {
			throw new SesionNoIniciadaException("Debe iniciar sesión para continuar.");
		}
        return usuarioActual;
    }
}
