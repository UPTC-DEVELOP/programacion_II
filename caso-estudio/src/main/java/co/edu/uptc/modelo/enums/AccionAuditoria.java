package co.edu.uptc.modelo.enums;

/**
 * ENUMERACIÓN AccionAuditoria  (paquete: modelo.enums)
 * ---------------------------------------------------------------------------
 * Define las acciones auditables del CRUD de libros (RF17/RF18).
 *
 * Se usa un enum, y no un String libre, para impedir acciones inválidas por
 * error de escritura. Gson serializa cada constante por su nombre, de modo que
 * el archivo auditoria.json conserva los valores "CREATE", "UPDATE" y
 * "DELETE" exigidos por la especificación.
 */
public enum AccionAuditoria {

    CREATE("Registro de libro"),
    UPDATE("Actualización de libro"),
    DELETE("Eliminación de libro");

    private final String descripcion;

    AccionAuditoria(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve una descripción legible de la acción.
     *
     * @return Texto descriptivo de la acción
     */
    public String getDescripcion() {
        return descripcion;
    }
}
