package controller;

import java.util.ArrayList;
import java.util.List;

import model.Libro;
import persistencia.IRepositorio;
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

    public ControladorLibro(IRepositorio<Libro> repositorio) {
        this.repositorio = repositorio;
        this.repositorio.cargar();
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
        repositorio.agregar(libro);
        repositorio.guardar();
        return true;
    }

    // LISTADO COMPLETO DE LIBROS (RF04)
    @Override
    public List<Libro> listar() {
        return new ArrayList<Libro>(repositorio.listar());
    }

    // BUSCAR UN LIBRO POR SU CODIGO ISBN (RF09)
    @Override
    public Libro buscar(String isbn) {
        if (isbn == null) {
            return null;
        }
        for (Libro libro : repositorio.listar()) {
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
        List<Libro> resultados = new ArrayList<Libro>();
        String filtro = criterio.toLowerCase();
        for (Libro libro : repositorio.listar()) {
            boolean coincideIsbn = libro.getIsbn() != null && libro.getIsbn().toLowerCase().contains(filtro);
            boolean coincideTitulo = libro.getTitulo() != null && libro.getTitulo().toLowerCase().contains(filtro);
            boolean coincideAutor = libro.getAutor() != null && libro.getAutor().toLowerCase().contains(filtro);
            if (coincideIsbn || coincideTitulo || coincideAutor) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    // ACTUALIZAR LOS DATOS DE UN LIBRO EXISTENTE (RF02)
    @Override
    public boolean actualizar(String isbn, Libro datos) {
        Libro actual = buscar(isbn);
        if (actual == null || datos == null) {
            return false;
        }
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
        repositorio.guardar();
        return true;
    }

    // ELIMINAR UN LIBRO POR ISBN (RF03)
    @Override
    public boolean eliminar(String isbn) {
        Libro actual = buscar(isbn);
        if (actual == null) {
            return false;
        }
        repositorio.eliminar(actual);
        repositorio.guardar();
        return true;
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
            libro.setCantidadDisponible(libro.getCantidadDisponible() - cantidadVendida);
            repositorio.guardar();
            return true;
        }
        return false;
    }
}
