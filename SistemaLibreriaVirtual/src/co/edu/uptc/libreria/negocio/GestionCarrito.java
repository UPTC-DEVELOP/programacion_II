package co.edu.uptc.libreria.negocio;

import co.edu.uptc.libreria.modelo.*;
import co.edu.uptc.libreria.persistencia.*;
import co.edu.uptc.libreria.negocio.*;
import co.edu.uptc.libreria.modelo.enums.*;

import java.util.ArrayList;
import java.util.List;

public class GestionCarrito {
	
	private List<ItemCarrito> items;
	private CarritoPersistencia persistencia;
	private ServicioAuditoria auditoria;
	private CalculadoraCarrito calculadora;
	private String usuarioActual = "";
	private boolean esPremium = false;
	private TipoCliente tipoCliente;
	
	
	public GestionCarrito(CarritoPersistencia persistencia, ServicioAuditoria auditoria) {
		this.persistencia = persistencia;
		this.auditoria = auditoria;
		this.calculadora = new CalculadoraCarrito();
		this.items = persistencia.cargar();
	}
	
	public void setUsuarioActual(Cliente clienteLogueado) {
		if (clienteLogueado != null) {
			this.usuarioActual = clienteLogueado.getNombreCompleto();
			if (clienteLogueado.getTipo() == TipoCliente.PREMIUM) {
				this.esPremium = true;
			} else {
				this.esPremium = false;
			}
		} else {
			this.usuarioActual = "";
			this.esPremium = false;
		}
	}
	
	public void agregarLibro(Libro libro, double precio, int cantidad) {
		for (ItemCarrito item : items) {
			if (item.getLibro().getCodigo().equals(libro.getCodigo())) {
				item.setCantidad(item.getCantidad() + cantidad);
				persistencia.guardar(items);
				
				auditoria.registrarAccion(usuarioActual, "Actualizar Cantidad", "Aumento Cantidad ISBN" + libro.getIsbn() + "a" + item.getCantidad());
				return;
			}
		}
		items.add(new ItemCarrito(libro, cantidad, Double.parseDouble(libro.getPrecio())));
		persistencia.guardar(items);
		
		auditoria.registrarAccion(usuarioActual, "Agregar Libro", "Agregar al carrito el libro" + libro.getTitulo());
	}
	
	public List<ItemCarrito> obtenerItems() {
		return new ArrayList<>(items);
	}
	
	public boolean actualizarCantidad(String , int nuevaCantidad) {
		for (ItemCarrito item : items) {
			if (item.getLibro().getCodigo().equals(codigo)) {
				if (nuevaCantidad <= 0) {
					return eliminarLibro(codigo);
				}
				item.setCantidad(nuevaCantidad);
				persistencia.guardar(items);
				return true;
			}
		}
		return false;
	}
	
	public boolean eliminarLibro(String isbn) {
		boolean eliminado = items.removeIf(item -> item.getLibro().getCodigo().equals(codigo));
		if (eliminado) {
			persistencia.guardar(items);
			
			auditoria.registrarAccion(usuarioActual, "Elimanr Libro", "Elimino del carrito el ISBN: " + isbn);
		}
		return eliminado;
	}
	
	public double calcularSubtotal() {
		return calculadora.calcularSubtotal(items);
	}
	
	public double calcularDescuento() {
		return calculadora.calcularDescuento(calcularSubtotal(), esPremium);
	}
	
	public double calcularIva() {
		return calculadora.calcularIva(calcularSubtotal(), calcularDescuento());
	}
	
	public double calcularTotal() {
		return calculadora.calcularTotalconIva(calcularSubtotal(), calcularDescuento(), calcularIva());
	}

	public void vaciarCarrito() {
		items.clear();
		persistencia.guardar(items);
		auditoria.registrarAccion(usuarioActual, "Vaciar Carrito", "Se realizo el checkout y se vacio el carrito");
		
	}
	
	public String getUsuarioActual() {
		return this.usuarioActual;
	}

}
