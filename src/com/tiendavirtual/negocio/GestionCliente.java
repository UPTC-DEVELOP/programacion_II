package com.tiendavirtual.negocio;

import java.util.ArrayList;

import com.tiendavirtual.modelo.Cliente;

public class GestionCliente {
	
	private ArrayList<Cliente> clientes;
	
	public GestionCliente() {
		// TODO Auto-generated constructor stub
		clientes=new ArrayList<>();
	}

		public boolean registrarCliente(Cliente cliente) {
			
			if (!camposCompletos(cliente)) {
				return false;
			}
			if (existeCorreo(cliente.getCorreo())) {
				return false;
			}
			
			if (existeDocumento(cliente.getNumeroDocumento())) {
				return false;
			}
			
			if (!contraseniaValida(cliente.getContrasenia())) {
				return false;
			}
			clientes.add(cliente);
			return true;
		}
		
		public Cliente iniciarSesion(String correo, String contrasenia) {
			for (Cliente cliente:clientes) {
				if (cliente.getCorreo().equals(correo)
					&& cliente.getContrasenia().equals(contrasenia)) {
						return cliente;
					}
			}
			return null;
		}
		
		private Cliente buscarPorDocumento(int numeroDocumento) {
			for (Cliente cliente:clientes) {
				if (cliente.getNumeroDocumento()==numeroDocumento) {
					return cliente;
				}
			}
			return null;
		}
		
		public boolean actualizarCliente(Cliente clienteActualizado) {

		    Cliente cliente = null;

		    for (Cliente c : clientes) {
		        if (c.getCorreo().equals(clienteActualizado.getCorreo())) {
		            cliente = c;
		            break;
		        }
		    }

		    if (cliente == null) {
		        return false;
		    }

		    if (!camposCompletos(clienteActualizado)) {
		        return false;
		    }

		    for (Cliente otroCliente : clientes) {

		        if (otroCliente != cliente
		                && otroCliente.getCorreo().equals(clienteActualizado.getCorreo())) {

		            return false;
		            
		        }
		    }

		    cliente.setNombre(clienteActualizado.getNombre());
		    cliente.setApellido(clienteActualizado.getApellido());
		    cliente.setCorreo(clienteActualizado.getCorreo());
		    cliente.setTelefono(clienteActualizado.getTelefono());

		    if (clienteActualizado.getContrasenia() != null
		            && !clienteActualizado.getContrasenia().isEmpty()) {

		        if (!contraseniaValida(clienteActualizado.getContrasenia())) {
		            return false;
		        }

		        cliente.setContrasenia(clienteActualizado.getContrasenia());
		    }

		    return true;
		}
		
		
		private boolean existeCorreo(String correo) {
			for (Cliente cliente:clientes) {
				if (cliente.getCorreo().equals(correo)) {
					return true;
				}
			}
			return false;
		}
		private boolean existeDocumento(int numeroDocumento) {
			for (Cliente cliente:clientes) {
				if (cliente.getNumeroDocumento() == numeroDocumento) {
					return true;
				}
			}
			return false;
		}
		
		private boolean contraseniaValida(String contrasenia) {
			return contrasenia !=null && contrasenia.length() >=6;
		}
		
		private boolean camposCompletos(Cliente cliente) {
			
		return cliente.getNombre() !=null && !cliente.getNombre().isEmpty()
				&& cliente.getApellido() !=null && !cliente.getApellido().isEmpty()
						&& cliente.getCorreo() !=null && !cliente.getCorreo().isEmpty()
		 && cliente.getTelefono() != null && !cliente.getTelefono().isEmpty();
		 
		}
}
