package co.edu.uptc.tienda.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Carrito {

    private final List<ItemCarrito> items = new ArrayList<>();

    public ItemCarrito buscarItem(String isbn) {
        for (ItemCarrito item : items) {
            if (item.getLibro().getIsbn().equals(isbn)) {
                return item;
            }
        }
        return null;
    }

    public void agregarItem(ItemCarrito item) {
        items.add(item);
    }

    public void quitarItem(ItemCarrito item) {
        items.remove(item);
    }

    public List<ItemCarrito> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}
