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

public class GestionUsuariosTest extends TestCase {

    private GestionUsuariosImpl gestionUsuarios;
    private RepoUsuarioMemoria repoMySQL;
    private RepoUsuarioMemoria repoJson;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        repoMySQL = new RepoUsuarioMemoria();
        repoJson = new RepoUsuarioMemoria();
        gestionUsuarios = new GestionUsuariosImpl(repoMySQL, repoJson, new RepoCarritoVacio(), null);
    }

    public void testRegistrarClienteExitoso() {
        Cliente nuevo = FabricaClientes.crear(
                "1001", "Ana Perez", "ana@uptc.edu.co",
                "Calle 10 # 5-20", "Boyaca", "Tunja", "3101234567",
                "REGULAR", "clave123"
        );

        String error = gestionUsuarios.registrarCliente(nuevo);
        assertNull("El registro valido no debe retornar error", error);
        assertNotNull("Debe quedar guardado en repositorio", repoMySQL.buscarPorId("1001"));
    }

    public void testRegistrarClienteCorreoInvalido() {
        Cliente invalido = FabricaClientes.crear(
                "1002", "Carlos Gomez", "correo_sin_arroba",
                "Calle 1", "Boyaca", "Tunja", "3101234567",
                "REGULAR", "clave123"
        );

        String error = gestionUsuarios.registrarCliente(invalido);
        assertNotNull("Debe rechazar correo con formato invalido", error);
    }

    public void testRegistrarClienteDuplicado() {
        Cliente c1 = FabricaClientes.crear("1003", "Laura", "laura@uptc.edu.co", "Dir", "Boyaca", "Tunja", "3101234567", "REGULAR", "clave123");
        Cliente c2 = FabricaClientes.crear("1003", "Laura 2", "otra@uptc.edu.co", "Dir", "Boyaca", "Tunja", "3101234567", "REGULAR", "clave123");

        assertNull(gestionUsuarios.registrarCliente(c1));
        assertEquals("Ya existe un usuario registrado con ese ID.", gestionUsuarios.registrarCliente(c2));
    }

    public void testIniciarYCerrarSesion() {
        Cliente c = FabricaClientes.crear("1004", "Pedro", "pedro@uptc.edu.co", "Dir", "Boyaca", "Tunja", "3101234567", "PREMIUM", "secreto99");
        gestionUsuarios.registrarCliente(c);

        assertFalse("Debe fallar con clave incorrecta", gestionUsuarios.iniciarSesion(new CredencialDto("pedro@uptc.edu.co", "incorrecta")));
        assertTrue("Debe iniciar sesion con credenciales validas", gestionUsuarios.iniciarSesion(new CredencialDto("pedro@uptc.edu.co", "secreto99")));
        assertNotNull("El usuario logueado no debe ser nulo", gestionUsuarios.getUsuarioLogueado());

        gestionUsuarios.cerrarSesion();
        assertNull("Al cerrar sesion el usuario logueado debe ser nulo", gestionUsuarios.getUsuarioLogueado());
    }

    public void testRestablecerContrasena() {
        Cliente c = FabricaClientes.crear("1005", "Maria", "maria@uptc.edu.co", "Dir", "Boyaca", "Tunja", "3101234567", "REGULAR", "claveVieja");
        gestionUsuarios.registrarCliente(c);

        String error = gestionUsuarios.restablecerContrasena("1005", "maria@uptc.edu.co", "claveNueva1");
        assertNull("Debe permitir restablecer la contrasena", error);
        assertTrue(gestionUsuarios.iniciarSesion(new CredencialDto("maria@uptc.edu.co", "claveNueva1")));
    }

    // --- Repositorios simulados en memoria ---
    private static class RepoUsuarioMemoria implements IRepositorioUsuario {
        private final Map<String, Cliente> datos = new HashMap<>();

        @Override
        public boolean guardarUsuario(Cliente cliente) {
            datos.put(cliente.getId(), cliente);
            return true;
        }

        @Override
        public boolean actualizarUsuario(Cliente cliente) {
            if (!datos.containsKey(cliente.getId())) return false;
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
                if (c.getCorreo() != null && c.getCorreo().equalsIgnoreCase(correo)) return c;
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

    private static class RepoCarritoVacio implements IRepositorioCarrito {
        @Override public Carrito obtenerCarritoPorCliente(String clienteId) { return new Carrito(clienteId); }
        @Override public boolean guardarCarrito(Carrito carrito) { return true; }
        @Override public boolean vaciarCarrito(String clienteId) { return true; }
    }
}