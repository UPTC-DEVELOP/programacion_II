package co.edu.uptc.negocio;

import java.util.Optional;
import java.util.regex.Pattern;

public class ClienteService {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^[0-9+()\\- ]{7,20}$");
    private static final int MIN_CONTRASENA = 6;

    private final ClienteRepositorio repositorio;
    private final BitacoraRepositorio bitacora;

    public ClienteService(ClienteRepositorio repositorio, BitacoraRepositorio bitacora) {
        this.repositorio = repositorio;
        this.bitacora = bitacora;
    }

    public Cliente registrarCliente(String nombre, String correo, String direccion,
                                    String telefono, TipoCliente tipo, String contrasena)
            throws ValidacionException {
        validarDatos(nombre, correo, direccion, telefono, tipo, contrasena, true);
        String correoNormalizado = correo.trim().toLowerCase();
        if (repositorio.buscarPorCorreo(correoNormalizado).isPresent()) {
            throw new ValidacionException("Ya existe un cliente registrado con el correo "
                    + correoNormalizado + ".");
        }
        Cliente cliente = new Cliente(nombre.trim(), correoNormalizado, direccion.trim(),
                telefono.trim(), tipo, PasswordUtil.hash(contrasena));
        repositorio.guardar(cliente);
        bitacora.registrar("REGISTRAR_CLIENTE", "correo=" + cliente.getCorreoElectronico()
                + ", tipo=" + cliente.getTipoCliente().getEtiqueta());
        return cliente;
    }

    public Cliente actualizarCliente(String correoActual, String nombre, String direccion,
                                     String telefono, TipoCliente tipo, String nuevaContrasena)
            throws ValidacionException {
        if (correoActual == null || correoActual.trim().isEmpty()) {
            throw new ValidacionException("No existe un cliente autenticado.");
        }
        Optional<Cliente> encontrado = repositorio.buscarPorCorreo(correoActual);
        if (!encontrado.isPresent()) {
            throw new ValidacionException("No existe el cliente indicado.");
        }
        validarDatosBasicos(nombre, correoActual, direccion, telefono, tipo);
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()
                && nuevaContrasena.length() < MIN_CONTRASENA) {
            throw new ValidacionException("La nueva contraseña debe tener mínimo "
                    + MIN_CONTRASENA + " caracteres.");
        }

        Cliente cliente = encontrado.get();
        cliente.setNombreCompleto(nombre.trim());
        cliente.setDireccionEnvio(direccion.trim());
        cliente.setTelefono(telefono.trim());
        cliente.setTipoCliente(tipo);
        if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
            cliente.setContrasenaHash(PasswordUtil.hash(nuevaContrasena));
        }
        repositorio.guardar(cliente);
        bitacora.registrar("ACTUALIZAR_CLIENTE", "correo=" + cliente.getCorreoElectronico());
        return cliente;
    }

    public Cliente autenticar(String correo, String contrasena) throws ValidacionException {
        if (correo == null || correo.trim().isEmpty() || contrasena == null || contrasena.isEmpty()) {
            throw new ValidacionException("Debe indicar correo y contraseña.");
        }
        String correoNormalizado = correo.trim().toLowerCase();
        Cliente cliente = repositorio.buscarPorCorreo(correoNormalizado)
                .orElseThrow(() -> new ValidacionException("Correo o contraseña incorrectos."));
        if (!PasswordUtil.hash(contrasena).equals(cliente.getContrasenaHash())) {
            throw new ValidacionException("Correo o contraseña incorrectos.");
        }
        bitacora.registrar("INICIO_SESION", "correo=" + cliente.getCorreoElectronico());
        return cliente;
    }

    public Optional<Cliente> buscarPorCorreo(String correo) {
        return repositorio.buscarPorCorreo(correo);
    }

    private void validarDatos(String nombre, String correo, String direccion, String telefono,
                               TipoCliente tipo, String contrasena, boolean validarContrasena)
            throws ValidacionException {
        validarDatosBasicos(nombre, correo, direccion, telefono, tipo);
        if (validarContrasena && (contrasena == null || contrasena.length() < MIN_CONTRASENA)) {
            throw new ValidacionException("La contraseña debe tener mínimo " + MIN_CONTRASENA + " caracteres.");
        }
    }

    private void validarDatosBasicos(String nombre, String correo, String direccion, String telefono,
                                     TipoCliente tipo) throws ValidacionException {
        validarTexto(nombre, "Nombre completo", 150);
        validarTexto(correo, "Correo electrónico", 150);
        validarTexto(direccion, "Dirección de envío", 250);
        validarTexto(telefono, "Teléfono", 20);
        if (!PATRON_CORREO.matcher(correo.trim()).matches()) {
            throw new ValidacionException("El correo electrónico no tiene un formato válido.");
        }
        if (!PATRON_TELEFONO.matcher(telefono.trim()).matches()) {
            throw new ValidacionException("El teléfono solo puede contener números y caracteres +, -, (, ), espacio.");
        }
        if (tipo == null) {
            throw new ValidacionException("Debe seleccionar el tipo de cliente.");
        }
    }

    private void validarTexto(String valor, String campo, int maximo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException("El campo \"" + campo + "\" es obligatorio.");
        }
        if (valor.trim().length() > maximo) {
            throw new ValidacionException("El campo \"" + campo + "\" no puede superar " + maximo + " caracteres.");
        }
    }
}
