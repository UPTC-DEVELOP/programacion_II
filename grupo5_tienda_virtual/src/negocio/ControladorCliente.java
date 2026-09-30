package negocio;

import java.util.ArrayList;
import java.util.List;

/**
 * Administra en memoria los clientes de la tienda (operaciones CRUD).
 */
public class ControladorCliente {

    private List<Cliente> clientes;

    public ControladorCliente() {
        this.clientes = new ArrayList<Cliente>();
    }

    public boolean registrar(Cliente cliente) {
        if (cliente == null || cliente.getCorreo() == null
                || cliente.getCorreo().trim().isEmpty()
                || buscar(cliente.getCorreo()) != null) {
            return false;
        }
        clientes.add(cliente);
        return true;
    }

    public List<Cliente> listar() {
        return new ArrayList<Cliente>(clientes);
    }

    public Cliente buscar(String correo) {
        if (correo == null) {
            return null;
        }
        for (Cliente cliente : clientes) {
            if (correo.equalsIgnoreCase(cliente.getCorreo())) {
                return cliente;
            }
        }
        return null;
    }

    public boolean actualizarDatos(String correo, String nombreCompleto,
            String direccion, String telefono) {
        Cliente actual = buscar(correo);
        if (actual == null) {
            return false;
        }
        actual.setNombreCompleto(nombreCompleto);
        actual.setDireccion(direccion);
        actual.setTelefono(telefono);
        return true;
    }

    public boolean eliminar(String correo) {
        Cliente actual = buscar(correo);
        if (actual == null) {
            return false;
        }
        clientes.remove(actual);
        return true;
    }

    public Cliente iniciarSesion(String correo, String contrasena) {
        Cliente cliente = buscar(correo);
        if (cliente != null && cliente.getContrasena() != null
                && cliente.getContrasena().equals(contrasena)) {
            return cliente;
        }
        return null;
    }

    public int cantidad() {
        return clientes.size();
    }
}
