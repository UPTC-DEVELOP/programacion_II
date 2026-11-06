package co.edu.uptc.persistencia;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.modelo.dto.AuditoriaDto;
import co.edu.uptc.modelo.dto.RegistroAuditoriaDto;
import co.edu.uptc.modelo.enums.AccionAuditoria;

/**
 * Implementación de la auditoría con persistencia en JSON (RF17/RF18).
 *
 * <p>Esta clase gestiona el archivo data/auditoria.json: lo crea si no existe,
 * agrega un registro por cada operación CRUD y permite consultar el historial.
 * No contiene reglas de negocio; solo persiste lo que la capa de negocio le
 * indica (SRP).</p>
 *
 * <p>Es thread-safe: todos los métodos públicos que acceden al archivo están
 * sincronizados para evitar corrupción por escrituras concurrentes.</p>
 *
 * <p>Aplica: SRP (persistencia de auditoría), DIP (implementa IAuditoria) y
 * OCP (puede reemplazarse por otra implementación sin cambiar el servicio).</p>
 */
public class AuditoriaRepository implements IAuditoria {

    /** Ruta usada por la aplicación real. */
    private static final String RUTA_POR_DEFECTO = "data/auditoria.json";

    private final Gson gson;
    private final Path rutaArchivo;

    /**
     * Construye el repositorio usando la ruta por defecto data/auditoria.json.
     */
    public AuditoriaRepository() {
        this(RUTA_POR_DEFECTO);
    }

    /**
     * Construye el repositorio con una ruta personalizada. Se usa en pruebas
     * para no contaminar el archivo real.
     *
     * @param rutaArchivo Ruta del archivo JSON de auditoría
     */
    public AuditoriaRepository(String rutaArchivo) {
        this.rutaArchivo = Paths.get(rutaArchivo);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        inicializarArchivo();
    }

    /**
     * Crea la carpeta y el archivo con la estructura base si no existen.
     *
     * @throws PersistenciaException si no se puede crear el archivo
     */
    private void inicializarArchivo() {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            if (!Files.exists(rutaArchivo)) {
                escribir(new AuditoriaDto());
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo inicializar el archivo de auditoría: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Registra una operación en el historial de auditoría.
     *
     * <p>Asigna un id consecutivo y la fecha/hora actual en formato ISO-8601.</p>
     *
     * @param accion   Acción realizada (CREATE, UPDATE, DELETE)
     * @param isbn     ISBN del libro afectado
     * @param usuario  Usuario que ejecutó la acción
     * @param detalles Descripción del cambio
     * @throws PersistenciaException si ocurre un error al leer o escribir
     */
    @Override
    public synchronized void registrar(AccionAuditoria accion, String isbn,
                                       String usuario, String detalles) {
        AuditoriaDto datos = leer();
        int nuevoId = datos.getUltimoId() + 1;
        datos.setUltimoId(nuevoId);

        RegistroAuditoriaDto registro = new RegistroAuditoriaDto(
                nuevoId,
                accion,
                isbn,
                usuario,
                LocalDateTime.now().toString(),
                detalles);
        datos.getRegistros().add(registro);

        escribir(datos);
    }

    /**
     * Devuelve una copia de todos los registros de auditoría.
     *
     * @return Lista de registros en orden de inserción
     * @throws PersistenciaException si ocurre un error al leer el archivo
     */
    @Override
    public synchronized List<RegistroAuditoriaDto> listar() {
        return new ArrayList<>(leer().getRegistros());
    }

    /**
     * Lee y deserializa el archivo de auditoría.
     *
     * @return Estructura raíz con los registros; nunca null
     * @throws PersistenciaException si el archivo no se puede leer o el JSON
     *                              está corrupto
     */
    private AuditoriaDto leer() {
        try {
            if (!Files.exists(rutaArchivo)) {
                return new AuditoriaDto();
            }
            String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
            if (contenido == null || contenido.trim().isEmpty()) {
                return new AuditoriaDto();
            }
            AuditoriaDto datos = gson.fromJson(contenido, AuditoriaDto.class);
            if (datos == null) {
                return new AuditoriaDto();
            }
            if (datos.getRegistros() == null) {
                datos.setRegistros(new ArrayList<>());
            }
            return datos;
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo de auditoría: "
                    + rutaArchivo, e);
        } catch (JsonParseException e) {
            throw new PersistenciaException("El archivo de auditoría contiene un JSON inválido: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Serializa y escribe el archivo de auditoría con UTF-8.
     *
     * @param datos Estructura completa a persistir
     * @throws PersistenciaException si ocurre un error de escritura
     */
    private void escribir(AuditoriaDto datos) {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            try (Writer writer = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
                gson.toJson(datos, writer);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo de auditoría: "
                    + rutaArchivo, e);
        }
    }
}
