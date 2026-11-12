package co.edu.uptc.tienda.persistencia;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;
import co.edu.uptc.tienda.negocio.IRepositorioCliente;
import co.edu.uptc.tienda.negocio.NegocioException;

public class ClienteJSON implements IRepositorioCliente {

	private static final Path RUTA_ARCHIVO = Paths.get("data", "clientes.json");

	private static final Type TIPO_LISTA = new TypeToken<List<RegistroCliente>>() {
	}.getType();

	private final Gson gson;

	public ClienteJSON() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
	}

	@Override
	public void guardar(Cliente cliente) {
		List<Cliente> clientes = leerArchivo();

		cliente.setIdCliente(calcularSiguienteId(clientes));
		clientes.add(cliente);

		escribirArchivo(clientes);
	}

	@Override
	public List<Cliente> listar() {
		return leerArchivo();
	}

	@Override
	public Cliente buscarPorIdentificacion(String identificacion) {
		for (Cliente cliente : leerArchivo()) {
			if (cliente.getIdentificacion().equals(identificacion)) {
				return cliente;
			}
		}
		return null;
	}

	@Override
	public boolean actualizar(Cliente clienteActualizado) {
		List<Cliente> clientes = leerArchivo();

		for (int i = 0; i < clientes.size(); i++) {
			if (clientes.get(i).getIdentificacion().equals(clienteActualizado.getIdentificacion())) {
				clientes.set(i, clienteActualizado);
				escribirArchivo(clientes);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean eliminar(String identificacion) {
		List<Cliente> clientes = leerArchivo();

		boolean eliminado = clientes.removeIf(cliente -> cliente.getIdentificacion().equals(identificacion));
		if (eliminado) {
			escribirArchivo(clientes);
		}
		return eliminado;
	}

	private int calcularSiguienteId(List<Cliente> clientes) {
		int mayorId = 0;
		for (Cliente cliente : clientes) {
			mayorId = Math.max(mayorId, cliente.getIdCliente());
		}
		return mayorId + 1;
	}

	private List<Cliente> leerArchivo() {
		List<Cliente> clientes = new ArrayList<>();

		if (!Files.exists(RUTA_ARCHIVO)) {
			return clientes;
		}

		try {
			String contenido = Files.readString(RUTA_ARCHIVO, StandardCharsets.UTF_8);
			List<RegistroCliente> registros = gson.fromJson(contenido, TIPO_LISTA);

			if (registros != null) {
				for (RegistroCliente registro : registros) {
					clientes.add(registro.aCliente());
				}
			}
			return clientes;

		} catch (IOException | JsonParseException | IllegalArgumentException | NullPointerException e) {
			// No se devuelve una lista vacía: al guardar se sobrescribirían los datos
			System.err.println("Error al leer el archivo de clientes -> " + e.getMessage());
			throw new NegocioException("No se pudo leer el archivo de clientes: " + RUTA_ARCHIVO.toAbsolutePath());
		}
	}

	private void escribirArchivo(List<Cliente> clientes) {
		List<RegistroCliente> registros = new ArrayList<>();
		for (Cliente cliente : clientes) {
			registros.add(new RegistroCliente(cliente));
		}

		try {
			Path carpeta = RUTA_ARCHIVO.getParent();
			if (carpeta != null) {
				Files.createDirectories(carpeta);
			}
			Files.writeString(RUTA_ARCHIVO, gson.toJson(registros, TIPO_LISTA), StandardCharsets.UTF_8);

		} catch (IOException e) {
			System.err.println("Error al escribir el archivo de clientes -> " + e.getMessage());
			throw new NegocioException("No se pudo guardar el archivo de clientes: " + RUTA_ARCHIVO.toAbsolutePath());
		}
	}

	/**
	 * Forma en que un cliente se escribe en el JSON. Cliente es abstracta, por
	 * eso se guarda su tipo y al leer se reconstruye con {@link TipoCliente}.
	 */
	private static class RegistroCliente {

		private int idCliente;
		private String tipoCliente;
		private String primerNombre;
		private String otrosNombres;
		private String primerApellido;
		private String otrosApellidos;
		private String tipoIdentificacion;
		private String identificacion;
		private String correoElectronico;
		private String celular;
		private String direccion;
		private String contrasenia;
		private String fechaRegistro;
		private int intentosFallidos;

		RegistroCliente(Cliente cliente) {
			this.idCliente = cliente.getIdCliente();
			this.tipoCliente = cliente.getTipoCliente().name();
			this.primerNombre = cliente.getPrimerNombre();
			this.otrosNombres = cliente.getOtrosNombres();
			this.primerApellido = cliente.getPrimerApellido();
			this.otrosApellidos = cliente.getOtrosApellidos();
			this.tipoIdentificacion = cliente.getTipoIdentificacion();
			this.identificacion = cliente.getIdentificacion();
			this.correoElectronico = cliente.getCorreoElectronico();
			this.celular = cliente.getCelular();
			this.direccion = cliente.getDireccion();
			this.contrasenia = cliente.getContrasenia();
			this.fechaRegistro = cliente.getFechaRegistro() != null ? cliente.getFechaRegistro().toString() : null;
			this.intentosFallidos = cliente.getIntentosFallidos();
		}

		Cliente aCliente() {
			Cliente cliente = TipoCliente.valueOf(tipoCliente).crear(primerNombre, otrosNombres, primerApellido,
					otrosApellidos, tipoIdentificacion, identificacion, correoElectronico, celular, direccion,
					contrasenia);

			cliente.setIdCliente(idCliente);
			cliente.setFechaRegistro(fechaRegistro != null ? Timestamp.valueOf(fechaRegistro) : null);
			cliente.setIntentosFallidos(intentosFallidos);
			return cliente;
		}
	}
}
