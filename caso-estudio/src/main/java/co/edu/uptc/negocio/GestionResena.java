package co.edu.uptc.negocio;

import java.util.List;

import co.edu.uptc.excepciones.ClienteNoEncontradoException;
import co.edu.uptc.excepciones.LibroNoEncontradoException;
import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.excepciones.ValidacionException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionResena;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IReviewRepository;
import co.edu.uptc.interfaces.IReviewRepository.Review;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.Venta;
import co.edu.uptc.modelo.enums.Rol;

/**
 * LÓGICA DE NEGOCIO DEL CRUD DE RESEÑAS Y CALIFICACIONES  (paquete: negocio)
 * ---------------------------------------------------------------------------
 * Implementa IGestionResena. Reglas aplicadas:
 *  - Calificación entera entre 1 y 5.
 *  - Comentario obligatorio, máximo 500 caracteres.
 *  - El libro y el cliente deben existir.
 *  - Una sola reseña por cliente y libro.
 *  - Solo el dueño edita su reseña; el dueño o un ADMIN pueden eliminarla.
 *
 * Solo reseña quien compró el libro: se verifica aquí con IVentaRepository (vale
 * para JDBC y para JSON) y PostgreSQL (RLS) lo exige además; si la base rechaza
 * la inserción se traduce a un mensaje comprensible.
 *
 * Persistencia dual: PostgreSQL (ReviewJdbcRepository) es la fuente de verdad y
 * ReviewJsonRepository funciona como espejo (data/resenas.json). Esta clase solo
 * conoce IReviewRepository, así que cualquiera de las dos es intercambiable.
 *
 * SOLID: SRP (solo reseñas) y DIP (depende de interfaces inyectadas).
 *
 * @author: Equipo de desarrollo grupo 7 UPTC
 */
public class GestionResena implements IGestionResena {

    public static final int CALIFICACION_MIN = 1;
    public static final int CALIFICACION_MAX = 5;
    public static final int COMENTARIO_MAX = 500;

    private final IReviewRepository reviewRepository;
    private final ILibroRepositorio libroRepository;
    private final IClienteRepositorio clienteRepository;
    /** Ventas para verificar que el cliente compró el libro (puede ser null). */
    private final IVentaRepository ventaRepository;
    /** Espejo JSON de las reseñas (puede ser null). La BD sigue siendo la fuente de verdad. */
    private final IReviewRepository reviewRepositoryJson;

    /** Constructor sin verificación de compra en el negocio (la hace la BD con RLS). */
    public GestionResena(IReviewRepository reviewRepository,
                         ILibroRepositorio libroRepository,
                         IClienteRepositorio clienteRepository) {
        this(reviewRepository, libroRepository, clienteRepository, null);
    }

    /**
     * Constructor con verificación de compra en el negocio. Así la regla
     * "solo reseña quien compró el libro" vale igual con la persistencia JDBC
     * que con la JSON (que no tiene políticas RLS).
     *
     * @param ventaRepository Repositorio de ventas (null desactiva la verificación)
     */
    public GestionResena(IReviewRepository reviewRepository,
                         ILibroRepositorio libroRepository,
                         IClienteRepositorio clienteRepository,
                         IVentaRepository ventaRepository) {
        this(reviewRepository, libroRepository, clienteRepository, ventaRepository, null);
    }

    /**
     * Constructor con espejo JSON. La persistencia principal (PostgreSQL) sigue
     * siendo la fuente de verdad; después de cada crear, actualizar o eliminar
     * confirmado, se copian al JSON las reseñas vigentes del libro (con los
     * mismos ids y fechas que tiene la BD).
     *
     * @param reviewRepositoryJson Repositorio JSON de respaldo (null desactiva el espejo)
     */
    public GestionResena(IReviewRepository reviewRepository,
                         ILibroRepositorio libroRepository,
                         IClienteRepositorio clienteRepository,
                         IVentaRepository ventaRepository,
                         IReviewRepository reviewRepositoryJson) {
        this.reviewRepository = reviewRepository;
        this.libroRepository = libroRepository;
        this.clienteRepository = clienteRepository;
        this.ventaRepository = ventaRepository;
        this.reviewRepositoryJson = reviewRepositoryJson;
    }

    @Override
    public void crear(int idCliente, String isbn, int calificacion, String comentario)
            throws ReglaNegocioException {
        validarCliente(idCliente);
        validarLibro(isbn);
        String texto = validarContenido(calificacion, comentario);
        validarCompra(idCliente, isbn);
        for (Review r : consultar(isbn)) {
            if (r.idCliente() == idCliente) {
                throw new ReglaNegocioException(
                        "Ya registró una reseña para este libro. Edítela o elimínela.");
            }
        }
        try {
            reviewRepository.guardar(new Review(0, isbn, idCliente, calificacion, texto));
        } catch (PersistenciaException e) {
            throw new ReglaNegocioException("No se pudo guardar la reseña. "
                    + "Verifique que haya comprado este libro.");
        }
        replicarEnJson(isbn);
    }

    @Override
    public List<Review> listarPorLibro(String isbn) throws ReglaNegocioException {
        validarLibro(isbn);
        return consultar(isbn);
    }

    @Override
    public java.util.Map<Integer, String> nombresAutores(String isbn) {
        try {
            return reviewRepository.nombresClientes(isbn);
        } catch (PersistenciaException e) {
            // Degradación elegante: sin nombres se muestra "Cliente #id".
            return java.util.Map.of();
        }
    }

