package co.edu.uptc.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.interfaces.IGestionCarrito;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.repository.CarritoJsonRepositorio;
import co.edu.uptc.repository.CarritoMySqlRepository;

public class GestionCarrito implements IGestionCarrito {

    private List<Carrito> itemsCarrito;
    private CarritoJsonRepositorio repositorio;
    private CarritoMySqlRepository mysqlRepositorio;

    public GestionCarrito() {
        this.repositorio = new CarritoJsonRepositorio();
        this.mysqlRepositorio = new CarritoMySqlRepository();
        try {
            this.itemsCarrito = mysqlRepositorio.listar();
            if (this.itemsCarrito == null) {
                this.itemsCarrito = new ArrayList<>();
            }
            repositorio.guardar(this.itemsCarrito);
        } catch (Exception e) {
            this.itemsCarrito = repositorio.cargar();
            if (this.itemsCarrito == null) {
                this.itemsCarrito = new ArrayList<>();
            }
        }
    }

    @Override
    public List<Carrito> obtenerItems() {
        return itemsCarrito;
    }

    @Override
    public void recargarDesdeJson() {
        this.itemsCarrito = repositorio.cargar();
    }

    @Override
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

        for (Carrito item : itemsCarrito) {
            if (item.getIdProducto().equalsIgnoreCase(idProducto.trim())) {
                int nuevaCantidad = item.getCantidad() + cantidad;
                mysqlRepositorio.actualizarCantidad(item.getIdProducto(), nuevaCantidad);
                item.setCantidad(nuevaCantidad);
                repositorio.guardar(itemsCarrito);
                return;
            }
        }

        Carrito nuevoItem = new Carrito(idProducto.trim(), titulo.trim(), cantidad, precioUnitario);
        mysqlRepositorio.guardar(nuevoItem);
        itemsCarrito.add(nuevoItem);
        repositorio.guardar(itemsCarrito);
    }

    @Override
    public void agregarItem(Carrito item) throws Exception {
        if (item == null) {
            throw new Exception("El ítem no puede ser nulo.");
        }
        agregarItem(item.getIdProducto(), item.getTitulo(), item.getCantidad(), item.getPrecioUnitario());
    }

    @Override
    public void modificarCantidad(int indice, int nuevaCantidad) throws Exception {
        if (indice < 0 || indice >= itemsCarrito.size()) {
            throw new Exception("Seleccione un ítem válido de la tabla.");
        }
        if (nuevaCantidad <= 0) {
            throw new Exception("La cantidad debe ser mayor a cero.");
        }

        Carrito item = itemsCarrito.get(indice);
        mysqlRepositorio.actualizarCantidad(item.getIdProducto(), nuevaCantidad);
        item.setCantidad(nuevaCantidad);
        repositorio.guardar(itemsCarrito);
    }

    @Override
    public void eliminarItem(int indice) throws Exception {
        if (indice < 0 || indice >= itemsCarrito.size()) {
            throw new Exception("Seleccione un ítem válido para eliminar.");
        }
        Carrito item = itemsCarrito.get(indice);
        mysqlRepositorio.eliminar(item.getIdProducto());
        itemsCarrito.remove(indice);
        repositorio.guardar(itemsCarrito);
    }

    @Override
    public void vaciarCarrito() {
        mysqlRepositorio.vaciar();
        itemsCarrito.clear();
        repositorio.guardar(itemsCarrito);
    }

    @Override
    public double calcularTotal() {
        double total = 0.0;
        for (Carrito item : itemsCarrito) {
            total += item.getCantidad() * item.getPrecioUnitario();
        }
        return total;
    }

    @Override
    public boolean estaVacio() {
        return itemsCarrito == null || itemsCarrito.isEmpty();
    }

    @Override
    public String[] procesarCompra(String nombreCliente) throws Exception {
        if (estaVacio()) {
            throw new Exception("El carrito está vacío. Agregue productos antes de realizar la compra.");
        }
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new Exception("Debe ingresar el nombre del cliente para confirmar la compra.");
        }

        double total = calcularTotal();
        String totalFormateado = "$" + String.format("%.2f", total);
        String fechaActual = LocalDate.now().toString();
        String idPedido = String.format("PED-%04d", (int) (Math.random() * 9000 + 1000));

        vaciarCarrito();

        return new String[]{
            idPedido,
            nombreCliente.trim(),
            fechaActual,
            totalFormateado,
            "Pendiente"
        };
    }
}