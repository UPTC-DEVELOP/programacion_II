package modelo;

import java.util.ArrayList;

import excepciones.ExcepcionValidacion;

public class RepositorioCarrito {
    private ArrayList<ItemCarrito> items;

    public RepositorioCarrito() {
        items = new ArrayList<ItemCarrito>();
    }

    public void agregar(Libro libro, int cantidad) throws ExcepcionValidacion {
        if (libro == null) {
            throw new ExcepcionValidacion("Debe seleccionar un libro.");
        }

        if (buscarIndice(libro) != -1) {
            throw new ExcepcionValidacion("El libro ya está en el carrito. Use Actualizar para cambiar la cantidad.");
        }

        items.add(new ItemCarrito(libro, cantidad));
    }

    public ArrayList<ItemCarrito> listar() {
        return new ArrayList<ItemCarrito>(items);
    }

    public void actualizarCantidad(int posicion, int nuevaCantidad) throws ExcepcionValidacion {
        validarPosicion(posicion);
        items.get(posicion).setCantidad(nuevaCantidad);
    }

    public void eliminar(int posicion) throws ExcepcionValidacion {
        validarPosicion(posicion);
        items.remove(posicion);
    }

    public int calcularTotal() {
        int total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    private int buscarIndice(Libro libro) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getLibro() == libro) {
                return i;
            }
        }
        return -1;
    }

    private void validarPosicion(int posicion) throws ExcepcionValidacion {
        if (posicion < 0 || posicion >= items.size()) {
            throw new ExcepcionValidacion("Seleccione un elemento válido del carrito.");
        }
    }
}
