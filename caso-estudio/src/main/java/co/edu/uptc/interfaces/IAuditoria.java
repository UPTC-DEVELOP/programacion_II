package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.dto.RegistroAuditoriaDto;
import co.edu.uptc.modelo.enums.AccionAuditoria;

/**
 * INTERFAZ IAuditoria  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Contrato de auditoría de las operaciones CRUD (RF17/RF18).
 *
 * La capa de negocio (GestionLibro) depende de esta abstracción y NO de
 * AuditoriaRepository (DIP). Así, la auditoría puede cambiar de archivo JSON a
 * base de datos sin tocar el servicio (OCP).
 */
public interface IAuditoria {

    /**
     * Registra una operación auditada.
     *
     * @param accion   Acción realizada (CREATE, UPDATE, DELETE)
     * @param isbn     ISBN del libro afectado
     * @param usuario  Usuario que ejecutó la acción
     * @param detalles Descripción del cambio
     */
    void registrar(AccionAuditoria accion, String isbn, String usuario, String detalles);

    /**
     * Devuelve todos los registros de auditoría almacenados.
     *
     * @return Lista de registros (copia defensiva)
     */
    List<RegistroAuditoriaDto> listar();
}
