import co.edu.uptc.negocio.*;
import java.nio.file.*;

public class SmokeTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void main(String[] args) throws Exception {
        Path root = Paths.get(".work", "smoke-data");
        if (Files.exists(root)) {
            Files.walk(root).sorted(java.util.Comparator.reverseOrder()).forEach(p -> {
                try { Files.delete(p); } catch (Exception e) { throw new RuntimeException(e); }
            });
        }
        Files.createDirectories(root.resolve("recibos"));
        BitacoraRepositorio bitacora = new BitacoraRepositorioTxt(root.resolve("bitacora.txt"));
        LibroRepositorio libros = new LibroRepositorioJson(root.resolve("libros.json"));
        ClienteRepositorio clientes = new ClienteRepositorioJson(root.resolve("clientes.json"));
        CarritoRepositorio carritos = new CarritoRepositorioJson(root.resolve("carritos.json"));
        CompraRepositorio compras = new CompraRepositorioJson(root.resolve("compras.json"));
        ClienteService clientesService = new ClienteService(clientes, bitacora);
        Cliente cliente = clientesService.registrarCliente(
                "Cliente Premium", "premium@test.com", "Calle 1", "3001234567",
                TipoCliente.PREMIUM, "secreto");
        check(clientesService.autenticar("PREMIUM@TEST.COM", "secreto").esPremium(), "login Premium");

        CarritoService carritoService = new CarritoService(libros, carritos, bitacora);
        Carrito carrito = carritoService.obtenerCarrito(cliente);
        Libro libro = libros.obtenerTodos().get(0);
        int stockInicial = libro.getStock();
        carritoService.agregar(carrito, libro.getIsbn(), 2);
        check(carrito.getItems().size() == 1 && carrito.getItems().get(0).getCantidad() == 2, "agregar carrito");
        carritoService.aumentar(carrito, libro.getIsbn());
        check(carrito.getItems().get(0).getCantidad() == 3, "aumentar carrito");
        carritoService.disminuir(carrito, libro.getIsbn());
        check(carrito.getItems().get(0).getCantidad() == 2, "disminuir carrito");
        boolean stockRechazado = false;
        try { carritoService.agregar(carrito, libro.getIsbn(), stockInicial); }
        catch (ValidacionException expected) { stockRechazado = true; }
        check(stockRechazado, "validacion de stock");

        ReciboService recibos = new ReciboService(root.resolve("recibos"));
        CompraService compraService = new CompraService(libros, compras, carritos, recibos, bitacora);
        Compra compra = compraService.confirmarCompra(cliente, carrito, MetodoPago.TARJETA);
        check(compra.getDescuentoPremium() > 0, "descuento Premium");
        check(libros.buscarPorIsbn(libro.getIsbn()).get().getStock() == stockInicial - 2, "actualizacion stock");
        check(compraService.listarPorCliente(cliente.getCorreoElectronico()).size() == 1, "historial");
        check(carritoService.obtenerCarrito(cliente).getItems().isEmpty(), "vaciado carrito");
        check(Files.exists(root.resolve("recibos").resolve("recibo-" + compra.getId() + ".txt")), "recibo TXT");
        System.out.println("SMOKE_TEST_OK");
    }
}
