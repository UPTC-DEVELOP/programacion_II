package co.edu.uptc.persistence.json;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.persistence.CarritoRepository;

// Cada fila JSON representa una línea; el correo permite reconstruir un carrito completo.
public class CarritoJsonRepository implements CarritoRepository {
    private final Path archivo;
    public CarritoJsonRepository(Path archivo) { this.archivo = archivo; ArchivoJson.leer(archivo); }
    @Override public Optional<Carrito> buscarPorCliente(String correo) {
        Carrito carrito = new Carrito(correo);
        for (Map<String, String> m : ArchivoJson.leer(archivo)) {
			if (correo.equalsIgnoreCase(m.get("correo"))) {
			    carrito.agregar(new ItemCarrito(m.get("isbn"), m.get("titulo"), decimal(m,"precio"), decimal(m,"descuento"), decimal(m,"iva"), entero(m,"cantidad")));
			}
		}
        return carrito.getItems().isEmpty() ? Optional.<Carrito>empty() : Optional.of(carrito);
    }
    @Override public void guardar(Carrito carrito) {
        List<Map<String, String>> filas = ArchivoJson.leer(archivo); quitar(filas, carrito.getCorreoCliente());
        for (ItemCarrito i : carrito.getItems()) {
            Map<String, String> m = new LinkedHashMap<>(); m.put("correo", carrito.getCorreoCliente()); m.put("isbn", i.getIsbn()); m.put("titulo", i.getTitulo());
            m.put("precio", String.valueOf(i.getPrecioBase())); m.put("descuento", String.valueOf(i.getPorcentajeDescuento())); m.put("iva", String.valueOf(i.getImpuestoIVA())); m.put("cantidad", String.valueOf(i.getCantidad())); filas.add(m);
        }
        ArchivoJson.escribir(archivo, filas);
    }
    @Override public void eliminar(String correo) { List<Map<String, String>> filas = ArchivoJson.leer(archivo); quitar(filas, correo); ArchivoJson.escribir(archivo, filas); }
    private void quitar(List<Map<String, String>> filas, String correo) { for (int i = filas.size() - 1; i >= 0; i--) {
		if (correo.equalsIgnoreCase(filas.get(i).get("correo"))) {
			filas.remove(i);
		}
	} }
    private static int entero(Map<String,String> m,String k){return Integer.parseInt(m.get(k));}
    private static double decimal(Map<String,String> m,String k){return Double.parseDouble(m.get(k));}
}
