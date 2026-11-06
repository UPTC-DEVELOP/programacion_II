package co.edu.uptc.negocio;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.excepciones.LibroConVentasException;
import co.edu.uptc.excepciones.LibroExistenteException;
import co.edu.uptc.excepciones.LibroNoEncontradoException;
import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.enums.AccionAuditoria;
import co.edu.uptc.modelo.mapper.LibroMapper;

/**
 * Capa de NEGOCIO de los libros (CRUD).
 *
 * <p>Depende de la abstracción ILibroRepositorio (DIP) y valida los datos con
 * ValidadorDatos antes de persistirlos. También registra cada operación en la
 * auditoría (RF17/RF18) a través de la abstracción IAuditoria.</p>
 *
 * <p>Aplica:
 * <ul>
 *   <li>SRP: contiene solo reglas de negocio y orquestación; no sabe cómo se
 *       guardan los datos.</li>
 *   <li>DIP: recibe las dependencias por constructor.</li>
 *   <li>OCP: puede cambiar la persistencia (JSON, JDBC, memoria) sin tocar esta
 *       clase.</li>
 *   <li>LSP: funciona con cualquier implementación de ILibroRepositorio.</li>
 * </ul>
 */
public class GestionLibro implements IGestionLibro {

    /** Usuario por defecto para las operaciones del panel administrador. */
    private static final String USUARIO_ADMIN = "admin";

    private final ILibroRepositorio persistencia;
    private final ValidadorDatos validador;
    private final IAuditoria auditoria;

    /**
     * Constructor compatible con el código existente (sin auditoría).
     *
     * @param persistencia Repositorio de libros
     * @param validador    Validador de datos
     */
    public GestionLibro(ILibroRepositorio persistencia, ValidadorDatos validador) {
        this(persistencia, validador, null);
    }

    /**
     * Constructor con inyección de auditoría (RF17/RF18).
     *
     * @param persistencia Repositorio de libros
     * @param validador    Validador de datos
     * @param auditoria    Servicio de auditoría (puede ser null)
     */
    public GestionLibro(ILibroRepositorio persistencia, ValidadorDatos validador,
                        IAuditoria auditoria) {
        this.persistencia = persistencia;
        this.validador = validador;
        this.auditoria = auditoria;
    }

    /**
     * Registra un libro nuevo validando las reglas de RF01.
     *
     * @param libroDto Libro a registrar
     * @throws ReglaNegocioException si los datos no son válidos
     * @throws LibroExistenteException si el ISBN ya está registrado
     */
    @Override
    public void guardarLibro(LibroDto libroDto) throws ReglaNegocioException {
        validador.validarLibro(libroDto);

        if (persistencia.existePorIsbn(libroDto.getIsbn())) {
            throw new LibroExistenteException("El ISBN " + libroDto.getIsbn()
                    + " ya está registrado en el catálogo.");
        }

        persistencia.guardar(LibroMapper.toEntity(libroDto));
        auditar(AccionAuditoria.CREATE, libroDto.getIsbn(),
                "Registro de nuevo libro: " + libroDto.getTitulo());
    }

    /**
     * Actualiza un libro existente validando las reglas de RF02.
     *
     * <p>El ISBN actúa como clave primaria y no se modifica: la GUI lo bloquea
     * en modo edición y el servicio reemplaza el registro con el mismo ISBN.</p>
     *
     * @param libroDto Libro con los datos actualizados
     * @throws ReglaNegocioException si los datos no son válidos
     * @throws LibroNoEncontradoException si el ISBN no existe
     */
    @Override
    public void actualizarLibro(LibroDto libroDto) throws ReglaNegocioException {
        validador.validarLibro(libroDto);

        if (!persistencia.existePorIsbn(libroDto.getIsbn())) {
            throw new LibroNoEncontradoException("No existe un libro con el ISBN "
                    + libroDto.getIsbn() + ".");
        }

        persistencia.actualizar(LibroMapper.toEntity(libroDto));
        auditar(AccionAuditoria.UPDATE, libroDto.getIsbn(),
                "Actualización de libro: " + libroDto.getTitulo()
                        + " (precio=" + libroDto.getPrecioVenta()
                        + ", stock=" + libroDto.getStock() + ")");
    }

    /**
     * Elimina un libro validando las reglas de RF03.
     *
     * <p>Verifica que el ISBN exista y que no tenga ventas asociadas. La
     * confirmación previa al borrado es responsabilidad de la GUI.</p>
     *
     * @param isbn ISBN del libro a eliminar
     * @throws ReglaNegocioException si el ISBN no es válido
     * @throws LibroNoEncontradoException si el libro no existe
     * @throws LibroConVentasException si el libro tiene ventas asociadas
     */
    @Override
    public void eliminarLibro(String isbn) throws ReglaNegocioException {
        validador.validarIsbn(isbn);

        Libro existente = persistencia.consultarPorIsbn(isbn);
        if (existente == null) {
            throw new LibroNoEncontradoException("No existe un libro con el ISBN " + isbn + ".");
        }

        if (persistencia.tieneVentas(isbn)) {
            throw new LibroConVentasException("El libro \"" + existente.getTitulo()
                    + "\" tiene ventas asociadas y no puede eliminarse.");
        }

        persistencia.eliminar(isbn);
        auditar(AccionAuditoria.DELETE, isbn,
                "Eliminación de libro: " + existente.getTitulo());
    }

    /**
     * Lista los libros aplicando el filtro cuando no es nulo (RF04).
     *
     * <p>El resultado se ordena alfabéticamente por título sin distinguir
     * mayúsculas, garantizando el requisito de ordenación del catálogo.</p>
     *
     * @param filtro Criterios de búsqueda opcionales
     * @return Lista de libros ordenada alfabéticamente
     */
    @Override
    public List<LibroDto> listar(FiltroLibroDto filtro) {
        List<Libro> entidades = (filtro == null)
                ? persistencia.listarTodos()
                : persistencia.listarConFiltro(filtro);

        return entidades.stream()
                .map(LibroMapper::toDto)
                .sorted(Comparator.comparing(
                        dto -> dto.getTitulo() == null ? "" : dto.getTitulo(),
                        String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    /**
     * Consulta un libro por ISBN.
     *
     * @param isbn ISBN a buscar
     * @return DTO del libro o null si no existe
     */
    @Override
    public LibroDto consultarPorIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        return LibroMapper.toDto(persistencia.consultarPorIsbn(isbn.trim()));
    }

    /**
     * Registra una operación en la auditoría sin interrumpir el CRUD si la
     * auditoría falla. Un fallo de auditoría se informa por consola, pero la
     * operación principal ya fue persistida y no debe reportarse como fallida.
     *
     * @param accion   Acción realizada
     * @param isbn     ISBN del libro afectado
     * @param detalles Descripción del cambio
     */
    private void auditar(AccionAuditoria accion, String isbn, String detalles) {
        if (auditoria == null) {
            return;
        }
        try {
            auditoria.registrar(accion, isbn, USUARIO_ADMIN, detalles);
        } catch (PersistenciaException e) {
            System.err.println("No se pudo registrar la auditoría: " + e.getMessage());
        }
    }
}
