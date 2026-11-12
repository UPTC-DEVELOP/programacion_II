package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;

import co.edu.uptc.tienda.modelo.Cliente;

public class ClienteConfig {

    private GestionCliente gestionCliente;

    public ClienteConfig() {
        gestionCliente = new GestionCliente();
    }

    public void guardar(Cliente cliente) {
        gestionCliente.guardar(cliente);
    }

    public ArrayList<Cliente> listar() {
        return gestionCliente.listar();
    }

    public Cliente buscarPorId(String id) {
        return gestionCliente.buscarPorId(id);
    }

    public void actualizar(Cliente cliente) {
        gestionCliente.actualizar(cliente);
    }

    public void eliminar(String id) {
        gestionCliente.eliminar(id);
    }
}