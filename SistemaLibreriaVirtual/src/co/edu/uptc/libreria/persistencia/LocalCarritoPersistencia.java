package co.edu.uptc.libreria.persistencia;

import co.edu.uptc.libreria.modelo.ItemCarrito;
import co.edu.uptc.libreria.modelo.Libro;

import co.edu.uptc.libreria.negocio.GestionLibro;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LocalCarritoPersistencia implements CarritoPersistencia{
	
	private final String rutaArchivo = "data/carrito.txt";

	private ControladorCatalogo controladorCatalogo;

	private GestionLibro gestionLibro;

	
	public LocalCarritoPersistencia(GestionLibro gestionlibro) {
		this.gestionLibro = gestionLibro;
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
				bw.write(item.getLibro().getCodigo() + ";" + item.getCantidad());
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
					
					Libro libroEntrante = null;
					
					for(Libro l : gestionLibro.listarLibros()) {
						if(l.getCodigo().equals(codigo)) {
							libroEntrante = l;
							break;
						}
						
					}
					
					if(libroEntrante != null) {
						double precioUnitario = Double.parseDouble(libroEntrante.getPrecio());
						ItemCarrito item = new ItemCarrito(libroEntrante, cantidad, precioUnitario);
						listaCargada.add(item);
					}
			
				}
			}
		} catch (IOException e) {
			System.err.println("Error al cargar el carrito: " + e.getMessage());
		}
		return listaCargada;
	}
	
		
}
