package co.uptc.edu.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.uptc.edu.negocio.GestionUsuarios;
import co.uptc.edu.negocio.ItemCarrito;
import co.uptc.edu.negocio.Libro;
import co.uptc.edu.negocio.Rol;
import co.uptc.edu.negocio.Usuario;



 
public class Tienda {

    public enum ResultadoCarrito { AGREGADO, CANTIDAD_INCREMENTADA, SIN_STOCK }

    private final ServicioLibro servicioLibros;
    private final List<ItemCarrito> carrito = new ArrayList<>();
    private final GestionUsuarios gestionUsuarios = new GestionUsuarios();
    private Usuario usuarioSesion;

    public Tienda(ServicioLibro servicioLibros) {
        this.servicioLibros = servicioLibros;
    }

    // ---------- Sesión y usuarios ----------
    public GestionUsuarios getGestionUsuarios() { return gestionUsuarios; }

    /** Inicia sesión con correo y contraseña . Devuelve false si las credenciales no son válidas. */
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
    public ServicioLibro getServicioLibros() { return servicioLibros; }

    public List<Libro> getInventario() { return servicioLibros.listar(); }

    public void agregarLibro(Libro libro) { servicioLibros.registrar(libro); }

    public void cargarDatosSimulados() {
        servicioLibros.vaciar();
        carrito.clear();
        usuarioSesion = null;
        gestionUsuarios.cargarDatosSimulados();
        servicioLibros.registrar(new Libro("9780000000101", "Cien Años de Soledad", "Gabriel García Márquez","1967", "Novela", "Editorial Sudamericana", 417, 45000, 15, "Físico"));
        servicioLibros.registrar(new Libro("9780000000102", "El Código Da Vinci", "Dan Brown","2003", "Misterio", "Doubleday", 489, 38000, 8, "Físico"));
        servicioLibros.registrar(new Libro("9780000000103", "Clean Code in Java", "Robert C. Martin","2008", "Tecnología", "Prentice Hall", 464, 120000, 5, "Digital"));
        servicioLibros.registrar(new Libro("9780000000104", "Hábitos Atómicos", "James Clear","2018", "Superación", "Avery", 320, 52000, 20, "Físico"));
        servicioLibros.registrar(new Libro("9780000000105", "El Principito", "Antoine de Saint-Exupéry","1943", "Fábula", "Reynal & Hitchcock", 96, 25000, 12, "Físico"));
        servicioLibros.registrar(new Libro("9780000000106", "Don Quijote de la Mancha", "Miguel de Cervantes","1605", "Clásico", "Francisco de Robles", 863, 60000, 7, "Digital"));
    }

    // ---------- Carrito ----------
    public List<ItemCarrito> getCarrito() { return Collections.unmodifiableList(carrito); }

    public ResultadoCarrito agregarAlCarrito(Libro libro) {
        for (ItemCarrito item : carrito) {
            if (item.getLibro().getIsbn().equals(libro.getIsbn())) {
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
