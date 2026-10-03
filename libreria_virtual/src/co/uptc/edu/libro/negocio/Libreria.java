package co.uptc.edu.libro.negocio;

import java.util.ArrayList;
import java.util.List;

import Interfaces.libro.ILibreria;
import co.uptc.edu.libro.modelo.Libro;
import co.uptc.edu.libro.negocio.LibreriaException;

public class Libreria implements ILibreria{
	private List<Libro>listaLibros=new ArrayList<>();
	
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
		    listaLibros.add(libro);
	 }
	    @Override
	    public Libro buscarLibro(String isbn) {
	        return listaLibros.stream()
	                .filter(l -> l.getIsbn().equals(isbn))
	                .findFirst()
	                .orElse(null);
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
	        listaLibros.removeIf(l -> l.getIsbn().equals(isbn));
	    }

	    @Override
	    public List<Libro> getListaLibros() { return listaLibros; }
	}
	

