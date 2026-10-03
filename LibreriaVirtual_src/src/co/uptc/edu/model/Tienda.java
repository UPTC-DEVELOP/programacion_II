package co.uptc.edu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lógica de negocio y estado compartido: sesión, inventario y carrito.
 * No depende de Swing, por lo que las pantallas solo se encargan de mostrar datos.
 */
public class Tienda {

    public enum ResultadoCarrito { AGREGADO, CANTIDAD_INCREMENTADA, SIN_STOCK }

    private final List<Libro> inventario = new ArrayList<>();
    private final List<ItemCarrito> carrito = new ArrayList<>();
    private String usuarioActual = "";
    private Rol rolActual = Rol.INVITADO;

    // ---------- Sesión ----------
    public void iniciarSesion(String usuario, Rol rol) {
        this.usuarioActual = (usuario == null || usuario.trim().isEmpty()) ? "Usuario Demo" : usuario.trim();
        this.rolActual = rol;
    }

    public String getUsuarioActual() { return usuarioActual; }
    public Rol getRolActual() { return rolActual; }

    // ---------- Inventario ----------
    public List<Libro> getInventario() { return Collections.unmodifiableList(inventario); }

    public void agregarLibro(Libro libro) { inventario.add(libro); }

    public void cargarDatosSimulados() {
        inventario.clear();
        carrito.clear();
        inventario.add(new Libro(101, "Cien Años de Soledad", "Gabriel García Márquez", 45000, 15, "Novela"));
        inventario.add(new Libro(102, "El Código Da Vinci", "Dan Brown", 38000, 8, "Misterio"));
        inventario.add(new Libro(103, "Clean Code in Java", "Robert C. Martin", 120000, 5, "Tecnología"));
        inventario.add(new Libro(104, "Hábitos Atómicos", "James Clear", 52000, 20, "Superación"));
        inventario.add(new Libro(105, "El Principito", "Antoine de Saint-Exupéry", 25000, 12, "Fábula"));
        inventario.add(new Libro(106, "Don Quijote de la Mancha", "Miguel de Cervantes", 60000, 7, "Clásico"));
    }

    // ---------- Carrito ----------
    public List<ItemCarrito> getCarrito() { return Collections.unmodifiableList(carrito); }

    public ResultadoCarrito agregarAlCarrito(Libro libro) {
        for (ItemCarrito item : carrito) {
            if (item.getLibro().getId() == libro.getId()) {
                if (item.getCantidad() < libro.getStock()) {
                    item.setCantidad(item.getCantidad() + 1);
                    return ResultadoCarrito.CANTIDAD_INCREMENTADA;
                }
                return ResultadoCarrito.SIN_STOCK;
            }
        }
        carrito.add(new ItemCarrito(libro, 1));
        return ResultadoCarrito.AGREGADO;
    }

    public void vaciarCarrito() { carrito.clear(); }

    public double getTotalCarrito() {
        double total = 0;
        for (ItemCarrito item : carrito) {
            total += item.getSubtotal();
        }
        return total;
    }

    /** Descuenta el stock de cada libro comprado y vacía el carrito. */
    public void finalizarCompra() {
        for (ItemCarrito item : carrito) {
            Libro l = item.getLibro();
            l.setStock(l.getStock() - item.getCantidad());
        }
        carrito.clear();
    }
}
