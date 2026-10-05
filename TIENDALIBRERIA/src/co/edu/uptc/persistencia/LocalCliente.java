package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.interfaces.IGestionCliente;

public class LocalCliente implements IGestionCliente {

    private List<Cliente> clientes;

    public LocalCliente() {
        clientes = new ArrayList<>();
    }

    @Override
    public void guardar(Cliente cliente) {
        clientes.add(cliente);
    }

    @Override
    public void actualizar(Cliente cliente) {

        for (int i = 0; i < clientes.size(); i++) {

            if (clientes.get(i).getCorreo()
                    .equals(cliente.getCorreo())) {

                clientes.set(i, cliente);
            }
        }
    }

    @Override
    public void eliminar(String correo) {

        for (Cliente cliente : clientes) {

            if (cliente.getCorreo().equals(correo)) {

                clientes.remove(cliente);
                break;
            }
        }
    }

    @Override
    public List<Cliente> listar() {
        return clientes;
    }
}