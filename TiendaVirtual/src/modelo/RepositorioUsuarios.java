package modelo;

import java.io.IOException;
import java.util.ArrayList;

import excepciones.ExcepcionValidacion;
import persistencia.PersistenciaUsuarios;

public class RepositorioUsuarios {
    private ArrayList<Usuario> usuarios;

    public RepositorioUsuarios() {
        try {
            usuarios = PersistenciaUsuarios.cargarUsuarios();
        } catch (IOException e) {
            usuarios = new ArrayList<Usuario>();
            System.err.println("No fue posible cargar usuarios.txt.");
        }
    }

    public void registrar(Usuario usuario) throws ExcepcionValidacion {
        validarUsuario(usuario);

        if (buscarPorCorreo(usuario.getCorreo()) != null) {
            throw new ExcepcionValidacion("El correo ya se encuentra registrado.");
        }

        try {
            PersistenciaUsuarios.guardarUsuario(usuario);
            usuarios.add(usuario);
        } catch (IOException e) {
            throw new ExcepcionValidacion("No fue posible guardar el usuario en el archivo de datos.");
        }
    }

    public Usuario autenticar(String correo, String contrasena) throws ExcepcionValidacion {
        validarCorreo(correo);

        if (contrasena == null || contrasena.isEmpty()) {
            throw new ExcepcionValidacion("Ingrese la contraseña.");
        }

        Usuario usuario = buscarPorCorreo(correo);

        if (usuario == null || !usuario.getContrasena().equals(contrasena)) {
            throw new ExcepcionValidacion("El correo o la contraseña no son correctos.");
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

        String anterior = usuario.getContrasena();
        usuario.setContrasena(nuevaContrasena);

        try {
            PersistenciaUsuarios.guardarTodos(usuarios);
        } catch (IOException e) {
            usuario.setContrasena(anterior);
            throw new ExcepcionValidacion("No fue posible actualizar la contraseña en el archivo de datos.");
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
        for (Usuario usuario : usuarios) {
            if (usuario.getCorreo().equalsIgnoreCase(correo.trim())) {
                return usuario;
            }
        }
        return null;
    }
    public Usuario buscarUsuario(String correo) {
        return buscarPorCorreo(correo);
    }
    public ArrayList<Usuario> listarUsuarios() {
        return new ArrayList<Usuario>(usuarios);
        
    }
    public void actualizarUsuario(Usuario usuarioActualizado) throws ExcepcionValidacion {
        Usuario usuarioExistente = buscarPorCorreo(usuarioActualizado.getCorreo());
        if (usuarioExistente == null) {
            throw new ExcepcionValidacion("No existe un usuario registrado con ese correo.");
        }
        usuarioExistente.setNombre(usuarioActualizado.getNombre());
        usuarioExistente.setDireccion(usuarioActualizado.getDireccion());
        usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
        usuarioExistente.setTipoCliente(usuarioActualizado.getTipoCliente());
        try {
            PersistenciaUsuarios.guardarTodos(usuarios);
        } catch (IOException e) {
            throw new ExcepcionValidacion("No fue posible actualizar el usuario.");
        }
    }
    public void eliminarUsuario(String correo) throws ExcepcionValidacion {
        Usuario usuario = buscarPorCorreo(correo);
        if (usuario == null) {
            throw new ExcepcionValidacion("No existe un usuario registrado con ese correo.");
        }
        usuarios.remove(usuario);
        try {
            PersistenciaUsuarios.guardarTodos(usuarios);
        } catch (IOException e) {
            throw new ExcepcionValidacion("No fue posible eliminar el usuario.");
        }
    }
}

