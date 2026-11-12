package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;

import co.edu.uptc.tienda.modelo.Cliente;

public interface IGestionCliente {

    void guardar(Cliente cliente);

    ArrayList<Cliente> listar();

    Cliente buscarPorId(String id);

    void actualizar(Cliente cliente);

    void eliminar(String id);
}