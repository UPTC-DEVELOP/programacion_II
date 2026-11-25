package co.edu.uptc.caso.estudio.persistence;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import junit.framework.TestCase;

// Pruebas unitarias para verificar la escritura y formato de auditoria en archivo TXT
public class RegistradorOperacionesTxtTest extends TestCase {

    private File archivoLogTemp;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        archivoLogTemp = File.createTempFile("test_operaciones_", ".txt");
    }

    @Override
    protected void tearDown() throws Exception {
        if (archivoLogTemp != null && archivoLogTemp.exists()) {
            archivoLogTemp.delete();
        }
        super.tearDown();
    }

    // Verifica que al registrar un evento se cree la linea con fecha y mensaje
    public void testRegistrarEventoEscribeLineaConFormato() throws Exception {
        RegistradorOperacionesTxt registrador = new RegistradorOperacionesTxt(archivoLogTemp.getAbsolutePath());
        registrador.registrar("LOGIN: Usuario diego@uptc.edu.co inicio sesion");

        List<String> lineas = Files.readAllLines(archivoLogTemp.toPath());
        assertEquals("Debe haber exactamente 1 linea registrada", 1, lineas.size());
        assertTrue("La linea debe iniciar con corchete de marca de tiempo", lineas.get(0).startsWith("["));
        assertTrue("La linea debe contener el mensaje registrado",
                lineas.get(0).contains("LOGIN: Usuario diego@uptc.edu.co inicio sesion"));
    }

    // Comprueba que multiples registros se agreguen al final sin sobrescribir los anteriores
    public void testMultiplesRegistrosSeAcumulanEnElArchivo() throws Exception {
        RegistradorOperacionesTxt registrador = new RegistradorOperacionesTxt(archivoLogTemp.getAbsolutePath());
        registrador.registrar("LIBRO: Registrado ISBN 978-10");
        registrador.registrar("COMPRA: Factura generada por $65000");

        List<String> lineas = Files.readAllLines(archivoLogTemp.toPath());
        assertEquals("Debe conservar ambas lineas registradas", 2, lineas.size());
        assertTrue(lineas.get(0).contains("978-10"));
        assertTrue(lineas.get(1).contains("Factura generada"));
    }
}