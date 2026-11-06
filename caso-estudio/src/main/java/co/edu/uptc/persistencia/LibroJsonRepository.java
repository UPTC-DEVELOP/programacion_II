package co.edu.uptc.persistencia;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;

/**
 * Implementación del repositorio de libros con persistencia en JSON.
 *
 * <p>Gestiona el archivo data/libros.json: lo crea con una estructura vacía si
 * no existe, serializa/deserializa con Gson y aplica los filtros de RF04. No
 * contiene validaciones de negocio; esas viven en GestionLibro y ValidadorDatos
 * (SRP).</p>
 *
 * <p>La clase cumple LSP: puede sustituir a LocalLibro en cualquier punto donde
 * se espere un ILibroRepositorio. Cumple DIP: la capa de negocio solo conoce la
 * interfaz.</p>
 *
 * <p>Es thread-safe: los métodos que leen o escriben el archivo están
 * sincronizados para evitar condiciones de carrera entre hilos.</p>
 *
 * <p>Estructura del archivo:</p>
 * <pre>
 * {
 *   "libros": [ ... ],
 *   "ultimoId": 1
 * }
 * </pre>
 *
 * @version 1.0
 */
public class LibroJsonRepository implements ILibroRepositorio {

    /** Ruta por defecto usada por la aplicación real. */
    private static final String RUTA_POR_DEFECTO = "data/libros.json";

    private final Gson gson;
    private final Path rutaArchivo;
    private final IVentaRepository ventaRepository;

    /**
     * Construye el repositorio usando data/libros.json y sin repositorio de
     * ventas (tieneVentas siempre devolverá false).
     */
    public LibroJsonRepository() {
        this(RUTA_POR_DEFECTO, null);
    }

    /**
     * Construye el repositorio usando data/libros.json y el repositorio de
     * ventas indicado para validar RF03.
     *
     * @param ventaRepository Repositorio de ventas para consultar asociaciones
     */
    public LibroJsonRepository(IVentaRepository ventaRepository) {
        this(RUTA_POR_DEFECTO, ventaRepository);
    }

    /**
     * Construye el repositorio con una ruta personalizada. Se usa en pruebas
     * para no contaminar el archivo real.
     *
     * @param rutaArchivo      Ruta del archivo JSON de libros
     * @param ventaRepository Repositorio de ventas (puede ser null)
     */
    public LibroJsonRepository(String rutaArchivo, IVentaRepository ventaRepository) {
        this.rutaArchivo = Paths.get(rutaArchivo);
        this.ventaRepository = ventaRepository;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        inicializarArchivo();
    }

    /**
     * Crea la carpeta data y el archivo JSON con la estructura base si no
     * existen.
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
                escribir(new LibroData());
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo inicializar el archivo de libros: "
                    + rutaArchivo, e);
        }
    }

    // -------------------------- ILibroRepositorio --------------------------

    /**
     * Guarda un nuevo libro en el archivo JSON (RF01).
     *
     * <p>La validación de ISBN único y de reglas de negocio corresponde a la
     * capa de servicio (SRP). Este método solo persiste el registro y actualiza
     * el contador interno.</p>
     *
     * @param libro Libro a persistir
     * @throws PersistenciaException si ocurre un error de escritura
     */
    @Override
    public synchronized void guardar(Libro libro) {
        if (libro == null) {
            return;
        }
        LibroData datos = leer();
        datos.getLibros().add(libro);
        datos.setUltimoId(datos.getUltimoId() + 1);
        escribir(datos);
    }

