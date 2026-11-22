package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioCarritoJson implements IRepositorioCarrito {

    private final ManejadorArchivoJson<Carrito> manejadorJson;

    public RepositorioCarritoJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.carritos", "data/carritos.json");
        Type tipoLista = new TypeToken<ArrayList<Carrito>>() {}.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    @Override
    public Carrito obtenerCarritoPorCliente(String clienteId) {
        if (clienteId == null) return new Carrito();
        return manejadorJson.leerLista().stream()
                .filter(c -> clienteId.equals(c.getClienteId()))
                .findFirst()
                .orElse(new Carrito(clienteId));
    }

    @Override
    public boolean guardarCarrito(Carrito carrito) {
        if (carrito == null || carrito.getClienteId() == null) return false;
        List<Carrito> lista = manejadorJson.leerLista();
        lista.removeIf(c -> carrito.getClienteId().equals(c.getClienteId()));
        lista.add(carrito);
        manejadorJson.guardarLista(lista);
        return true;
    }

    @Override
    public boolean vaciarCarrito(String clienteId) {
        if (clienteId == null) return false;
        List<Carrito> lista = manejadorJson.leerLista();
        boolean removido = lista.removeIf(c -> clienteId.equals(c.getClienteId()));
        if (removido) {
            manejadorJson.guardarLista(lista);
        }
        return removido;
    }
}