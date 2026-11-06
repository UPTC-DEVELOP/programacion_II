package co.edu.uptc.modelo.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO contenedor del archivo data/auditoria.json (RF17/RF18).
 *
 * Mantiene la lista de registros y el último identificador asignado para que
 * cada nueva operación reciba un id consecutivo.
 *
 * Aplica: SRP (Single Responsibility Principle) - representa la estructura
 * raíz del archivo de auditoría.
 */
public class AuditoriaDto {

    private List<RegistroAuditoriaDto> registros;
    private int ultimoId;

    /**
     * Constructor vacío que inicializa la lista para evitar null.
     */
    public AuditoriaDto() {
        this.registros = new ArrayList<>();
    }

    /**
     * Constructor completo.
     *
     * @param registros Lista de registros de auditoría
     * @param ultimoId Último identificador asignado
     */
    public AuditoriaDto(List<RegistroAuditoriaDto> registros, int ultimoId) {
        this.registros = (registros == null) ? new ArrayList<>() : registros;
        this.ultimoId = ultimoId;
    }

    // Getters y Setters
    public List<RegistroAuditoriaDto> getRegistros() { return registros; }
    public void setRegistros(List<RegistroAuditoriaDto> registros) { this.registros = registros; }
    public int getUltimoId() { return ultimoId; }
    public void setUltimoId(int ultimoId) { this.ultimoId = ultimoId; }
}
