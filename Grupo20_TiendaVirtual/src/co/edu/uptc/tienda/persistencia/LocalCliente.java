package co.edu.uptc.tienda.persistencia;

import java.util.ArrayList;

import co.edu.uptc.tienda.modelo.Cliente;

public class LocalCliente {

    private ArrayList<Cliente> clientes;

    public LocalCliente() {
        clientes = new ArrayList<>();
    }

    public void guardar(Cliente cliente) {
        clientes.add(cliente);
    }

    public ArrayList<Cliente> listar() {
        return clientes;
    }

    public Cliente buscarPorId(String id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }

        return null;
    }

    public void actualizar(Cliente cliente) {
        Cliente clienteExistente = buscarPorId(cliente.getId());

        if (clienteExistente != null) {
            clienteExistente.setNombreCompleto(cliente.getNombreCompleto());
            clienteExistente.setCorreoElectronico(cliente.getCorreoElectronico());
            clienteExistente.setDireccionEnvio(cliente.getDireccionEnvio());
            clienteExistente.setCelular(cliente.getCelular());
            clienteExistente.setTipoCliente(cliente.getTipoCliente());
        }
    }

    public void eliminar(String id) {
        Cliente cliente = buscarPorId(id);

        if (cliente != null) {
            clientes.remove(cliente);
        }
    }
}