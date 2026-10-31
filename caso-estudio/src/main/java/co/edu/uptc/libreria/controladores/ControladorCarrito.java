package co.edu.uptc.libreria.controladores;

import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.negocio.GestionCarrito;

import co.edu.uptc.libreria.modelo.Libro;

import co.edu.uptc.libreria.persistencia.ServicioAuditoria;
import co.edu.uptc.libreria.modelo.*;

//Controlador Carrito

import java.util.List;

public class ControladorCarrito {
	
	private GestionCarrito gestionCarrito;
	private ServicioAuditoria auditoria;

	public ControladorCarrito(GestionCarrito gestionCarrito, ServicioAuditoria auditoria) {
		this.gestionCarrito = gestionCarrito;
		this.auditoria = auditoria;
	}
	
	public void agregarAlCarrito(Libro libro, double precio, int cantidad) {
		gestionCarrito.agregarLibro(libro, precio, cantidad);
	}
	
	public List<ItemCarrito> listarCarrito() {
		return gestionCarrito.obtenerItems();
	}
	
	public void modificarCantidad(String codigo, int nuevaCantidad) {
		gestionCarrito.actualizarCantidad(codigo, nuevaCantidad);
	}
	
	public void finalizarCompra(String usuarioActual) {
		auditoria.registrarCompra(usuarioActual, listarCarrito(), obtenerTotal());
		gestionCarrito.vaciarCarrito();
	}
	
	public void eliminarElemento(String codigo) {
		gestionCarrito.eliminarLibro(codigo);
	}
	
	public double obtenerSubtotal() {
		return gestionCarrito.calcularSubtotal();
	}
	
	public double obtenerDescuento() {
		return gestionCarrito.calcularDescuento();
	}
	
	public double obtenerIva() {
		return gestionCarrito.calcularIva();
	}
	
	public double obtenerTotal() {
		return gestionCarrito.calcularTotal();
	}
	
	public void setClienteActual(Cliente cliente) {
		gestionCarrito.setUsuarioActual(cliente);
	}
	
	public void vaciarCarrito() {
		gestionCarrito.vaciarCarrito();
	}
	
	public String getUsuarioActual() {
		return gestionCarrito.getUsuarioActual();
	}

}
