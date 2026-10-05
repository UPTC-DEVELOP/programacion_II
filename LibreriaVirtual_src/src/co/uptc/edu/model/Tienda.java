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
    private final GestionUsuarios gestionUsuarios = new GestionUsuarios();
    private Usuario usuarioSesion;

    // ---------- Sesión y usuarios ----------
    public GestionUsuarios getGestionUsuarios() { return gestionUsuarios; }

    /** Inicia sesión con correo y contraseña (RF-01). Devuelve false si las credenciales no son válidas. */
    public boolean iniciarSesion(String correo, String contrasena) {
        Usuario u = gestionUsuarios.autenticar(correo, contrasena);
        usuarioSesion = u;
        return u != null;
    }

    public void cerrarSesion() {
        usuarioSesion = null;
    }

    public Usuario getUsuarioSesion() { return usuarioSesion; }
    public String getUsuarioActual() { return usuarioSesion == null ? "" : usuarioSesion.getNombreCompleto(); }
    public Rol getRolActual() { return usuarioSesion == null ? Rol.INVITADO : usuarioSesion.getTipoUsuario(); }

    // ---------- Inventario ----------
    public List<Libro> getInventario() { return Collections.unmodifiableList(inventario); }

    public void agregarLibro(Libro libro) { inventario.add(libro); }

    public void cargarDatosSimulados() {
        inventario.clear();
        carrito.clear();
        usuarioSesion = null;
        gestionUsuarios.cargarDatosSimulados();
        inventario.add(new Libro("9780000000101", "Cien Años de Soledad", "Gabriel García Márquez","1967", "Novela", "Editorial Sudamericana", 417, 45000, 15, "Físico"));
        inventario.add(new Libro("9780000000102", "El Código Da Vinci", "Dan Brown","2003", "Misterio", "Doubleday", 489, 38000, 8, "Físico"));
        inventario.add(new Libro("9780000000103", "Clean Code in Java", "Robert C. Martin","2008", "Tecnología", "Prentice Hall", 464, 120000, 5, "Digital"));
        inventario.add(new Libro("9780000000104", "Hábitos Atómicos", "James Clear","2018", "Superación", "Avery", 320, 52000, 20, "Físico"));
        inventario.add(new Libro("9780000000105", "El Principito", "Antoine de Saint-Exupéry","1943", "Fábula", "Reynal & Hitchcock", 96, 25000, 12, "Físico"));
        inventario.add(new Libro("9780000000106", "Don Quijote de la Mancha", "Miguel de Cervantes","1605", "Clásico", "Francisco de Robles", 863, 60000, 7, "Digital"));
    }

    // ---------- Carrito ----------
    public List<ItemCarrito> getCarrito() { return Collections.unmodifiableList(carrito); }

    public ResultadoCarrito agregarAlCarrito(Libro libro) {
        for (ItemCarrito item : carrito) {
            if (item.getLibro().getIsbn() == libro.getIsbn()) {
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


    public void finalizarCompra() {
        for (ItemCarrito item : carrito) {
            Libro l = item.getLibro();
            l.setStock(l.getStock() - item.getCantidad());
        }
        carrito.clear();
    }
}
