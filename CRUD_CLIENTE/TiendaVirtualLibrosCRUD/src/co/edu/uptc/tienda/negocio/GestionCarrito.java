package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.persistencia.PersistenciaCarritoJSON;

public class GestionCarrito implements IGestionCarrito {

    private final List<ItemCarrito> items;
    private final IPersistenciaCarrito persistenciaCarrito;
    private final IRepositorioLibro repositorioLibro;
    private String clienteActualClave;

    public GestionCarrito() {
        this(new PersistenciaCarritoJSON(), null);
    }

    public GestionCarrito(IPersistenciaCarrito persistenciaCarrito, IRepositorioLibro repositorioLibro) {
        this.items = new ArrayList<>();
        this.persistenciaCarrito = persistenciaCarrito;
        this.repositorioLibro = repositorioLibro;
    }

    public void establecerClienteActivo(Cliente cliente) {
        this.clienteActualClave = extraerClaveCliente(cliente);
        cargarDesdePersistencia();
    }

    public void cerrarSesionCliente() {
        if (this.clienteActualClave != null && this.persistenciaCarrito != null) {
            this.persistenciaCarrito.guardarCarritoCliente(this.clienteActualClave, this.items);
        }
        this.clienteActualClave = null;
        this.items.clear();
    }

    @Override
    public void agregarLibro(Libro libro, int cantidad) {
        if (libro == null) {
            throw new NegocioException("Seleccione un libro del catálogo.");
        }

        if (cantidad <= 0) {
            throw new NegocioException("La cantidad debe ser mayor a 0.");
        }

        ItemCarrito itemExistente = buscarItem(libro.getIsbn());
        int cantidadEnCarrito = itemExistente != null ? itemExistente.getCantidad() : 0;

        if (cantidadEnCarrito + cantidad > libro.getStockDisponible()) {
            throw new NegocioException("Stock insuficiente para '" + libro.getTitulo() + "'. Disponibles: "
                    + libro.getStockDisponible() + ", en el carrito: " + cantidadEnCarrito + ".");
        }

        if (itemExistente != null) {
            itemExistente.setCantidad(cantidadEnCarrito + cantidad);
        } else {
            items.add(new ItemCarrito(libro, cantidad));
        }

        guardarEnPersistencia();
    }

    @Override
    public void quitarLibro(String isbn) {
        if (!items.removeIf(item -> item.getLibro() != null
                && item.getLibro().getIsbn() != null
                && item.getLibro().getIsbn().equals(isbn))) {
            throw new NegocioException("El libro con ISBN " + isbn + " no está en el carrito.");
        }

        guardarEnPersistencia();
    }

    @Override
    public void vaciar() {
        items.clear();
        if (this.clienteActualClave != null && this.persistenciaCarrito != null) {
            this.persistenciaCarrito.eliminarCarritoCliente(this.clienteActualClave);
        }
    }

    @Override
    public List<ItemCarrito> listarItems() {
        return new ArrayList<>(items);
    }

    @Override
    public boolean estaVacio() {
        return items.isEmpty();
    }

    @Override
    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return redondear(subtotal);
    }

    @Override
    public double calcularDescuento(Cliente cliente) {
        if (cliente == null) {
            return 0;
        }
        return redondear(cliente.calcularDescuento(calcularSubtotal()));
    }

    @Override
    public double calcularTotal(Cliente cliente) {
        return redondear(calcularSubtotal() - calcularDescuento(cliente));
    }

    private void cargarDesdePersistencia() {
        this.items.clear();
        if (this.clienteActualClave == null || this.persistenciaCarrito == null || this.repositorioLibro == null) {
            return;
        }

        List<IPersistenciaCarrito.ItemGuardadoCarrito> guardados =
                this.persistenciaCarrito.cargarCarritoCliente(this.clienteActualClave);
        if (guardados == null || guardados.isEmpty()) {
            return;
        }

        List<Libro> catalogo = this.repositorioLibro.listar();
        if (catalogo == null || catalogo.isEmpty()) {
            return;
        }

        for (IPersistenciaCarrito.ItemGuardadoCarrito guardado : guardados) {
            if (guardado == null || guardado.getIsbn() == null || guardado.getCantidad() <= 0) {
                continue;
            }
            for (Libro libro : catalogo) {
                if (libro != null && libro.getIsbn() != null
                        && libro.getIsbn().trim().equalsIgnoreCase(guardado.getIsbn().trim())) {
                    int cantidadAjustada = Math.min(guardado.getCantidad(), libro.getStockDisponible());
                    if (cantidadAjustada > 0) {
                        this.items.add(new ItemCarrito(libro, cantidadAjustada));
                    }
                    break;
                }
            }
        }

        guardarEnPersistencia();
    }

    private void guardarEnPersistencia() {
        if (this.clienteActualClave != null && this.persistenciaCarrito != null) {
            this.persistenciaCarrito.guardarCarritoCliente(this.clienteActualClave, this.items);
        }
    }

    private String extraerClaveCliente(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        if (cliente.getCorreoElectronico() != null && !cliente.getCorreoElectronico().isBlank()) {
            return cliente.getCorreoElectronico().trim().toLowerCase();
        }
        if (cliente.getIdentificacion() != null && !cliente.getIdentificacion().isBlank()) {
            return cliente.getIdentificacion().trim();
        }
        return null;
    }

    private ItemCarrito buscarItem(String isbn) {
        for (ItemCarrito item : items) {
            if (item.getLibro() != null && isbn.equals(item.getLibro().getIsbn())) {
                return item;
            }
        }
        return null;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}