package controller;

import java.util.List;

import model.Libro;
import persistencia.IRepositorio;
import persistencia.PersistenciaException;
import service.IServicioLibros;

/**
 * Logica de negocio del inventario de libros.
 *
 * Implementa IServicioLibros (la interfaz que usan las ventanas) y delega el
 * almacenamiento en un IRepositorio&lt;Libro&gt; inyectado por constructor.
 * El stock se guarda fisicamente tras cada cambio (SOLID: DIP + SRP).
 */
public class ControladorLibro implements IServicioLibros {

    private final IRepositorio<Libro> repositorio;
    private final ControladorOperaciones operaciones;

    public ControladorLibro(IRepositorio<Libro> repositorio) {
        this(repositorio, null);
    }

    public ControladorLibro(IRepositorio<Libro> repositorio,
            ControladorOperaciones operaciones) {
        this.repositorio = repositorio;
        this.operaciones = operaciones;
    }

    // REGISTRO DE UN NUEVO LIBRO (RF01)
    @Override
    public boolean registrar(Libro libro) {
        if (libro == null) {
            return false;
        }
        if (libro.getIsbn() == null || libro.getIsbn().trim().isEmpty()) {
            return false;
        }
        if (libro.getTitulo() == null || libro.getTitulo().trim().isEmpty()) {
            return false;
        }
        if (libro.getPrecio() <= 0) {
            return false;
        }
        if (buscar(libro.getIsbn()) != null) {
            return false;
        }
        try {
            repositorio.insertar(libro);
            registrarOperacion("Libro registrado: " + libro.getIsbn());
            return true;
        } catch (PersistenciaException e) {
            return false;
        }
    }

    // LISTADO COMPLETO DE LIBROS (RF04)
    @Override
    public List<Libro> listar() {
        return repositorio.listarUltimos(10);
    }

    // BUSCAR UN LIBRO POR SU CODIGO ISBN (RF09)
    @Override
    public Libro buscar(String isbn) {
        if (isbn == null) {
            return null;
        }
        for (Libro libro : repositorio.buscar(isbn)) {
            if (isbn.equalsIgnoreCase(libro.getIsbn())) {
                return libro;
            }
        }
        return null;
    }

    // BUSCAR POR COINCIDENCIA EN ISBN, TITULO O AUTOR (RF09)
    @Override
    public List<Libro> buscarPorCriterio(String criterio) {
        if (criterio == null || criterio.trim().isEmpty()) {
            return listar();
        }
        return repositorio.buscar(criterio);
    }

    // ACTUALIZAR LOS DATOS DE UN LIBRO EXISTENTE (RF02)
    @Override
    public boolean actualizar(String isbn, Libro datos) {
        Libro actual = buscar(isbn);
        if (actual == null || datos == null) {
            return false;
        }
        try {
            actual.setTitulo(datos.getTitulo());
            actual.setAutor(datos.getAutor());
            actual.setAnioPublicacion(datos.getAnioPublicacion());
            actual.setCategoria(datos.getCategoria());
            actual.setEditorial(datos.getEditorial());
            actual.setNumeroPaginas(datos.getNumeroPaginas());
            actual.setPrecio(datos.getPrecio());
            actual.setPorcentajeIva(datos.getPorcentajeIva());
            actual.setCantidadDisponible(datos.getCantidadDisponible());
            actual.setFormato(datos.getFormato());
            repositorio.actualizar(actual);
            registrarOperacion("Libro actualizado: " + isbn);
            return true;
        } catch (PersistenciaException e) {
            return false;
        }
    }

    // ELIMINAR UN LIBRO POR ISBN (RF03)
    @Override
    public boolean eliminar(String isbn) {
        Libro actual = buscar(isbn);
        if (actual == null) {
            return false;
        }
        try {
            boolean eliminado = repositorio.eliminarPersistente(actual);
            if (eliminado) {
                registrarOperacion("Libro eliminado: " + isbn);
            }
            return eliminado;
        } catch (PersistenciaException e) {
            return false;
        }
    }

    // VALIDAR LA DISPONIBILIDAD DE STOCK (RF17)
    @Override
    public boolean validarDisponibilidad(String isbn, int cantidadSolicitada) {
        Libro libro = buscar(isbn);
        if (libro != null) {
            return libro.getCantidadDisponible() >= cantidadSolicitada;
        }
        return false;
    }

    // ACTUALIZAR EL INVENTARIO TRAS UNA VENTA (RF18)
    @Override
    public boolean actualizarInventario(String isbn, int cantidadVendida) {
        Libro libro = buscar(isbn);
        if (libro != null && libro.getCantidadDisponible() >= cantidadVendida) {
            try {
                libro.setCantidadDisponible(libro.getCantidadDisponible() - cantidadVendida);
                repositorio.actualizar(libro);
                return true;
            } catch (PersistenciaException e) {
                return false;
            }
        }
        return false;
    }

    private void registrarOperacion(String descripcion) {
        if (operaciones != null) {
            operaciones.registrar(descripcion);
        }
    }
}
