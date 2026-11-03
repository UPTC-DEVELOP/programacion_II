package co.edu.uptc.persistencia;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * IMPLEMENTACIÓN EN MEMORIA del repositorio de clientes.
 * Implementa IClienteRepositorio: la capa de negocio solo ve la interfaz.
 * Carga datos de ejemplo (semillas) para poder probar la aplicación sin base
 * de datos.
 */
public class LocalCliente implements IClienteRepositorio {

	private final List<Cliente> clientes = new ArrayList<>();
	private int nextId = 1;

	public LocalCliente() {
		cargarDatosDeEjemplo();
	}

	// ------------------------- IClienteRepositorio -------------------------

	@Override
	public void guardar(Cliente cliente) {
		if (cliente == null) {
			return;
		}
		if (cliente.getIdCliente() == 0) {
			cliente.setIdCliente(nextId++);
		} else {
			asesurarId(cliente.getIdCliente());
		}
		clientes.add(cliente);
	}

	@Override
	public void actualizar(Cliente clienteActualizado) {
		if (clienteActualizado == null) {
			return;
		}
		for (int i = 0; i < clientes.size(); i++) {
			Cliente actual = clientes.get(i);
			if (actual.getIdCliente() == clienteActualizado.getIdCliente()
					|| actual.getIdentificacion().equals(clienteActualizado.getIdentificacion())) {
				clientes.set(i, clienteActualizado);
				return;
			}
		}
	}

	@Override
	public void eliminar(String identificacion) {
		clientes.removeIf(c -> c.getIdentificacion().equals(identificacion));
	}

	@Override
	public void eliminarPorId(int idCliente) {
		clientes.removeIf(c -> c.getIdCliente() == idCliente);
	}

	@Override
	public List<Cliente> listar() {
		return new ArrayList<>(clientes);
	}

	@Override
	public Cliente buscar(String identificacion) {
		if (identificacion == null) return null;
		return clientes.stream()
				.filter(c -> identificacion.equals(c.getIdentificacion()))
				.findFirst().orElse(null);
	}

	@Override
	public Cliente buscarPorCorreo(String correo) {
		if (correo == null) return null;
		return clientes.stream()
				.filter(c -> correo.equalsIgnoreCase(c.getCorreoElectronico()))
				.findFirst().orElse(null);
	}

	@Override
	public Cliente buscarPorId(int idCliente) {
		return clientes.stream()
				.filter(c -> c.getIdCliente() == idCliente)
				.findFirst().orElse(null);
	}

	// ------------------------- Métodos de apoyo ---------------------------

	public Cliente buscarPorIdentificacion(String identificacion) {
		return buscar(identificacion);
	}

	public List<Cliente> listarTodos() {
		return listar();
	}

	private void asesurarId(int id) {
		while (nextId <= id) {
			nextId++;
		}
	}

	// ------------------------- Datos de ejemplo ---------------------------

	private void cargarDatosDeEjemplo() {
		Timestamp ahora = new Timestamp(System.currentTimeMillis());

		Cliente admin = new ClientePremium(
				"Ana", "Maria", "Gomez", "Restrepo", "CC", "1000000001",
				"admin@libros.com", "3001112233", "Calle 1 # 2-3",
				0, TipoCliente.PREMIUM, "admin123", ahora, 0, Rol.ADMIN);
		admin.setFechaRegistro(ahora);

		Cliente clienteUno = new ClienteRegular(
				"Luis", "Andres", "Martinez", "Diaz", "CC", "1000000002",
				"cliente@libros.com", "3004445566", "Carrera 5 # 6-7",
				0, TipoCliente.REGULAR, "cliente123", ahora, 0, Rol.CLIENTE);
		clienteUno.setFechaRegistro(ahora);

		Cliente clienteDos = new ClientePremium(
				"Maria", "Elena", "Sanchez", "Ortiz", "CC", "1000000003",
				"maria@libros.com", "3007778899", "Avenida 8 # 9-10",
				0, TipoCliente.PREMIUM, "maria123", ahora, 0, Rol.CLIENTE);
		clienteDos.setFechaRegistro(ahora);

		guardar(admin);
		guardar(clienteUno);
		guardar(clienteDos);
	}
}
