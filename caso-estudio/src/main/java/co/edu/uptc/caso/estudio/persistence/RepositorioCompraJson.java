package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.Compra;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Persistencia JSON para el registro y consulta histórica de compras
public class RepositorioCompraJson implements IRepositorioCompra {

    private final ManejadorArchivoJson<Compra> manejadorJson;

    public RepositorioCompraJson() {
        String ruta = ConfiguracionTienda.obtener("ruta.json.compras", "data/compras.json");
        Type tipoLista = new TypeToken<ArrayList<Compra>>() {
        }.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(ruta, tipoLista);
    }

    // Construye y guarda una nueva compra si el cliente y el carrito son válidos
    @Override
    public boolean registrarCompra(Cliente cliente, Carrito carrito, String metodoPago) {
        if (cliente == null || cliente.getId() == null || carrito == null || carrito.estaVacio()) {
            return false;
        }

        Compra compra = new Compra(generarIdCompra(), cliente, carrito, metodoPago);
        List<Compra> compras = manejadorJson.leerLista();
        compras.add(compra);
        manejadorJson.guardarLista(compras);
        return true;
    }

    // Filtra y retorna todas las compras asociadas al identificador del cliente
    @Override
    public List<Compra> obtenerComprasPorCliente(String clienteId) {
        List<Compra> resultado = new ArrayList<>();
        if (clienteId == null) {
            return resultado;
        }

        List<Compra> todas = manejadorJson.leerLista();
        for (Compra compra : todas) {
            if (compra != null && clienteId.equals(compra.getClienteId())) {
                resultado.add(compra);
            }
        }
        return resultado;
    }

    private String generarIdCompra() {
        return "CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}