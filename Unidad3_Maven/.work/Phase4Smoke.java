import co.edu.uptc.model.*;
import co.edu.uptc.persistence.*;
import co.edu.uptc.persistence.json.*;
import co.edu.uptc.persistence.txt.*;
import co.edu.uptc.service.*;
import java.nio.file.*;
import java.util.*;

public class Phase4Smoke {
    public static void main(String[] args) throws Exception {
        Path root = Paths.get(".work", "phase4-data");
        if (Files.exists(root)) Files.walk(root).sorted(Comparator.reverseOrder()).forEach(p -> { try { Files.delete(p); } catch (Exception e) { throw new RuntimeException(e); } });
        Files.createDirectories(root);
        LibroRepository libros = new LibroJsonRepository(root.resolve("libros.json"));
        UsuarioRepository usuarios = new UsuarioJsonRepository(root.resolve("clientes.json"));
        PedidoRepository pedidos = new PedidoJsonRepository(root.resolve("pedidos.json"));
        CarritoRepository carritos = new CarritoJsonRepository(root.resolve("carritos.json"));
        RegistroOperaciones registro = new RegistroOperacionesTxt(root.resolve("registro.txt"));
        Sesion sesion = new Sesion();
        Cliente cliente = (Cliente) usuarios.buscarPorCorreo("carlos@correo.com").get();
        sesion.iniciar(cliente);
        ReciboService recibos = new ReciboService(root.resolve("recibos"));
        CompraService compras = new CompraService(libros, pedidos, carritos, registro, recibos, sesion, new DescuentoClienteFrecuente());
        Libro libro = libros.buscarPorIsbn("978-958-00-0001-1").get();
        int stockInicial = libro.getStock();
        Carrito carrito = new Carrito(cliente.getCorreo());
        carrito.agregar(new ItemCarrito(libro.getIsbn(), libro.getTitulo(), libro.getPrecioBase(), libro.getPorcentajeDescuento(), libro.getImpuestoIVA(), 2));
        carritos.guardar(carrito);
        Pedido primero = compras.finalizarCompra(carrito, MetodoPago.EFECTIVO);
        if (Math.round(primero.getSubtotal()) != 100000 || Math.round(primero.getImpuestos()) != 19000 || Math.round(primero.getDescuento()) != 11900 || Math.round(primero.getTotal()) != 107100) throw new AssertionError("caso premium");
        if (libros.buscarPorIsbn(libro.getIsbn()).get().getStock() != stockInicial - 2) throw new AssertionError("stock");
        if (!Files.exists(root.resolve("recibos").resolve("recibo_1.txt"))) throw new AssertionError("recibo");
        for (int i = 0; i < 3; i++) compras.registrarPedidoRapido("978-958-00-0002-8", 1, MetodoPago.PSE);
        Pedido cuarto = compras.registrarPedidoRapido("978-958-00-0002-8", 1, MetodoPago.PSE);
        if (cuarto.getDescuento() <= 0 || pedidos.listarPorCliente(cliente.getCorreo()).size() != 5) throw new AssertionError("frecuente");
        System.out.println("PHASE4_SERVICES_OK");
    }
}
