package co.edu.uptc.libreria.negocio;

import co.edu.uptc.libreria.modelo.*;
import java.util.List;

public class CalculadoraCarrito {
	
	private final double porcentajeIva = 0.19;
	private final double porcentajeDescuento = 0.10;
	
	public double calcularSubtotal(List<ItemCarrito> items) {
		double subtotal = 0;
		for (ItemCarrito item : items) {
			subtotal += item.getSubtotal();
		}
		return subtotal;
	}
	
	public double calcularDescuento(double subtotal, boolean esPremium) {
		if (esPremium) {
			return subtotal * porcentajeDescuento;
		}
		return 0.0;
	}
	
	public double calcularIva(double subtotal, double descuento) {
		double baseGravable = subtotal - descuento;
		return baseGravable * porcentajeIva;
	}
	
	public double calcularTotalconIva(double subtotal, double descuento, double iva) {
		return subtotal - descuento + iva;
	}

}
