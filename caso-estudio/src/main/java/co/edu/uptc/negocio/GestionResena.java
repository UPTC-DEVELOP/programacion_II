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
import co.edu.uptc.modelo.Cliente;
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
 * PostgreSQL (RLS) exige además que quien reseña haya comprado el libro; si la
 * base rechaza la inserción se traduce a un mensaje comprensible.
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

    public GestionResena(IReviewRepository reviewRepository,
                         ILibroRepositorio libroRepository,
                         IClienteRepositorio clienteRepository) {
        this.reviewRepository = reviewRepository;
        this.libroRepository = libroRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void crear(int idCliente, String isbn, int calificacion, String comentario)
            throws ReglaNegocioException {
        validarCliente(idCliente);
        validarLibro(isbn);
        String texto = validarContenido(calificacion, comentario);
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
    }

    // ------------------------------ auxiliares ------------------------------

    private Cliente validarCliente(int idCliente) throws ReglaNegocioException {
        Cliente cliente = clienteRepository.buscarPorId(idCliente);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No existe el cliente que reseña el libro.");
        }
        return cliente;
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
