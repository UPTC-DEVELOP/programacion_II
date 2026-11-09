package co.edu.uptc.negocio;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionSeguridad;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.dto.CredencialDto;
import co.edu.uptc.persistencia.conexion.ConexionDB;

/**
 * Clase que gestiona la autenticación y seguridad del sistema.
 *
 * PRINCIPIO SOLID aplicado:
 * - SRP: Esta clase SOLO se encarga de autenticación y seguridad.
 * - DIP: Depende de la abstracción IClienteRepositorio, no de una clase concreta.
 *
 * @version 1.0
 */
public class GestionSeguridad implements IGestionSeguridad {

    private final IClienteRepositorio repositorioClientes;
    private Cliente clienteAutenticado;

    public GestionSeguridad(IClienteRepositorio repositorioClientes) {
        this.repositorioClientes = repositorioClientes;
    }

    @Override
    public boolean iniciarSesion(CredencialDto credencial) {
        if (credencial == null) {
            return false;
        }
        /*modificacion:Brayan Panqueva*/
        String correoOriginal = credencial.getCorreoElectronico().trim().toLowerCase();
        String correo = normalizarCorreoAdministrativo(correoOriginal);
        ConexionDB.establecerUsuarioSesion(correoOriginal);
        Cliente cliente = repositorioClientes.buscarPorCorreo(correoOriginal);
        if (cliente == null) {
            ConexionDB.establecerUsuarioSesion(correo);
            cliente = repositorioClientes.buscarPorCorreo(correo);
        }

        // se compara el hash de la contraseña ingresada con el hash almacenado
        String hashIngresado = CifradorContrasena.hash(credencial.getContrasena());
        if (cliente != null && cliente.validarContrasena(hashIngresado)) {
            if (!correoOriginal.equals(correo) && correoOriginal.equals(cliente.getCorreoElectronico())) {
                cliente.setCorreoElectronico(correo);
                repositorioClientes.actualizar(cliente);
                ConexionDB.establecerUsuarioSesion(correo);
            }
            cliente.setIntentosFallidos(0);
            repositorioClientes.actualizar(cliente);
            this.clienteAutenticado = cliente;
            return true;
        }
        if (cliente != null) {
            cliente.setIntentosFallidos(cliente.getIntentosFallidos() + 1);
            repositorioClientes.actualizar(cliente);
        }
        return false;
    }

    @Override
    public void cerrarSesion() {
        this.clienteAutenticado = null;
        ConexionDB.establecerUsuarioSesion("");
    }

    @Override
    public boolean haySesionActiva() {
        return clienteAutenticado != null;
    }

    public Cliente getClienteAutenticado() {
        return clienteAutenticado;
    }

    private String normalizarCorreoAdministrativo(String correo) {
        String normalizado = correo == null ? "" : correo.trim().toLowerCase();
        return normalizado.replaceFirst("@co\\.edu\\.uptc$", "@uptc.edu.co");
    }
}
