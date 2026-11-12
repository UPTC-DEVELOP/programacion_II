package co.edu.uptc.persistence;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    List<Usuario> listar() throws PersistenciaException;
    Optional<Usuario> buscarPorCorreo(String correo) throws PersistenciaException;
    void guardar(Usuario usuario) throws PersistenciaException;
}
