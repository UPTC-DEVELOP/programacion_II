package co.edu.uptc.negocio.admin.memoria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.gui.interfaz.admin.IAuditoria;


/**
 * CLASE AuditoriaMemoria  (paquete: negocio.memoria)  implements IAuditoria
 * ---------------------------------------------------------------------------
 * IMPLEMENTACIÓN TEMPORAL del log de auditoría (RF02/RF03): cada operación se
 * guarda como una línea de texto en una lista en memoria y además se imprime en
 * la consola de Eclipse, así se ve que el registro "automático" funciona.
 * Cuando se pida el archivo .txt, se crea AuditoriaTxt que implemente IAuditoria.
 */
public class AuditoriaMemoria implements IAuditoria {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final List<String> registros = new ArrayList<>();

    @Override
    public void registrar(String operacion, String detalle) {
        String linea = LocalDateTime.now().format(FORMATO) + " | " + operacion + " | " + detalle;
        registros.add(linea);
        System.out.println("[AUDITORIA] " + linea);
    }

    /** Permite revisar lo registrado (útil para pruebas). */
    public List<String> getRegistros() {
        return new ArrayList<>(registros);
    }
}
