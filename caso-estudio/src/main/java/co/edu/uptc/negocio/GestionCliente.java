package co.edu.uptc.negocio;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.mapper.ClienteMapper;

/**
 * Capa de NEGOCIO de los clientes (CRUD).
 * Valida los datos con ValidadorDatos y delega el guardado en la interfaz
 * IClienteRepositorio, así no depende de la implementación concreta (DIP).
 */
public class GestionCliente implements IGestionCliente {

	private final IClienteRepositorio persistencia;
	private final ValidadorDatos validador;

	// constructor
	public GestionCliente(IClienteRepositorio persistencia, ValidadorDatos validador) {
		this.persistencia = persistencia;
		this.validador = validador;
	}

	@Override
	public boolean registrarCliente(ClienteDto clienteDto) throws ReglaNegocioException {
		validador.validarCliente(clienteDto);
		validador.validarContrasenaNueva(clienteDto.getContrasenia());

		if (persistencia.buscar(clienteDto.getIdentificacion().trim()) != null) {
			throw new ReglaNegocioException("Ya existe un cliente con esa identificación.");
		}
		if (persistencia.buscarPorCorreo(clienteDto.getCorreoElectronico().trim()) != null) {
			throw new ReglaNegocioException("Ya existe un cliente con ese correo electrónico.");
		}

		clienteDto.setIdCliente(0); // el repositorio asigna el ID
		Cliente entidad = ClienteMapper.toEntity(clienteDto);
		entidad.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
		persistencia.guardar(entidad);
		clienteDto.setIdCliente(entidad.getIdCliente());
		return true;
	}

	@Override
	public boolean actualizarCliente(ClienteDto clienteDto) throws ReglaNegocioException {
		validador.validarCliente(clienteDto);

		Cliente existente = persistencia.buscarPorId(clienteDto.getIdCliente());
		if (existente == null) {
			existente = persistencia.buscar(clienteDto.getIdentificacion());
		}
		if (existente == null) {
			throw new ReglaNegocioException("El cliente no existe.");
		}

		ClienteDto repetido = buscarPorIdentificacion(clienteDto.getIdentificacion());
		if (repetido != null && repetido.getIdCliente() != existente.getIdCliente()) {
			throw new ReglaNegocioException("Ya existe un cliente con esa identificación.");
		}
		Cliente porCorreo = persistencia.buscarPorCorreo(clienteDto.getCorreoElectronico());
		if (porCorreo != null && porCorreo.getIdCliente() != existente.getIdCliente()) {
			throw new ReglaNegocioException("Ya existe un cliente con ese correo electrónico.");
		}

		clienteDto.setIdCliente(existente.getIdCliente());
		// si el formulario no trae contraseña se conserva la actual
		if (clienteDto.getContrasenia() == null || clienteDto.getContrasenia().isEmpty()) {
			clienteDto.setContrasenia(existente.getContrasenia());
		} else {
			validador.validarContrasenaNueva(clienteDto.getContrasenia());
		}

		Cliente entidad = ClienteMapper.toEntity(clienteDto);
		entidad.setFechaRegistro(existente.getFechaRegistro());
		entidad.setIntentosFallidos(existente.getIntentosFallidos());
		persistencia.actualizar(entidad);
		return true;
	}

	@Override
	public boolean eliminarCliente(int idCliente) throws ReglaNegocioException {
		Cliente existente = persistencia.buscarPorId(idCliente);
		if (existente == null) {
			throw new ReglaNegocioException("El cliente no existe.");
		}
		if (persistencia.listar().size() <= 1) {
			throw new ReglaNegocioException("No se puede eliminar el único usuario del sistema.");
		}
		persistencia.eliminarPorId(idCliente);
		return true;
	}

	@Override
	public ClienteDto buscarPorId(int idCliente) {
		return ClienteMapper.toDto(persistencia.buscarPorId(idCliente));
	}

	@Override
	public ClienteDto buscarPorIdentificacion(String identificacion) {
		if (identificacion == null) return null;
		return ClienteMapper.toDto(persistencia.buscar(identificacion.trim()));
	}

	@Override
	public List<ClienteDto> listarClientes() {
		return persistencia.listar().stream()
				.map(ClienteMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public List<ClienteDto> buscarClientes(String texto) {
		List<ClienteDto> todos = listarClientes();
		if (texto == null || texto.trim().isEmpty()) {
			return todos;
		}
		String criterio = texto.trim().toLowerCase();
		return todos.stream()
				.filter(c -> (c.getNombreCompleto() != null && c.getNombreCompleto().toLowerCase().contains(criterio))
						|| (c.getCorreoElectronico() != null && c.getCorreoElectronico().toLowerCase().contains(criterio))
						|| (c.getIdentificacion() != null && c.getIdentificacion().toLowerCase().contains(criterio)))
				.collect(Collectors.toList());
	}

	@Override
	public boolean iniciarSesion(String correo, String contrasena) {
		if (correo == null || contrasena == null) {
			return false;
		}
		Cliente cliente = persistencia.buscarPorCorreo(correo.trim());
		if (cliente == null) {
			return false;
		}
		if (cliente.validarContrasena(contrasena)) {
			cliente.setIntentosFallidos(0);
			return true;
		}
		cliente.setIntentosFallidos(cliente.getIntentosFallidos() + 1);
		return false;
	}
}
