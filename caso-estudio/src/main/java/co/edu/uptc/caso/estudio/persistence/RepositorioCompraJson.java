package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Compra;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import co.edu.uptc.caso.estudio.model.Cliente;

public class RepositorioCompraJson implements IRepositorioCompra {

    private final ManejadorArchivoJson<Compra> manejadorJson;

    public RepositorioCompraJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.compras", "compras.json");
        Type tipoLista = new TypeToken<ArrayList<Compra>>() {}.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    @Override
    public boolean registrarCompra(Cliente cliente, Carrito carrito, String metodoPago) {
        if (cliente == null || cliente.getId() == null || carrito == null || carrito.estaVacio()) {
            return false;
        }

        Compra compra = new Compra();
        compra.setIdCompra("CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        compra.setClienteId(cliente.getId());
        compra.setNombreCliente(cliente.getNombreCompleto());
        compra.setItems(new ArrayList<>(carrito.getItems()));
        compra.setSubtotalSinIva(carrito.calcularSubtotalSinIva());
        compra.setTotalImpuestos(carrito.calcularTotalImpuestos());
        compra.setDescuentoAplicado(carrito.calcularDescuento(cliente));
        compra.setTotalPagado(carrito.calcularTotalFinal(cliente));
        compra.setMetodoPago(metodoPago);

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