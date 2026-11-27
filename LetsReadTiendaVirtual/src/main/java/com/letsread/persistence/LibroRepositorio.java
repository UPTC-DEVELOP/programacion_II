package com.letsread.persistence;

import java.util.List;

import com.letsread.model.Libro;

/**
 * Contrato para almacenar libros. Las implementaciones deciden
 * donde se guardan (JSON, base de datos, etc.).
 */
public interface LibroRepositorio {

    List<Libro> listar();

    Libro buscarPorIsbn(String isbn);

    void guardar(Libro libro);

    void actualizar(Libro libro);

    void eliminar(String isbn);
}