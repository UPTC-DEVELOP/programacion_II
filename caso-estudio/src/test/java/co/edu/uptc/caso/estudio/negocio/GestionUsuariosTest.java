package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioUsuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;

// Pruebas unitarias para el registro, validaciones e inicio de sesion de clientes
public class GestionUsuariosTest extends TestCase {

    private RepoUsuarioMemoria repoMySQL;
    private RepoUsuarioMemoria repoJson;
    private RepoCarritoMemoria repoCarritoJson;
    private GestionUsuariosImpl gestionUsuarios;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        // Preparamos repositorios limpios en memoria antes de cada prueba
        repoMySQL = new RepoUsuarioMemoria();
        repoJson = new RepoUsuarioMemoria();
        repoCarritoJson = new RepoCarritoMemoria();
        gestionUsuarios = new GestionUsuariosImpl(repoMySQL, repoJson, repoCarritoJson, null);
    }

    // Verifica que un cliente con datos validos quede guardado en ambos repositorios
    public void testRegistrarClienteExitoso() {
        Cliente cliente = FabricaClientes.crear(
                "1001", "Laura Gomez", "laura@uptc.edu.co",
                "Calle 10 # 4-20", "Boyaca", "Tunja", "3101234567",
                "REGULAR", "clave123");

        String error = gestionUsuarios.registrarCliente(cliente);
        assertNull("No deberia retornar error al registrar un cliente valido", error);
        assertNotNull("El cliente debe quedar guardado en MySQL", repoMySQL.buscarPorId("1001"));
        assertNotNull("El cliente debe quedar guardado en JSON", repoJson.buscarPorId("1001"));
    }

    // Comprueba que el sistema rechace el registro si el correo ya existe
    public void testNoPermiteRegistrarCorreoDuplicado() {
        Cliente c1 = FabricaClientes.crear("1001", "Laura", "laura@uptc.edu.co", "Dir 1",
                "Boyaca", "Tunja", "3101234567", "REGULAR", "clave123");
        Cliente c2 = FabricaClientes.crear("1002", "Pedro", "laura@uptc.edu.co", "Dir 2",
                "Boyaca", "Tunja", "3107654321", "REGULAR", "clave456");

        gestionUsuarios.registrarCliente(c1);
        String error = gestionUsuarios.registrarCliente(c2);

        assertNotNull("Debe retornar error si el correo ya existe", error);
        assertNull("No debe guardar el segundo cliente", repoMySQL.buscarPorId("1002"));
    }

    // Valida que no deje registrar usuarios con contrasenas cortas
    public void testValidacionContrasenaCorta() {
        Cliente c = FabricaClientes.crear("1003", "Ana", "ana@uptc.edu.co", "Dir 1",
                "Boyaca", "Tunja", "3101234567", "REGULAR", "123");
        String error = gestionUsuarios.registrarCliente(c);
        assertNotNull("Debe rechazar contrasenas menores a 4 caracteres", error);
    }

    // Revisa que el login funcione con credenciales correctas y cargue el usuario activo
    public void testInicioSesionExitoso() {
        Cliente c = FabricaClientes.crear("1004", "Carlos", "carlos@uptc.edu.co", "Dir 1",
                "Boyaca", "Tunja", "3101234567", "PREMIUM", "segura99");
        gestionUsuarios.registrarCliente(c);

        boolean okLogin = gestionUsuarios.iniciarSesion(new CredencialDto("carlos@uptc.edu.co", "segura99"));
        assertTrue("El login valido debe retornar true", okLogin);
        Cliente logueado = gestionUsuarios.getUsuarioLogueado();
        assertNotNull("Debe haber un usuario logueado", logueado);
        assertEquals("1004", logueado.getId());
    }

    // Asegura que el inicio de sesion falle cuando la contrasena no coincide
    public void testInicioSesionContrasenaIncorrecta() {
        Cliente c = FabricaClientes.crear("1005", "Marta", "marta@uptc.edu.co", "Dir 1",
                "Boyaca", "Tunja", "3101234567", "REGULAR", "claveOk");
        gestionUsuarios.registrarCliente(c);

        boolean okLogin = gestionUsuarios.iniciarSesion(new CredencialDto("marta@uptc.edu.co", "claveMala"));
        assertFalse("Debe retornar false cuando la clave no coincide", okLogin);
        assertNull("No debe iniciar sesion con clave incorrecta", gestionUsuarios.getUsuarioLogueado());
    }

    // Verifica que al cerrar sesion el usuario activo vuelva a quedar en null
    public void testCerrarSesionLimpiaUsuarioActivo() {
        Cliente c = FabricaClientes.crear("1006", "Diego", "diego@uptc.edu.co", "Dir 1",
                "Boyaca", "Tunja", "3109876543", "REGULAR", "clave123");
        gestionUsuarios.registrarCliente(c);
        gestionUsuarios.iniciarSesion(new CredencialDto("diego@uptc.edu.co", "clave123"));

        assertNotNull("Debe haber un usuario activo tras iniciar sesion", gestionUsuarios.getUsuarioLogueado());
        gestionUsuarios.cerrarSesion();
        assertNull("La sesion debe quedar vacia tras cerrar sesion", gestionUsuarios.getUsuarioLogueado());
    }

    // Comprueba que iniciar sesion con un correo inexistente retorne error
    public void testLoginUsuarioInexistenteFalla() {
        boolean okLogin = gestionUsuarios.iniciarSesion(new CredencialDto("noexiste@uptc.edu.co", "clave123"));
        assertFalse("Debe retornar false si el correo no existe", okLogin);
        assertNull("No debe iniciar sesion con credenciales invalidas", gestionUsuarios.getUsuarioLogueado());
    }

    // Simula la base de datos de usuarios en memoria usando un HashMap
    private static class RepoUsuarioMemoria implements IRepositorioUsuario {
        private final Map<String, Cliente> datos = new HashMap<>();

        @Override
        public boolean guardarUsuario(Cliente cliente) {
            datos.put(cliente.getId(), cliente);
            return true;
        }

        @Override
        public boolean actualizarUsuario(Cliente cliente) {
            datos.put(cliente.getId(), cliente);
            return true;
        }

        @Override
        public boolean eliminarUsuario(String id) {
            return datos.remove(id) != null;
        }

        @Override
        public Cliente buscarPorCorreo(String correo) {
            for (Cliente c : datos.values()) {
                if (c.getCorreo().equalsIgnoreCase(correo)) {
                    return c;
                }
            }
            return null;
        }

        @Override
        public Cliente buscarPorId(String id) {
            return datos.get(id);
        }

        @Override
        public List<Cliente> obtenerTodos() {
            return new ArrayList<>(datos.values());
        }
    }

    // Simula el almacenamiento de carritos en memoria
    private static class RepoCarritoMemoria implements IRepositorioCarrito {
        private final Map<String, Carrito> carritos = new HashMap<>();

        @Override
        public Carrito obtenerCarritoPorCliente(String clienteId) {
            return carritos.get(clienteId);
        }

        @Override
        public boolean guardarCarrito(Carrito carrito) {
            carritos.put(carrito.getClienteId(), carrito);
            return true;
        }

        @Override
        public boolean vaciarCarrito(String clienteId) {
            return carritos.remove(clienteId) != null;
        }
    }
}