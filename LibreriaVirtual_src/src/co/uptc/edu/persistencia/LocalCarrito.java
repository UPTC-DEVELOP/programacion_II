package co.uptc.edu.persistencia;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.uptc.edu.interfaces.IGestionCarrito;
import co.uptc.edu.model.ItemCarrito;

/** Carritos en memoria: se conservan mientras la aplicación esté abierta. */
public class LocalCarrito implements IGestionCarrito {

    private final Map<String, Map<String, Integer>> carritos = new HashMap<>();

    @Override
    public void guardar(String correo, List<ItemCarrito> items) {
        Map<String, Integer> carrito = new LinkedHashMap<>();
        for (ItemCarrito item : items) {
            carrito.put(item.getLibro().getIsbn(), item.getCantidad());
        }
        carritos.put(correo, carrito);
    }

    @Override
    public Map<String, Integer> cargar(String correo) {
        Map<String, Integer> carrito = carritos.get(correo);
        return carrito == null ? new LinkedHashMap<>() : new LinkedHashMap<>(carrito);
    }
}