    /**
     * Reemplaza en el archivo el libro que tenga el mismo ISBN (RF02).
     *
     * <p>Si el ISBN no existe, el registro se agrega al final; la verificación
     * de existencia previa corresponde al servicio.</p>
     *
     * @param libro Libro actualizado
     * @throws PersistenciaException si ocurre un error de lectura o escritura
     */
    @Override
    public synchronized void actualizar(Libro libro) {
        if (libro == null || libro.getIsbn() == null) {
            return;
        }
        LibroData datos = leer();
        List<Libro> libros = datos.getLibros();
        for (int i = 0; i < libros.size(); i++) {
            if (libro.getIsbn().equals(libros.get(i).getIsbn())) {
                libros.set(i, libro);
                escribir(datos);
                return;
            }
        }
        libros.add(libro);
        datos.setUltimoId(datos.getUltimoId() + 1);
        escribir(datos);
    }

    /**
     * Elimina físicamente del archivo el libro con el ISBN indicado (RF03).
     *
     * <p>La validación "no tiene ventas asociadas" y la confirmación previa
     * corresponden a las capas de servicio y presentación respectivamente.</p>
     *
     * @param isbn ISBN del libro a eliminar
     * @throws PersistenciaException si ocurre un error de lectura o escritura
     */
    @Override
    public synchronized void eliminar(String isbn) {
        if (isbn == null) {
            return;
        }
        LibroData datos = leer();
        boolean eliminado = datos.getLibros().removeIf(libro -> isbn.equals(libro.getIsbn()));
        if (eliminado) {
            escribir(datos);
        }
    }

    /**
     * Verifica si un ISBN existe en el archivo JSON.
     *
     * @param isbn ISBN a buscar
     * @return true si existe un libro con ese ISBN
     */
    @Override
    public synchronized boolean existePorIsbn(String isbn) {
        return consultarPorIsbn(isbn) != null;
    }

