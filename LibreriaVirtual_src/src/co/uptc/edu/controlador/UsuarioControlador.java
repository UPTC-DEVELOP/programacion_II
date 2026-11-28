package co.uptc.edu.controlador;

import java.util.List;

import co.uptc.edu.model.GestionUsuarios;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Usuario;

// Controlador del CRUD de usuarios (RF-02, RF-04, RF-11)
// Recibe los datos de las pantallas, verifica permisos y delega las reglas de negocio en GestionUsuarios
public class UsuarioControlador {

    private final GestionUsuarios gestionUsuarios;
    private final SesionControlador sesion;

    public UsuarioControlador(GestionUsuarios gestionUsuarios, SesionControlador sesion) {
        this.gestionUsuarios = gestionUsuarios;
        this.sesion = sesion;
    }

    // ---------- Read ----------
    public List<Usuario> listarUsuarios() {
        sesion.exigirAdministrador("consultar los usuarios");
        return gestionUsuarios.listarUsuarios();
    }

    public Usuario buscarUsuario(String correo) {
        return gestionUsuarios.buscarPorCorreo(correo);
    }

    // Un administrador no puede quitarse a sí mismo el rol desde la gestión de usuarios
    public boolean puedeCambiarRol(String correo) {
        Usuario actual = sesion.getUsuarioSesion();
        return actual == null || !actual.getCorreo().equalsIgnoreCase(correo == null ? "" : correo.trim());
    }

    // ---------- Create ----------
    public void crearUsuario(String nombre, String apellido, String correo, String direccion,
                             String telefono, Rol rol, String contrasena) {
        sesion.exigirAdministrador("crear usuarios");
        gestionUsuarios.agregarUsuario(new Usuario(nombre, apellido, correo, direccion, telefono, rol, contrasena));
    }

    // Registro público (RF-02): siempre crea un cliente
    public void registrarCliente(String nombre, String apellido, String correo, String direccion,
                                 String telefono, String contrasena, String confirmacion) {
        exigirCoincidencia(contrasena, confirmacion, "Las contraseñas no coinciden.");
        gestionUsuarios.registrarCliente(nombre, apellido, correo, direccion, telefono, contrasena);
    }

    // ---------- Update ----------
    public void actualizarUsuario(String correoActual, String nombre, String apellido, String correoNuevo,
                                  String direccion, String telefono, Rol rol, String nuevaContrasena) {
        sesion.exigirAdministrador("editar usuarios");
        Usuario enSesion = sesion.getUsuarioSesion();
        boolean editaSuCuenta = enSesion != null && enSesion.getCorreo().equals(correoActual);
        Usuario actualizado = gestionUsuarios.actualizarUsuario(correoActual, nombre, apellido, correoNuevo,
                direccion, telefono, rol, nuevaContrasena);
        if (editaSuCuenta) {
            sesion.actualizarUsuarioSesion(actualizado);
        }
    }

    // Actualización de los datos propios (RF-11)
    public void actualizarPerfil(String nombre, String apellido, String correo, String direccion, String telefono,
                                 String contrasenaActual, String nuevaContrasena, String confirmacion) {
        Usuario enSesion = sesion.exigirSesion();
        exigirCoincidencia(nuevaContrasena, confirmacion, "La nueva contraseña y su confirmación no coinciden.");
        Usuario actualizado = gestionUsuarios.actualizarPerfil(enSesion.getCorreo(), nombre, apellido, correo,
                direccion, telefono, contrasenaActual, nuevaContrasena);
        sesion.actualizarUsuarioSesion(actualizado);
    }

    // ---------- Delete ----------
    public void eliminarUsuario(String correo) {
        sesion.exigirAdministrador("eliminar usuarios");
        Usuario enSesion = sesion.getUsuarioSesion();
        gestionUsuarios.eliminarUsuario(correo, enSesion == null ? null : enSesion.getCorreo());
    }

    private static void exigirCoincidencia(String valor, String confirmacion, String mensaje) {
        String a = valor == null ? "" : valor;
        String b = confirmacion == null ? "" : confirmacion;
        if (!a.equals(b)) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
