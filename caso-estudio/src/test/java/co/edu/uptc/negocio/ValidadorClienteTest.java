package co.edu.uptc.negocio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.persistencia.ClienteJsonRepository;

/**
 * Pruebas de las restricciones de registro de clientes:
 * contraseña, identificación, celular, correo y campos obligatorios.
 */
public class ValidadorClienteTest {

    @Rule
    public TemporaryFolder carpetaTemporal = new TemporaryFolder();

    private ValidadorDatos validador;
    private GestionCliente gestionCliente;

    @Before
    public void setUp() throws ReglaNegocioException {
        validador = new ValidadorDatos();
        String ruta = carpetaTemporal.getRoot().toPath().resolve("clientes.json").toString();
        gestionCliente = new GestionCliente(new ClienteJsonRepository(ruta), validador);

        // cliente que ya existe, para probar correo e identificación repetidos
        ClienteDto existente = clienteValido();
        existente.setCorreoElectronico("ana@correo.com");
        existente.setIdentificacion("1000000001");
        existente.setContrasenia("Clave1234");
        gestionCliente.registrarCliente(existente);
    }

    /** Cliente válido que cumple todas las restricciones. */
    private ClienteDto clienteValido() {
        ClienteDto dto = new ClienteDto();
        dto.setPrimerNombre("Carlos");
        dto.setPrimerApellido("Ruiz");
        dto.setTipoIdentificacion("CC");
        dto.setIdentificacion("1234567890");
        dto.setCorreoElectronico("carlos@correo.com");
        dto.setCelular("3101234567");
        dto.setDireccion("Calle 10 # 5-20");
        dto.setTipoCliente(TipoCliente.REGULAR);
        dto.setRol(Rol.CLIENTE);
        dto.setContrasenia("Segura123");
        return dto;
    }

    // ----------------------------- Contraseña -----------------------------

    @Test
    public void contrasenaValidaEsAceptada() throws ReglaNegocioException {
        validador.validarContrasenaNueva("Segura123");
    }

    @Test
    public void contrasenaCortaEsRechazada() {
        assertThrows(ReglaNegocioException.class, () -> validador.validarContrasenaNueva("Abc123"));
    }

    @Test
    public void contrasenaSinMayusculaEsRechazada() {
        assertThrows(ReglaNegocioException.class, () -> validador.validarContrasenaNueva("segura123"));
    }

    @Test
    public void contrasenaSinDigitoEsRechazada() {
        assertThrows(ReglaNegocioException.class, () -> validador.validarContrasenaNueva("SeguraSinNumero"));
    }

    @Test
    public void contrasenaVaciaEsRechazada() {
        assertThrows(ReglaNegocioException.class, () -> validador.validarContrasenaNueva("   "));
    }

    // --------------------------- Identificación ---------------------------

    @Test
    public void identificacionDeDiezDigitosEsAceptada() throws ReglaNegocioException {
        validador.validarIdentificacion("CC", "1234567890");
    }

    @Test
    public void identificacionDeOnceDigitosEsRechazada() {
        assertThrows(ReglaNegocioException.class,
                () -> validador.validarIdentificacion("CC", "12345678901"));
    }

    @Test
    public void identificacionConLetrasEsRechazadaParaCedula() {
        assertThrows(ReglaNegocioException.class,
                () -> validador.validarIdentificacion("CC", "12AB5678"));
    }

    @Test
    public void pasaporteAlfanumericoEsAceptado() throws ReglaNegocioException {
        validador.validarIdentificacion("Pasaporte", "AB1234567");
    }

    @Test
    public void pasaporteDeOnceCaracteresEsRechazado() {
        assertThrows(ReglaNegocioException.class,
                () -> validador.validarIdentificacion("Pasaporte", "AB123456789"));
    }

    // ------------------------------- Celular -------------------------------

    @Test
    public void celularDeDiezDigitosEsAceptado() throws ReglaNegocioException {
        validador.validarCelular("3101234567");
    }

    @Test
    public void celularDeNueveDigitosEsRechazado() {
        assertThrows(ReglaNegocioException.class, () -> validador.validarCelular("310123456"));
    }

    @Test
    public void celularConLetrasEsRechazado() {
        assertThrows(ReglaNegocioException.class, () -> validador.validarCelular("310ABC4567"));
    }

    // ------------------------ Validación del cliente ------------------------

    @Test
    public void clienteValidoPasaLaValidacion() throws ReglaNegocioException {
        validador.validarCliente(clienteValido());
    }

    @Test
    public void correoConFormatoInvalidoEsRechazado() {
        ClienteDto dto = clienteValido();
        dto.setCorreoElectronico("carlos.correo.com");
        assertThrows(ReglaNegocioException.class, () -> validador.validarCliente(dto));
    }

    @Test
    public void nombreVacioEsRechazado() {
        ClienteDto dto = clienteValido();
        dto.setPrimerNombre("  ");
        assertThrows(ReglaNegocioException.class, () -> validador.validarCliente(dto));
    }

    @Test
    public void direccionVaciaEsRechazada() {
        ClienteDto dto = clienteValido();
        dto.setDireccion(null);
        assertThrows(ReglaNegocioException.class, () -> validador.validarCliente(dto));
    }

    @Test
    public void tipoClienteNuloEsRechazado() {
        ClienteDto dto = clienteValido();
        dto.setTipoCliente(null);
        assertThrows(ReglaNegocioException.class, () -> validador.validarCliente(dto));
    }

    // ------------------------ Registro (GestionCliente) ------------------------

    @Test
    public void registroConDatosValidosAsignaId() throws ReglaNegocioException {
        ClienteDto dto = clienteValido();
        assertTrue(gestionCliente.registrarCliente(dto));
        assertTrue(dto.getIdCliente() > 0);
    }

    @Test
    public void registroConContrasenaDebilEsRechazado() {
        ClienteDto dto = clienteValido();
        dto.setContrasenia("abc123");
        assertThrows(ReglaNegocioException.class, () -> gestionCliente.registrarCliente(dto));
    }

    @Test
    public void registroConCorreoRepetidoEsRechazado() {
        ClienteDto dto = clienteValido();
        dto.setCorreoElectronico("ana@correo.com"); // ya existe (registrado en setUp)
        assertThrows(ReglaNegocioException.class, () -> gestionCliente.registrarCliente(dto));
    }

    @Test
    public void registroConIdentificacionRepetidaEsRechazado() {
        ClienteDto dto = clienteValido();
        dto.setIdentificacion("1000000001"); // ya existe (registrado en setUp)
        assertThrows(ReglaNegocioException.class, () -> gestionCliente.registrarCliente(dto));
    }

    @Test
    public void editarClienteSinCambiarContrasenaConservaLaClave() throws ReglaNegocioException {
        ClienteDto cliente = gestionCliente.buscarPorIdentificacion("1000000001");
        cliente.setContrasenia(""); // vacío = conservar la contraseña actual
        cliente.setDireccion("Nueva dirección 123");
        assertTrue(gestionCliente.actualizarCliente(cliente));
        assertEquals("Nueva dirección 123",
                gestionCliente.buscarPorIdentificacion("1000000001").getDireccion());
        assertTrue(gestionCliente.iniciarSesion("ana@correo.com", "Clave1234"));
    }
}
