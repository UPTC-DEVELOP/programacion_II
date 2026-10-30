package service;

import java.util.List;

import model.Libro;

/**
 * Contrato de la logica de negocio de libros (inventario).
 *
 * Las ventanas dependen de esta interfaz y no de la implementacion concreta,
 * lo que permite cambiar la logica sin tocar la interfaz grafica.
 */
public interface IServicioLibros {

    boolean registrar(Libro libro);

    List<Libro> listar();

    Libro buscar(String isbn);

    List<Libro> buscarPorCriterio(String criterio);

    boolean actualizar(String isbn, Libro datos);

    boolean eliminar(String isbn);

    boolean validarDisponibilidad(String isbn, int cantidadSolicitada);

    boolean actualizarInventario(String isbn, int cantidadVendida);
}