    /**
     * Consulta un libro por su ISBN.
     *
     * @param isbn ISBN a buscar
     * @return El libro encontrado o null si no existe
     */
    @Override
    public synchronized Libro consultarPorIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        return leer().getLibros().stream()
                .filter(libro -> isbn.equals(libro.getIsbn()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Lista todos los libros ordenados alfabéticamente por título (RF04).
     *
     * @return Copia ordenada de todos los libros
     */
    @Override
    public synchronized List<Libro> listarTodos() {
        return ordenarPorTitulo(leer().getLibros());
    }

    /**
     * Lista los libros aplicando los filtros opcionales de RF04 (título, autor,
     * categoría y rango de precio) y los ordena alfabéticamente por título.
     *
     * @param filtro Criterios de búsqueda; si es null se listan todos
     * @return Lista filtrada y ordenada
     */
    @Override
    public synchronized List<Libro> listarConFiltro(FiltroLibroDto filtro) {
        if (filtro == null) {
            return listarTodos();
        }
        List<Libro> resultado = leer().getLibros().stream()
                .filter(libro -> coincideFiltro(libro, filtro))
                .collect(Collectors.toList());
        return ordenarPorTitulo(resultado);
    }

    /**
     * Indica si el libro tiene ventas asociadas (RF03).
     *
     * <p>Consulta el repositorio de ventas inyectado. Si no se inyectó ninguno,
     * devuelve false (comportamiento de la implementación en memoria). Se
     * consideran todas las ventas registradas, independientemente de su estado,
     * para proteger la integridad histórica del catálogo.</p>
     *
     * @param isbn ISBN del libro a verificar
     * @return true si alguna venta referencia el ISBN
     */
    @Override
    public synchronized boolean tieneVentas(String isbn) {
        if (isbn == null || ventaRepository == null) {
            return false;
        }
        return ventaRepository.listarTodas().stream()
                .anyMatch(venta -> venta.getIsbns() != null
                        && venta.getIsbns().contains(isbn));
    }

    // -------------------------- Métodos auxiliares --------------------------

    /**
     * Comprueba si un libro cumple todos los criterios del filtro.
     *
     * @param libro  Libro evaluado
     * @param filtro Criterios de búsqueda
     * @return true si el libro coincide con todos los criterios informados
     */
    private boolean coincideFiltro(Libro libro, FiltroLibroDto filtro) {
        String tituloBuscado = filtro.getTitulo();
        if (tituloBuscado != null && !tituloBuscado.trim().isEmpty()) {
            String titulo = libro.getTitulo() == null ? "" : libro.getTitulo();
            if (!titulo.toLowerCase(Locale.ROOT).contains(tituloBuscado.trim().toLowerCase(Locale.ROOT))) {
                return false;
            }
        }

        String autorBuscado = filtro.getAutor();
        if (autorBuscado != null && !autorBuscado.trim().isEmpty()) {
            if (libro.getAutores() == null) {
                return false;
            }
            String patron = autorBuscado.trim().toLowerCase(Locale.ROOT);
            boolean coincideAutor = libro.getAutores().stream()
                    .filter(autor -> autor != null)
                    .anyMatch(autor -> autor.toLowerCase(Locale.ROOT).contains(patron));
            if (!coincideAutor) {
                return false;
            }
        }

        if (filtro.getCategoria() != null && libro.getCategoria() != filtro.getCategoria()) {
            return false;
        }

        if (filtro.getPrecioMin() != null && libro.getPrecioVenta() < filtro.getPrecioMin()) {
            return false;
        }
        return filtro.getPrecioMax() == null || libro.getPrecioVenta() <= filtro.getPrecioMax();
    }

    /**
     * Ordena una lista de libros por título, sin distinguir mayúsculas.
     *
     * @param libros Lista de libros a ordenar
     * @return Nueva lista ordenada (copia defensiva)
     */
    private List<Libro> ordenarPorTitulo(List<Libro> libros) {
        List<Libro> copia = new ArrayList<>(libros);
        copia.sort(Comparator.comparing(
                libro -> libro.getTitulo() == null ? "" : libro.getTitulo(),
                String.CASE_INSENSITIVE_ORDER));
        return copia;
    }

    /**
     * Lee y deserializa el archivo de libros.
     *
     * @return Estructura raíz con los libros; nunca null
     * @throws PersistenciaException si el archivo no se puede leer o el JSON
     *                              está corrupto
     */
    private LibroData leer() {
        try {
            if (!Files.exists(rutaArchivo)) {
                return new LibroData();
            }
            String contenido = Files.readString(rutaArchivo, StandardCharsets.UTF_8);
            if (contenido == null || contenido.trim().isEmpty()) {
                return new LibroData();
            }
            LibroData datos = gson.fromJson(contenido, LibroData.class);
            if (datos == null) {
                return new LibroData();
            }
            if (datos.getLibros() == null) {
                datos.setLibros(new ArrayList<>());
            }
            return datos;
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo leer el archivo de libros: "
                    + rutaArchivo, e);
        } catch (JsonParseException e) {
            throw new PersistenciaException("El archivo de libros contiene un JSON inválido: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Serializa y escribe el archivo de libros con UTF-8.
     *
     * @param datos Estructura completa a persistir
     * @throws PersistenciaException si ocurre un error de escritura
     */
    private void escribir(LibroData datos) {
        try {
            Path carpeta = rutaArchivo.toAbsolutePath().getParent();
            if (carpeta != null) {
                Files.createDirectories(carpeta);
            }
            try (Writer writer = Files.newBufferedWriter(rutaArchivo, StandardCharsets.UTF_8)) {
                gson.toJson(datos, writer);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo escribir el archivo de libros: "
                    + rutaArchivo, e);
        }
    }

    /**
     * Estructura raíz del archivo JSON de libros.
     *
     * <p>Se declara como clase anidada privada porque solo este repositorio la
     * necesita; así se evita exponer un detalle de persistencia (SRP).</p>
     */
    private static final class LibroData {

        private List<Libro> libros = new ArrayList<>();
        private int ultimoId;

        public LibroData() {
        }

        public List<Libro> getLibros() {
            return libros;
        }

        public void setLibros(List<Libro> libros) {
            this.libros = libros;
        }

        public int getUltimoId() {
            return ultimoId;
        }

        public void setUltimoId(int ultimoId) {
            this.ultimoId = ultimoId;
        }
    }
}
