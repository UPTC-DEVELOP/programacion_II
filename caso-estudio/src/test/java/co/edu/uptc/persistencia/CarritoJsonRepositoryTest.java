package co.edu.uptc.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * Pruebas de integración del repositorio JSON de carritos. Cada prueba usa un
 * archivo temporal para no modificar data/carritos.json del proyecto.
 */
public class CarritoJsonRepositoryTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private CarritoJsonRepository repositorio;
    private Path rutaCarritos;

    @Before
    public void setUp() {
        rutaCarritos = carpetaTemporal.getRoot().toPath()
                .resolve("data").resolve("carritos.json");
        repositorio = new CarritoJsonRepository(rutaCarritos.toString());
    }

    @Test
    public void testInicializaArchivoConEstructuraVacia() throws Exception {
        assertTrue(Files.exists(rutaCarritos));

        String contenido = Files.readString(rutaCarritos, StandardCharsets.UTF_8);
        assertTrue(contenido.contains("carritos"));
        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    public void testGuardarYConsultarCarrito() {
        CarritoCompras carrito = crearCarrito(1001, TipoCliente.PREMIUM,
                "9783161484100", "Clean Code", 2, 100.0, 0.19);

        repositorio.guardarCarrito(carrito);

        CarritoCompras recuperado = repositorio.obtenerCarritoPorCliente(1001);
        assertNotNull(recuperado);
        assertEquals(1001, recuperado.getIdCliente());
        assertEquals(TipoCliente.PREMIUM, recuperado.getTipoCliente());
        assertEquals(1, recuperado.obtenerItems().size());

        ItemCarrito item = recuperado.obtenerItems().get(0);
        assertEquals("9783161484100", item.getIsbn());
        assertEquals("Clean Code", item.getTitulo());
        assertEquals(2, item.getCantidad());
        assertEquals(100.0, item.getPrecioUnitario(), 0.001);
        assertEquals(0.19, item.getTasaIva(), 0.001);
        assertEquals(200.0, item.getSubtotal(), 0.001);
        assertEquals(38.0, item.getIva(), 0.001);
    }

    @Test
    public void testGuardarReemplazaElCarritoDelMismoCliente() {
        repositorio.guardarCarrito(crearCarrito(1001, TipoCliente.REGULAR,
                "9783161484100", "Clean Code", 1, 100.0, 0.19));
        repositorio.guardarCarrito(crearCarrito(1001, TipoCliente.REGULAR,
                "9783161484100", "Clean Code", 3, 100.0, 0.19));

        List<CarritoCompras> carritos = repositorio.listarTodos();
        assertEquals(1, carritos.size());
        assertEquals(3, carritos.get(0).obtenerItems().get(0).getCantidad());
    }

    @Test
    public void testListarTodosConservaVariosClientes() {
        repositorio.guardarCarrito(crearCarrito(1001, TipoCliente.REGULAR,
                "9783161484100", "Clean Code", 1, 100.0, 0.19));
        repositorio.guardarCarrito(crearCarrito(1002, TipoCliente.PREMIUM,
                "9780134685991", "Effective Java", 2, 50.0, 0.19));

        List<CarritoCompras> carritos = repositorio.listarTodos();
        assertEquals(2, carritos.size());
    }

    @Test
    public void testEliminarCarrito() {
        repositorio.guardarCarrito(crearCarrito(1001, TipoCliente.REGULAR,
                "9783161484100", "Clean Code", 1, 100.0, 0.19));

        repositorio.eliminarCarrito(1001);

        assertNull(repositorio.obtenerCarritoPorCliente(1001));
        assertTrue(repositorio.listarTodos().isEmpty());
    }

    @Test
    public void testPersisteTotalesConDescuentoPremium() throws Exception {
        repositorio.guardarCarrito(crearCarrito(1001, TipoCliente.PREMIUM,
                "9783161484100", "Clean Code", 2, 100.0, 0.19));

        String contenido = Files.readString(rutaCarritos, StandardCharsets.UTF_8);
        JsonObject raiz = JsonParser.parseString(contenido).getAsJsonObject();
        JsonObject carrito = raiz.getAsJsonArray("carritos").get(0).getAsJsonObject();

        assertEquals(200.0, carrito.get("subtotalGeneral").getAsDouble(), 0.001);
        assertEquals(38.0, carrito.get("totalImpuestos").getAsDouble(), 0.001);
        assertEquals(20.0, carrito.get("descuentoAplicado").getAsDouble(), 0.001);
        assertEquals(218.0, carrito.get("totalFinal").getAsDouble(), 0.001);
    }

    private CarritoCompras crearCarrito(int idCliente, TipoCliente tipoCliente,
                                        String isbn, String titulo, int cantidad,
                                        double precioUnitario, double tasaIva) {
        CarritoCompras carrito = new CarritoCompras(idCliente, idCliente);
        carrito.setTipoCliente(tipoCliente);

        ItemCarrito item = new ItemCarrito(isbn, cantidad, precioUnitario, tasaIva);
        item.setTitulo(titulo);
        carrito.agregarItem(item);
        return carrito;
    }
}
