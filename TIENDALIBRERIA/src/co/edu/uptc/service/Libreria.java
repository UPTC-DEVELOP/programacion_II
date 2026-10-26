package co.edu.uptc.service;

import java.util.List;

import co.edu.uptc.interfaces.ILibreria;
import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.model.Libro;
import co.edu.uptc.exceptions.LibreriaException;

public class Libreria implements ILibreria{
	private final ILibroRepositorio repositorio;

	public Libreria(ILibroRepositorio repositorio) {
		this.repositorio = repositorio;
	}
	
	 @Override
	 public void agregarLibro(Libro libro) throws LibreriaException {
		    // 1. Validacion
		    if (libro == null) {
		        throw new LibreriaException("El libro no puede ser nulo.");
		    }
		    if (buscarLibro(libro.getIsbn()) != null) {
		        throw new LibreriaException("Ya existe un libro registrado con el ISBN: " + libro.getIsbn());
		    }
		    if (libro.getPrecioVenta() < 0) {
		        throw new LibreriaException("El precio de venta no puede ser negativo.");
		    }
		    if (libro.getPaginas() <= 0) {
		        throw new LibreriaException("El número de páginas debe ser mayor a cero.");
		    }
		    repositorio.guardar(libro);
	 }
	    @Override
	    public Libro buscarLibro(String isbn) {
	        return repositorio.buscar(isbn);
	    }

	   	    @Override
	    public void actualizarLibro(String isbn, Libro libroActualizado) throws LibreriaException {
	        
	        if (libroActualizado == null) {
	            throw new LibreriaException("El libro actualizado no puede ser nulo.");
	        }
	           Libro libroExistente = buscarLibro(isbn);
	        if (libroExistente == null) {
	            throw new LibreriaException("No se puede actualizar porque no existe un libro registrado con el ISBN: " + isbn);
	        }
	        if (libroActualizado.getPrecioVenta() < 0) {
	            throw new LibreriaException("El precio de venta no puede ser negativo.");
	        }
	        if (libroActualizado.getPaginas() <= 0) {
	            throw new LibreriaException("El número de páginas debe ser mayor a cero.");
	        }
	        libroExistente.setTituloLibro(libroActualizado.getTituloLibro());
	        libroExistente.setAutor(libroActualizado.getAutor());
	        libroExistente.setFechaPublicacion(libroActualizado.getFechaPublicacion());
	        libroExistente.setEditorial(libroActualizado.getEditorial());
	        libroExistente.setPaginas(libroActualizado.getPaginas());
	        libroExistente.setPrecioVenta(libroActualizado.getPrecioVenta());
	        libroExistente.setCantidadDisponible(libroActualizado.getCantidadDisponible());
	        libroExistente.setCategoria(libroActualizado.getCategoria());
	        libroExistente.setFormato(libroActualizado.getFormato());
	    }

	    @Override
	    public void eliminarLibro(String isbn) {
	        repositorio.eliminar(isbn);
	    }

	    @Override
	    public List<Libro> getListaLibros() { return repositorio.listar(); }
	}
	

