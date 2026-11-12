import co.edu.uptc.controller.HistorialController;
import co.edu.uptc.persistence.CarritoRepository;
import co.edu.uptc.persistence.LibroRepository;
import co.edu.uptc.persistence.PedidoRepository;
import co.edu.uptc.persistence.RegistroOperaciones;
import co.edu.uptc.persistence.json.CarritoJsonRepository;
import co.edu.uptc.persistence.json.LibroJsonRepository;
import co.edu.uptc.persistence.json.PedidoJsonRepository;
import co.edu.uptc.persistence.txt.RegistroOperacionesTxt;
import co.edu.uptc.service.CompraService;
import co.edu.uptc.service.DescuentoClienteFrecuente;
import co.edu.uptc.service.Sesion;
import co.edu.uptc.service.ReciboService;
import co.edu.uptc.view.PanelHistorial;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class AdminHistorySmoke {
    public static void main(String[] args) throws Exception {
        Path root = Paths.get(".work", "admin-history-data");
        if (Files.exists(root)) Files.walk(root).sorted(Comparator.reverseOrder()).forEach(p -> { try { Files.delete(p); } catch (Exception e) { throw new RuntimeException(e); } });
        Files.createDirectories(root);
        LibroRepository libros = new LibroJsonRepository(root.resolve("libros.json"));
        PedidoRepository pedidos = new PedidoJsonRepository(root.resolve("pedidos.json"));
        CarritoRepository carritos = new CarritoJsonRepository(root.resolve("carritos.json"));
        RegistroOperaciones registro = new RegistroOperacionesTxt(root.resolve("registro.txt"));
        Sesion sesion = new Sesion();
        CompraService compras = new CompraService(libros, pedidos, carritos, registro, new ReciboService(root.resolve("recibos")), sesion, new DescuentoClienteFrecuente());
        PanelHistorial panel = new PanelHistorial();
        new HistorialController(panel, compras, true);
        if (panel.getModelo().getRowCount() != 0) throw new AssertionError("historial inicial");
        System.out.println("ADMIN_HISTORY_OK");
    }
}