    @Override
    public java.util.Map<Integer, java.time.LocalDateTime> fechasResenas(String isbn) {
        try {
            return reviewRepository.fechasResenas(isbn);
        } catch (PersistenciaException e) {
            return java.util.Map.of();
        }
    }

    @Override
    public double promedioCalificacion(String isbn) throws ReglaNegocioException {
        List<Review> resenas = listarPorLibro(isbn);
        if (resenas.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Review r : resenas) {
            suma += r.calificacion();
        }
        return Math.round(suma / resenas.size() * 10.0) / 10.0;
    }

    @Override
    public void actualizar(int idCliente, String isbn, int idResena, int calificacion,
                           String comentario) throws ReglaNegocioException {
        validarCliente(idCliente);
        validarLibro(isbn);
        String texto = validarContenido(calificacion, comentario);
        Review existente = buscar(isbn, idResena);
        if (existente.idCliente() != idCliente) {
            throw new ReglaNegocioException("Solo puede editar sus propias reseñas.");
        }
        try {
            reviewRepository.actualizar(
                    new Review(idResena, isbn, idCliente, calificacion, texto));
        } catch (PersistenciaException e) {
            throw new ReglaNegocioException("No se pudo actualizar la reseña.");
        }
        replicarEnJson(isbn);
    }

    @Override
    public void eliminar(int idCliente, String isbn, int idResena) throws ReglaNegocioException {
        Cliente cliente = validarCliente(idCliente);
        validarLibro(isbn);
        Review existente = buscar(isbn, idResena);
        boolean esAdmin = cliente.getRol() == Rol.ADMIN;
        if (existente.idCliente() != idCliente && !esAdmin) {
            throw new ReglaNegocioException("Solo puede eliminar sus propias reseñas.");
        }
        try {
            reviewRepository.eliminar(idResena, idCliente);
        } catch (PersistenciaException e) {
            throw new ReglaNegocioException("No se pudo eliminar la reseña.");
        }
        replicarEnJson(isbn);
    }

    // ------------------------------ auxiliares ------------------------------

    /**
     * Copia al espejo JSON las reseñas vigentes del libro, leídas de la BD
     * (fuente de verdad), con sus ids y fechas reales. Un fallo del espejo se
     * informa por consola y NO invalida la operación ya confirmada en la BD.
     *
     * @param isbn ISBN del libro cuyas reseñas cambiaron
     */
    private void replicarEnJson(String isbn) {
        if (reviewRepositoryJson == null) {
            return;
        }
        try {
            reviewRepositoryJson.sincronizarLibro(isbn,
                    reviewRepository.listarPorLibro(isbn),
                    reviewRepository.fechasResenas(isbn));
        } catch (PersistenciaException e) {
            System.err.println("No se pudo replicar en JSON las reseñas del libro "
                    + isbn + ": " + e.getMessage());
        }
    }

    private Cliente validarCliente(int idCliente) throws ReglaNegocioException {
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No existe el cliente que reseña el libro.");
        }
        return cliente;
    }

    /**
     * Verifica que el cliente haya comprado el libro. Si no se inyectó el
     * repositorio de ventas no se verifica aquí (la BD lo exige con RLS).
     */
    private void validarCompra(int idCliente, String isbn) throws ReglaNegocioException {
        if (ventaRepository == null) {
            return;
        }
        try {
            for (Venta venta : ventaRepository.listarTodas()) {
                if (venta.getIdCliente() == idCliente
                        && venta.getIsbns() != null && venta.getIsbns().contains(isbn)) {
                    return;
                }
            }
        } catch (PersistenciaException e) {
            throw new ReglaNegocioException("No se pudo verificar sus compras. Intente de nuevo.");
        }
        throw new ReglaNegocioException("Solo puede reseñar libros que haya comprado.");
    }

    private void validarLibro(String isbn) throws ReglaNegocioException {
        if (isbn == null || isbn.isBlank() || !libroRepository.existePorIsbn(isbn)) {
            throw new LibroNoEncontradoException("El libro indicado no existe.");
        }
    }

    private String validarContenido(int calificacion, String comentario)
            throws ValidacionException {
        if (calificacion < CALIFICACION_MIN || calificacion > CALIFICACION_MAX) {
            throw new ValidacionException("La calificación debe estar entre "
                    + CALIFICACION_MIN + " y " + CALIFICACION_MAX + ".");
        }
        String texto = comentario == null ? "" : comentario.trim();
        if (texto.isEmpty()) {
            throw new ValidacionException("El comentario es obligatorio.");
        }
        if (texto.length() > COMENTARIO_MAX) {
            throw new ValidacionException("El comentario no puede superar "
                    + COMENTARIO_MAX + " caracteres.");
        }
        return texto;
    }

    private List<Review> consultar(String isbn) throws ReglaNegocioException {
        try {
            return reviewRepository.listarPorLibro(isbn);
        } catch (PersistenciaException e) {
            throw new ReglaNegocioException("No se pudieron consultar las reseñas.");
        }
    }

    private Review buscar(String isbn, int idResena) throws ReglaNegocioException {
        for (Review r : consultar(isbn)) {
            if (r.id() == idResena) {
                return r;
            }
        }
        throw new ReglaNegocioException("La reseña indicada no existe.");
    }
}
