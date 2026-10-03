package co.edu.uptc.interfaces;

import java.util.List;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.Cliente;

/**
 * INTERFAZ IGestionCliente  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * CONTRATO de la capa de NEGOCIO para el CRUD de clientes. La GUI depende de
 * esta interfaz y no de la clase concreta GestionCliente (DIP), igual que
 * el módulo admin depende de IGestionLibro.
 * El almacenamiento va por otro contrato: IClienteRepositorio.
 */
public interface IGestionCliente {

    void agregarCliente(Cliente cliente) throws ReglaNegocioException;

    void actualizarCliente(Cliente cliente) throws ReglaNegocioException;

    void eliminarCliente(String identificacion) throws ReglaNegocioException;

    List<Cliente> listarClientes();

    /** @return el cliente o null si no existe. */
    Cliente buscarCliente(String identificacion);

    /** @return el cliente o null si no existe. */
    Cliente buscarClientePorCorreo(String correo);

    /** @return el cliente si correo y contraseña coinciden, null en otro caso. */
    Cliente autenticar(String correo, String contrasenia);
}
