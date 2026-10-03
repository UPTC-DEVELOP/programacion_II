package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra en memoria la lista de productos de la tienda.
 */
public class ControladorProducto {

    private List<Producto> productos;

    public ControladorProducto() {
        this.productos = new ArrayList<Producto>();
    }

    public void agregar(Producto producto) {
        if (producto != null) {
            productos.add(producto);
        }
    }

    public List<Producto> listar() {
        return new ArrayList<Producto>(productos);
    }

    public Producto buscar(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Producto producto : productos) {
            if (nombre.equalsIgnoreCase(producto.getNombre())) {
                return producto;
            }
        }
        return null;
    }

    public int cantidad() {
        return productos.size();
    }
}
