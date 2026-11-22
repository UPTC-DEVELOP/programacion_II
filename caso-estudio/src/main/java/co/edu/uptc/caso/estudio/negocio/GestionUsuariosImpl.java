package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioUsuario;
import co.edu.uptc.caso.estudio.persistence.RegistradorOperacionesTxt;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GestionUsuariosImpl implements IGestionUsuarios {

    public static final int LONGITUD_MINIMA_CONTRASENA = 6;
    private final IRepositorioUsuario repoMySQL;
    private final IRepositorioUsuario repoJson;
    private final IRepositorioCarrito repoCarritoJson;
    private final RegistradorOperacionesTxt registradorTxt;
    private Cliente usuarioLogueado;

    public GestionUsuariosImpl(IRepositorioUsuario repoMySQL, IRepositorioUsuario repoJson,
                               IRepositorioCarrito repoCarritoJson, RegistradorOperacionesTxt registradorTxt) {
        this.repoMySQL = repoMySQL;
        this.repoJson = repoJson;
        this.repoCarritoJson = repoCarritoJson;
        this.registradorTxt = registradorTxt;
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null || cliente.getId().isBlank()
                || cliente.getCorreo() == null || cliente.getCorreo().isBlank()) {
            return false;
        }
        String clavePlana = cliente.getContrasenaHash();
        if (clavePlana == null || clavePlana.length() < LONGITUD_MINIMA_CONTRASENA) {
            return false;
        }

        if (buscarPorIdConRespaldo(cliente.getId()) != null) return false;
        if (buscarPorCorreoConRespaldo(cliente.getCorreo().trim()) != null) return false;

        cliente.setContrasenaHash(sha256(clavePlana));
        boolean okMySQL = repoMySQL.guardarUsuario(cliente);
        boolean okJson = repoJson.guardarUsuario(cliente);

        if (okMySQL || okJson) {
            registrarEvento("USUARIO REGISTRADO - ID: " + cliente.getId() + " | Correo: " + cliente.getCorreo());
            return true;
        }
        return false;
    }

    @Override
    public boolean iniciarSesion(CredencialDto credenciales) {
        if (credenciales == null || credenciales.getCorreo() == null || credenciales.getContrasena() == null) {
            return false;
        }
        Cliente c = buscarPorCorreoConRespaldo(credenciales.getCorreo().trim());
        if (c != null && c.getContrasenaHash() != null
                && c.getContrasenaHash().equals(sha256(credenciales.getContrasena()))) {
            this.usuarioLogueado = c;
            registrarEvento("INICIO DE SESIÓN - Usuario: " + c.getCorreo());
            return true;
        }
        return false;
    }

    @Override
    public void cerrarSesion() {
        if (this.usuarioLogueado != null) {
            registrarEvento("CIERRE DE SESIÓN - Usuario: " + this.usuarioLogueado.getCorreo());
            this.usuarioLogueado = null;
        }
    }

    @Override
    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
    }

    @Override
    public String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena) {
        if (usuarioLogueado == null) return "Debe iniciar sesión para actualizar sus datos.";
        if (nuevosDatos == null) return "Los datos del usuario no pueden ser nulos.";
        if (nuevosDatos.getNombreCompleto() == null || nuevosDatos.getNombreCompleto().isBlank()) {
            return "El nombre no puede estar vacío.";
        }

        boolean cambiaContrasena = nuevaContrasena != null && !nuevaContrasena.isEmpty();
        if (cambiaContrasena && nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            return "La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";
        }

        String tipo = nuevosDatos.getTipoCliente() != null ? nuevosDatos.getTipoCliente() : usuarioLogueado.getTipoCliente();
        Cliente actualizado = new Cliente(
                usuarioLogueado.getId(), nuevosDatos.getNombreCompleto(), usuarioLogueado.getCorreo(),
                nuevosDatos.getDireccionEnvio(), nuevosDatos.getDepartamento(), nuevosDatos.getCiudad(),
                nuevosDatos.getTelefono(), tipo
        );
        actualizado.setContrasenaHash(cambiaContrasena ? sha256(nuevaContrasena) : usuarioLogueado.getContrasenaHash());

        boolean okMySQL = repoMySQL.actualizarUsuario(actualizado);
        boolean okJson = repoJson.actualizarUsuario(actualizado);

        if (!okMySQL && !okJson) {
            return "No se encontró el usuario para actualizar.";
        }

        this.usuarioLogueado = actualizado;
        registrarEvento("USUARIO ACTUALIZADO - ID: " + actualizado.getId() + " | Correo: " + actualizado.getCorreo());
        return null;
    }

    @Override
    public boolean eliminarUsuarioLogueado(String contrasena) {
        if (usuarioLogueado == null || contrasena == null) return false;
        if (!sha256(contrasena).equals(usuarioLogueado.getContrasenaHash())) return false;

        String id = usuarioLogueado.getId();
        String correo = usuarioLogueado.getCorreo();

        boolean okMySQL = repoMySQL.eliminarUsuario(id);
        boolean okJson = repoJson.eliminarUsuario(id);

        if (!okMySQL && !okJson) {
            return false;
        }

        if (repoCarritoJson != null) {
            repoCarritoJson.vaciarCarrito(id);
        }

        this.usuarioLogueado = null;
        registrarEvento("USUARIO ELIMINADO - ID: " + id + " | Correo: " + correo);
        return true;
    }

    private Cliente buscarPorIdConRespaldo(String id) {
        Cliente c = repoMySQL.buscarPorId(id);
        return (c != null) ? c : repoJson.buscarPorId(id);
    }

    private Cliente buscarPorCorreoConRespaldo(String correo) {
        Cliente c = repoMySQL.buscarPorCorreo(correo);
        return (c != null) ? c : repoJson.buscarPorCorreo(correo);
    }

    private void registrarEvento(String mensaje) {
        if (registradorTxt != null) {
            registradorTxt.registrar(mensaje);
        }
    }

    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
}