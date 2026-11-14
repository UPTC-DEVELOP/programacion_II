package co.edu.uptc.negocio;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class PedidoService {

    private final LibroRepositorio libroRepositorio;
    private final PedidoRepositorio pedidoRepositorio;
    private final BitacoraRepositorio bitacora;

    public PedidoService(LibroRepositorio libroRepositorio,
                         PedidoRepositorio pedidoRepositorio,
                         BitacoraRepositorio bitacora) {
        this.libroRepositorio = libroRepositorio;
        this.pedidoRepositorio = pedidoRepositorio;
        this.bitacora = bitacora;
    }

    public List<Libro> listarProductos() {
        return libroRepositorio.obtenerTodos();
    }

    public List<Pedido> listarHistorial() {
        return pedidoRepositorio.obtenerTodos();
    }

    public Pedido registrarPedido(String isbn, int cantidad) throws ValidacionException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidacionException("Debe seleccionar un producto.");
        }
    
        if (cantidad <= 0) {
            throw new ValidacionException("La cantidad debe ser un número entero mayor a 0.");
        }
        Optional<Libro> encontrado = libroRepositorio.buscarPorIsbn(isbn);
        if (!encontrado.isPresent()) {
            throw new ValidacionException("El producto seleccionado ya no existe en el catálogo.");
        }
        Libro libro = encontrado.get();

    
        if (cantidad > libro.getStock()) {
            throw new ValidacionException("Stock insuficiente: solo hay " + libro.getStock()
                    + " unidad(es) disponible(s) de \"" + libro.getTitulo() + "\".");
        }

        libro.disminuirStock(cantidad);
        Pedido pedido = new Pedido(pedidoRepositorio.siguienteId(), LocalDateTime.now(),
                libro.getIsbn(), libro.getTitulo(), cantidad, libro.calcularPrecioFinal());
        try {
            libroRepositorio.guardar(libro);
            pedidoRepositorio.guardar(pedido);
        } catch (PersistenciaException e) {
            libro.aumentarStock(cantidad); 
            throw e;
        }
        bitacora.registrar("REGISTRAR_PEDIDO", "Pedido #" + pedido.getId() + ", ISBN=" + pedido.getIsbn()
                + ", cantidad=" + cantidad + ", total=" + pedido.getTotal());
        return pedido;
    }

    public Pedido cancelarPedido(long idPedido) throws ValidacionException {
        Optional<Pedido> encontrado = pedidoRepositorio.buscarPorId(idPedido);
        if (!encontrado.isPresent()) {
            throw new ValidacionException("El pedido #" + idPedido + " no existe en el historial.");
        }
        Pedido pedido = encontrado.get();

        Optional<Libro> producto = libroRepositorio.buscarPorIsbn(pedido.getIsbn());
        if (!producto.isPresent()) {
            throw new ValidacionException("No se puede cancelar: el libro del pedido ya no existe.");
        }
        Libro libro = producto.get();

        libro.aumentarStock(pedido.getCantidad()); 
        try {
            libroRepositorio.guardar(libro);
            pedidoRepositorio.eliminar(idPedido);
        } catch (PersistenciaException e) {
            libro.disminuirStock(pedido.getCantidad());
            throw e;
        }
        bitacora.registrar("CANCELAR_PEDIDO", "Pedido #" + idPedido + ", ISBN=" + pedido.getIsbn()
                + ", cantidad devuelta al stock=" + pedido.getCantidad());
        return pedido;
    }
}
