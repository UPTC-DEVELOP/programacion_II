package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.modelo.dto.RegistroAuditoriaDto;
import co.edu.uptc.modelo.enums.AccionAuditoria;

/**
 * Auditoría compuesta que escribe en dos destinos: el archivo TXT exigido por
 * RF18 y el archivo JSON adicional. La falla de un destino no impide que el
 * otro registre la operación ni bloquea el CRUD que ya fue persistido.
 */
public class AuditoriaCompuesta implements IAuditoria {

    private final IAuditoria auditoriaTxt;
    private final IAuditoria auditoriaJson;

    /**
     * @param auditoriaTxt  Destino TXT principal (RF18)
     * @param auditoriaJson Destino JSON adicional (puede ser null)
     */
    public AuditoriaCompuesta(IAuditoria auditoriaTxt, IAuditoria auditoriaJson) {
        this.auditoriaTxt = auditoriaTxt;
        this.auditoriaJson = auditoriaJson;
    }

    @Override
    public void registrar(AccionAuditoria accion, String isbn, String usuario, String detalles) {
        registrarEn(auditoriaTxt, "TXT", accion, isbn, usuario, detalles);
        registrarEn(auditoriaJson, "JSON", accion, isbn, usuario, detalles);
    }

    @Override
    public List<RegistroAuditoriaDto> listar() {
        List<RegistroAuditoriaDto> registros = listarDe(auditoriaTxt, "TXT");
        if (!registros.isEmpty()) {
            return registros;
        }
        return listarDe(auditoriaJson, "JSON");
    }

    private void registrarEn(IAuditoria auditoria, String destino,
                             AccionAuditoria accion, String isbn,
                             String usuario, String detalles) {
        if (auditoria == null) {
            return;
        }
        try {
            auditoria.registrar(accion, isbn, usuario, detalles);
        } catch (RuntimeException e) {
            System.err.println("No se pudo registrar la auditoría " + destino
                    + ": " + e.getMessage());
        }
    }

    private List<RegistroAuditoriaDto> listarDe(IAuditoria auditoria, String destino) {
        if (auditoria == null) {
            return new ArrayList<>();
        }
        try {
            return auditoria.listar();
        } catch (RuntimeException e) {
            System.err.println("No se pudo leer la auditoría " + destino
                    + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
