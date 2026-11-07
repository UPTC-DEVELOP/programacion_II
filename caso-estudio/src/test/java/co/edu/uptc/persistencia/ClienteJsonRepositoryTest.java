package co.edu.uptc.persistencia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.CredencialDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.negocio.CifradorContrasena;
import co.edu.uptc.negocio.GestionCliente;
import co.edu.uptc.negocio.GestionSeguridad;
import co.edu.uptc.negocio.ValidadorDatos;

/**
 * Pruebas del repositorio JSON de clientes y del CRUD de clientes sobre él.
 *
 * <p>Cada prueba usa un directorio temporal propio (TemporaryFolder), de modo
 * que nunca se altera data/clientes.json del proyecto.</p>
 */
public class ClienteJsonRepositoryTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private String rutaArchivo;
    private ClienteJsonRepository repositorio;
    private GestionCliente gestionCliente;

    @Before
    public void setUp() throws Exception {
        rutaArchivo = carpetaTemporal.getRoot().toPath().resolve("data/clientes.json").toString();
        repositorio = new ClienteJsonRepository(rutaArchivo);
        gestionCliente = new GestionCliente(repositorio, new ValidadorDatos());
    }

    private ClienteDto clienteNuevo(String correo, String identificacion) {
        ClienteDto dto = new ClienteDto();
        dto.setPrimerNombre("Sofia");
        dto.setPrimerApellido("Lopez");
        dto.setTipoIdentificacion("CC");
        dto.setIdentificacion(identificacion);
        dto.setCorreoElectronico(correo);
        dto.setCelular("3157654321");
        dto.setDireccion("Carrera 7 # 8-9");
        dto.setTipoCliente(TipoCliente.REGULAR);
        dto.setRol(Rol.CLIENTE);
        dto.setContrasenia("Clave2026");
        return dto;
    }

    private String contenidoArchivo() throws Exception {
        return new String(Files.readAllBytes(Path.of(rutaArchivo)), StandardCharsets.UTF_8);
    }

    // ------------------------------ Archivo ------------------------------

    @Test
    public void creaElArchivoConLosClientesDeEjemplo() throws Exception {
        assertTrue(Files.exists(Path.of(rutaArchivo)));
        assertEquals(3, repositorio.listar().size());
        assertNotNull(repositorio.buscarPorCorreo("admin@libros.com"));
        assertTrue(contenidoArchivo().contains("\"ultimoId\": 3"));
    }

    @Test
    public void elArchivoNoContieneContrasenasEnTextoPlano() throws Exception {
        String contenido = contenidoArchivo();
        assertFalse(contenido.contains("admin123"));
        assertTrue(contenido.contains(CifradorContrasena.hash("admin123")));
    }

    @Test
    public void archivoVacioSeReinicializaConEjemplos() throws Exception {
        Files.write(Path.of(rutaArchivo), new byte[0]);
        ClienteJsonRepository otro = new ClienteJsonRepository(rutaArchivo);
        assertEquals(3, otro.listar().size());
    }

    @Test
    public void jsonCorruptoLanzaPersistenciaException() throws Exception {
        Files.write(Path.of(rutaArchivo), "{ esto no es json".getBytes(StandardCharsets.UTF_8));
        assertThrows(PersistenciaException.class, () -> repositorio.listar());
    }

    // ------------------------------- Tipos -------------------------------

    @Test
    public void conservaElTipoDeClienteAlLeer() {
        assertTrue(repositorio.buscarPorCorreo("admin@libros.com") instanceof ClientePremium);
        assertTrue(repositorio.buscarPorCorreo("cliente@libros.com") instanceof ClienteRegular);
        assertEquals(Rol.ADMIN, repositorio.buscarPorCorreo("admin@libros.com").getRol());
    }

    // ------------------------------- CRUD -------------------------------

    @Test
    public void registrarAsignaIdSecuencialYFecha() throws ReglaNegocioException {
        ClienteDto dto = clienteNuevo("sofia@correo.com", "1111111111");
        gestionCliente.registrarCliente(dto);
        assertEquals(4, dto.getIdCliente());

        Cliente guardado = repositorio.buscarPorId(4);
        assertNotNull(guardado);
        assertNotNull(guardado.getFechaRegistro());
        assertEquals(CifradorContrasena.hash("Clave2026"), guardado.getContrasenia());
    }

    @Test
    public void losIdsNoSeRepitenTrasReiniciarNiTrasEliminar() throws ReglaNegocioException {
        ClienteDto primero = clienteNuevo("uno@correo.com", "1111111111");
        gestionCliente.registrarCliente(primero);           // id 4
        gestionCliente.eliminarCliente(primero.getIdCliente());

        // Se "reinicia" la aplicación creando otro repositorio sobre el mismo archivo
        GestionCliente otraSesion = new GestionCliente(
                new ClienteJsonRepository(rutaArchivo), new ValidadorDatos());
        ClienteDto segundo = clienteNuevo("dos@correo.com", "2222222222");
        otraSesion.registrarCliente(segundo);
        assertEquals(5, segundo.getIdCliente());
    }

    @Test
    public void losDatosPersistenEntreInstancias() throws ReglaNegocioException {
        gestionCliente.registrarCliente(clienteNuevo("sofia@correo.com", "1111111111"));

        ClienteJsonRepository otro = new ClienteJsonRepository(rutaArchivo);
        Cliente leido = otro.buscarPorCorreo("sofia@correo.com");
        assertNotNull(leido);
        assertEquals("Sofia", leido.getPrimerNombre());
        assertEquals("3157654321", leido.getCelular());
        assertEquals(4, otro.listar().size());
    }

    @Test
    public void buscarPorCorreoIgnoraMayusculas() {
        assertNotNull(repositorio.buscarPorCorreo("ADMIN@LIBROS.COM"));
    }

    @Test
    public void buscarInexistenteDevuelveNull() {
        assertNull(repositorio.buscar("9999999999"));
        assertNull(repositorio.buscarPorId(99));
        assertNull(repositorio.buscarPorCorreo("nadie@correo.com"));
        assertNull(repositorio.buscar(null));
    }

    @Test
    public void actualizarConservaFechaYContrasena() throws ReglaNegocioException {
        Cliente antes = repositorio.buscarPorId(2);
        Timestamp fechaAntes = antes.getFechaRegistro();

        ClienteDto dto = gestionCliente.buscarPorId(2);
        dto.setDireccion("Diagonal 15 # 1-1");
        dto.setContrasenia(""); // conservar la actual
        gestionCliente.actualizarCliente(dto);

        Cliente despues = new ClienteJsonRepository(rutaArchivo).buscarPorId(2);
        assertEquals("Diagonal 15 # 1-1", despues.getDireccion());
        assertEquals(antes.getContrasenia(), despues.getContrasenia());
        assertEquals(fechaAntes, despues.getFechaRegistro());
    }

    @Test
    public void actualizarPuedeCambiarElTipoDeCliente() throws ReglaNegocioException {
        ClienteDto dto = gestionCliente.buscarPorId(2);
        dto.setContrasenia("");
        dto.setTipoCliente(TipoCliente.PREMIUM);
        gestionCliente.actualizarCliente(dto);
        assertTrue(new ClienteJsonRepository(rutaArchivo).buscarPorId(2) instanceof ClientePremium);
    }

    @Test
    public void eliminarPorIdYPorIdentificacion() {
        repositorio.eliminarPorId(3);
        assertNull(repositorio.buscarPorId(3));
        repositorio.eliminar("1000000002");
        assertNull(repositorio.buscar("1000000002"));
        assertEquals(1, new ClienteJsonRepository(rutaArchivo).listar().size());
    }

    @Test
    public void correoRepetidoSigueRechazadoConJson() {
        ClienteDto dto = clienteNuevo("maria@libros.com", "1111111111");
        assertThrows(ReglaNegocioException.class, () -> gestionCliente.registrarCliente(dto));
    }

    // -------------------------- Inicio de sesión --------------------------

    @Test
    public void inicioDeSesionFuncionaConClientesDelArchivo() throws ReglaNegocioException {
        GestionSeguridad seguridad = new GestionSeguridad(repositorio);
        assertTrue(seguridad.iniciarSesion(new CredencialDto("admin@libros.com", "admin123")));

        gestionCliente.registrarCliente(clienteNuevo("sofia@correo.com", "1111111111"));
        GestionSeguridad otraSesion = new GestionSeguridad(new ClienteJsonRepository(rutaArchivo));
        assertTrue(otraSesion.iniciarSesion(new CredencialDto("sofia@correo.com", "Clave2026")));
        assertFalse(otraSesion.iniciarSesion(new CredencialDto("sofia@correo.com", "clave2026")));
    }
}
