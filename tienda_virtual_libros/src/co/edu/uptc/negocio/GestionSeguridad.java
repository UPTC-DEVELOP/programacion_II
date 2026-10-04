package co.edu.uptc.negocio;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.interfaces.IGestionSeguridad;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.dto.CredencialDto;

/**
 * Clase que gestiona la autenticación y seguridad del sistema.
 *
 * PRINCIPIO SOLID aplicado:
 * - SRP: Esta clase SOLO se encarga de autenticación y seguridad.
 * - DIP: Depende de la abstracción IClienteRepositorio, no de una clase concreta.
 *
 * @author Grupo 7
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
        Cliente cliente = repositorioClientes.buscarPorCorreo(credencial.getCorreoElectronico());

        if (cliente != null && cliente.validarContrasena(credencial.getContrasena())) {
            cliente.setIntentosFallidos(0);
            this.clienteAutenticado = cliente;
            return true;
        }
        if (cliente != null) {
            cliente.setIntentosFallidos(cliente.getIntentosFallidos() + 1);
        }
        return false;
    }

    @Override
    public void cerrarSesion() {
        this.clienteAutenticado = null;
    }

    @Override
    public boolean haySesionActiva() {
        return clienteAutenticado != null;
    }

    public Cliente getClienteAutenticado() {
        return clienteAutenticado;
    }
}
