package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.modelo.Cliente;

public interface IGestionCliente {

    void guardar(Cliente cliente);

    void actualizar(Cliente cliente);

    void eliminar(String correo);

    List<Cliente> listar();
}