package co.edu.uptc.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.CredencialDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.persistencia.ClienteJsonRepository;

/**
 * Pruebas del cifrado de contraseñas (SHA-256) y del inicio de sesión
 * con contraseñas cifradas.
 */
public class CifradorContrasenaTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private IClienteRepositorio repositorio;
    private GestionCliente gestionCliente;
    private GestionSeguridad gestionSeguridad;

    @Before
    public void setUp() throws ReglaNegocioException {
        String ruta = carpetaTemporal.getRoot().toPath().resolve("clientes.json").toString();
        repositorio = new ClienteJsonRepository(ruta);
        gestionCliente = new GestionCliente(repositorio, new ValidadorDatos());
        gestionSeguridad = new GestionSeguridad(repositorio);

        // cliente que ya existe, registrado con su contraseña cifrada
        gestionCliente.registrarCliente(clienteNuevo("ana@correo.com", "1000000002", "Clave1234"));
    }

    private ClienteDto clienteNuevo(String correo, String identificacion, String contrasena) {
        ClienteDto dto = new ClienteDto();
        dto.setPrimerNombre("Laura");
        dto.setPrimerApellido("Perez");
        dto.setTipoIdentificacion("CC");
        dto.setIdentificacion(identificacion);
        dto.setCorreoElectronico(correo);
        dto.setCelular("3201234567");
        dto.setDireccion("Calle 20 # 3-4");
        dto.setTipoCliente(TipoCliente.REGULAR);
        dto.setRol(Rol.CLIENTE);
        dto.setContrasenia(contrasena);
        return dto;
    }

    // ------------------------------ Cifrador ------------------------------

    @Test
    public void hashTieneSesentaYCuatroCaracteresHexadecimales() {
        String hash = CifradorContrasena.hash("Segura123");
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[0-9a-f]{64}"));
    }

    @Test
    public void hashEsDeterministaYDistintoDelTextoPlano() {
        assertEquals(CifradorContrasena.hash("Segura123"), CifradorContrasena.hash("Segura123"));
        assertNotEquals("Segura123", CifradorContrasena.hash("Segura123"));
        assertNotEquals(CifradorContrasena.hash("Segura123"), CifradorContrasena.hash("Segura124"));
    }

    @Test
    public void hashConocidoDeSha256() {
        // Valor de referencia de SHA-256 para "abc"
        assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
                CifradorContrasena.hash("abc"));
    }

    @Test
    public void hashDeNullEsNull() {
        assertNull(CifradorContrasena.hash(null));
    }

    @Test
    public void coincideCompararTextoConHash() {
        String hash = CifradorContrasena.hash("Segura123");
        assertTrue(CifradorContrasena.coincide("Segura123", hash));
        assertFalse(CifradorContrasena.coincide("segura123", hash));
        assertFalse(CifradorContrasena.coincide(null, hash));
        assertFalse(CifradorContrasena.coincide("Segura123", null));
    }

    // ------------------------- Almacenamiento -------------------------

    @Test
    public void registroGuardaLaContrasenaCifrada() throws ReglaNegocioException {
        gestionCliente.registrarCliente(clienteNuevo("laura@correo.com", "1234567890", "Segura123"));
        Cliente guardado = repositorio.buscarPorCorreo("laura@correo.com");
        assertNotEquals("Segura123", guardado.getContrasenia());
        assertEquals(CifradorContrasena.hash("Segura123"), guardado.getContrasenia());
    }

    @Test
    public void clienteExistenteNoGuardaTextoPlano() {
        assertEquals(CifradorContrasena.hash("Clave1234"),
                repositorio.buscarPorCorreo("ana@correo.com").getContrasenia());
    }

    @Test
    public void editarSinContrasenaConservaElHash() throws ReglaNegocioException {
        String hashAntes = repositorio.buscarPorCorreo("ana@correo.com").getContrasenia();
        ClienteDto dto = gestionCliente.buscarPorIdentificacion("1000000002");
        dto.setContrasenia("");
        gestionCliente.actualizarCliente(dto);
        assertEquals(hashAntes, repositorio.buscarPorCorreo("ana@correo.com").getContrasenia());
    }

    @Test
    public void editarConContrasenaNuevaLaCifra() throws ReglaNegocioException {
        ClienteDto dto = gestionCliente.buscarPorIdentificacion("1000000002");
        dto.setContrasenia("NuevaClave9");
        gestionCliente.actualizarCliente(dto);
        assertEquals(CifradorContrasena.hash("NuevaClave9"),
                repositorio.buscarPorCorreo("ana@correo.com").getContrasenia());
        assertTrue(gestionCliente.iniciarSesion("ana@correo.com", "NuevaClave9"));
        assertFalse(gestionCliente.iniciarSesion("ana@correo.com", "Clave1234"));
    }

    // ------------------------- Inicio de sesión -------------------------

    @Test
    public void clienteExistenteIniciaSesionConSuContrasena() {
        assertTrue(gestionSeguridad.iniciarSesion(new CredencialDto("ana@correo.com", "Clave1234")));
        assertTrue(gestionSeguridad.haySesionActiva());
    }

    @Test
    public void contrasenaIncorrectaNoIniciaSesion() {
        assertFalse(gestionSeguridad.iniciarSesion(new CredencialDto("ana@correo.com", "clave1234")));
        assertFalse(gestionSeguridad.haySesionActiva());
    }

    @Test
    public void elHashNoSirveComoContrasena() {
        // Quien conozca el hash guardado no puede usarlo para entrar
        String hash = repositorio.buscarPorCorreo("ana@correo.com").getContrasenia();
        assertFalse(gestionSeguridad.iniciarSesion(new CredencialDto("ana@correo.com", hash)));
    }

    @Test
    public void clienteRegistradoIniciaSesion() throws ReglaNegocioException {
        gestionCliente.registrarCliente(clienteNuevo("laura@correo.com", "1234567890", "Segura123"));
        assertTrue(gestionSeguridad.iniciarSesion(new CredencialDto("laura@correo.com", "Segura123")));
    }
}
