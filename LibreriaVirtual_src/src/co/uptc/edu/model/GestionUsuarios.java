package co.uptc.edu.model;

import java.util.Collections;
import java.util.List;

import co.uptc.edu.interfaces.IGestionUsuario;

public class GestionUsuarios {

    private final IGestionUsuario persistencia;

    public GestionUsuarios(IGestionUsuario persistencia) {
        if (persistencia == null) {
            throw new IllegalArgumentException("La persistencia de usuarios es obligatoria.");
        }
        this.persistencia = persistencia;
    }


    public List<Usuario> listarUsuarios() {
        return Collections.unmodifiableList(persistencia.listar());
    }

    //Busca por correo sin distinguir mayúsculas. Devuelve null si no existe
    public Usuario buscarPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        return persistencia.buscar(correo.trim().toLowerCase());
    }

    public int contarAdministradores() {
        int total = 0;
        for (Usuario u : persistencia.listar()) {
            if (u.getTipoUsuario() == Rol.ADMIN) {
                total++;
            }
        }
        return total;
    }

    // Alta de un usuario ya construido (valida unicidad del correo)
    public void agregarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo.");
        }
        if (buscarPorCorreo(usuario.getCorreo()) != null) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo " + usuario.getCorreo() + ".");
        }
        persistencia.guardar(usuario);
    }

    // Registro público (RF-02): siempre crea un usuario con rol Cliente
    public Usuario registrarCliente(String nombre, String apellido, String correo, String direccion,
                                    String telefono, String contrasena) {
        Usuario nuevo = new Usuario(nombre, apellido, correo, direccion, telefono, Rol.CLIENTE, contrasena);
        agregarUsuario(nuevo);
        return nuevo;
    }

    public Usuario actualizarUsuario(String correoActual, String nombre, String apellido, String correoNuevo,
                                     String direccion, String telefono, Rol tipoUsuario, String nuevaContrasena) {
        Usuario u = requerir(correoActual);
        String correoAnterior = u.getCorreo();
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
        persistencia.actualizar(correoAnterior, u);
        return u;
    }

    public Usuario actualizarPerfil(String correoActual, String nombre, String apellido, String correoNuevo,
                                    String direccion, String telefono,
                                    String contrasenaActual, String nuevaContrasena) {
        Usuario u = requerir(correoActual);
        String correoAnterior = u.getCorreo();
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
        persistencia.actualizar(correoAnterior, u);
        return u;
    }

    public void eliminarUsuario(String correo, String correoSolicitante) {
        Usuario u = requerir(correo);
        if (correoSolicitante != null && u.getCorreo().equals(correoSolicitante.trim().toLowerCase())) {
            throw new IllegalArgumentException("No puedes eliminar tu propia cuenta.");
        }
        if (u.getTipoUsuario() == Rol.ADMIN && contarAdministradores() <= 1) {
            throw new IllegalArgumentException("No se puede eliminar al único administrador del sistema.");
        }
        persistencia.eliminar(u.getCorreo());
    }

    //Devuelve el usuario si las credenciales son correctas; null en caso contrario
    public Usuario autenticar(String correo, String contrasena) {
        Usuario u = buscarPorCorreo(correo);
        return (u != null && u.iniciarSesion(contrasena)) ? u : null;
    }

    // Crea las cuentas de demostración solo cuando todavía no hay ningún usuario guardado
    public void cargarDatosSimulados() {
        if (!persistencia.listar().isEmpty()) {
            return;
        }
        agregarUsuario(new Usuario("Administrador", "Sistema", "admin@bibliotech.com",
                "Calle 1 # 1-01, Tunja", "3000000001", Rol.ADMIN, "Admin123"));
        agregarUsuario(new Usuario("Cliente", "Demo", "cliente@bibliotech.com",
                "Carrera 2 # 2-02, Tunja", "3000000002", Rol.CLIENTE, "Cliente123"));
    }

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
        if (otro != null && !otro.getCorreo().equals(actual.getCorreo())) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con el correo " + correo + ".");
        }
        return correo;
    }
}
