package co.edu.uptc.negocio.cliente.memoria;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.gui.interfaz.cliente.IGestionCliente;
import co.edu.uptc.negocio.modelo.Cliente;

/**
 * CLASE ClienteRepositorioMemoria  (paquete: negocio.cliente.memoria)  implements IGestionCliente
 * ---------------------------------------------------------------------------
 * Almacenamiento TEMPORAL de clientes en una lista en RAM. Solo guarda y
 * recupera: no valida nada (eso es responsabilidad de la capa de negocio).
 * Al cerrar la aplicación los datos se pierden.
 */
public class ClienteRepositorioMemoria implements IGestionCliente {

	private final List<Cliente> clientes = new ArrayList<>();
	private int siguienteId = 1;

	@Override
	public void guardar(Cliente cliente) {
		cliente.setIdCliente(siguienteId++);     // id autoincremental, como lo haría una BD
		clientes.add(cliente);
	}

	@Override
	public void actualizar(Cliente cliente) {
		for (int i = 0; i < clientes.size(); i++) {
			if (clientes.get(i).getIdentificacion().equals(cliente.getIdentificacion())) {
				clientes.set(i, cliente);
				return;
			}
		}
	}

	@Override
	public void eliminar(String identificacion) {
		clientes.removeIf(c -> c.getIdentificacion().equals(identificacion));
	}

	@Override
	public List<Cliente> listar() {
		return new ArrayList<>(clientes);        // copia: nadie altera la lista interna
	}

	@Override
	public Cliente buscar(String identificacion) {
		for (Cliente c : clientes) {
			if (c.getIdentificacion().equals(identificacion)) {
				return c;
			}
		}
		return null;
	}

	//El correo no distingue mayusculas/minusculas
	@Override
	public Cliente buscarPorCorreo(String correo) {
		for (Cliente c : clientes) {
			if (c.getCorreoElectronico().equalsIgnoreCase(correo)) {
				return c;
			}
		}
		return null;
	}
}
