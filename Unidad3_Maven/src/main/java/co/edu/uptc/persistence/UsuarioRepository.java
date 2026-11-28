package co.edu.uptc.persistence;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Usuario;

public interface UsuarioRepository {
    List<Usuario> listar() throws PersistenciaException;
    Optional<Usuario> buscarPorCorreo(String correo) throws PersistenciaException;
    void guardar(Usuario usuario) throws PersistenciaException;
}
