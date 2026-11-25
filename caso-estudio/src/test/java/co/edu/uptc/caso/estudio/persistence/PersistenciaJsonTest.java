package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import co.edu.uptc.caso.estudio.model.Libro;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

// Pruebas unitarias para verificar la lectura, escritura y tolerancia a fallos en archivos JSON
public class PersistenciaJsonTest extends TestCase {

    private File archivoTempLibros;
    private File archivoTempClientes;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Creamos archivos temporales aislados para no modificar los datos reales de la tienda
        archivoTempLibros = File.createTempFile("test_libros_", ".json");
        archivoTempClientes = File.createTempFile("test_clientes_", ".json");
    }

    @Override
    protected void tearDown() throws Exception {
        if (archivoTempLibros != null && archivoTempLibros.exists()) {
            archivoTempLibros.delete();
        }
        if (archivoTempClientes != null && archivoTempClientes.exists()) {
            archivoTempClientes.delete();
        }
        super.tearDown();
    }

    // Verifica que leer un archivo inexistente o vacio devuelva una lista vacia sin fallar
    public void testLeerArchivoInexistenteOVacioRetornaListaVacia() {
        Type tipoListaLibros = new TypeToken<List<Libro>>() {}.getType();
        ManejadorArchivoJson<Libro> manejador = new ManejadorArchivoJson<>(archivoTempLibros.getAbsolutePath(), tipoListaLibros);

        List<Libro> resultado = manejador.leerLista();
        assertNotNull("La lista retornada nunca debe ser null", resultado);
        assertTrue("Un archivo recien creado y vacio debe devolver lista vacia", resultado.isEmpty());

        // Probamos tambien con una ruta que aun no existe en disco
        File noExiste = new File(archivoTempLibros.getParent(), "no_existe_" + System.nanoTime() + ".json");
        ManejadorArchivoJson<Libro> manejadorInexistente = new ManejadorArchivoJson<>(noExiste.getAbsolutePath(), tipoListaLibros);
        assertTrue("Una ruta inexistente debe devolver lista vacia", manejadorInexistente.leerLista().isEmpty());
    }

    // Comprueba que los libros guardados en JSON se recuperen con todos sus atributos intactos
    public void testGuardarYLeerListaLibrosJson() {
        Type tipoListaLibros = new TypeToken<List<Libro>>() {}.getType();
        ManejadorArchivoJson<Libro> manejador = new ManejadorArchivoJson<>(archivoTempLibros.getAbsolutePath(), tipoListaLibros);

        Libro l1 = new Libro("978-10", "Cien Anos de Soledad", "Gabriel Garcia Marquez", 1967,
                "Novela", "Sudamericana", 471, 65000, 12, "Físico", 19);
        Libro l2 = new Libro("978-11", "Clean Architecture", "Robert C. Martin", 2017,
                "Tecnologia", "Prentice Hall", 432, 130000, 5, "Digital", 5);

        manejador.guardarLista(Arrays.asList(l1, l2));
        List<Libro> recuperados = manejador.leerLista();

        assertEquals("Debe recuperar exactamente 2 libros", 2, recuperados.size());
        assertEquals("978-10", recuperados.get(0).getIsbn());
        assertEquals("Cien Anos de Soledad", recuperados.get(0).getTitulo());
        assertEquals(12, recuperados.get(0).getStock());
        assertEquals("Clean Architecture", recuperados.get(1).getTitulo());
    }

    // Valida la serializacion de clientes incluyendo adaptadores de LocalDate/LocalDateTime
    public void testGuardarYLeerListaClientesJson() {
        Type tipoListaClientes = new TypeToken<List<Cliente>>() {}.getType();
        ManejadorArchivoJson<Cliente> manejador = new ManejadorArchivoJson<>(archivoTempClientes.getAbsolutePath(), tipoListaClientes);

        Cliente c1 = FabricaClientes.crear("2001", "Camila Ortiz", "camila@uptc.edu.co",
                "Calle 18 # 9-10", "Boyaca", "Tunja", "3112223344", "PREMIUM", "claveSegura1");

        manejador.guardarLista(Arrays.asList(c1));
        List<Cliente> recuperados = manejador.leerLista();

        assertEquals("Debe recuperar 1 cliente", 1, recuperados.size());
        assertEquals("2001", recuperados.get(0).getId());
        assertEquals("camila@uptc.edu.co", recuperados.get(0).getCorreo());
    }

    // Asegura que si el archivo JSON esta corrupto el manejador no rompa la aplicacion
    public void testArchivoJsonCorruptoReiniciaListaVacia() throws Exception {
        try (FileWriter writer = new FileWriter(archivoTempLibros)) {
            writer.write("{ json_invalido_sin_cerrar: [ 1, 2, ");
        }

        Type tipoListaLibros = new TypeToken<List<Libro>>() {}.getType();
        ManejadorArchivoJson<Libro> manejador = new ManejadorArchivoJson<>(archivoTempLibros.getAbsolutePath(), tipoListaLibros);

        List<Libro> resultado = manejador.leerLista();
        assertNotNull("Ante un JSON corrupto no debe retornar null", resultado);
        assertTrue("Ante un JSON corrupto debe reiniciar a lista vacia", resultado.isEmpty());
    }
}