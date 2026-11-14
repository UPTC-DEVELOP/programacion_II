package modelo;

import java.util.ArrayList;
import excepciones.ExcepcionValidacion;

public class RepositorioUsuarios {
    private final usuariosjdbc.repositorio.RepositorioUsuarios repoJdbc;

    public RepositorioUsuarios() {
        this.repoJdbc = new usuariosjdbc.repositorio.RepositorioUsuarios();
    }

    public void registrar(Usuario usuario) throws ExcepcionValidacion {
        validarUsuario(usuario);

        if (buscarPorCorreo(usuario.getCorreo()) != null) {
            throw new ExcepcionValidacion("El correo ya se encuentra registrado.");
        }

        try {
            usuariosjdbc.modelo.Usuario usuarioJdbc = new usuariosjdbc.modelo.Usuario(
                usuario.getNombre(),
                usuario.getCorreo().trim(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getTipoCliente(),
                SeguridadContrasenas.generar(usuario.getContrasena())
            );
            repoJdbc.guardarUsuario(usuarioJdbc);
        } catch (Exception e) {
            throw new ExcepcionValidacion("No fue posible guardar el usuario en MySQL: " + e.getMessage());
        }
    }

    public Usuario autenticar(String correo, String contrasena) throws ExcepcionValidacion {
        validarCorreo(correo);

        if (contrasena == null || contrasena.isEmpty()) {
            throw new ExcepcionValidacion("Ingrese la contraseña.");
        }

        Usuario usuario = buscarPorCorreo(correo);

        if (usuario == null || !SeguridadContrasenas.verificar(contrasena, usuario.getContrasena())) {
            throw new ExcepcionValidacion("El correo o la contraseña no son correctos.");
        }

        // Actualiza automaticamente las cuentas antiguas que usaban texto plano.
        if (!SeguridadContrasenas.esHash(usuario.getContrasena())) {
            try {
                repoJdbc.actualizarUsuario(new usuariosjdbc.modelo.Usuario(
                    usuario.getNombre(), usuario.getCorreo(), usuario.getDireccion(),
                    usuario.getTelefono(), usuario.getTipoCliente(), SeguridadContrasenas.generar(contrasena)));
            } catch (RuntimeException e) {
                throw new ExcepcionValidacion("No fue posible actualizar la seguridad de la cuenta: " + e.getMessage());
            }
        }
        return usuario;
    }

    public void cambiarContrasena(String correo, String nuevaContrasena) throws ExcepcionValidacion {
        validarCorreo(correo);
        validarContrasena(nuevaContrasena);

        Usuario usuario = buscarPorCorreo(correo);
        if (usuario == null) {
            throw new ExcepcionValidacion("No existe un usuario registrado con ese correo.");
        }

        try {
            usuariosjdbc.modelo.Usuario usuarioJdbc = new usuariosjdbc.modelo.Usuario(
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getDireccion(),
                usuario.getTelefono(),
                usuario.getTipoCliente(),
                SeguridadContrasenas.generar(nuevaContrasena)
            );
            repoJdbc.actualizarUsuario(usuarioJdbc);
        } catch (Exception e) {
            throw new ExcepcionValidacion("No fue posible actualizar la contraseña en MySQL: " + e.getMessage());
        }
    }

    private void validarUsuario(Usuario usuario) throws ExcepcionValidacion {
        if (usuario.getNombre().trim().isEmpty()) {
            throw new ExcepcionValidacion("Ingrese el nombre completo.");
        }

        if (!usuario.getNombre().matches("^[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]+$")) {
            throw new ExcepcionValidacion("El nombre solo debe contener letras y espacios.");
        }

        validarCorreo(usuario.getCorreo());

        if (usuario.getDireccion().trim().isEmpty()) {
            throw new ExcepcionValidacion("Ingrese la dirección.");
        }

        if (!usuario.getTelefono().matches("^[0-9]{7,10}$")) {
            throw new ExcepcionValidacion("El teléfono debe contener entre 7 y 10 números.");
        }

        validarContrasena(usuario.getContrasena());
    }

    private void validarCorreo(String correo) throws ExcepcionValidacion {
        if (correo == null || correo.trim().isEmpty()) {
            throw new ExcepcionValidacion("Ingrese el correo electrónico.");
        }

        if (!correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new ExcepcionValidacion("Ingrese un correo válido, por ejemplo usuario@correo.com.");
        }
    }

    private void validarContrasena(String contrasena) throws ExcepcionValidacion {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new ExcepcionValidacion("Ingrese la contraseña.");
        }

        if (!contrasena.matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            throw new ExcepcionValidacion("La contraseña debe tener mínimo 8 caracteres, con letras y números.");
        }
    }

    private Usuario buscarPorCorreo(String correo) {
        if (correo == null) {
            return null;
        }
        try {
            usuariosjdbc.modelo.Usuario u = repoJdbc.buscarUsuario(correo.trim());
            if (u == null) {
                return null;
            }
            return new Usuario(
                u.getNombre(),
                u.getCorreo(),
                u.getDireccion(),
                u.getTelefono(),
                u.getTipoCliente(),
                u.getContrasena()
            );
        } catch (RuntimeException e) {
            throw new IllegalStateException("No fue posible consultar usuarios en la base de datos: " + e.getMessage(), e);
        }
    }

    public Usuario buscarUsuario(String correo) {
        return buscarPorCorreo(correo);
    }

    public ArrayList<Usuario> listarUsuarios() {
        ArrayList<Usuario> resultado = new ArrayList<>();
        for (usuariosjdbc.modelo.Usuario u : repoJdbc.listarUsuarios()) {
            resultado.add(new Usuario(u.getNombre(), u.getCorreo(), u.getDireccion(),
                u.getTelefono(), u.getTipoCliente(), u.getContrasena()));
        }
        return resultado;
    }

    public void actualizarUsuario(Usuario usuarioActualizado) throws ExcepcionValidacion {
        Usuario usuarioExistente = buscarPorCorreo(usuarioActualizado.getCorreo());
        if (usuarioExistente == null) {
            throw new ExcepcionValidacion("No existe un usuario registrado con ese correo.");
        }
        try {
            usuariosjdbc.modelo.Usuario usuarioJdbc = new usuariosjdbc.modelo.Usuario(
                usuarioActualizado.getNombre(),
                usuarioActualizado.getCorreo(),
                usuarioActualizado.getDireccion(),
                usuarioActualizado.getTelefono(),
                usuarioActualizado.getTipoCliente(),
                usuarioExistente.getContrasena()
            );
            repoJdbc.actualizarUsuario(usuarioJdbc);
        } catch (Exception e) {
            throw new ExcepcionValidacion("No fue posible actualizar el usuario en MySQL: " + e.getMessage());
        }
    }

    public void eliminarUsuario(String correo) throws ExcepcionValidacion {
        Usuario usuario = buscarPorCorreo(correo);
        if (usuario == null) {
            throw new ExcepcionValidacion("No existe un usuario registrado con ese correo.");
        }
        try {
            repoJdbc.eliminarUsuario(correo.trim());
        } catch (Exception e) {
            throw new ExcepcionValidacion("No fue posible eliminar el usuario en MySQL: " + e.getMessage());
        }
    }
}