package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.Libro;

public interface IGestionLibro {

    void guardar(Libro libro);

    void actualizar(Libro libro);

    void eliminar(String codigo);

    List<Libro> listar();
}