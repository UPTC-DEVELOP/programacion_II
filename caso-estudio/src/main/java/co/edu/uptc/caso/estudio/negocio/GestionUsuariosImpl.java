// Servicio de usuarios: valida los datos, cifra las contraseñas con SHA-256 y guarda en MySQL y JSON.
package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
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

    // Valida datos del cliente, cifra la contraseña con SHA-256 y persiste en MySQL y JSON
    @Override
    public String registrarCliente(Cliente cliente) {
        if (cliente == null) {
            return "Los datos del cliente son obligatorios.";
        }
        if (esVacio(cliente.getId())) {
            return "El ID / Cédula es obligatorio.";
        }
        if (esVacio(cliente.getNombreCompleto())) {
            return "El nombre completo es obligatorio.";
        }
        if (cliente.getCorreo() == null
                || !cliente.getCorreo().trim().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Ingrese un correo electrónico con formato válido (ej. usuario@dominio.com).";
        }
        if (cliente.getTelefono() != null && !esVacio(cliente.getTelefono())
                && !cliente.getTelefono().trim().matches("\\d{7,15}")) {
            return "El teléfono debe contener solo números (entre 7 y 15 dígitos).";
        }

        String clavePlana = cliente.getContrasenaHash();
        if (clavePlana == null || clavePlana.length() < LONGITUD_MINIMA_CONTRASENA) {
            return "La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";
        }

        if (buscarPorIdConRespaldo(cliente.getId()) != null) {
            return "Ya existe un usuario registrado con ese ID.";
        }
        if (buscarPorCorreoConRespaldo(cliente.getCorreo().trim()) != null) {
            return "Ya existe un usuario registrado con ese Correo.";
        }

        cliente.setContrasenaHash(sha256(clavePlana));
        boolean okMySQL = repoMySQL != null && repoMySQL.guardarUsuario(cliente);
        boolean okJson = repoJson != null && repoJson.guardarUsuario(cliente);

        if (okMySQL || okJson) {
            registrarEvento("USUARIO REGISTRADO - ID: " + cliente.getId()
                    + " | Correo: " + cliente.getCorreo());
            return null;
        }
        return "No se pudo guardar el usuario en la persistencia.";
    }

    // Autentica al usuario comparando el hash SHA-256 de la contraseña ingresada
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

    // Cierra la sesión activa y registra el evento en el log TXT
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

    // Actualiza perfil y opcionalmente la contraseña del usuario autenticado
    @Override
    public String actualizarUsuario(Cliente nuevosDatos, String nuevaContrasena) {
        if (usuarioLogueado == null) {
            return "Debe iniciar sesión para actualizar sus datos.";
        }
        if (nuevosDatos == null) {
            return "Los datos del usuario no pueden ser nulos.";
        }
        if (esVacio(nuevosDatos.getNombreCompleto())) {
            return "El nombre no puede estar vacío.";
        }

        boolean cambiaContrasena = nuevaContrasena != null && !nuevaContrasena.isEmpty();
        if (cambiaContrasena && nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            return "La contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";
        }

        String tipo = nuevosDatos.getTipoCliente() != null
                ? nuevosDatos.getTipoCliente()
                : usuarioLogueado.getTipoCliente();
        String hashFinal = cambiaContrasena
                ? sha256(nuevaContrasena)
                : usuarioLogueado.getContrasenaHash();

        Cliente actualizado = FabricaClientes.crear(
                usuarioLogueado.getId(),
                nuevosDatos.getNombreCompleto(),
                usuarioLogueado.getCorreo(),
                nuevosDatos.getDireccionEnvio(),
                nuevosDatos.getDepartamento(),
                nuevosDatos.getCiudad(),
                nuevosDatos.getTelefono(),
                tipo,
                hashFinal);

        boolean okMySQL = repoMySQL != null && repoMySQL.actualizarUsuario(actualizado);
        boolean okJson = repoJson != null && repoJson.actualizarUsuario(actualizado);

        if (!okMySQL && !okJson) {
            return "No se encontró el usuario para actualizar.";
        }

        this.usuarioLogueado = actualizado;
        registrarEvento("USUARIO ACTUALIZADO - ID: " + actualizado.getId()
                + " | Correo: " + actualizado.getCorreo());
        return null;
    }

    // Verifica ID y correo y actualiza la contraseña cifrada con SHA-256
    @Override
    public String restablecerContrasena(String id, String correo, String nuevaContrasena) {
        if (esVacio(id)) {
            return "Ingrese su ID / Cédula para verificar su identidad.";
        }
        if (esVacio(correo)) {
            return "Ingrese su correo electrónico registrado.";
        }
        if (nuevaContrasena == null || nuevaContrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            return "La nueva contraseña debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";
        }

        Cliente encontrado = buscarPorIdConRespaldo(id.trim());
        if (encontrado == null || encontrado.getCorreo() == null
                || !encontrado.getCorreo().trim().equalsIgnoreCase(correo.trim())) {
            return "Los datos no coinciden con ningún usuario registrado.";
        }

        Cliente actualizado = FabricaClientes.crear(
                encontrado.getId(),
                encontrado.getNombreCompleto(),
                encontrado.getCorreo(),
                encontrado.getDireccionEnvio(),
                encontrado.getDepartamento(),
                encontrado.getCiudad(),
                encontrado.getTelefono(),
                encontrado.getTipoCliente(),
                sha256(nuevaContrasena));

        boolean okMySQL = repoMySQL != null && repoMySQL.actualizarUsuario(actualizado);
        boolean okJson = repoJson != null && repoJson.actualizarUsuario(actualizado);

        if (!okMySQL && !okJson) {
            return "No se pudo actualizar la contraseña en el repositorio.";
        }

        if (usuarioLogueado != null && usuarioLogueado.getId().equals(actualizado.getId())) {
            this.usuarioLogueado = actualizado;
        }

        registrarEvento("CONTRASEÑA RESTABLECIDA - ID: " + actualizado.getId()
                + " | Correo: " + actualizado.getCorreo());
        return null;
    }

    // Elimina la cuenta del usuario autenticado tras confirmar su contraseña actual
    @Override
    public boolean eliminarUsuarioLogueado(String contrasena) {
        if (usuarioLogueado == null || contrasena == null) {
            return false;
        }
        if (!sha256(contrasena).equals(usuarioLogueado.getContrasenaHash())) {
            return false;
        }

        String id = usuarioLogueado.getId();
        String correo = usuarioLogueado.getCorreo();

        boolean okMySQL = repoMySQL != null && repoMySQL.eliminarUsuario(id);
        boolean okJson = repoJson != null && repoJson.eliminarUsuario(id);

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

    // Busca un cliente por ID en MySQL y recurre a JSON si no aparece
    private Cliente buscarPorIdConRespaldo(String id) {
        Cliente c = repoMySQL != null ? repoMySQL.buscarPorId(id) : null;
        if (c != null) {
            return c;
        }
        return repoJson != null ? repoJson.buscarPorId(id) : null;
    }

    // Busca un cliente por correo en MySQL y recurre a JSON si no aparece
    private Cliente buscarPorCorreoConRespaldo(String correo) {
        Cliente c = repoMySQL != null ? repoMySQL.buscarPorCorreo(correo) : null;
        if (c != null) {
            return c;
        }
        return repoJson != null ? repoJson.buscarPorCorreo(correo) : null;
    }

    // Escribe el evento en el archivo de registro si está configurado
    private void registrarEvento(String mensaje) {
        if (registradorTxt != null) {
            registradorTxt.registrar(mensaje);
        }
    }

    // Calcula el digest SHA-256 en minúsculas hexadecimal
    private String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}