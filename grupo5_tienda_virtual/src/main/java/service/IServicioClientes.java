package service;

import java.util.List;

import model.Cliente;

/**
 * Contrato de la logica de negocio de clientes.
 *
 * Las ventanas dependen de esta interfaz (no del controlador concreto):
 * si cambia la logica de negocio, la interfaz grafica no se entera
 * (SOLID: Dependency Inversion / segregacion de interfaces).
 */
public interface IServicioClientes {

    boolean registrar(Cliente cliente);

    boolean registrarCliente(String tipo, String nombreCompleto, String correo,
            String direccion, String telefono, String contrasena);

    List<Cliente> listar();

    Cliente buscar(String correo);

    boolean actualizarDatos(String correo, String nombreCompleto, String direccion, String telefono);

    boolean eliminar(String correo);

    Cliente iniciarSesion(String correo, String contrasena);

    int cantidad();
}
