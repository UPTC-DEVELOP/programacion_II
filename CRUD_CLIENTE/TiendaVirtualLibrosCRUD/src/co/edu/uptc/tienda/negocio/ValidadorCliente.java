package co.edu.uptc.tienda.negocio;

import co.edu.uptc.tienda.modelo.Cliente;


public class ValidadorCliente {

	public void validar(Cliente cliente) {
		if (estaVacio(cliente.getPrimerNombre()) || estaVacio(cliente.getPrimerApellido())
				|| estaVacio(cliente.getTipoIdentificacion()) || estaVacio(cliente.getIdentificacion())
				|| estaVacio(cliente.getCorreoElectronico())) {
			throw new NegocioException("Por favor complete todos los campos obligatorios (*).");
		}
	}

	private boolean estaVacio(String valor) {
		return valor == null || valor.trim().isEmpty();
	}
}
