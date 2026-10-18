package co.edu.uptc.tienda.interfaces;

import java.util.List;
import co.edu.uptc.tienda.modelo.Cliente;

public interface IGestionCliente {

    void agregar(Cliente cliente);

    List<Cliente> listar();

    Cliente buscar(String identificacion);

    boolean actualizar(Cliente cliente);

    boolean eliminar(String identificacion);
}