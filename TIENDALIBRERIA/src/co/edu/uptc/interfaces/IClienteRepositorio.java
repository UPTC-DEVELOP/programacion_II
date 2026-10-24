package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.model.Cliente;

public interface IClienteRepositorio {

    void guardar(Cliente cliente);

    void actualizar(Cliente cliente);

    void eliminar(String correo);

    List<Cliente> listar();
}