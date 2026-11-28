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
import model.ClienteRegular;
import model.FormatoLibro;
import model.Libro;
import persistencia.RepositorioClientesArchivoPlano;
import persistencia.RepositorioComprasArchivoPlano;
import persistencia.RepositorioLibrosArchivoPlano;
import service.ResultadoCompra;

class CrudCompletoTest {

    @TempDir
    Path temp;

    @Test
    void clientesCrudCompleto() {
        ControladorCliente clientes = new ControladorCliente(
                new RepositorioClientesArchivoPlano(ruta("crud_clientes.txt")));

        assertTrue(clientes.registrarCliente("Regular", "Cliente Uno",
                "uno@correo.com", "Calle 1", "300", "1234"));
        assertTrue(clientes.registrarCliente("Premium", "Cliente Dos",
                "dos@correo.com", "Calle 2", "301", "1234"));
        assertEquals(2, clientes.listar().size());

        Cliente encontrado = clientes.buscar("uno@correo.com");
        assertNotNull(encontrado);
        assertEquals("Cliente Uno", encontrado.getNombreCompleto());

        assertTrue(clientes.actualizarDatos("uno@correo.com",
                "Cliente Uno Actualizado", "Calle 3", "302"));
        assertEquals("Cliente Uno Actualizado",
                clientes.buscar("uno@correo.com").getNombreCompleto());

        assertTrue(clientes.eliminar("dos@correo.com"));
        assertNull(clientes.buscar("dos@correo.com"));
        assertEquals(1, clientes.cantidad());
    }

    @Test
    void librosCrudCompleto() {
        RepositorioComprasArchivoPlano compras = new RepositorioComprasArchivoPlano(
                ruta("crud_compras.txt"));
        ControladorLibro libros = new ControladorLibro(
                new RepositorioLibrosArchivoPlano(ruta("crud_libros.txt")),
                null, new ConsultaVentas(compras));

        Libro libro = libro("CRUD-LIB-1");
        assertTrue(libros.registrar(libro));
        assertNotNull(libros.buscar("CRUD-LIB-1"));
        assertEquals(1, libros.buscarPorCriterio("CRUD").size());

        Libro cambios = libro("CRUD-LIB-1");
        cambios.setTitulo("Titulo actualizado");
        assertTrue(libros.actualizar("CRUD-LIB-1", cambios));
        assertEquals("Titulo actualizado", libros.buscar("CRUD-LIB-1").getTitulo());

        assertTrue(libros.eliminar("CRUD-LIB-1"));
        assertNull(libros.buscar("CRUD-LIB-1"));
    }

    @Test
    void comprasCrudCompleto() {
        ControladorLibro libros = new ControladorLibro(
                new RepositorioLibrosArchivoPlano(ruta("crud_compra_libros.txt")));
        Libro libro = libro("CRUD-COMPRA-1");
        assertTrue(libros.registrar(libro));

        ControladorCompras compras = new ControladorCompras(
                new RepositorioComprasArchivoPlano(ruta("crud_compra_compras.txt")),
                libros);
        CarritoCompras carrito = new CarritoCompras();
        carrito.agregarLibro(libro, 1);
        assertEquals(1, carrito.getItems().size());

        carrito.modificarCantidad(libro, 2);
        assertEquals(2, carrito.getItems().get(libro));
        carrito.eliminarLibro(libro);
        assertTrue(carrito.estaVacio());

        carrito.agregarLibro(libro, 1);
        ResultadoCompra resultado = compras.finalizarCompra(
                new ClienteRegular("Cliente CRUD", "crud@correo.com", "1234"),
                carrito, "Efectivo");
        assertTrue(resultado.isExitoso());
        assertEquals(1, compras.cantidad());
        assertEquals(1, compras.listarPorCliente("crud@correo.com").size());
        assertFalse(compras.listarCompras().isEmpty());
    }

    private Libro libro(String isbn) {
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo("Libro CRUD");
        libro.setAutor("Autor CRUD");
        libro.setAnioPublicacion(2020);
        libro.setNumeroPaginas(100);
        libro.setPrecio(100);
        libro.setPorcentajeIva(19);
        libro.setCantidadDisponible(5);
        libro.setFormato(FormatoLibro.FISICO);
        return libro;
    }

    private String ruta(String archivo) {
        return temp.resolve(archivo).toString();
    }
}
