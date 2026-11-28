package persistencia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import controller.ConsultaVentas;
import controller.ControladorCompras;
import controller.ControladorLibro;
import model.CarritoCompras;
import model.Cliente;
import model.ClienteRegular;
import model.Compra;
import model.FormatoLibro;
import model.Libro;
import service.ResultadoCompra;

class FlujosPersistenciaTest {

    @TempDir
    Path temp;

    @Test
    void clientesUltimosRespetaLimiteYOrden() {
        probarClientes(new RepositorioClientesArchivoPlano(ruta("clientes.txt")), "plano");
        probarClientes(new RepositorioClientesJson(ruta("clientes.json")), "JSON");
        probarClientes(new RepositorioClientesBD(ruta("clientes.db")), "BD");
    }

    @Test
    void compraSobreviveReinicioEnLosTresAlmacenamientos() {
        probarCompra(new RepositorioLibrosArchivoPlano(ruta("libros.txt")),
                new RepositorioComprasArchivoPlano(ruta("compras.txt")), "plano");
        probarCompra(new RepositorioLibrosJson(ruta("libros.json")),
                new RepositorioComprasJson(ruta("compras.json")), "JSON");
        probarCompra(new RepositorioLibrosBD(ruta("tienda.db")),
                new RepositorioComprasBD(ruta("tienda.db")), "BD");
    }

    @Test
    void noEliminaLibroConVentas() {
        IRepositorio<Compra> repoCompras =
                new RepositorioComprasArchivoPlano(ruta("ventas_compras.txt"));
        ConsultaVentas consultaVentas = new ConsultaVentas(repoCompras);
        ControladorLibro libros = new ControladorLibro(
                new RepositorioLibrosArchivoPlano(ruta("ventas_libros.txt")), null, consultaVentas);
        Libro libro = libro("VENTA-1", 5);
        assertTrue(libros.registrar(libro));

        ControladorCompras compras = new ControladorCompras(repoCompras, libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 1);
        assertTrue(compras.finalizarCompra(
                new ClienteRegular("Cliente venta", "venta@correo.com", "1"),
                carrito, "Efectivo").isExitoso());

        assertFalse(libros.eliminar("VENTA-1"));
    }

    @Test
    void falloAlGuardarNoActualizaStockNiLista() {
        ControladorLibro libros = new ControladorLibro(
                new RepositorioLibrosArchivoPlano(ruta("fallo_libros.txt")));
        Libro libro = libro("FALLO-1", 5);
        assertTrue(libros.registrar(libro));

        ControladorCompras compras = new ControladorCompras(
                new RepositorioComprasFallido(), libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 2);

        ResultadoCompra resultado = compras.finalizarCompra(
                new ClienteRegular("Cliente fallo", "fallo@correo.com", "1"),
                carrito, "Efectivo");

        assertFalse(resultado.isExitoso());
        assertEquals(0, compras.cantidad());
        assertEquals(5, libros.buscar("FALLO-1").getCantidadDisponible());
    }

    private void probarClientes(IRepositorio<Cliente> repo, String nombre) {
        for (int i = 0; i < 12; i++) {
            repo.insertar(new ClienteRegular("Cliente " + i,
                    "cliente" + i + "@correo.com", "1"));
        }

        IRepositorio<Cliente> recargado = nuevoClientes(nombre);
        assertEquals(12, recargado.contar(), nombre + ": conteo");
        List<Cliente> ultimos = recargado.listarUltimos(10);
        assertEquals(10, ultimos.size(), nombre + ": limite");
        assertEquals("cliente11@correo.com", ultimos.get(0).getCorreo(),
                nombre + ": orden");
        assertEquals("cliente2@correo.com", ultimos.get(9).getCorreo(),
                nombre + ": orden final");
    }

    private void probarCompra(IRepositorio<Libro> repoLibros,
            IRepositorio<Compra> repoCompras, String nombre) {
        ControladorLibro libros = new ControladorLibro(repoLibros);
        Libro libro = libro("COMPRA-1", 5);
        assertTrue(libros.registrar(libro), nombre + ": registro libro");

        ControladorCompras compras = new ControladorCompras(repoCompras, libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 2);
        assertTrue(compras.finalizarCompra(
                new ClienteRegular("Cliente compra", "compra@correo.com", "1"),
                carrito, "Efectivo").isExitoso(), nombre + ": compra");

        ControladorLibro librosReiniciado = new ControladorLibro(nuevoLibros(nombre));
        ControladorCompras comprasReiniciado = new ControladorCompras(
                nuevoCompras(nombre), librosReiniciado);

        assertEquals(1, comprasReiniciado.cantidad(), nombre + ": compra persistida");
        assertEquals(1, comprasReiniciado.listarPorCliente("compra@correo.com").size(),
                nombre + ": historial");
        assertEquals(3, librosReiniciado.buscar("COMPRA-1").getCantidadDisponible(),
                nombre + ": stock");
    }

    private IRepositorio<Cliente> nuevoClientes(String nombre) {
        if (nombre.equals("plano")) {
            return new RepositorioClientesArchivoPlano(ruta("clientes.txt"));
        }
        if (nombre.equals("JSON")) {
            return new RepositorioClientesJson(ruta("clientes.json"));
        }
        return new RepositorioClientesBD(ruta("clientes.db"));
    }

    private IRepositorio<Libro> nuevoLibros(String nombre) {
        if (nombre.equals("plano")) {
            return new RepositorioLibrosArchivoPlano(ruta("libros.txt"));
        }
        if (nombre.equals("JSON")) {
            return new RepositorioLibrosJson(ruta("libros.json"));
        }
        return new RepositorioLibrosBD(ruta("tienda.db"));
    }

    private IRepositorio<Compra> nuevoCompras(String nombre) {
        if (nombre.equals("plano")) {
            return new RepositorioComprasArchivoPlano(ruta("compras.txt"));
        }
        if (nombre.equals("JSON")) {
            return new RepositorioComprasJson(ruta("compras.json"));
        }
        return new RepositorioComprasBD(ruta("tienda.db"));
    }

    private String ruta(String archivo) {
        return temp.resolve(archivo).toString();
    }

    private Libro libro(String isbn, int stock) {
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo("Libro " + isbn);
        libro.setAutor("Autor " + isbn);
        libro.setPrecio(100);
        libro.setPorcentajeIva(19);
        libro.setCantidadDisponible(stock);
        libro.setFormato(FormatoLibro.FISICO);
        return libro;
    }

    private static class RepositorioComprasFallido implements IRepositorio<Compra> {

        @Override
        public void insertar(Compra elemento) {
            throw new PersistenciaException("fallo simulado");
        }

        @Override
        public List<Compra> listarUltimos(int limite) {
            return new ArrayList<Compra>();
        }

        @Override
        public List<Compra> listarPagina(int inicio, int cantidad) {
            return new ArrayList<Compra>();
        }

        @Override
        public int contar() {
            return 0;
        }

        @Override
        public List<Compra> buscar(String criterio) {
            return new ArrayList<Compra>();
        }

        @Override
        public List<Compra> listar() {
            return new ArrayList<Compra>();
        }

        @Override
        public void agregar(Compra elemento) {
        }

        @Override
        public boolean eliminar(Compra elemento) {
            return false;
        }

        @Override
        public void cargar() {
        }

        @Override
        public void guardar() {
        }
    }
}
