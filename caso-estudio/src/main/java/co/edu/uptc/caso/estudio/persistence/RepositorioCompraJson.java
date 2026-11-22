package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Compra;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class RepositorioCompraJson implements IRepositorioCompra {

    private final ManejadorArchivoJson<Compra> manejadorJson;

    public RepositorioCompraJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.compras", "compras.json");
        Type tipoLista = new TypeToken<ArrayList<Compra>>() {}.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    @Override
    public boolean registrarCompra(String clienteId, Carrito carrito) {
        if (clienteId == null || carrito == null || carrito.estaVacio()) {
            return false;
        }

        Compra compra = new Compra();
        compra.setIdCompra("CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        compra.setClienteId(clienteId);
        compra.setItems(new ArrayList<>(carrito.getItems()));
        compra.setSubtotalSinIva(carrito.calcularSubtotalSinIva());
        compra.setTotalImpuestos(carrito.calcularTotalImpuestos());
        compra.setTotalPagado(carrito.calcularTotal());

        List<Compra> compras = manejadorJson.leerLista();
        compras.add(compra);
        manejadorJson.guardarLista(compras);
        return true;
    }

    @Override
    public List<Compra> obtenerComprasPorCliente(String clienteId) {
        if (clienteId == null) {
            return new ArrayList<>();
        }
        return manejadorJson.leerLista().stream()
                .filter(c -> clienteId.equals(c.getClienteId()))
                .collect(Collectors.toList());
    }
}