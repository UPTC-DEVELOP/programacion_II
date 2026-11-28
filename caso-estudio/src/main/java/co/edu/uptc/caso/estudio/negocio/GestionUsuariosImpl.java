// Servicio de usuarios: coordina autenticación, perfil y persistencia en MySQL y JSON.
package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import co.edu.uptc.caso.estudio.negocio.dto.CredencialDto;
import co.edu.uptc.caso.estudio.persistence.IRegistradorOperaciones;
import co.edu.uptc.caso.estudio.persistence.IRepositorioCarrito;
import co.edu.uptc.caso.estudio.persistence.IRepositorioUsuario;

public class GestionUsuariosImpl implements IGestionUsuarios {

    public static final int LONGITUD_MINIMA_CONTRASENA = SeguridadUsuario.LONGITUD_MINIMA_CONTRASENA;

    private final IRepositorioUsuario repoMySQL;
    private final IRepositorioUsuario repoJson;
    private final IRepositorioCarrito repoCarritoJson;
    private final IRegistradorOperaciones registradorOperaciones;
    private final SeguridadUsuario seguridadUsuario;
    private Cliente usuarioLogueado;

    public GestionUsuariosImpl(IRepositorioUsuario repoMySQL, IRepositorioUsuario repoJson,
                               IRepositorioCarrito repoCarritoJson,
                               IRegistradorOperaciones registradorOperaciones) {
        this.repoMySQL = repoMySQL;
        this.repoJson = repoJson;
        this.repoCarritoJson = repoCarritoJson;
        this.registradorOperaciones = registradorOperaciones;
        this.seguridadUsuario = new SeguridadUsuario();
    }

    // Valida datos del cliente, cifra la contraseña con SHA-256 y exige persistencia en MySQL
    @Override
    public String registrarCliente(Cliente cliente) {
        String errorValidacion = seguridadUsuario.validarRegistro(cliente);
        if (errorValidacion != null) {
            return errorValidacion;
        }

        String idLimpio = cliente.getId() != null ? cliente.getId().trim() : "";
        String correoLimpio = cliente.getCorreo() != null ? cliente.getCorreo().trim() : "";

        if (buscarPorIdConRespaldo(idLimpio) != null) {
            return "Ya existe un usuario registrado con ese ID.";
        }
        if (buscarPorCorreoConRespaldo(correoLimpio) != null) {
            return "Ya existe un usuario registrado con ese Correo.";
        }

        String clavePlana = cliente.getContrasenaHash();
        cliente.setContrasenaHash(seguridadUsuario.sha256(clavePlana));

        boolean okMySQL = repoMySQL != null && repoMySQL.guardarUsuario(cliente);
        if (!okMySQL) {
            return "No se pudo guardar el usuario en MySQL. Verifica la conexión y la base de datos.";
        }

        if (repoJson != null) {
            repoJson.guardarUsuario(cliente);
        }

        registrarEvento("USUARIO REGISTRADO - ID: " + cliente.getId()
                + " | Correo: " + cliente.getCorreo());
        return null;
    }

    // Autentica al usuario en MySQL (sincronizando desde JSON si hace falta) y fija la sesión
    @Override
    public boolean iniciarSesion(CredencialDto credenciales) {
        if (credenciales == null || credenciales.getCorreo() == null || credenciales.getContrasena() == null) {
            return false;
        }

        String correoLimpio = credenciales.getCorreo().trim();
        String hashIngresado = seguridadUsuario.sha256(credenciales.getContrasena());

        Cliente c = repoMySQL != null ? repoMySQL.buscarPorCorreo(correoLimpio) : null;

        if (c == null && repoJson != null) {
            Cliente desdeJson = repoJson.buscarPorCorreo(correoLimpio);
            if (desdeJson != null && desdeJson.getContrasenaHash() != null
                    && desdeJson.getContrasenaHash().equals(hashIngresado)
                    && repoMySQL != null && repoMySQL.guardarUsuario(desdeJson)) {
                c = repoMySQL.buscarPorCorreo(correoLimpio);
                if (c == null) {
                    c = desdeJson;
                }
            }
        }

        if (c != null && c.getContrasenaHash() != null && c.getContrasenaHash().equals(hashIngresado)) {
            this.usuarioLogueado = c;
            registrarEvento("INICIO DE SESIÓN - Usuario: " + c.getCorreo());
            return true;
        }
        return false;
    }

    // Cierra la sesión activa y registra el evento
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
        if (cambiaContrasena) {
            String errorClave = seguridadUsuario.validarContrasena(nuevaContrasena, "La contraseña");
            if (errorClave != null) {
                return errorClave;
            }
        }

        String tipo = nuevosDatos.getTipoCliente() != null
                ? nuevosDatos.getTipoCliente()
                : usuarioLogueado.getTipoCliente();
        String hashFinal = cambiaContrasena
                ? seguridadUsuario.sha256(nuevaContrasena)
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
        String errorClave = seguridadUsuario.validarContrasena(nuevaContrasena, "La nueva contraseña");
        if (errorClave != null) {
            return errorClave;
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
                seguridadUsuario.sha256(nuevaContrasena));

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
        if (!seguridadUsuario.sha256(contrasena).equals(usuarioLogueado.getContrasenaHash())) {
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

    // Escribe el evento a través del registrador inyectado si está configurado
    private void registrarEvento(String mensaje) {
        if (registradorOperaciones != null) {
            registradorOperaciones.registrar(mensaje);
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}