package co.edu.uptc.modelo.dto;

import co.edu.uptc.modelo.enums.AccionAuditoria;

/**
 * DTO que representa un registro de auditoría (RF17/RF18).
 *
 * Estructura serializada en data/auditoria.json:
 * <pre>
 * {
 *   "id": 1,
 *   "accion": "CREATE",
 *   "isbn": "9783161484100",
 *   "usuario": "admin",
 *   "fecha": "2026-11-07T10:30:00",
 *   "detalles": "Registro de nuevo libro: Clean Code"
 * }
 * </pre>
 *
 * Aplica: SRP (Single Responsibility Principle) - solo transporta los datos de
 * una operación auditada.
 */
public class RegistroAuditoriaDto {

    private int id;
    private AccionAuditoria accion;
    private String isbn;
    private String usuario;
    private String fecha;
    private String detalles;

    /**
     * Constructor vacío requerido por Gson.
     */
    public RegistroAuditoriaDto() {
    }

    /**
     * Constructor completo.
     *
     * @param id       Identificador consecutivo del registro
     * @param accion   Acción realizada (CREATE, UPDATE, DELETE)
     * @param isbn     ISBN del libro afectado
     * @param usuario  Usuario que ejecutó la acción
     * @param fecha    Fecha y hora en formato ISO-8601
     * @param detalles Descripción del cambio
     */
    public RegistroAuditoriaDto(int id, AccionAuditoria accion, String isbn,
                                String usuario, String fecha, String detalles) {
        this.id = id;
        this.accion = accion;
        this.isbn = isbn;
        this.usuario = usuario;
        this.fecha = fecha;
        this.detalles = detalles;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public AccionAuditoria getAccion() { return accion; }
    public void setAccion(AccionAuditoria accion) { this.accion = accion; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }
}
