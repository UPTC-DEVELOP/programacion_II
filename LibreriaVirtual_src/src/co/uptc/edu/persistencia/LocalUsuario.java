package co.uptc.edu.persistencia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.uptc.edu.interfaces.IGestionUsuario;
import co.uptc.edu.model.Usuario;

/** Persistencia en memoria: los usuarios se pierden al cerrar la aplicación. */
public class LocalUsuario implements IGestionUsuario {

    /** La llave es el correo; LinkedHashMap conserva el orden de registro. */
    private final Map<String, Usuario> usuarios = new LinkedHashMap<>();

    @Override
    public void guardar(Usuario usuario) {
        usuarios.put(usuario.getCorreo(), usuario);
    }

    @Override
    public void actualizar(String correoAnterior, Usuario usuario) {
        if (!usuario.getCorreo().equals(correoAnterior)) {
            usuarios.remove(correoAnterior);
        }
        usuarios.put(usuario.getCorreo(), usuario);
    }

    @Override
    public void eliminar(String correo) {
        usuarios.remove(correo);
    }

    @Override
    public Usuario buscar(String correo) {
        return usuarios.get(correo);
    }

    @Override
    public List<Usuario> listar() {
        return new ArrayList<>(usuarios.values());
    }
}
