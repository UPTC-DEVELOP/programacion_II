package co.edu.uptc.negocio.acceso;

import java.util.regex.Pattern;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IAdministradorRepositorio;
import co.edu.uptc.interfaces.IGestionAcceso;
import co.edu.uptc.interfaces.IGestionCliente;
import co.edu.uptc.modelo.Administrador;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.Rol;

/**
 * CLASE GestionAcceso  (paquete: negocio.acceso)  implements IGestionAcceso
 * ---------------------------------------------------------------------------
 * REGLAS DE NEGOCIO del inicio de sesión y del registro:
 *  - la contraseña debe coincidir con la guardada,
 *  - el perfil elegido (Usuario / Admin) debe corresponder a la cuenta,
 *  - un correo no puede pertenecer a un cliente y a un administrador a la vez.
 * Los clientes los maneja IGestionCliente (reglas y conteo de intentos
 * fallidos); los administradores se guardan en IAdministradorRepositorio.
 * Solo conoce interfaces (DIP): no sabe si los datos están en RAM o en una BD.
 */
public class GestionAcceso implements IGestionAcceso {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._\\-#])[A-Za-z\\d@$!%*?&._\\-#]{8,}$";
    private static final String CREDENCIALES_INVALIDAS = "Correo o contraseña incorrectos.";

    private final IGestionCliente gestionCliente;
    private final IAdministradorRepositorio administradores;

    //constructor: inyeccion de dependencias
    public GestionAcceso(IGestionCliente gestionCliente, IAdministradorRepositorio administradores) {
        this.gestionCliente = gestionCliente;
        this.administradores = administradores;
    }

    @Override
    public Rol autenticar(String correo, String contrasenia, Rol perfil) throws ReglaNegocioException {
        if (vacio(correo) || vacio(contrasenia)) {
            throw new ReglaNegocioException("Ingrese el correo y la contraseña.");
        }
        String correoLimpio = correo.trim();
        Administrador admin = administradores.buscarPorCorreo(correoLimpio);

        if (perfil == Rol.ADMINISTRADOR) {
            if (admin == null) {
                if (gestionCliente.buscarClientePorCorreo(correoLimpio) != null) {
                    throw new ReglaNegocioException("Acceso denegado: no tiene permisos de Administrador.");
                }
                throw new ReglaNegocioException(CREDENCIALES_INVALIDAS);
            }
            if (!admin.getContrasenia().equals(contrasenia)) {
                throw new ReglaNegocioException(CREDENCIALES_INVALIDAS);
            }
            return Rol.ADMINISTRADOR;
        }

        if (admin != null) {
            throw new ReglaNegocioException("Su cuenta es de Administrador. Seleccione el perfil 'Admin'.");
        }
        // GestionCliente lleva la cuenta de intentos fallidos.
        if (gestionCliente.autenticar(correoLimpio, contrasenia) == null) {
            throw new ReglaNegocioException(CREDENCIALES_INVALIDAS);
        }
        return Rol.CLIENTE;
    }

    @Override
    public void registrarCliente(Cliente cliente) throws ReglaNegocioException {
        if (cliente.getCorreoElectronico() != null
                && administradores.buscarPorCorreo(cliente.getCorreoElectronico().trim()) != null) {
            throw new ReglaNegocioException("Este correo ya se encuentra registrado.");
        }
        // El resto de reglas (campos obligatorios, duplicados, formato) las aplica GestionCliente.
        gestionCliente.agregarCliente(cliente);
    }

    @Override
    public void registrarAdministrador(Administrador administrador) throws ReglaNegocioException {
        if (vacio(administrador.getPrimerNombre())) {
            throw new ReglaNegocioException("El nombre es obligatorio.");
        }
        String correo = administrador.getCorreoElectronico();
        if (vacio(correo) || !Pattern.matches(EMAIL_REGEX, correo.trim())) {
            throw new ReglaNegocioException("Correo inválido. Ejemplo: usuario@dominio.com");
        }
        if (vacio(administrador.getContrasenia()) || !Pattern.matches(PASSWORD_REGEX, administrador.getContrasenia())) {
            throw new ReglaNegocioException(
                    "Contraseña débil: mín. 8 caracteres, 1 mayúscula, 1 número y 1 carácter especial.");
        }
        if (administradores.buscarPorCorreo(correo.trim()) != null
                || gestionCliente.buscarClientePorCorreo(correo) != null) {
            throw new ReglaNegocioException("Este correo ya se encuentra registrado.");
        }
        administrador.setCorreoElectronico(correo.trim());
        administradores.guardar(administrador);
    }

    private static boolean vacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
