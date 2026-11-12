package co.edu.uptc.persistence;

import co.edu.uptc.exception.PersistenciaException;

public interface RegistroOperaciones {
    void registrar(String nivel, String correo, String operacion) throws PersistenciaException;
}
