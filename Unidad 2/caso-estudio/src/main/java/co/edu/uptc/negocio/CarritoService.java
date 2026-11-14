package co.edu.uptc.negocio;

import java.util.Optional;

public class CarritoService {

    private final LibroRepositorio libroRepositorio;
    private final CarritoRepositorio carritoRepositorio;
    private final BitacoraRepositorio bitacora;

    public CarritoService(LibroRepositorio libroRepositorio, CarritoRepositorio carritoRepositorio,
                          BitacoraRepositorio bitacora) {
        this.libroRepositorio = libroRepositorio;
        this.carritoRepositorio = carritoRepositorio;
        this.bitacora = bitacora;
    }

    public Carrito obtenerCarrito(Cliente cliente) {
        exigirCliente(cliente);
        return carritoRepositorio.buscarPorCliente(cliente.getCorreoElectronico())
                .orElseGet(() -> new Carrito(cliente.getCorreoElectronico()));
    }

    public void agregar(Carrito carrito, String isbn, int cantidad) throws ValidacionException {
        if (carrito == null) {
            throw new ValidacionException("No existe un carrito activo.");
        }
        if (cantidad <= 0) {
            throw new ValidacionException("La cantidad debe ser mayor a 0.");
        }
        Libro libro = libroRepositorio.buscarPorIsbn(isbn)
                .orElseThrow(() -> new ValidacionException("El libro seleccionado ya no existe en el catálogo."));
        CarritoItem existente = carrito.buscarItem(isbn);
        int cantidadFinal = cantidad + (existente == null ? 0 : existente.getCantidad());
        if (cantidadFinal > libro.getStock()) {
            throw new ValidacionException("Stock insuficiente: solo hay " + libro.getStock()
                    + " unidad(es) disponible(s) de \"" + libro.getTitulo() + "\".");
        }
        if (existente == null) {
            carrito.agregar(new CarritoItem(libro.getIsbn(), libro.getTitulo(), libro.getFormato(),
                    libro.getPrecioBase(), libro.getPorcentajeDescuento(), libro.getImpuestoIVA(), cantidad));
        } else {
            existente.aumentarCantidad(cantidad);
        }
        guardar(carrito);
        bitacora.registrar("AGREGAR_CARRITO", "cliente=" + carrito.getCorreoCliente()
                + ", ISBN=" + isbn + ", cantidad=" + cantidad);
    }

    public void aumentar(Carrito carrito, String isbn) throws ValidacionException {
        Libro libro = libroRepositorio.buscarPorIsbn(isbn)
                .orElseThrow(() -> new ValidacionException("El libro ya no existe en el catálogo."));
        CarritoItem item = exigirItem(carrito, isbn);
        if (item.getCantidad() + 1 > libro.getStock()) {
            throw new ValidacionException("No hay más unidades disponibles de \"" + libro.getTitulo() + "\".");
        }
        carrito.aumentar(isbn);
        guardar(carrito);
    }

    public void disminuir(Carrito carrito, String isbn) throws ValidacionException {
        exigirItem(carrito, isbn);
        carrito.disminuir(isbn);
        guardar(carrito);
    }

    public void eliminar(Carrito carrito, String isbn) throws ValidacionException {
        exigirItem(carrito, isbn);
        carrito.eliminar(isbn);
        guardar(carrito);
    }

    public void vaciar(Carrito carrito) {
        if (carrito == null) {
            return;
        }
        carrito.vaciar();
        guardar(carrito);
    }


    public void refrescarPrecios(Carrito carrito) throws ValidacionException {
        if (carrito == null) {
            return;
        }
        for (CarritoItem item : carrito.getItems()) {
            Libro libro = libroRepositorio.buscarPorIsbn(item.getIsbn())
                    .orElseThrow(() -> new ValidacionException("El libro \"" + item.getTitulo()
                            + "\" ya no existe en el catálogo."));
            item.actualizarDatos(libro.getTitulo(), libro.getFormato(), libro.getPrecioBase(),
                    libro.getPorcentajeDescuento(), libro.getImpuestoIVA());
        }
        guardar(carrito);
    }

    public void guardar(Carrito carrito) {
        if (carrito == null) {
            return;
        }
        if (carrito.getItems().isEmpty()) {
            carritoRepositorio.eliminar(carrito.getCorreoCliente());
        } else {
            carritoRepositorio.guardar(carrito);
        }
    }

    private CarritoItem exigirItem(Carrito carrito, String isbn) throws ValidacionException {
        if (carrito == null) {
            throw new ValidacionException("No existe un carrito activo.");
        }
        CarritoItem item = carrito.buscarItem(isbn);
        if (item == null) {
            throw new ValidacionException("El libro no está en el carrito.");
        }
        return item;
    }

    private void exigirCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Debe indicar el cliente.");
        }
    }
}
