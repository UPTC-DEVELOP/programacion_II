package co.edu.uptc.negocio;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.modelo.mapper.ClienteMapper;
import co.edu.uptc.modelo.enums.AccionAuditoria;
import co.edu.uptc.persistencia.conexion.ConexionDB;

/**
 * Capa de NEGOCIO de los clientes (CRUD).
 * Valida los datos con ValidadorDatos y delega el guardado en la interfaz
 * IClienteRepositorio, así no depende de la implementación concreta (DIP).
 */
public class GestionCliente implements IGestionCliente {

	/** Compras necesarias para que un cliente REGULAR ascienda a PREMIUM. */
	public static final int COMPRAS_PARA_PREMIUM = 2;

	private final IClienteRepositorio persistencia;
	private final IClienteRepositorio persistenciaJson;
	private final ValidadorDatos validador;
	private final IAuditoria auditoria;

	// constructor
	public GestionCliente(IClienteRepositorio persistencia, ValidadorDatos validador) {
		this(persistencia, validador, null);
	}

	/*modificacion:Brayan Panqueva*/
	public GestionCliente(IClienteRepositorio persistencia, ValidadorDatos validador,
			IClienteRepositorio persistenciaJson) {
		this(persistencia, validador, persistenciaJson, null);
	}

	/*modificacion:Brayan Panqueva*/
	public GestionCliente(IClienteRepositorio persistencia, ValidadorDatos validador,
			IClienteRepositorio persistenciaJson, IAuditoria auditoria) {
		this.persistencia = persistencia;
		this.validador = validador;
		this.persistenciaJson = persistenciaJson;
		this.auditoria = auditoria;
	}

	@Override
	public boolean registrarCliente(ClienteDto clienteDto) throws ReglaNegocioException {
		if (clienteDto != null) {
			// Todo cliente nuevo inicia como REGULAR; asciende a PREMIUM por compras
			clienteDto.setTipoCliente(TipoCliente.REGULAR);
		}
		/*modificacion:Brayan Panqueva*/
		if (clienteDto != null && clienteDto.getCorreoElectronico() != null) {
			clienteDto.setCorreoElectronico(clienteDto.getCorreoElectronico().trim().toLowerCase());
			ConexionDB.establecerUsuarioSesion(clienteDto.getCorreoElectronico());
		}
		validador.validarCliente(clienteDto);
		validador.validarContrasenaNueva(clienteDto.getContrasenia());

		if (persistencia.buscar(clienteDto.getIdentificacion().trim()) != null) {
			throw new ReglaNegocioException("Ya existe un cliente con esa identificación.");
		}
		if (persistencia.buscarPorCorreo(clienteDto.getCorreoElectronico().trim()) != null) {
			throw new ReglaNegocioException("Ya existe un cliente con ese correo electrónico.");
		}

		clienteDto.setIdCliente(0); // el repositorio asigna el ID
		// la contraseña se guarda cifrada, nunca en texto plano
		clienteDto.setContrasenia(CifradorContrasena.hash(clienteDto.getContrasenia().trim()));
		Cliente entidad = ClienteMapper.toEntity(clienteDto);
		entidad.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
		persistencia.guardar(entidad);
		if (persistenciaJson != null) persistenciaJson.guardar(entidad);
		auditar(AccionAuditoria.CREATE, entidad.getIdentificacion(), "Registro de cliente: " + entidad.getCorreoElectronico());
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
		// si el formulario no trae contraseña se conserva la actual (ya cifrada);
		// si trae una nueva, se valida y se cifra
		if (clienteDto.getContrasenia() == null || clienteDto.getContrasenia().isEmpty()) {
			clienteDto.setContrasenia(existente.getContrasenia());
		} else {
			validador.validarContrasenaNueva(clienteDto.getContrasenia());
			clienteDto.setContrasenia(CifradorContrasena.hash(clienteDto.getContrasenia().trim()));
		}

		Cliente entidad = ClienteMapper.toEntity(clienteDto);
		entidad.setFechaRegistro(existente.getFechaRegistro());
		entidad.setIntentosFallidos(existente.getIntentosFallidos());
		// las compras solo cambian con registrarCompra, nunca desde el formulario
		entidad.setComprasRealizadas(existente.getComprasRealizadas());
		persistencia.actualizar(entidad);
		if (persistenciaJson != null) persistenciaJson.actualizar(entidad);
		auditar(AccionAuditoria.UPDATE, entidad.getIdentificacion(), "Actualización de cliente: " + entidad.getCorreoElectronico());
		return true;
	}

	/**
	 * Registra una compra confirmada. Si el cliente es REGULAR y alcanza
	 * COMPRAS_PARA_PREMIUM compras, se convierte en PREMIUM.
	 *
	 * @param idCliente Identificador del cliente
	 * @return true si el cliente ascendió a PREMIUM con esta compra
	 * @throws ReglaNegocioException si el cliente no existe
	 */
	@Override
	public boolean registrarCompra(int idCliente) throws ReglaNegocioException {
		Cliente cliente = persistencia.buscarPorId(idCliente);
		if (cliente == null) {
			throw new ReglaNegocioException("El cliente no existe.");
		}
		int compras = cliente.getComprasRealizadas() + 1;
		boolean asciende = cliente.getTipoCliente() == TipoCliente.REGULAR
				&& compras >= COMPRAS_PARA_PREMIUM;

		Cliente actualizado = cliente;
		if (asciende) {
			// ClientePremium y ClienteRegular son clases distintas: se crea la
			// entidad PREMIUM conservando todos los datos del cliente
			ClienteDto dto = ClienteMapper.toDto(cliente);
			dto.setTipoCliente(TipoCliente.PREMIUM);
			actualizado = ClienteMapper.toEntity(dto);
			actualizado.setFechaRegistro(cliente.getFechaRegistro());
			actualizado.setIntentosFallidos(cliente.getIntentosFallidos());
		}
		actualizado.setComprasRealizadas(compras);
			persistencia.actualizar(actualizado);
			if (persistenciaJson != null) persistenciaJson.actualizar(actualizado);
		return asciende;
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
		if (persistenciaJson != null) persistenciaJson.eliminarPorId(idCliente);
		auditar(AccionAuditoria.DELETE, existente.getIdentificacion(), "Eliminación de cliente: " + existente.getCorreoElectronico());
		return true;
	}

	private void auditar(AccionAuditoria accion, String identificacion, String detalles) {
		if (auditoria != null) auditoria.registrar(accion, identificacion, "admin", detalles);
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
		if (cliente.validarContrasena(CifradorContrasena.hash(contrasena))) {
			cliente.setIntentosFallidos(0);
			return true;
		}
		cliente.setIntentosFallidos(cliente.getIntentosFallidos() + 1);
		return false;
	}
}
