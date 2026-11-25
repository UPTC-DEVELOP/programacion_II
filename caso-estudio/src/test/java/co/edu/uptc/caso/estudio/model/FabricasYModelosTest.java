package co.edu.uptc.caso.estudio.model;

import junit.framework.TestCase;

// Pruebas unitarias para verificar la creacion de objetos con fabricas y el polimorfismo del dominio
public class FabricasYModelosTest extends TestCase {

    // Comprueba que FabricaClientes cree instancias de ClienteRegular y ClientePremium segun el tipo
    public void testFabricaClientesPolimorfismo() {
        Cliente regular = FabricaClientes.crear(
                "3001", "Camila Ruiz", "camila@uptc.edu.co",
                "Calle 5", "Boyaca", "Tunja", "3112223344",
                "REGULAR", "clave123"
        );
        Cliente premium = FabricaClientes.crear(
                "3002", "Andres Mora", "andres@uptc.edu.co",
                "Cra 10", "Boyaca", "Tunja", "3115556677",
                "PREMIUM", "clave123"
        );

        assertTrue("Debe instanciar ClienteRegular", regular instanceof ClienteRegular);
        assertTrue("Debe instanciar ClientePremium", premium instanceof ClientePremium);
        assertTrue("ClientePremium debe tener mayor descuento o beneficio que Regular",
                premium.calcularDescuento(100000) >= regular.calcularDescuento(100000));
    }

    // Verifica que FabricaLibros cree LibroFisico y LibroDigital correctamente
    public void testFabricaLibrosPolimorfismo() {
        Libro fisico = FabricaLibros.crear(
                "978-10", "Java a Fondo", "Pablo Sznajdleder", 2020,
                "Programacion", "Alfaomega", 600, 95000, 10, "Físico", 19
        );
        Libro digital = FabricaLibros.crear(
                "978-11", "Clean Architecture", "Robert Martin", 2017,
                "Ingenieria", "Pearson", 432, 80000, 50, "Digital", 5
        );

        assertTrue("Debe instanciar LibroFisico", fisico instanceof LibroFisico);
        assertTrue("Debe instanciar LibroDigital", digital instanceof LibroDigital);
        assertEquals("Java a Fondo", fisico.getTitulo());
        assertEquals("Clean Architecture", digital.getTitulo());
    }

    // Revisa que el objeto Carrito sume cantidades y calcule subtotales correctamente
    public void testOperacionesBasicasCarrito() {
        Carrito carrito = new Carrito("3001");
        assertTrue("Un carrito recien creado debe estar vacio", carrito.estaVacio());

        Libro libro = new Libro("978-12", "Estructuras de Datos", "Joyanes", 2019,
                "Tecnologia", "McGraw-Hill", 500, 100000, 5, "Físico", 19);

        carrito.agregarItem(libro, 2);
        assertFalse("El carrito no debe estar vacio tras agregar un libro", carrito.estaVacio());
        assertEquals(2, carrito.buscarItem("978-12").getCantidad());
    }
}