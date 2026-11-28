package co.edu.uptc.libreria.persistencia;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.libreria.interfaces.IGestionCliente;
import co.edu.uptc.libreria.modelo.Cliente;

public class ClienteJson implements IGestionCliente {

	private File archivo;
	private Gson gson;
	private Type tipoLista;

	public ClienteJson(String ruta) {
		super();
		archivo = new File(ruta);
		gson = new GsonBuilder().setPrettyPrinting().create();
		tipoLista = new TypeToken<List<Cliente>>() {}.getType();
	}

	@Override
	public void guardar(Cliente cliente) {
		Map<String, Cliente> clientes = leer();
		clientes.put(cliente.getCorreo(), cliente);
		escribir(clientes);
	}

	@Override
	public void actualizar(Cliente cliente) {
		Map<String, Cliente> clientes = leer();
		clientes.put(cliente.getCorreo(), cliente);
		escribir(clientes);
	}

	@Override
	public void eliminar(String correo) {
		Map<String, Cliente> clientes = leer();
		clientes.remove(correo);
		escribir(clientes);
	}

	@Override
	public Cliente buscar(String correo) {
		return leer().get(correo);
	}

	@Override
	public List<Cliente> listar() {
		return new ArrayList<Cliente>(leer().values());
	}

	private Map<String, Cliente> leer() {
		Map<String, Cliente> clientes = new LinkedHashMap<String, Cliente>();
		if (!archivo.exists()) {
			return clientes;
		}
		try (Reader reader = new FileReader(archivo)) {
			List<Cliente> lista = gson.fromJson(reader, tipoLista);
			if (lista != null) {
				for (Cliente c : lista) {
					clientes.put(c.getCorreo(), c);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new IllegalStateException("No se pudo leer el archivo de clientes");
		}
		return clientes;
	}

	private void escribir(Map<String, Cliente> clientes) {
		try (Writer writer = new FileWriter(archivo)) {
			gson.toJson(new ArrayList<Cliente>(clientes.values()), writer);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new IllegalStateException("No se pudo escribir el archivo de clientes");
		}
	}
}