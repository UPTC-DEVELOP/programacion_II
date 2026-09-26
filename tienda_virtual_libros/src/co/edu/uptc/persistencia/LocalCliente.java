package co.edu.uptc.tienda.persistencia;

import co.edu.uptc.tienda.interfaces.IGestionCliente;
import co.edu.uptc.tienda.modelo.Cliente;
import java.util.ArrayList;
import java.util.List;

public class LocalCliente implements IGestionCliente {

   
    private List<Cliente> listaClientes;

    public LocalCliente() {
        this.listaClientes = new ArrayList<>();
    }

    @Override
    public boolean agregarCliente(Cliente cliente) {
        return listaClientes.add(cliente);
    }

    @Override
    public Cliente buscarPorIdentificacion(String identificacion) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getIdentificacion().equalsIgnoreCase(identificacion)) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        for (Cliente cliente : listaClientes) {
            if (cliente.getCorreoElectronico().equalsIgnoreCase(correo)) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return new ArrayList<>(listaClientes);
    }

    @Override
    public boolean actualizarCliente(Cliente clienteActualizado) {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getIdentificacion().equalsIgnoreCase(clienteActualizado.getIdentificacion())) {
                listaClientes.set(i, clienteActualizado);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminarCliente(String identificacion) {
        Cliente c = buscarPorIdentificacion(identificacion);
        if (c != null) {
            return listaClientes.remove(c);
        }
        return false;
    }
}
