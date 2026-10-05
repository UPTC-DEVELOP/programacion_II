package Interfaces.libro;

import java.util.List;

import co.uptc.edu.libro.modelo.Libro;
import co.uptc.edu.libro.negocio.LibreriaException;

public interface ILibreria {
	    void agregarLibro(Libro libro) throws LibreriaException;
	    Libro buscarLibro(String isbn);
	    void actualizarLibro(String isbn, Libro nuevo) throws LibreriaException;
	    void eliminarLibro(String isbn);
	    List<Libro> getListaLibros();
	}

