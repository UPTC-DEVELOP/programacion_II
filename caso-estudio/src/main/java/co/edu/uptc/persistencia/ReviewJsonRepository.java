package co.edu.uptc.persistencia;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IReviewRepository;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.enums.Rol;

/**
 * Implementación del repositorio de reseñas con persistencia en JSON.
 *
 * <p>Funciona como ESPEJO de {@link ReviewJdbcRepository}: ambas implementan
 * {@link IReviewRepository}, por lo que la capa de negocio (GestionResena) no
 * cambia (LSP, OCP y DIP). PostgreSQL es la fuente de verdad: después de cada
 * operación, GestionResena llama a {@link #sincronizarLibro} con las reseñas del
 * libro leídas de la BD, de modo que los ids y las fechas coinciden. Los demás
 * métodos (guardar, actualizar, eliminar) permiten usar este repositorio como
 * única persistencia si se inyecta en lugar del JDBC.</p>
 *
 * <p>Gestiona el archivo data/resenas.json: si no existe lo crea vacío y en
 * cada operación lee el archivo, aplica el cambio y lo vuelve a escribir. No
 * contiene reglas de negocio (calificación, comentario, una reseña por
 * cliente, haber comprado el libro); esas viven en GestionResena (SRP).</p>
 *
 * <p>El identificador se asigna de forma secuencial con el campo ultimoId, que
 * se guarda en el archivo para que los ids no se repitan después de reiniciar
 * la aplicación ni de eliminar reseñas.</p>
 *
 * <p>Estructura del archivo:</p>
 * <pre>
 * {
 *   "resenas": [ { "id": 1, "isbn": "...", "idCliente": 2, "calificacion": 5,
 *                  "comentario": "...", "fecha": "2026-11-26T10:15:30" } ],
 *   "ultimoId": 1
 * }
 * </pre>
 *
 * @author: Equipo de desarrollo grupo 7 UPTC
 */
public class ReviewJsonRepository implements IReviewRepository {

    /** Ruta usada por la aplicación real. */
    private static final String RUTA_POR_DEFECTO = "data/resenas.json";

    private final Gson gson;
    private final Path rutaArchivo;
    /** Se usa para el nombre del autor y para saber si quien elimina es ADMIN. */
    private final IClienteRepositorio clienteRepository;

    /**
     * Construye el repositorio usando la ruta por defecto data/resenas.json.
     *
     * @param clienteRepository Repositorio de clientes (nombres de autores y rol)
     */
    public ReviewJsonRepository(IClienteRepositorio clienteRepository) {
        this(RUTA_POR_DEFECTO, clienteRepository);
    }

    /**
     * Construye el repositorio con una ruta personalizada. Sirve para las
     * pruebas, para no modificar el archivo real.
     *
     * @param rutaArchivo       Ruta del archivo JSON de reseñas
     * @param clienteRepository Repositorio de clientes (puede ser null)
     */
    public ReviewJsonRepository(String rutaArchivo, IClienteRepositorio clienteRepository) {
        this.rutaArchivo = Paths.get(rutaArchivo);
        this.clienteRepository = clienteRepository;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        inicializarArchivo();
    }

