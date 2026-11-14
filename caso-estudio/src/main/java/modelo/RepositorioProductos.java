package modelo;

import java.util.ArrayList;

import excepciones.ExcepcionValidacion;

public class RepositorioProductos {
    private ArrayList<Producto> productos;

    public RepositorioProductos() {
        productos = new ArrayList<Producto>();
        cargarProductosIniciales();
    }

    private void cargarProductosIniciales() {
        try {
            productos.add(new Producto(
                    "Cien años de soledad", 45000, 12, 10, 19));
            productos.add(new Producto(
                    "El principito", 18000, 20, 5, 19));
            productos.add(new Producto(
                    "1984", 32000, 8, 15, 19));
            productos.add(new Producto(
                    "Don Quijote de la Mancha", 25000, 10, 0, 19));
        } catch (ExcepcionValidacion e) {
            throw new IllegalStateException(
                    "No fue posible cargar los productos iniciales.", e);
        }
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<Producto>(productos);
    }
}
