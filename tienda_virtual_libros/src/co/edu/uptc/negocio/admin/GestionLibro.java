package co.edu.uptc.negocio.admin;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.interfaces.IConsultaVentas;
import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.interfaces.IValidadorLibro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.Libro;


/**
 * CLASE GestionLibro  (paquete: negocio)  implements IGestionLibro
 * ---------------------------------------------------------------------------
 * CAPA DE NEGOCIO: implementa RF01, RF02, RF03 y RF04.
 * Equivale a GestionEmpleadoFijo del proyecto del profesor.
 *
 * SOLID en esta clase:
 *  - S: orquesta el CRUD; validar -> validador, guardar -> repositorio,
 *       auditar -> auditoría, convertir -> mapper.
 *  - O: nuevas reglas se agregan inyectando otro validador, sin editar esto.
 *  - L: cualquier ILibroRepositorio (JSON, JDBC...) funciona igual aquí.
 *  - I: usa interfaces pequeñas y específicas.
 *  - D: RECIBE sus dependencias por el constructor (inyección de dependencias)
 *       y solo conoce interfaces; no hace "new" de clases de almacenamiento.
 */
public class GestionLibro implements IGestionLibro {

    private final ILibroRepositorio repositorio;
    private final IConsultaVentas ventas;
    private final IAuditoria auditoria;
    private final IValidadorLibro validador;

    public GestionLibro(ILibroRepositorio repositorio, IConsultaVentas ventas,
                        IAuditoria auditoria, IValidadorLibro validador) {
        this.repositorio = repositorio;
        this.ventas = ventas;
        this.auditoria = auditoria;
        this.validador = validador;
    }

    // =====================================================================
    // RF01 - Registrar libro
    // =====================================================================
    @Override
    public void registrar(LibroDto dto) throws ReglaNegocioException {
        // 1) Validaciones de campos (longitudes, rangos, enums).
        validador.validarDatos(dto);

        // 2) Regla que SOLO el negocio puede comprobar: el ISBN debe ser único.
        if (repositorio.buscarPorIsbn(dto.getIsbn().trim()) != null) {
            auditoria.registrar("REGISTRAR_LIBRO_RECHAZADO", "ISBN duplicado: " + dto.getIsbn());
            throw new ReglaNegocioException("Ya existe un libro registrado con el ISBN " + dto.getIsbn() + ".");
        }

        // 3) Se convierte a entidad y se entrega al repositorio para que la almacene.
        repositorio.guardar(LibroMapper.aEntidad(dto));

        // 4) Se deja rastro en el log de auditoría.
        auditoria.registrar("REGISTRAR_LIBRO", "ISBN " + dto.getIsbn() + " - " + dto.getTitulo());
    }

    // =====================================================================
    // RF02 - Actualizar libro
    // =====================================================================
    @Override
    public void actualizar(LibroDto dto) throws ReglaNegocioException {
        // El ISBN es clave primaria: se usa para BUSCAR, nunca para modificar.
        Libro existente = repositorio.buscarPorIsbn(dto.getIsbn());
        if (existente == null) {
            throw new ReglaNegocioException("No existe un libro con el ISBN " + dto.getIsbn() + ".");
        }

        // Mismas reglas de campos (precio > 0, stock >= 0, longitudes...).
        validador.validarDatos(dto);

        Libro actualizado = LibroMapper.aEntidad(dto);
        actualizado.setIsbn(existente.getIsbn());   // blindaje: el ISBN original manda
        repositorio.actualizar(actualizado);

        auditoria.registrar("ACTUALIZAR_LIBRO", "ISBN " + existente.getIsbn());
    }

    // =====================================================================
    // RF03 - Eliminar libro
    // =====================================================================
    @Override
    public void eliminar(String isbn) throws ReglaNegocioException {
        if (repositorio.buscarPorIsbn(isbn) == null) {
            throw new ReglaNegocioException("No existe un libro con el ISBN " + isbn + ".");
        }

        // Regla de negocio clave: no se borra un libro con ventas en el historial.
        if (ventas.libroTieneVentas(isbn)) {
            auditoria.registrar("ELIMINAR_LIBRO_RECHAZADO", "ISBN " + isbn + " tiene ventas");
            throw new ReglaNegocioException("El libro no puede ser eliminado porque tiene ventas registradas");
        }

        repositorio.eliminar(isbn);   // se quita el libro de la colección
        auditoria.registrar("ELIMINAR_LIBRO", "ISBN " + isbn);
    }

    // =====================================================================
    // RF04 - Listar catálogo con filtros opcionales y combinables
    // =====================================================================
    @Override
    public List<LibroDto> listar(FiltroLibroDto filtro) {
        List<Libro> resultado = new ArrayList<>();

        for (Libro libro : repositorio.listarTodos()) {
            if (cumpleFiltro(libro, filtro)) {
                resultado.add(libro);
            }
        }

        // Orden alfabético por título (Collator respeta tildes y la ñ en español).
        Collator collator = Collator.getInstance(Locale.forLanguageTag("es-CO"));
        resultado.sort(Comparator.comparing(Libro::getTitulo, collator));

        List<LibroDto> dtos = new ArrayList<>();
        for (Libro libro : resultado) {
            dtos.add(LibroMapper.aDto(libro));
        }
        return dtos;
    }

    /** Todos los filtros son opcionales: un filtro nulo/vacío no descarta nada. */
    private boolean cumpleFiltro(Libro libro, FiltroLibroDto f) {
        if (f == null) {
            return true;
        }
        if (hayTexto(f.getTexto())) {
            String t = f.getTexto().trim().toLowerCase();
            boolean enIsbn = libro.getIsbn().toLowerCase().contains(t);
            boolean enTitulo = libro.getTitulo().toLowerCase().contains(t);
            if (!enIsbn && !enTitulo) {
                return false;
            }
        }
        if (f.getCategoria() != null && libro.getCategoria() != f.getCategoria()) {
            return false;
        }
        if (hayTexto(f.getTitulo())
                && !libro.getTitulo().toLowerCase().contains(f.getTitulo().trim().toLowerCase())) {
            return false;
        }
        if (hayTexto(f.getAutor()) && !algunAutorContiene(libro, f.getAutor().trim().toLowerCase())) {
            return false;
        }
        if (f.getPrecioMin() != null && libro.getPrecioVenta() < f.getPrecioMin()) {
            return false;
        }
        return f.getPrecioMax() == null || libro.getPrecioVenta() <= f.getPrecioMax();
    }

    private boolean algunAutorContiene(Libro libro, String texto) {
        for (String autor : libro.getAutores()) {
            if (autor.toLowerCase().contains(texto)) {
                return true;
            }
        }
        return false;
    }

    private boolean hayTexto(String s) {
        return s != null && !s.trim().isEmpty();
    }

    @Override
    public LibroDto buscarPorIsbn(String isbn) {
        Libro libro = repositorio.buscarPorIsbn(isbn);
        return libro == null ? null : LibroMapper.aDto(libro);
    }
}
