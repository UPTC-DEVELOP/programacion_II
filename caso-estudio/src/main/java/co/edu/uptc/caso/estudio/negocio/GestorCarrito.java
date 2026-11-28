package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import java.util.Iterator;
import java.util.List;

// Administra la consulta, modificación y sincronización (MySQL -> JSON) del carrito del usuario
public class GestorCarrito {

    private static final String ID_CLIENTE_ANONIMO = "anonimo";

    private final IRepositorioCarrito repoCarritoMySQL;
    private final IRepositorioCarrito repoCarritoJson;
    private final IGestionUsuarios gestionUsuarios;
    private final IGestionLibros gestionLibros;

    public GestorCarrito(
            IRepositorioCarrito repoCarritoMySQL,
            IRepositorioCarrito repoCarritoJson,
            IGestionUsuarios gestionUsuarios,
            IGestionLibros gestionLibros) {
        this.repoCarritoMySQL = repoCarritoMySQL;
        this.repoCarritoJson = repoCarritoJson;
        this.gestionUsuarios = gestionUsuarios;
        this.gestionLibros = gestionLibros;
    }

    public Carrito obtenerCarritoActual() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null) {
            return new Carrito(ID_CLIENTE_ANONIMO);
        }

        Carrito carrito = buscarCarritoPersistido(logueado.getId());
        if (carrito == null) {
            carrito = new Carrito(logueado.getId());
        }
        refrescarLibrosDelCarrito(carrito);
        return carrito;
    }

    public void agregarLibro(Libro libro, int cantidad) {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null || libro == null || cantidad <= 0) {
            return;
        }

        Carrito c = obtenerCarritoActual();
        ItemCarrito existente = c.buscarItem(libro.getIsbn());
        int cantidadActual = (existente != null) ? existente.getCantidad() : 0;

        Libro libroActualizado = gestionLibros != null
                ? gestionLibros.buscarLibroPorIsbn(libro.getIsbn())
                : null;
        int stockDisponible = (libroActualizado != null)
                ? libroActualizado.getStock()
                : libro.getStock();

        if (cantidadActual + cantidad > stockDisponible) {
            return;
        }

        c.agregarItem(libroActualizado != null ? libroActualizado : libro, cantidad);
        sincronizarCarrito(logueado, c);
    }

    public String actualizarCantidad(String isbn, int nuevaCantidad) {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null) {
            return "Debe iniciar sesión primero.";
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            return "ISBN inválido.";
        }
        if (nuevaCantidad <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        Carrito c = obtenerCarritoActual();
        ItemCarrito item = c.buscarItem(isbn);
        if (item == null) {
            return "El libro no se encuentra en el carrito.";
        }

        Libro l = gestionLibros != null ? gestionLibros.buscarLibroPorIsbn(isbn) : null;
        if (l != null && nuevaCantidad > l.getStock()) {
            return "Stock insuficiente. Disponible: " + l.getStock();
        }

        c.actualizarCantidad(isbn, nuevaCantidad);
        sincronizarCarrito(logueado, c);
        return null;
    }

    public boolean eliminarLibros(List<String> isbns) {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null || isbns == null || isbns.isEmpty()) {
            return false;
        }

        Carrito c = obtenerCarritoActual();
        boolean eliminado = c.eliminarItems(isbns);
        if (eliminado) {
            sincronizarCarrito(logueado, c);
        }
        return eliminado;
    }

    public void vaciarCarrito() {
        Cliente logueado = obtenerUsuarioLogueado();
        if (logueado == null) {
            return;
        }
        String idCliente = logueado.getId();
        if (repoCarritoMySQL != null) {
            repoCarritoMySQL.vaciarCarrito(idCliente);
            repoCarritoMySQL.guardarCarrito(new Carrito(idCliente));
        }
        if (repoCarritoJson != null) {
            repoCarritoJson.vaciarCarrito(idCliente);
            repoCarritoJson.guardarCarrito(new Carrito(idCliente));
        }
    }

    private Carrito buscarCarritoPersistido(String idCliente) {
        Carrito carrito = repoCarritoMySQL != null
                ? repoCarritoMySQL.obtenerCarritoPorCliente(idCliente)
                : null;
        if (carrito == null || carrito.estaVacio()) {
            Carrito respaldoJson = repoCarritoJson != null
                    ? repoCarritoJson.obtenerCarritoPorCliente(idCliente)
                    : null;
            if (respaldoJson != null && !respaldoJson.estaVacio()) {
                return respaldoJson;
            }
        }
        return carrito;
    }

    private void refrescarLibrosDelCarrito(Carrito carrito) {
        if (carrito == null || carrito.getItems() == null) {
            return;
        }
        Iterator<ItemCarrito> it = carrito.getItems().iterator();
        while (it.hasNext()) {
            ItemCarrito item = it.next();
            Libro actual = (item != null && item.getLibro() != null && gestionLibros != null)
                    ? gestionLibros.buscarLibroPorIsbn(item.getLibro().getIsbn())
                    : null;
            if (actual == null) {
                it.remove();
            } else {
                item.setLibro(actual);
            }
        }
    }

    private void sincronizarCarrito(Cliente logueado, Carrito carrito) {
        if (logueado == null || carrito == null) {
            return;
        }
        if (repoCarritoMySQL != null) {
            repoCarritoMySQL.guardarCarrito(carrito);
        }
        if (repoCarritoJson != null) {
            repoCarritoJson.guardarCarrito(carrito);
        }
    }

    private Cliente obtenerUsuarioLogueado() {
        return gestionUsuarios != null ? gestionUsuarios.getUsuarioLogueado() : null;
    }
}