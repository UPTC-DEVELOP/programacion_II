package co.edu.uptc.libreria.persistencia;

import co.edu.uptc.libreria.interfaces.ICarritoPersistencia;
import co.edu.uptc.libreria.interfaces.IGestionLibro;
import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.modelo.Libro;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.libreria.negocio.GestionLibro;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocalCarritoPersistencia implements ICarritoPersistencia{
	
	private final String rutaArchivo = "data/carrito.json";

	//private ControladorCatalogo controladorCatalogo;

	private IGestionLibro persistenciaLibro;
	
	private Gson gson;

	
	public LocalCarritoPersistencia(IGestionLibro persistenciaLibro) {
		this.persistenciaLibro = persistenciaLibro;
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		File carpeta = new File("data");
		
		if (!carpeta.exists()) {
			carpeta.mkdirs();
		}
	}

	@Override
	public void guardar(List<ItemCarrito> items) {
		// TODO Auto-generated method stub
		try (Writer writer = new FileWriter(rutaArchivo)) {
			gson.toJson(items, writer);
		} catch (IOException e) {
			System.err.println("Error al guardar el carrito: " + e.getMessage());
		}
	}

	@Override
	public List<ItemCarrito> cargar() {
		// TODO Auto-generated method stub
		List<ItemCarrito> listaCargada = new ArrayList<>();
		File archivo = new File(rutaArchivo);
		
		if (!archivo.exists()) {
			return listaCargada;
		}
		
		try (Reader reader = new FileReader(archivo)) {
			Type tipoLista = new TypeToken<ArrayList<ItemCarrito>>() {}.getType();
			listaCargada = gson.fromJson(reader, tipoLista);
			
			if (listaCargada == null) {
				listaCargada = new ArrayList<>();
			}
		} catch (IOException e) {
			System.err.println("Error al cargar json del carrito: " + e.getMessage());
		}
		
		return listaCargada;
	}
	
	
		
}
