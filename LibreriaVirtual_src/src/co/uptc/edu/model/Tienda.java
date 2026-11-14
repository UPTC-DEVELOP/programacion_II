package co.uptc.edu.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import co.uptc.edu.interfaces.IGestionCarrito;
import co.uptc.edu.interfaces.IGestionCompra;
import co.uptc.edu.interfaces.IGestionLibro;

/**
 * Lógica de negocio y estado compartido: sesión, inventario y carrito.
 * No depende de Swing, por lo que las pantallas solo se encargan de mostrar datos.
 */
public class Tienda {

    public enum ResultadoCarrito { AGREGADO, CANTIDAD_INCREMENTADA, SIN_STOCK }

    private final List<Libro> inventario = new ArrayList<>();
    private final CarritoDeCompras carrito = new CarritoDeCompras();
    private final GestionUsuarios gestionUsuarios;
    private final IGestionLibro persistenciaLibros;
    private final IGestionCompra persistenciaCompras;
    private final IGestionCarrito persistenciaCarrito;
    private Usuario usuarioSesion;

    /** Recibe la gestión de usuarios y la persistencia de libros, compras y carrito ya configuradas. */
    public Tienda(GestionUsuarios gestionUsuarios, IGestionLibro persistenciaLibros,
                  IGestionCompra persistenciaCompras, IGestionCarrito persistenciaCarrito) {
        this.gestionUsuarios = gestionUsuarios;
        this.persistenciaLibros = persistenciaLibros;
        this.persistenciaCompras = persistenciaCompras;
        this.persistenciaCarrito = persistenciaCarrito;
    }

    // ---------- Sesión y usuarios ----------
    public GestionUsuarios getGestionUsuarios() { return gestionUsuarios; }

    /** Inicia sesión con correo y contraseña (RF-01). Devuelve false si las credenciales no son válidas. */
    public boolean iniciarSesion(String correo, String contrasena) {
        Usuario u = gestionUsuarios.autenticar(correo, contrasena);
        usuarioSesion = u;
        if (u != null) {
            cargarCarritoGuardado();
        }
        return u != null;
    }

    /** Al salir se limpia el carrito en pantalla; el carrito guardado del usuario se conserva. */
    public void cerrarSesion() {
        usuarioSesion = null;
        carrito.vaciarCarrito();
    }

    /** Mantiene al día los datos del usuario en sesión después de editarlos. */
    public void actualizarUsuarioSesion(Usuario usuario) {
        if (usuarioSesion != null && usuario != null) {
            usuarioSesion = usuario;
        }
    }

    public Usuario getUsuarioSesion() { return usuarioSesion; }
    public String getUsuarioActual() { return usuarioSesion == null ? "" : usuarioSesion.getNombreCompleto(); }
    public Rol getRolActual() { return usuarioSesion == null ? Rol.INVITADO : usuarioSesion.getTipoUsuario(); }

    // ---------- Inventario ----------
    public List<Libro> getInventario() { return Collections.unmodifiableList(inventario); }

    /** Guarda el libro en la persistencia y lo agrega al inventario. */
    public void agregarLibro(Libro libro) {
        persistenciaLibros.guardar(libro);
        inventario.add(libro);
    }

    public void cargarDatosSimulados() {
        inventario.clear();
        carrito.vaciarCarrito();
        usuarioSesion = null;
        gestionUsuarios.cargarDatosSimulados();
        // Si ya hay libros guardados se usan esos; el catálogo de ejemplo solo se crea la primera vez.
        inventario.addAll(persistenciaLibros.listar());
        if (!inventario.isEmpty()) {
            return;
        }
        agregarLibro(new Libro("9780000000101", "Cien Años de Soledad", "Gabriel García Márquez","1967", "Novela", "Editorial Sudamericana", 417, 45000, 15, "Físico"));
        agregarLibro(new Libro("9780000000102", "El Código Da Vinci", "Dan Brown","2003", "Misterio", "Doubleday", 489, 38000, 8, "Físico"));
        agregarLibro(new Libro("9780000000103", "Clean Code in Java", "Robert C. Martin","2008", "Tecnología", "Prentice Hall", 464, 120000, 5, "Digital"));
        agregarLibro(new Libro("9780000000104", "Hábitos Atómicos", "James Clear","2018", "Superación", "Avery", 320, 52000, 20, "Físico"));
        agregarLibro(new Libro("9780000000105", "El Principito", "Antoine de Saint-Exupéry","1943", "Fábula", "Reynal & Hitchcock", 96, 25000, 12, "Físico"));
        agregarLibro(new Libro("9780000000106", "Don Quijote de la Mancha", "Miguel de Cervantes","1605", "Clásico", "Francisco de Robles", 863, 60000, 7, "Digital"));
    }

