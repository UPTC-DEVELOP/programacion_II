package co.uptc.edu.interfaces;

import java.util.List;
import java.util.Map;

import co.uptc.edu.model.ItemCarrito;

/** Contrato para conservar el carrito de cada usuario entre sesiones (RF-05). */
public interface IGestionCarrito {

    /** Deja guardado el carrito del usuario exactamente con los ítems recibidos. */
    void guardar(String correo, List<ItemCarrito> items);

    /** Devuelve el carrito guardado del usuario: ISBN de cada libro y su cantidad. */
    Map<String, Integer> cargar(String correo);
}
