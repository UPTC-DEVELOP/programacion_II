package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import model.CarritoCompras;
import model.Cliente;
import model.ClientePremium;
import model.ClienteRegular;
import model.Compra;
import model.FormatoLibro;
import model.Libro;
import persistencia.RepositorioComprasArchivoPlano;
import persistencia.RepositorioLibrosArchivoPlano;
import service.ResultadoCompra;

class ReglasNegocioTest {

    @TempDir
    Path temp;

    @Test
    void noFinalizaCompraSinStock() {
        ControladorLibro libros = librosCon(RepositorioLibrosArchivoPlano::new);
        Libro libro = libro("STOCK-1", 1);
        assertTrue(libros.registrar(libro));

        ControladorCompras compras = new ControladorCompras(
                new RepositorioComprasArchivoPlano(ruta("compras_stock.txt")), libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 2);

        ResultadoCompra resultado = compras.finalizarCompra(
                new ClienteRegular("Cliente stock", "stock@correo.com", "1234"),
                carrito, "Efectivo");

        assertFalse(resultado.isExitoso());
        assertEquals(0, compras.cantidad());
        assertEquals(1, libros.buscar("STOCK-1").getCantidadDisponible());
    }

    @Test
    void aplicaDescuentoPremiumEnLaCompra() {
        ControladorLibro libros = librosCon(RepositorioLibrosArchivoPlano::new);
        Libro libro = libro("PREMIUM-1", 5);
        assertTrue(libros.registrar(libro));

        ControladorCompras compras = new ControladorCompras(
                new RepositorioComprasArchivoPlano(ruta("compras_premium.txt")), libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 1);

        ResultadoCompra resultado = compras.finalizarCompra(
                new ClientePremium("Cliente premium", "premium@correo.com", "1234"),
                carrito, "Efectivo");

        assertTrue(resultado.isExitoso());
        assertEquals(109.0, resultado.getCompra().getTotal(), 0.01);
    }

    @Test
    void rechazaCompraSinClienteOSinMetodoPago() {
        ControladorLibro libros = librosCon(RepositorioLibrosArchivoPlano::new);
        Libro libro = libro("VALID-1", 5);
        assertTrue(libros.registrar(libro));

        ControladorCompras compras = new ControladorCompras(
                new RepositorioComprasArchivoPlano(ruta("compras_valid.txt")), libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 1);

        assertFalse(compras.finalizarCompra(null, carrito, "Efectivo").isExitoso());
        assertFalse(compras.finalizarCompra(
                new ClienteRegular("Cliente", "cliente@correo.com", "1234"),
                carrito, "").isExitoso());
        assertFalse(compras.finalizarCompra(
                new ClienteRegular("Cliente", "cliente@correo.com", "1234"),
                new CarritoCompras(), "Efectivo").isExitoso());
    }

    @Test
    void rechazaClienteDuplicadoEInicioSesionInvalido() {
        ControladorCliente clientes = new ControladorCliente(
                new persistencia.RepositorioClientesArchivoPlano(ruta("clientes_valid.txt")));
        assertTrue(clientes.registrarCliente("Regular", "Cliente Uno",
                "uno@correo.com", "Calle", "300", "1234"));
        assertFalse(clientes.registrarCliente("Regular", "Cliente Dos",
                "uno@correo.com", "Calle", "301", "5678"));

        Cliente cliente = clientes.iniciarSesion("uno@correo.com", "1234");
        assertNotNull(cliente);
        assertNull(clientes.iniciarSesion("uno@correo.com", "incorrecta"));
    }

    @Test
    void permiteEliminarLibroSinVentas() {
        RepositorioComprasArchivoPlano compras = new RepositorioComprasArchivoPlano(
                ruta("compras_sin_ventas.txt"));
        ControladorLibro libros = new ControladorLibro(
                new RepositorioLibrosArchivoPlano(ruta("libros_sin_ventas.txt")),
                null, new ConsultaVentas(compras));
        Libro libro = libro("SIN-VENTA-1", 5);
        assertTrue(libros.registrar(libro));
        assertTrue(libros.eliminar("SIN-VENTA-1"));
    }

    @Test
    void actualizaInventarioYRechazaCantidadMayorAlStock() {
        ControladorLibro libros = librosCon(RepositorioLibrosArchivoPlano::new);
        Libro libro = libro("INV-1", 5);
        assertTrue(libros.registrar(libro));

        assertTrue(libros.actualizarInventario("INV-1", 2));
        assertEquals(3, libros.buscar("INV-1").getCantidadDisponible());
        assertFalse(libros.actualizarInventario("INV-1", 10));
        assertEquals(3, libros.buscar("INV-1").getCantidadDisponible());
    }

    private ControladorLibro librosCon(
            java.util.function.Function<String, RepositorioLibrosArchivoPlano> creador) {
        return new ControladorLibro(creador.apply(ruta("libros.txt")));
    }

    private Libro libro(String isbn, int stock) {
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo("Libro " + isbn);
        libro.setAutor("Autor " + isbn);
        libro.setAnioPublicacion(2020);
        libro.setNumeroPaginas(100);
        libro.setPrecio(100);
        libro.setPorcentajeIva(19);
        libro.setCantidadDisponible(stock);
        libro.setFormato(FormatoLibro.FISICO);
        return libro;
    }

    private String ruta(String archivo) {
        return temp.resolve(archivo).toString();
    }
}
