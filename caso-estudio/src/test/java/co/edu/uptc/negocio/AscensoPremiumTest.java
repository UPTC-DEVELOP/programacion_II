package co.edu.uptc.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.TotalesCarritoDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.persistencia.CarritoRepositoryEnMemoria;
import co.edu.uptc.persistencia.ClienteJsonRepository;
import co.edu.uptc.persistencia.LocalLibro;

/**
 * Pruebas del ascenso automático de cliente REGULAR a PREMIUM tras
 * COMPRAS_PARA_PREMIUM compras, usando el repositorio JSON en una carpeta
 * temporal.
 */
public class AscensoPremiumTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private String rutaArchivo;
    private GestionCliente gestionCliente;

    @Before
    public void setUp() {
        rutaArchivo = carpetaTemporal.getRoot().toPath().resolve("clientes.json").toString();
        gestionCliente = new GestionCliente(new ClienteJsonRepository(rutaArchivo), new ValidadorDatos());
    }

    private ClienteDto registrar(String correo, String identificacion, TipoCliente tipoSolicitado)
            throws ReglaNegocioException {
        ClienteDto dto = new ClienteDto();
        dto.setPrimerNombre("Pedro");
        dto.setPrimerApellido("Mora");
        dto.setTipoIdentificacion("CC");
        dto.setIdentificacion(identificacion);
        dto.setCorreoElectronico(correo);
        dto.setCelular("3009876543");
        dto.setDireccion("Calle 50 # 10-10");
        dto.setTipoCliente(tipoSolicitado);
        dto.setRol(Rol.CLIENTE);
        dto.setContrasenia("Compras22");
        gestionCliente.registrarCliente(dto);
        return dto;
    }

    @Test
    public void elUmbralEsDosCompras() {
        assertEquals(2, GestionCliente.COMPRAS_PARA_PREMIUM);
    }

    @Test
    public void todoClienteNuevoSeRegistraComoRegular() throws ReglaNegocioException {
        // aunque intente registrarse como PREMIUM, queda REGULAR
        ClienteDto dto = registrar("pedro@correo.com", "1212121212", TipoCliente.PREMIUM);
        ClienteDto guardado = gestionCliente.buscarPorId(dto.getIdCliente());
        assertEquals(TipoCliente.REGULAR, guardado.getTipoCliente());
        assertEquals(0, guardado.getComprasRealizadas());
    }

    @Test
    public void registroSinTipoTambienQuedaRegular() throws ReglaNegocioException {
        ClienteDto dto = registrar("pedro@correo.com", "1212121212", null);
        assertEquals(TipoCliente.REGULAR, gestionCliente.buscarPorId(dto.getIdCliente()).getTipoCliente());
    }

    @Test
    public void conUnaCompraSigueRegular() throws ReglaNegocioException {
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        assertFalse(gestionCliente.registrarCompra(id));
        ClienteDto cliente = gestionCliente.buscarPorId(id);
        assertEquals(TipoCliente.REGULAR, cliente.getTipoCliente());
        assertEquals(1, cliente.getComprasRealizadas());
    }

    @Test
    public void conDosComprasPasaAPremium() throws ReglaNegocioException {
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        gestionCliente.registrarCompra(id);
        assertTrue(gestionCliente.registrarCompra(id));

        ClienteDto cliente = gestionCliente.buscarPorId(id);
        assertEquals(TipoCliente.PREMIUM, cliente.getTipoCliente());
        assertEquals(2, cliente.getComprasRealizadas());
    }

    @Test
    public void elAscensoPersisteYConservaLosDatos() throws ReglaNegocioException {
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        gestionCliente.registrarCompra(id);
        gestionCliente.registrarCompra(id);

        // "reinicio" de la aplicación: nuevo repositorio sobre el mismo archivo
        ClienteJsonRepository otro = new ClienteJsonRepository(rutaArchivo);
        assertTrue(otro.buscarPorId(id) instanceof ClientePremium);
        assertEquals("pedro@correo.com", otro.buscarPorId(id).getCorreoElectronico());
        assertEquals(CifradorContrasena.hash("Compras22"), otro.buscarPorId(id).getContrasenia());
        assertTrue(new GestionCliente(otro, new ValidadorDatos())
                .iniciarSesion("pedro@correo.com", "Compras22"));
    }

    @Test
    public void unClientePremiumNoCambiaDeTipo() throws ReglaNegocioException {
        // se registra un cliente y se cambia a PREMIUM desde la edición
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        ClienteDto dto = gestionCliente.buscarPorId(id);
        dto.setContrasenia("");
        dto.setTipoCliente(TipoCliente.PREMIUM);
        gestionCliente.actualizarCliente(dto);

        // como ya es PREMIUM, las compras no lo "ascienden" otra vez
        assertFalse(gestionCliente.registrarCompra(id));
        assertFalse(gestionCliente.registrarCompra(id));
        ClienteDto premium = gestionCliente.buscarPorId(id);
        assertEquals(TipoCliente.PREMIUM, premium.getTipoCliente());
        assertEquals(2, premium.getComprasRealizadas());
    }

    @Test
    public void elAscensoSoloOcurreUnaVez() throws ReglaNegocioException {
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        gestionCliente.registrarCompra(id);
        assertTrue(gestionCliente.registrarCompra(id));
        assertFalse(gestionCliente.registrarCompra(id)); // tercera compra: ya era PREMIUM
        assertEquals(3, gestionCliente.buscarPorId(id).getComprasRealizadas());
    }

    @Test
    public void editarDesdeElFormularioNoModificaLasCompras() throws ReglaNegocioException {
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        gestionCliente.registrarCompra(id);

        ClienteDto dto = gestionCliente.buscarPorId(id);
        dto.setContrasenia("");
        dto.setComprasRealizadas(50); // intento de alterar el contador
        gestionCliente.actualizarCliente(dto);
        assertEquals(1, gestionCliente.buscarPorId(id).getComprasRealizadas());
    }

    @Test
    public void registrarCompraDeClienteInexistenteFalla() {
        assertThrows(ReglaNegocioException.class, () -> gestionCliente.registrarCompra(999));
    }

    @Test
    public void elDescuentoPremiumSeAplicaEnElCarritoTrasElAscenso() throws ReglaNegocioException {
        int id = registrar("pedro@correo.com", "1212121212", TipoCliente.REGULAR).getIdCliente();
        CarritoServiceImpl carrito = new CarritoServiceImpl(
                new CarritoRepositoryEnMemoria(), new LocalLibro(), gestionCliente);
        carrito.agregarLibro(id, "9780134685991", 1); // Effective Java, 180.000

        assertEquals(0.0, carrito.calcularTotales(id).getDescuento(), 0.001);

        gestionCliente.registrarCompra(id);
        gestionCliente.registrarCompra(id);

        TotalesCarritoDto totales = carrito.calcularTotales(id);
        assertEquals(18000.0, totales.getDescuento(), 0.001); // 10 % de 180.000
    }
}
