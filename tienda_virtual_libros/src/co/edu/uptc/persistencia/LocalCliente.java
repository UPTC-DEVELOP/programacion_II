package co.edu.uptc.persistencia;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 * CLASE LocalCliente  (paquete: persistencia)  implements IClienteRepositorio
 * ---------------------------------------------------------------------------
 * Almacenamiento TEMPORAL de clientes en una lista en RAM. Solo guarda y
 * recupera: no valida nada (eso es responsabilidad de la capa de negocio).
 * Al cerrar la aplicación los datos se pierden.
 */
public class LocalCliente implements IClienteRepositorio {

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
