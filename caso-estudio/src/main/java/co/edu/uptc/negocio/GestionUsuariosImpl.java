package co.edu.uptc.negocio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.negocio.dto.CredencialDto;
import co.edu.uptc.persistence.IRepositorioUsuario;

public class GestionUsuariosImpl implements IGestionUsuarios {

    public static final int LONGITUD_MINIMA_CONTRASENA = 6;
    private final IRepositorioUsuario repoMySQL;
    private final IRepositorioUsuario repoJson;
    private final IGestionLibros gestionLibros;
    private IGestionCompras gestionCompras;
    private Cliente usuarioLogueado;

    public GestionUsuariosImpl(IRepositorioUsuario repoMySQL, IRepositorioUsuario repoJson, IGestionLibros gestionLibros) {
        this.repoMySQL = repoMySQL;
        this.repoJson = repoJson;
        this.gestionLibros = gestionLibros;
    }

    public void setGestionCompras(IGestionCompras gestionCompras) {
        this.gestionCompras = gestionCompras;
    }

    @Override
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null || cliente.getId() == null || cliente.getCorreo() == null) return false;
        if (repoMySQL.buscarPorId(cliente.getId()) != null) return false;
        if (repoMySQL.buscarPorCorreo(cliente.getCorreo()) != null) return false;

        cliente.setContrasenaHash(sha256(cliente.getContrasenaHash()));
        boolean guardado = repoMySQL.guardarUsuario(cliente);
        if (guardado) repoJson.guardarUsuario(cliente);
        return guardado;
    }

    @Override
    public boolean iniciarSesion(CredencialDto credenciales) {
        if (credenciales == null || credenciales.getCorreo() == null || credenciales.getContrasena() == null) return false;
        Cliente c = repoMySQL.buscarPorCorreo(credenciales.getCorreo().trim());
        if (c != null && c.getContrasenaHash() != null && c.getContrasenaHash().equals(sha256(credenciales.getContrasena()))) {
            this.usuarioLogueado = c;
            return true;
        }
        return false;
    }

    @Override
    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    @Override
    public Cliente getUsuarioLogueado() {
        return usuarioLogueado;
    }

    @Override
    public String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena) {
        if (usuarioLogueado == null) return "Debe iniciar sesión para actualizar sus datos.";
        if (nuevosDatos == null) return "Los datos del usuario no pueden ser nulos.";
        if (nuevosDatos.getNombreCompleto() == null || nuevosDatos.getNombreCompleto().isBlank())
            return "El nombre no puede estar vacío.";

        boolean cambiaContrasena = nuevaContrasena != null && !nuevaContrasena.isEmpty();
        if (cambiaContrasena && nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA)
            return "La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";

        String tipo = nuevosDatos.getTipoCliente() != null ? nuevosDatos.getTipoCliente() : usuarioLogueado.getTipoCliente();
        Cliente actualizado = new Cliente(
            usuarioLogueado.getId(), nuevosDatos.getNombreCompleto(), usuarioLogueado.getCorreo(),
            nuevosDatos.getDireccionEnvio(), nuevosDatos.getDepartamento(), nuevosDatos.getCiudad(),
            nuevosDatos.getTelefono(), tipo);

        actualizado.setContrasenaHash(cambiaContrasena ? sha256(nuevaContrasena) : usuarioLogueado.getContrasenaHash());

        if (!repoMySQL.actualizarUsuario(actualizado)) return "No se encontró el usuario para actualizar.";
        repoJson.actualizarUsuario(actualizado);
        this.usuarioLogueado = actualizado;
        return null;
    }

    @Override
    public boolean eliminarUsuarioLogueado(String contrasena) {
        if (usuarioLogueado == null || contrasena == null) return false;
        if (!sha256(contrasena).equals(usuarioLogueado.getContrasenaHash())) return false;

        if (gestionCompras != null) {
            for (ItemCarrito item : gestionCompras.obtenerCarritoActual().getItems()) {
                if (item.getLibro() != null) gestionLibros.ajustarStock(item.getLibro().getIsbn(), item.getCantidad());
            }
            gestionCompras.vaciarCarrito();
        }

        String id = usuarioLogueado.getId();
        if (!repoMySQL.eliminarUsuario(id)) return false;
        repoJson.eliminarUsuario(id);
        this.usuarioLogueado = null;
        return true;
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
