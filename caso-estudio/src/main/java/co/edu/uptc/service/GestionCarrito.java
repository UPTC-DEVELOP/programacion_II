package co.edu.uptc.service;

import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.repository.CarritoJsonRepositorio;

public class GestionCarrito {

    private List<Carrito> itemsCarrito;
    private CarritoJsonRepositorio repositorio;

    public GestionCarrito() {
        this.repositorio = new CarritoJsonRepositorio();
        this.itemsCarrito = repositorio.cargar();
        if (this.itemsCarrito == null) {
            this.itemsCarrito = new ArrayList<>();
        }
    }

    // Consultar ítems del carrito (Read)
    public List<Carrito> obtenerItems() {
        return itemsCarrito;
    }

    public void recargarDesdeJson() {
        this.itemsCarrito = repositorio.cargar();
    }

    // Agregar producto al carrito (Create) - Si ya existe el ID, suma la cantidad
    public void agregarItem(String idProducto, String titulo, int cantidad, double precioUnitario) throws Exception {
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new Exception("El ID del producto es obligatorio.");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new Exception("El título del producto es obligatorio.");
        }
        if (cantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }
        if (precioUnitario < 0) {
            throw new Exception("El precio unitario no puede ser negativo.");
        }

        // Verificar si el producto ya está en el carrito para acumular cantidad
        for (Carrito item : itemsCarrito) {
            if (item.getIdProducto().equalsIgnoreCase(idProducto.trim())) {
                item.setCantidad(item.getCantidad() + cantidad);
                repositorio.guardar(itemsCarrito);
                return;
            }
        }

        Carrito nuevoItem = new Carrito(idProducto.trim(), titulo.trim(), cantidad, precioUnitario);
        itemsCarrito.add(nuevoItem);
        repositorio.guardar(itemsCarrito);
    }

    // Sobrecarga por si se envía directamente el objeto Carrito
    public void agregarItem(Carrito item) throws Exception {
        if (item == null) {
            throw new Exception("El ítem no puede ser nulo.");
        }
        agregarItem(item.getIdProducto(), item.getTitulo(), item.getCantidad(), item.getPrecioUnitario());
    }

    // Modificar cantidad de un ítem (Update)
    public void modificarCantidad(int indice, int nuevaCantidad) throws Exception {
        if (indice < 0 || indice >= itemsCarrito.size()) {
            throw new Exception("Seleccione un ítem válido de la tabla.");
        }
        if (nuevaCantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }

        itemsCarrito.get(indice).setCantidad(nuevaCantidad);
        repositorio.guardar(itemsCarrito);
    }

    //  Eliminar un ítem específico (Delete)
    public void eliminarItem(int indice) throws Exception {
        if (indice < 0 || indice >= itemsCarrito.size()) {
            throw new Exception("Seleccione un ítem válido para eliminar.");
        }
        itemsCarrito.remove(indice);
        repositorio.guardar(itemsCarrito);
    }

    //  Vaciar todo el carrito (Delete)
    public void vaciarCarrito() {
        itemsCarrito.clear();
        repositorio.guardar(itemsCarrito);
    }

    //  Calcular total acumulado
    public double calcularTotal() {
        double total = 0.0;
        for (Carrito item : itemsCarrito) {
            total += item.getCantidad() * item.getPrecioUnitario();
        }
        return total;
    }

    //  Validar si hay productos antes de confirmar el pedido
    public boolean estaVacio() {
        return itemsCarrito == null || itemsCarrito.isEmpty();
    }
}