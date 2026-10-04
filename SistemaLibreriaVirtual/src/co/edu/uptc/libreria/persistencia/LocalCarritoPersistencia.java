package co.edu.uptc.libreria.persistencia;

import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.modelo.Libro;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalCarritoPersistencia implements CarritoPersistencia{
	
	private final String rutaArchivo = "data/carrito.txt";
	private ControladorCatalogo controladorCatalogo;
	
	public LocalCarritoPersistencia(ControladorCatalogo controladorCatalogo) {
		this.controladorCatalogo = controladorCatalogo;
		File carpeta = new File("data");
		
		if (!carpeta.exists()) {
			carpeta.mkdirs();
		}
	}

	@Override
	public void guardar(List<ItemCarrito> items) {
		// TODO Auto-generated method stub
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {
			for (ItemCarrito item : items) {
				bw.write(item.getLibro().getIsbn() + ";" + item.getCantidad());
				bw.newLine();
			}
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
		
		try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			String linea;
			while ((linea = br.readLine()) != null) {
				String[] datos = linea.split(";");
				if (datos.length >= 2) {
					String isbn = datos[0];
					int cantidad = Integer.parseInt(datos[1]);
					
					Libro libroEntrante = controladorCatalogo.buscarLibroPorIsbn(isbn);
					
					ItemCarrito item = new ItemCarrito(libroEntrante, cantidad, libroEntrante.getPrecio());
					listaCargada.add(item);
				}
			}
		} catch (IOException e) {
			System.err.println("Error al cargar el carrito: " + e.getMessage());
		}
		return listaCargada;
	}
	
		
}
