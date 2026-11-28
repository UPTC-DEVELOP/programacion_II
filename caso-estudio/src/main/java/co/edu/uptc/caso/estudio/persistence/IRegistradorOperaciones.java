package co.edu.uptc.caso.estudio.persistence;

// Abstracción para registrar eventos operativos del sistema
public interface IRegistradorOperaciones {

    void registrar(String mensaje);
}