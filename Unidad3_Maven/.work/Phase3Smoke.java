import co.edu.uptc.model.*;
import co.edu.uptc.persistence.*;
import co.edu.uptc.persistence.json.*;
import co.edu.uptc.persistence.txt.*;
import java.nio.file.*;
import java.util.*;

public class Phase3Smoke {
    public static void main(String[] args) throws Exception {
        Path root = Paths.get(".work", "phase3-data");
        if (Files.exists(root)) Files.walk(root).sorted(Comparator.reverseOrder()).forEach(p -> { try { Files.delete(p); } catch (Exception e) { throw new RuntimeException(e); } });
        Files.createDirectories(root);
        LibroRepository libros = new LibroJsonRepository(root.resolve("libros.json"));
        UsuarioRepository usuarios = new UsuarioJsonRepository(root.resolve("clientes.json"));
        if (libros.listar().size() < 6 || usuarios.listar().size() != 3) throw new AssertionError("semillas");
        Usuario ana = usuarios.buscarPorCorreo("ana@correo.com").get();
        CarritoRepository carritos = new CarritoJsonRepository(root.resolve("carritos.json"));
        Carrito carrito = new Carrito(ana.getCorreo());
        Libro libro = libros.listar().get(0);
        carrito.agregar(new ItemCarrito(libro.getIsbn(), libro.getTitulo(), libro.getPrecioBase(), libro.getPorcentajeDescuento(), libro.getImpuestoIVA(), 2));
        carritos.guardar(carrito);
        if (!carritos.buscarPorCliente(ana.getCorreo()).isPresent()) throw new AssertionError("carrito");
        RegistroOperaciones registro = new RegistroOperacionesTxt(root.resolve("registro_operaciones.txt"));
        registro.registrar("INFO", ana.getCorreo(), "prueba de persistencia");
        if (!Files.exists(root.resolve("registro_operaciones.txt"))) throw new AssertionError("registro TXT");
        System.out.println("PHASE3_PERSISTENCE_OK");
    }
}
