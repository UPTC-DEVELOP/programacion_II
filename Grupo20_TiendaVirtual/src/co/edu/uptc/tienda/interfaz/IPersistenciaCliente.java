package co.edu.uptc.tienda.interfaz;

import java.util.List;

import co.edu.uptc.tienda.excepcion.ClienteExcepcion;
import co.edu.uptc.tienda.modelo.Cliente;

public interface IPersistenciaCliente {

    void guardar(List<Cliente> clientes) throws ClienteExcepcion;

    List<Cliente> leer() throws ClienteExcepcion;
}