    // ---------- Carrito (RF-05) ----------
    public CarritoDeCompras getCarritoDeCompras() { return carrito; }

    public List<ItemCarrito> getCarrito() { return carrito.getItems(); }

    public ResultadoCarrito agregarAlCarrito(Libro libro) {
        ResultadoCarrito resultado = carrito.agregarLibro(libro);
        if (resultado != ResultadoCarrito.SIN_STOCK) {
            guardarCarrito();
        }
        return resultado;
    }

    public void ajustarCantidadCarrito(String isbn, int cantidad) {
        carrito.ajustarCantidad(isbn, cantidad);
        guardarCarrito();
    }

    public void incrementarCantidadCarrito(String isbn) {
        carrito.incrementarCantidad(isbn);
        guardarCarrito();
    }

    public void decrementarCantidadCarrito(String isbn) {
        carrito.decrementarCantidad(isbn);
        guardarCarrito();
    }

    public void eliminarDelCarrito(String isbn) {
        carrito.eliminarLibro(isbn);
        guardarCarrito();
    }

    public void vaciarCarrito() {
        carrito.vaciarCarrito();
        guardarCarrito();
    }

    public double getSubtotalCarrito() { return carrito.calcularSubtotal(); }

    public double getIvaCarrito() { return carrito.calcularImpuestoTotal(); }

    /** Total a pagar: subtotal + IVA. */
    public double getTotalCarrito() { return carrito.calcularTotalPagar(); }

    public void finalizarCompra() {
        if (carrito.estaVacio()) {
            return;
        }
        // Primero se registra la compra: si falla, no se descuenta nada.
        persistenciaCompras.registrar(usuarioSesion, carrito.getItems(),
                carrito.calcularSubtotal(), carrito.calcularImpuestoTotal(), carrito.calcularTotalPagar());
        for (ItemCarrito item : carrito.getItems()) {
            Libro l = item.getLibro();
            l.setStock(l.getStock() - item.getCantidad());
            persistenciaLibros.actualizarStock(l.getIsbn(), l.getStock());
        }
        carrito.vaciarCarrito();
        guardarCarrito();
    }

    // ---------- Carrito guardado por usuario ----------
    /** Guarda el carrito del usuario en sesión. Si falla, el carrito sigue funcionando en pantalla. */
    private void guardarCarrito() {
        if (usuarioSesion == null) {
            return;
        }
        try {
            persistenciaCarrito.guardar(usuarioSesion.getCorreo(), carrito.getItems());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    /** Recupera el carrito que el usuario dejó en su sesión anterior, sin superar el stock actual. */
    private void cargarCarritoGuardado() {
        carrito.vaciarCarrito();
        Map<String, Integer> guardado;
        try {
            guardado = persistenciaCarrito.cargar(usuarioSesion.getCorreo());
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
            return;
        }
        for (Map.Entry<String, Integer> entrada : guardado.entrySet()) {
            Libro libro = buscarLibro(entrada.getKey());
            if (libro == null || libro.getStock() < 1) {
                continue;
            }
            carrito.agregarLibro(libro);
            carrito.ajustarCantidad(libro.getIsbn(), Math.max(1, Math.min(entrada.getValue(), libro.getStock())));
        }
    }

    private Libro buscarLibro(String isbn) {
        for (Libro libro : inventario) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }
}