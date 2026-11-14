package co.edu.uptc.service;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.exception.LibroConVentasException;
import co.edu.uptc.exception.LibroDuplicadoException;
import co.edu.uptc.exception.ValidacionException;
import co.edu.uptc.model.Libro;
import co.edu.uptc.model.Pedido;
import co.edu.uptc.persistence.LibroRepository;
import co.edu.uptc.persistence.PedidoRepository;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.util.Validador;

// CatalogoService concentra las reglas del catálogo y no conoce controles Swing ni JSON.
public class CatalogoService {
    private final LibroRepository libros;
    private final PedidoRepository pedidos;
    private final RegistroOperaciones registro;
    public CatalogoService(LibroRepository libros, PedidoRepository pedidos, RegistroOperaciones registro) { this.libros=libros; this.pedidos=pedidos; this.registro=registro; }
    public List<Libro> listar() { return libros.listar(); }
    public Optional<Libro> buscar(String isbn) { return libros.buscarPorIsbn(isbn); }
    public void registrar(Libro libro) {
        validarLibro(libro);
        if (libros.existe(libro.getIsbn())) {
			throw new LibroDuplicadoException("Ya existe un libro con ese ISBN.");
		}
        libros.guardar(libro); registro.registrar("INFO", "admin", "Libro registrado: " + libro.getIsbn());
    }
    public void actualizar(Libro libro) { validarLibro(libro); if (!libros.existe(libro.getIsbn())) {
		throw new ValidacionException("El libro no existe.");
	} libros.guardar(libro); registro.registrar("INFO", "admin", "Libro actualizado: " + libro.getIsbn()); }
    public void eliminar(String isbn) {
        for (Pedido pedido : pedidos.listar()) {
			for (co.edu.uptc.model.LineaPedido linea : pedido.getLineas()) {
				if (linea.getIsbn().equals(isbn)) {
					throw new LibroConVentasException("No se puede eliminar un libro con ventas asociadas.");
				}
			}
		}
        libros.eliminar(isbn); registro.registrar("INFO", "admin", "Libro eliminado: " + isbn);
    }
    private void validarLibro(Libro libro) { Validador.isbn(libro.getIsbn()); Validador.obligatorio(libro.getTitulo(), "título"); Validador.anio(libro.getAnioPublicacion()); Validador.positivo(libro.getPrecioBase(), "precio"); Validador.noNegativo(libro.getStock(), "stock"); Validador.iva(libro.getImpuestoIVA()); Validador.descuento(libro.getPorcentajeDescuento()); }
}
