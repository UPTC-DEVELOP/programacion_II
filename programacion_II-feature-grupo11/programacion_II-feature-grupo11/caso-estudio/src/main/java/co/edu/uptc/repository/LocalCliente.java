package co.edu.uptc.repository;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.model.Cliente;

public class LocalCliente implements IClienteRepositorio {

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
            if (clientes.get(i).getCedula().equals(cliente.getCedula())) {
                clientes.set(i, cliente);
                return;
            }
        }
    }

    @Override
    public void eliminar(String cedula) {
        clientes.removeIf(c -> c.getCedula().equals(cedula));
    }

    @Override
    public List<Cliente> listar() {
        return clientes;
    }
}