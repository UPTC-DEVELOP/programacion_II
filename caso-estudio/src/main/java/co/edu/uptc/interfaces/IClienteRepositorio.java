package co.edu.uptc.interfaces;

import co.edu.uptc.modelo.Cliente;
import java.util.List;

/**
 * INTERFAZ IClienteRepositorio  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de PERSISTENCIA de clientes. La capa de negocio (GestionCliente)
 * solo conoce esta interfaz, nunca la clase concreta que guarda los datos.
 * Hoy la implementa LocalCliente (lista en RAM); para usar
 * archivos, XML o JDBC basta crear otra clase que la implemente y cambiar
 * UNA línea en AppLibros (OCP / DIP).
 */
public interface IClienteRepositorio {

    void guardar(Cliente cliente);
    void actualizar(Cliente cliente);          // reemplaza el que tenga el mismo ID
    void eliminar(String identificacion);
    void eliminarPorId(int idCliente);
    List<Cliente> listar();
    Cliente buscar(String identificacion);
    Cliente buscarPorCorreo(String correo);
    Cliente buscarPorId(int idCliente);
}
