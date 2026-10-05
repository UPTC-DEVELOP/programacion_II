package co.uptc.edu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * CRUD y reglas de negocio de los usuarios (RF-02, RF-04, RF-11).
 * No depende de Swing. Todos los errores de validación se informan con IllegalArgumentException.
 */
public class GestionUsuarios {

    private final List<Usuario> usuarios = new ArrayList<>();

    // ---------- Read ----------
    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    /** Busca por correo sin distinguir mayúsculas. Devuelve null si no existe. */
    public Usuario buscarPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        String buscado = correo.trim().toLowerCase();
        for (Usuario u : usuarios) {
            if (u.getCorreo().equals(buscado)) {
                return u;
            }
        }
        return null;
    }

    public int contarAdministradores() {
        int total = 0;
        for (Usuario u : usuarios) {
            if (u.getTipoUsuario() == Rol.ADMIN) {
                total++;
            }
        }
        return total;
    }

    // ---------- Create ----------
    /** Alta de un usuario ya construido (valida unicidad del correo). */
    public void agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (buscarPorCorreo(usuario.getCorreo()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo " + usuario.getCorreo() + ".");
        }
        usuarios.add(usuario);
    }

    /** Registro público (RF-02): siempre crea un usuario con rol Cliente. */
    public Usuario registrarCliente(String nombre, String apellido, String correo, String direccion,
                                    String telefono, String contrasena) {
        Usuario nuevo = new Usuario(nombre, apellido, correo, direccion, telefono, Rol.CLIENTE, contrasena);
        agregarUsuario(nuevo);
        return nuevo;
    }

    // ---------- Update ----------
    /**
     * Edición hecha por un administrador (RF-04): permite cambiar datos, correo y rol.
     * @param nuevaContrasena si es null o vacía se conserva la contraseña actual.
     */
    public Usuario actualizarUsuario(String correoActual, String nombre, String apellido, String correoNuevo,
                                     String direccion, String telefono, Rol tipoUsuario, String nuevaContrasena) {
        Usuario u = requerir(correoActual);
        String correoValidado = validarCorreoDisponible(u, correoNuevo);
        ValidadorUsuario.tipoUsuario(tipoUsuario);
        if (u.getTipoUsuario() == Rol.ADMIN && tipoUsuario != Rol.ADMIN && contarAdministradores() <= 1) {
            throw new IllegalArgumentException("Debe existir al menos un administrador en el sistema.");
        }
        boolean cambiaClave = nuevaContrasena != null && !nuevaContrasena.isEmpty();
        if (cambiaClave) {
            ValidadorUsuario.contrasena(nuevaContrasena);
        }
        u.actualizarDatos(nombre, apellido, direccion, telefono);
        u.cambiarCorreo(correoValidado);
        u.cambiarTipoUsuario(tipoUsuario);
        if (cambiaClave) {
            u.cambiarContrasena(nuevaContrasena);
        }
        return u;
    }

    /**
     * Actualización de los datos propios (RF-11). No permite cambiar el rol.
     * Para cambiar la contraseña se exige la contraseña actual.
     */
    public Usuario actualizarPerfil(String correoActual, String nombre, String apellido, String correoNuevo,
                                    String direccion, String telefono,
                                    String contrasenaActual, String nuevaContrasena) {
        Usuario u = requerir(correoActual);
        String correoValidado = validarCorreoDisponible(u, correoNuevo);
        boolean cambiaClave = nuevaContrasena != null && !nuevaContrasena.isEmpty();
        if (cambiaClave) {
            if (!u.iniciarSesion(contrasenaActual)) {
                throw new IllegalArgumentException("La contraseña actual no es correcta.");
            }
            ValidadorUsuario.contrasena(nuevaContrasena);
        }
        u.actualizarDatos(nombre, apellido, direccion, telefono);
        u.cambiarCorreo(correoValidado);
        if (cambiaClave) {
            u.cambiarContrasena(nuevaContrasena);
        }
        return u;
    }

    // ---------- Delete ----------
    public void eliminarUsuario(String correo, String correoSolicitante) {
        Usuario u = requerir(correo);
        if (correoSolicitante != null && u.getCorreo().equals(correoSolicitante.trim().toLowerCase())) {
            throw new IllegalArgumentException("No puedes eliminar tu propia cuenta.");
        }
        if (u.getTipoUsuario() == Rol.ADMIN && contarAdministradores() <= 1) {
            throw new IllegalArgumentException("No se puede eliminar al único administrador del sistema.");
        }
        usuarios.remove(u);
    }

    // ---------- Autenticación (RF-01) ----------
    /** Devuelve el usuario si las credenciales son correctas; null en caso contrario. */
    public Usuario autenticar(String correo, String contrasena) {
        Usuario u = buscarPorCorreo(correo);
        return (u != null && u.iniciarSesion(contrasena)) ? u : null;
    }

    // ---------- Datos iniciales ----------
    public void cargarDatosSimulados() {
        usuarios.clear();
        agregarUsuario(new Usuario("Administrador", "Sistema", "admin@bibliotech.com",
                "Calle 1 # 1-01, Tunja", "3000000001", Rol.ADMIN, "Admin123"));
        agregarUsuario(new Usuario("Cliente", "Demo", "cliente@bibliotech.com",
                "Carrera 2 # 2-02, Tunja", "3000000002", Rol.CLIENTE, "Cliente123"));
    }

    // ---------- Auxiliares ----------
    private Usuario requerir(String correo) {
        Usuario u = buscarPorCorreo(correo);
        if (u == null) {
            throw new IllegalArgumentException("El usuario no existe.");
        }
        return u;
    }

    private String validarCorreoDisponible(Usuario actual, String correoNuevo) {
        String correo = ValidadorUsuario.correo(correoNuevo);
        Usuario otro = buscarPorCorreo(correo);
        if (otro != null && otro != actual) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo " + correo + ".");
        }
        return correo;
    }
}
