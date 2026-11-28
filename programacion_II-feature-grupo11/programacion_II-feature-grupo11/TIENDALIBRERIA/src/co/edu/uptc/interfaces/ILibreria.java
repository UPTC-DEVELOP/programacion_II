package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.model.Libro;
import co.edu.uptc.exceptions.LibreriaException;

public interface ILibreria {
	    void agregarLibro(Libro libro) throws LibreriaException;
	    Libro buscarLibro(String isbn);
	    void actualizarLibro(String isbn, Libro nuevo) throws LibreriaException;
	    void eliminarLibro(String isbn);
	    List<Libro> getListaLibros();
	}

