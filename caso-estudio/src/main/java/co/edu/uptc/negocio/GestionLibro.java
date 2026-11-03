package co.edu.uptc.negocio;

import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.mapper.LibroMapper;

/**
 * Capa de NEGOCIO de los libros (CRUD).
 * Depende de la abstracción ILibroRepositorio (DIP) y valida los datos con
 * ValidadorDatos antes de persistirlos.
 */
public class GestionLibro implements IGestionLibro {

    private final ILibroRepositorio persistencia;
    private final ValidadorDatos validador;

    public GestionLibro(ILibroRepositorio persistencia, ValidadorDatos validador) {
        this.persistencia = persistencia;
        this.validador = validador;
    }

    @Override
    public void guardarLibro(LibroDto libroDto) throws ReglaNegocioException {
        validador.validarLibro(libroDto);

        if (persistencia.existePorIsbn(libroDto.getIsbn())) {
            throw new ReglaNegocioException("Ya existe un libro con ese ISBN.");
        }
        persistencia.guardar(LibroMapper.toEntity(libroDto));
    }

    @Override
    public void actualizarLibro(LibroDto libroDto) throws ReglaNegocioException {
        validador.validarLibro(libroDto);

        if (!persistencia.existePorIsbn(libroDto.getIsbn())) {
            throw new ReglaNegocioException("El libro no existe.");
        }
        persistencia.actualizar(LibroMapper.toEntity(libroDto));
    }

    @Override
    public void eliminarLibro(String isbn) throws ReglaNegocioException {
        validador.validarIsbn(isbn);
        if (!persistencia.existePorIsbn(isbn)) {
            throw new ReglaNegocioException("El libro no existe.");
        }
        persistencia.eliminar(isbn);
    }

    @Override
    public List<LibroDto> listar(FiltroLibroDto filtro) {
        List<Libro> entidades = (filtro == null)
                ? persistencia.listarTodos()
                : persistencia.listarConFiltro(filtro);
        return entidades.stream().map(LibroMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public LibroDto consultarPorIsbn(String isbn) {
        if (isbn == null) return null;
        return LibroMapper.toDto(persistencia.consultarPorIsbn(isbn.trim()));
    }
}
