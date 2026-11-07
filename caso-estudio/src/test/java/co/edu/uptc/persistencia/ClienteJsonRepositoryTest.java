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

    /** Registra un cliente por la capa de negocio y devuelve su id. */
    private int registrar(String correo, String identificacion) throws ReglaNegocioException {
        ClienteDto dto = clienteNuevo(correo, identificacion);
        gestionCliente.registrarCliente(dto);
        return dto.getIdCliente();
    }

    // ------------------------------ Archivo ------------------------------

    @Test
    public void creaElArchivoVacioSiNoExiste() throws Exception {
        assertTrue(Files.exists(Path.of(rutaArchivo)));
        assertEquals(0, repositorio.listar().size());
        assertTrue(contenidoArchivo().contains("\"ultimoId\": 0"));
    }

    @Test
    public void elArchivoNoContieneContrasenasEnTextoPlano() throws Exception {
        registrar("sofia@correo.com", "1111111111");
        String contenido = contenidoArchivo();
        assertFalse(contenido.contains("Clave2026"));
        assertTrue(contenido.contains(CifradorContrasena.hash("Clave2026")));
    }

    @Test
    public void archivoVacioSeReinicializaSinClientes() throws Exception {
        Files.write(Path.of(rutaArchivo), new byte[0]);
        ClienteJsonRepository otro = new ClienteJsonRepository(rutaArchivo);
        assertEquals(0, otro.listar().size());
    }

    @Test
    public void jsonCorruptoLanzaPersistenciaException() throws Exception {
        Files.write(Path.of(rutaArchivo), "{ esto no es json".getBytes(StandardCharsets.UTF_8));
        assertThrows(PersistenciaException.class, () -> repositorio.listar());
    }

    // ------------------------------- Tipos -------------------------------

    @Test
    public void conservaElTipoYElRolDelClienteAlLeer() throws ReglaNegocioException {
        int idRegular = registrar("sofia@correo.com", "1111111111");
        int idPremium = registrar("juan@correo.com", "2222222222");

        // se cambia el tipo y el rol del segundo cliente desde la edición
        ClienteDto dto = gestionCliente.buscarPorId(idPremium);
        dto.setContrasenia("");
        dto.setTipoCliente(TipoCliente.PREMIUM);
        dto.setRol(Rol.ADMIN);
        gestionCliente.actualizarCliente(dto);

        ClienteJsonRepository otro = new ClienteJsonRepository(rutaArchivo);
        assertTrue(otro.buscarPorId(idRegular) instanceof ClienteRegular);
        assertTrue(otro.buscarPorId(idPremium) instanceof ClientePremium);
        assertEquals(Rol.ADMIN, otro.buscarPorId(idPremium).getRol());
    }

    // ------------------------------- CRUD -------------------------------

    @Test
    public void registrarAsignaIdSecuencialYFecha() throws ReglaNegocioException {
        assertEquals(1, registrar("sofia@correo.com", "1111111111"));
        assertEquals(2, registrar("juan@correo.com", "2222222222"));

        Cliente guardado = repositorio.buscarPorId(2);
        assertNotNull(guardado);
        assertNotNull(guardado.getFechaRegistro());
        assertEquals(CifradorContrasena.hash("Clave2026"), guardado.getContrasenia());
    }

    @Test
    public void losIdsNoSeRepitenTrasReiniciarNiTrasEliminar() throws ReglaNegocioException {
        registrar("base@correo.com", "1111111111");                 // id 1
        int idEliminado = registrar("uno@correo.com", "2222222222"); // id 2
        gestionCliente.eliminarCliente(idEliminado);

        // Se "reinicia" la aplicación creando otro repositorio sobre el mismo archivo
        GestionCliente otraSesion = new GestionCliente(
                new ClienteJsonRepository(rutaArchivo), new ValidadorDatos());
        ClienteDto segundo = clienteNuevo("dos@correo.com", "3333333333");
        otraSesion.registrarCliente(segundo);
        assertEquals(3, segundo.getIdCliente()); // no reutiliza el 2
    }

    @Test
    public void losDatosPersistenEntreInstancias() throws ReglaNegocioException {
        registrar("sofia@correo.com", "1111111111");

        ClienteJsonRepository otro = new ClienteJsonRepository(rutaArchivo);
        Cliente leido = otro.buscarPorCorreo("sofia@correo.com");
        assertNotNull(leido);
        assertEquals("Sofia", leido.getPrimerNombre());
        assertEquals("3157654321", leido.getCelular());
        assertEquals(1, otro.listar().size());
    }

    @Test
    public void buscarPorCorreoIgnoraMayusculas() throws ReglaNegocioException {
        registrar("sofia@correo.com", "1111111111");
        assertNotNull(repositorio.buscarPorCorreo("SOFIA@CORREO.COM"));
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
        int id = registrar("sofia@correo.com", "1111111111");
        Cliente antes = repositorio.buscarPorId(id);
        Timestamp fechaAntes = antes.getFechaRegistro();

        ClienteDto dto = gestionCliente.buscarPorId(id);
        dto.setDireccion("Diagonal 15 # 1-1");
        dto.setContrasenia(""); // conservar la actual
        gestionCliente.actualizarCliente(dto);

        Cliente despues = new ClienteJsonRepository(rutaArchivo).buscarPorId(id);
        assertEquals("Diagonal 15 # 1-1", despues.getDireccion());
        assertEquals(antes.getContrasenia(), despues.getContrasenia());
        assertEquals(fechaAntes, despues.getFechaRegistro());
    }

    @Test
    public void actualizarPuedeCambiarElTipoDeCliente() throws ReglaNegocioException {
        int id = registrar("sofia@correo.com", "1111111111");
        ClienteDto dto = gestionCliente.buscarPorId(id);
        dto.setContrasenia("");
        dto.setTipoCliente(TipoCliente.PREMIUM);
        gestionCliente.actualizarCliente(dto);
        assertTrue(new ClienteJsonRepository(rutaArchivo).buscarPorId(id) instanceof ClientePremium);
    }

    @Test
    public void eliminarPorIdYPorIdentificacion() throws ReglaNegocioException {
        registrar("uno@correo.com", "1111111111");
        int id2 = registrar("dos@correo.com", "2222222222");
        registrar("tres@correo.com", "3333333333");

        repositorio.eliminarPorId(id2);
        assertNull(repositorio.buscarPorId(id2));
        repositorio.eliminar("3333333333");
        assertNull(repositorio.buscar("3333333333"));
        assertEquals(1, new ClienteJsonRepository(rutaArchivo).listar().size());
    }

    @Test
    public void correoRepetidoSigueRechazadoConJson() throws ReglaNegocioException {
        registrar("sofia@correo.com", "1111111111");
        ClienteDto dto = clienteNuevo("sofia@correo.com", "2222222222");
        assertThrows(ReglaNegocioException.class, () -> gestionCliente.registrarCliente(dto));
    }

    // -------------------------- Inicio de sesión --------------------------

    @Test
    public void inicioDeSesionFuncionaConClientesDelArchivo() throws ReglaNegocioException {
        gestionCliente.registrarCliente(clienteNuevo("sofia@correo.com", "1111111111"));
        GestionSeguridad otraSesion = new GestionSeguridad(new ClienteJsonRepository(rutaArchivo));
        assertTrue(otraSesion.iniciarSesion(new CredencialDto("sofia@correo.com", "Clave2026")));
        assertFalse(otraSesion.iniciarSesion(new CredencialDto("sofia@correo.com", "clave2026")));
    }
}
