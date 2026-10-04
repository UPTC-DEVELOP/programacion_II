package co.edu.uptc.libreria.persistencia;

import co.edu.uptc.libreria.modelo.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServicioAuditoria {
	
	private String rutaArchivo = "auditoria.txt";
	
	public void registrarOperacion(String usuario, String accion, String detalles) {
		File archivo = new File(rutaArchivo);
		
		LocalDateTime ahora = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaHora = ahora.format(formato);
		
		String lineaLog = String.format("[%s] | %s | %s | %s", fechaHora, usuario, accion, detalles);
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo, true))) {
			bw.write(lineaLog);
			bw.newLine();
		} catch (IOException e) {
			System.err.println("Error al guardar: " + e.getMessage());
		}
	}
	
	public void registrarCompra(String usuario, List<ItemCarrito> itemsComprados, double totalPagado) {
		StringBuilder resumenLibros = new StringBuilder("Libros: ");
		for(ItemCarrito item : itemsComprados) {
			resumenLibros.append(item.getLibro().getCodigo()).append("(cant: ").append(item.getCantidad()).append("). ");
		}
		
		String detallesCompra = resumenLibros.toString() + "Total Pagado: $" + totalPagado;
		
		registrarOperacion(usuario, "Compra Finalizada", detallesCompra);
	}
}
