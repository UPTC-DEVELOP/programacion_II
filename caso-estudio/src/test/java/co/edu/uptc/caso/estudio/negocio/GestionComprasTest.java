package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.model.enums.MetodoPago;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;
import co.edu.uptc.caso.estudio.negocio.dto.PagoDto;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCompra;
import co.edu.uptc.caso.estudio.persistence.IRepositorioLibros;
import co.edu.uptc.caso.estudio.persistence.IRepositorioUsuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

// Pruebas unitarias para el carrito de compras, pagos y finalizacion de pedidos
public class GestionComprasTest extends TestCase {

    private GestionUsuariosImpl gestionUsuarios;
    private GestionLibrosImpl gestionLibros;
    private GestionComprasImpl gestionCompras;
    private RepoCompraMemoria repoCompraMySQL;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Armamos los repositorios en memoria para aislar las pruebas
        RepoUsuarioMemoria repoUser = new RepoUsuarioMemoria();
        RepoLibrosMemoria repoLibros = new RepoLibrosMemoria();
        RepoCarritoMemoria repoCarritoMySQL = new RepoCarritoMemoria();
        RepoCarritoMemoria repoCarritoJson = new RepoCarritoMemoria();
        repoCompraMySQL = new RepoCompraMemoria();
        RepoCompraMemoria repoCompraJson = new RepoCompraMemoria();

        gestionUsuarios = new GestionUsuariosImpl(repoUser, repoUser, repoCarritoJson, null);
        gestionLibros = new GestionLibrosImpl(repoLibros, repoLibros, null);
        gestionCompras = new GestionComprasImpl(
                repoCarritoMySQL, repoCarritoJson,
                repoCompraMySQL, repoCompraJson,
                gestionUsuarios, gestionLibros, null
        );

        // Creamos un cliente con sesion iniciada y un libro con 5 unidades disponibles
        Cliente cliente = FabricaClientes.crear("2001", "Juan Lopez", "juan@uptc.edu.co",
                "Cra 5 # 12-30", "Boyaca", "Tunja", "3209876543", "REGULAR", "clave123");
        gestionUsuarios.registrarCliente(cliente);
        gestionUsuarios.iniciarSesion(new CredencialDto("juan@uptc.edu.co", "clave123"));

