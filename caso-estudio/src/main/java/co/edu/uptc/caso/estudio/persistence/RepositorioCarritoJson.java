package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Persistencia JSON para el carrito de compras asociado a cada cliente
public class RepositorioCarritoJson implements IRepositorioCarrito {

    private final ManejadorArchivoJson<Carrito> manejadorJson;

    public RepositorioCarritoJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.carritos", "data/carritos.json");
        Type tipoLista = new TypeToken<ArrayList<Carrito>>() {
        }.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    // Busca y retorna el carrito del cliente; si no existe, devuelve uno nuevo vacío
    @Override
    public Carrito obtenerCarritoPorCliente(String clienteId) {
        if (clienteId == null) {
            return new Carrito();
        }
        List<Carrito> lista = manejadorJson.leerLista();
        for (Carrito carrito : lista) {
            if (carrito != null && clienteId.equals(carrito.getClienteId())) {
                return carrito;
            }
        }
        return new Carrito(clienteId);
    }

    // Reemplaza el carrito previo del cliente (si existía) y persiste la lista actualizada
    @Override
    public boolean guardarCarrito(Carrito carrito) {
        if (carrito == null || carrito.getClienteId() == null) {
            return false;
        }
        List<Carrito> lista = manejadorJson.leerLista();
        Iterator<Carrito> it = lista.iterator();
        while (it.hasNext()) {
            Carrito actual = it.next();
            if (actual != null && carrito.getClienteId().equals(actual.getClienteId())) {
                it.remove();
            }
        }
        lista.add(carrito);
        manejadorJson.guardarLista(lista);
        return true;
    }

    // Elimina el carrito del cliente en el archivo JSON si estaba registrado
    @Override
    public boolean vaciarCarrito(String clienteId) {
        if (clienteId == null) {
            return false;
        }
        List<Carrito> lista = manejadorJson.leerLista();
        boolean removido = false;
        Iterator<Carrito> it = lista.iterator();
        while (it.hasNext()) {
            Carrito actual = it.next();
            if (actual != null && clienteId.equals(actual.getClienteId())) {
                it.remove();
                removido = true;
            }
        }
        if (removido) {
            manejadorJson.guardarLista(lista);
        }
        return removido;
    }
}