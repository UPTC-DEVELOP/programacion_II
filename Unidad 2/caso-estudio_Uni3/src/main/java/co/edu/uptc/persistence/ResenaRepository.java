package co.edu.uptc.persistence;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Resena;
import java.util.List;

public interface ResenaRepository {
    List<Resena> listar() throws PersistenciaException;
    void guardar(Resena resena) throws PersistenciaException;
    boolean existeParaCliente(String isbn, String correo) throws PersistenciaException;
}