    /** Crea la carpeta y el archivo vacío si no existen o si está vacío. */
    private void inicializarArchivo() {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            if (!Files.exists(rutaArchivo) || Files.size(rutaArchivo) == 0) {
                escribir(new ResenaData());
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo inicializar el archivo de reseñas: "
                    + rutaArchivo, e);
        }
    }

    // ------------------------- IReviewRepository -------------------------

    /**
     * Guarda una reseña nueva: le asigna el siguiente id secuencial
     * (ultimoId + 1) y la fecha y hora actuales.
     */
    @Override
    public synchronized void guardar(Review review) {
        if (review == null) {
            return;
        }
        ResenaData datos = leer();
        int nuevoId = datos.ultimoId + 1;
        datos.ultimoId = nuevoId;
        datos.resenas.add(new ResenaRegistro(nuevoId, review.isbn(), review.idCliente(),
                review.calificacion(), review.comentario(), LocalDateTime.now().toString()));
        escribir(datos);
    }

    /** Devuelve las reseñas del libro, la más reciente primero. */
    @Override
    public synchronized List<Review> listarPorLibro(String isbn) {
        List<ResenaRegistro> filtradas = new ArrayList<>();
        for (ResenaRegistro r : leer().resenas) {
            if (isbn != null && isbn.equals(r.isbn)) {
                filtradas.add(r);
            }
        }
        // Más reciente primero; a igual fecha gana el id más alto.
        filtradas.sort(Comparator
                .comparing((ResenaRegistro r) -> fechaDe(r))
                .thenComparingInt(r -> r.id)
                .reversed());
        List<Review> resultado = new ArrayList<>();
        for (ResenaRegistro r : filtradas) {
            resultado.add(new Review(r.id, r.isbn, r.idCliente, r.calificacion, r.comentario));
        }
        return resultado;
    }

    /** Cambia la calificación y el comentario; conserva autor, libro y fecha. */
    @Override
    public synchronized void actualizar(Review review) {
        if (review == null) {
            return;
        }
        ResenaData datos = leer();
        for (ResenaRegistro r : datos.resenas) {
            if (r.id == review.id()) {
                r.calificacion = review.calificacion();
                r.comentario = review.comentario();
                escribir(datos);
                return;
            }
        }
    }

    /**
     * Elimina la reseña solo si quien la elimina es su dueño o un ADMIN,
     * igual que el DELETE de la implementación JDBC.
     *
     * @param id        Id de la reseña
     * @param idCliente Cliente que solicita la eliminación
     */
    @Override
    public synchronized void eliminar(int id, int idCliente) {
        ResenaData datos = leer();
        boolean esAdmin = esAdmin(idCliente);
        boolean cambio = datos.resenas.removeIf(
                r -> r.id == id && (r.idCliente == idCliente || esAdmin));
        if (cambio) {
            escribir(datos);
        }
    }

    /** Nombres de los autores de las reseñas del libro (id de cliente -> nombre). */
    @Override
    public synchronized Map<Integer, String> nombresClientes(String isbn) {
        Map<Integer, String> nombres = new HashMap<>();
        if (clienteRepository == null) {
            return nombres;
        }
        for (ResenaRegistro r : leer().resenas) {
            if (isbn != null && isbn.equals(r.isbn) && !nombres.containsKey(r.idCliente)) {
                Cliente autor = clienteRepository.buscarPorId(r.idCliente);
                if (autor != null) {
                    nombres.put(r.idCliente, autor.getNombreCompleto());
                }
            }
        }
        return nombres;
    }

    /** Fecha de cada reseña del libro, indexada por id de reseña. */
    @Override
    public synchronized Map<Integer, LocalDateTime> fechasResenas(String isbn) {
        Map<Integer, LocalDateTime> fechas = new HashMap<>();
        for (ResenaRegistro r : leer().resenas) {
            if (isbn != null && isbn.equals(r.isbn)) {
                LocalDateTime fecha = convertirFecha(r.fecha);
                if (fecha != null) {
                    fechas.put(r.id, fecha);
                }
            }
        }
        return fechas;
    }

    /**
     * Reemplaza todas las reseñas del libro por las indicadas (espejo de la BD).
     * Conserva los ids y las fechas que vienen de la BD, para que ambos
     * almacenes coincidan, y mantiene ultimoId por encima del id más alto.
     */
    @Override
    public synchronized void sincronizarLibro(String isbn, List<Review> resenas,
                                              Map<Integer, LocalDateTime> fechas) {
        if (isbn == null) {
            return;
        }
        ResenaData datos = leer();
        datos.resenas.removeIf(r -> isbn.equals(r.isbn));
        if (resenas != null) {
            for (Review review : resenas) {
                LocalDateTime fecha = (fechas == null) ? null : fechas.get(review.id());
                datos.resenas.add(new ResenaRegistro(review.id(), review.isbn(),
                        review.idCliente(), review.calificacion(), review.comentario(),
                        fecha == null ? null : fecha.toString()));
                datos.ultimoId = Math.max(datos.ultimoId, review.id());
            }
        }
        escribir(datos);
    }

    // -------------------------- Métodos auxiliares --------------------------

    /** Indica si el cliente existe y tiene rol ADMIN. */
    private boolean esAdmin(int idCliente) {
        if (clienteRepository == null) {
            return false;
        }
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        return cliente != null && cliente.getRol() == Rol.ADMIN;
    }

    /** Fecha de una reseña para ordenar; las que no tienen fecha van al final. */
    private static LocalDateTime fechaDe(ResenaRegistro registro) {
        LocalDateTime fecha = convertirFecha(registro.fecha);
        return fecha == null ? LocalDateTime.MIN : fecha;
    }

    /** Convierte la fecha ISO-8601 del archivo (null si no es válida). */
    private static LocalDateTime convertirFecha(String fechaIso) {
        if (fechaIso == null || fechaIso.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(fechaIso.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Lee y deserializa el archivo de reseñas.
     *
     * @return Estructura raíz con las reseñas; nunca null
     * @throws PersistenciaException si no se puede leer o el JSON está corrupto
     */
    private ResenaData leer() {
        try {
            if (!Files.exists(rutaArchivo)) {
                return new ResenaData();
            }
            String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
            if (contenido == null || contenido.trim().isEmpty()) {
                return new ResenaData();
            }
            ResenaData datos = gson.fromJson(contenido, ResenaData.class);
            if (datos == null) {
                return new ResenaData();
            }
            if (datos.resenas == null) {
                datos.resenas = new ArrayList<>();
            }
            return datos;
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo de reseñas: "
                    + rutaArchivo, e);
        } catch (JsonParseException e) {
            throw new PersistenciaException("El archivo de reseñas contiene un JSON inválido: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Serializa y escribe el archivo de reseñas con UTF-8.
     *
     * @param datos Estructura completa a persistir
     * @throws PersistenciaException si ocurre un error de escritura
     */
    private void escribir(ResenaData datos) {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            try (Writer writer = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
                gson.toJson(datos, writer);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo de reseñas: "
                    + rutaArchivo, e);
        }
    }

    /** Estructura raíz del archivo JSON. Es privada porque solo este repositorio la usa. */
    private static final class ResenaData {
        private List<ResenaRegistro> resenas = new ArrayList<>();
        private int ultimoId;
    }

    /** Un registro del archivo: una reseña con su fecha (texto ISO-8601). */
    private static final class ResenaRegistro {
        private int id;
        private String isbn;
        private int idCliente;
        private int calificacion;
        private String comentario;
        private String fecha;

        private ResenaRegistro(int id, String isbn, int idCliente, int calificacion,
                               String comentario, String fecha) {
            this.id = id;
            this.isbn = isbn;
            this.idCliente = idCliente;
            this.calificacion = calificacion;
            this.comentario = comentario;
            this.fecha = fecha;
        }
    }
}
