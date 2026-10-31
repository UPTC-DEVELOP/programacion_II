package co.edu.uptc.libreria.interfaces;

import java.util.List;

import co.edu.uptc.libreria.modelo.Libro;

public interface IGestionLibro {

    void guardar(Libro libro);

    void actualizar(Libro libro);

    void eliminar(String codigo);

    List<Libro> listar();
}