        Libro libro = new Libro("978-99", "Algoritmos en Java", "Sedgewick", 2011,
                "Tecnologia", "Pearson", 955, 100000, 5, "Físico", 19);
        gestionLibros.registrarLibro(libro);
    }

    // Verifica que al agregar un libro al carrito se guarde la cantidad y se calcule el total
    public void testAgregarAlCarritoYCalcularTotal() {
        Libro libro = gestionLibros.buscarLibroPorIsbn("978-99");
        gestionCompras.agregarAlCarrito(libro, 2);

        Carrito carrito = gestionCompras.obtenerCarritoActual();
        assertFalse("El carrito no debe estar vacio", carrito.estaVacio());
        assertEquals(2, carrito.buscarItem("978-99").getCantidad());
        assertTrue("El total final debe ser mayor a 0", gestionCompras.calcularTotalFinal() > 0);
    }

    // Comprueba que no deje agregar mas unidades de las que hay en stock
    public void testNoPermiteAgregarMasDelStockDisponible() {
        Libro libro = gestionLibros.buscarLibroPorIsbn("978-99"); // Stock = 5
        gestionCompras.agregarAlCarrito(libro, 10);

        Carrito carrito = gestionCompras.obtenerCarritoActual();
        assertTrue("No debe agregar items si superan el stock", carrito.estaVacio());
    }

    // Revisa que al quitar un libro del carrito se elimine bien y el total vuelva a cero
    public void testEliminarItemDelCarrito() {
        Libro libro = gestionLibros.buscarLibroPorIsbn("978-99");
        gestionCompras.agregarAlCarrito(libro, 2);

        gestionCompras.eliminarDelCarrito(java.util.Arrays.asList("978-99"));
        Carrito carrito = gestionCompras.obtenerCarritoActual();
        assertTrue("El carrito debe quedar vacio tras eliminar el unico item", carrito.estaVacio());
        assertEquals(0.0, gestionCompras.calcularTotalFinal(), 0.01);
    }

    // Valida que no se pueda cerrar una compra si el carrito esta vacio
    public void testFinalizarCompraConCarritoVacioFalla() {
        boolean compraOk = gestionCompras.finalizarCompra();
        assertFalse("No debe permitir finalizar una compra sin productos", compraOk);
        assertEquals("No debe registrar ninguna compra en BD", 0, repoCompraMySQL.totalComprasRegistradas);
    }

    // Prueba el flujo completo: agregar libro, registrar pago Nequi, finalizar compra y vaciar carrito
    public void testRegistrarPagoNequiYFinalizarCompra() {
        Libro libro = gestionLibros.buscarLibroPorIsbn("978-99");
        gestionCompras.agregarAlCarrito(libro, 2);

        PagoDto pagoNequi = new PagoDto(
                MetodoPago.TRANSFERENCIA.getEtiqueta(),
                null, null, null, null, "3151234567"
        );

        String errorPago = gestionCompras.registrarPago(pagoNequi);
        assertNull("El pago valido por Nequi no debe retornar error", errorPago);

        boolean compraOk = gestionCompras.finalizarCompra();
        assertTrue("La compra debe finalizar exitosamente", compraOk);
        assertEquals("Debe quedar registrada 1 compra", 1, repoCompraMySQL.totalComprasRegistradas);
        assertTrue("El carrito debe quedar vacio tras comprar", gestionCompras.obtenerCarritoActual().estaVacio());
    }

    // Simula la tabla de usuarios en memoria
    private static class RepoUsuarioMemoria implements IRepositorioUsuario {
        private final Map<String, Cliente> datos = new HashMap<>();
        @Override public boolean guardarUsuario(Cliente c) { datos.put(c.getId(), c); return true; }
        @Override public boolean actualizarUsuario(Cliente c) { datos.put(c.getId(), c); return true; }
        @Override public boolean eliminarUsuario(String id) { return datos.remove(id) != null; }
        @Override public Cliente buscarPorCorreo(String correo) {
            for (Cliente c : datos.values()) if (c.getCorreo().equalsIgnoreCase(correo)) return c;
            return null;
        }
        @Override public Cliente buscarPorId(String id) { return datos.get(id); }
        @Override public List<Cliente> obtenerTodos() { return new ArrayList<>(datos.values()); }
    }

    // Simula el catalogo de libros en memoria
    private static class RepoLibrosMemoria implements IRepositorioLibros {
        private final Map<String, Libro> datos = new HashMap<>();
        @Override public boolean guardarLibro(Libro l) { datos.put(l.getIsbn(), l); return true; }
        @Override public boolean actualizarLibro(Libro l) { datos.put(l.getIsbn(), l); return true; }
        @Override public boolean eliminarLibro(String isbn) { return datos.remove(isbn) != null; }
        @Override public Libro buscarPorIsbn(String isbn) { return datos.get(isbn); }
        @Override public List<Libro> obtenerTodos() { return new ArrayList<>(datos.values()); }
    }

    // Simula los carritos activos en memoria
    private static class RepoCarritoMemoria implements IRepositorioCarrito {
        private final Map<String, Carrito> carritos = new HashMap<>();
        @Override public Carrito obtenerCarritoPorCliente(String id) { return carritos.get(id); }
        @Override public boolean guardarCarrito(Carrito c) { carritos.put(c.getClienteId(), c); return true; }
        @Override public boolean vaciarCarrito(String id) { carritos.remove(id); return true; }
    }

    // Simula el historial de compras registradas
    private static class RepoCompraMemoria implements IRepositorioCompra {
        int totalComprasRegistradas = 0;
        @Override public boolean registrarCompra(Cliente c, Carrito carrito, String metodo) {
            totalComprasRegistradas++;
            return true;
        }
    }
}