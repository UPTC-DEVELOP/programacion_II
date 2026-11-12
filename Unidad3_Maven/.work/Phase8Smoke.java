import co.edu.uptc.exception.*;
import co.edu.uptc.model.*;
import co.edu.uptc.persistence.*;
import co.edu.uptc.persistence.json.*;
import co.edu.uptc.persistence.txt.*;
import co.edu.uptc.service.*;
import java.nio.file.*;
import java.util.*;

public class Phase8Smoke {
    public static void main(String[] args) throws Exception {
        Path root = Paths.get(".work", "phase8-data");
        if (Files.exists(root)) Files.walk(root).sorted(Comparator.reverseOrder()).forEach(p -> { try { Files.delete(p); } catch (Exception e) { throw new RuntimeException(e); } });
        Files.createDirectories(root);
        LibroRepository libros = new LibroJsonRepository(root.resolve("libros.json"));
        UsuarioRepository usuarios = new UsuarioJsonRepository(root.resolve("clientes.json"));
        PedidoRepository pedidos = new PedidoJsonRepository(root.resolve("pedidos.json"));
        CarritoRepository carritos = new CarritoJsonRepository(root.resolve("carritos.json"));
        ResenaRepository resenas = new ResenaJsonRepository(root.resolve("resenas.json"));
        RegistroOperaciones registro = new RegistroOperacionesTxt(root.resolve("registro.txt"));
        Sesion sesion = new Sesion();
        Cliente cliente = (Cliente) usuarios.buscarPorCorreo("ana@correo.com").get();
        sesion.iniciar(cliente);
        CompraService compraService = new CompraService(libros, pedidos, carritos, registro,
                new ReciboService(root.resolve("recibos")), sesion, new DescuentoClienteFrecuente());
        ResenaService resenaService = new ResenaService(resenas, pedidos, registro);
        boolean noCompradoRechazado = false;
        try { resenaService.registrar("978-958-00-0006-6", cliente.getCorreo(), 5, "No comprado"); }
        catch (ValidacionException esperado) { noCompradoRechazado = true; }
        if (!noCompradoRechazado) throw new AssertionError("reseña sin compra");
        Pedido primerPedido = compraService.registrarPedidoRapido("978-958-00-0001-1", 1, MetodoPago.EFECTIVO);
        Resena resena = resenaService.registrar("978-958-00-0001-1", cliente.getCorreo(), 5, "Excelente libro");
        if (resena.getCalificacion() != 5 || resenas.listar().size() != 1) throw new AssertionError("reseña guardada");
        boolean duplicadaRechazada = false;
        try { resenaService.registrar("978-958-00-0001-1", cliente.getCorreo(), 4, "Otra opinión"); }
        catch (ValidacionException esperado) { duplicadaRechazada = true; }
        if (!duplicadaRechazada) throw new AssertionError("reseña duplicada");
        compraService.registrarPedidoRapido("978-958-00-0002-8", 1, MetodoPago.EFECTIVO);
        compraService.registrarPedidoRapido("978-958-00-0002-8", 1, MetodoPago.EFECTIVO);
        Pedido cuarto = compraService.registrarPedidoRapido("978-958-00-0002-8", 1, MetodoPago.EFECTIVO);
        if (Math.round(cuarto.getDescuento()) != 2363) throw new AssertionError("descuento frecuente");
        System.out.println("PHASE8_REVIEW_FREQUENT_OK");
    }
}
