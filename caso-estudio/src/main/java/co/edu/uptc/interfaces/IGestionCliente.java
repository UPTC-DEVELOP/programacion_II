package co.edu.uptc.interfaces;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.ClienteDto;
import java.util.List;

/**
 * INTERFAZ IGestionCliente  (paquete: interfaces)
 * ---------------------------------------------------------------------------
 * Contrato de la capa de NEGOCIO para el CRUD de clientes.
 * La capa de presentación (GUI) depende de esta abstracción (DIP) y nunca de
 * la clase concreta GestionCliente.
 */
public interface IGestionCliente {

    /**
     * Registra (CREATE) un nuevo cliente en el sistema.
     * @param clienteDto DTO con los datos del cliente
     * @return true si se registró exitosamente
     */
    boolean registrarCliente(ClienteDto clienteDto) throws ReglaNegocioException;

    /**
     * Actualiza (UPDATE) los datos de un cliente existente.
     * @param clienteDto DTO con los datos actualizados
     * @return true si se actualizó exitosamente
     */
    boolean actualizarCliente(ClienteDto clienteDto) throws ReglaNegocioException;

    /**
     * Elimina (DELETE) un cliente del sistema.
     * @param idCliente ID del cliente a eliminar
     * @return true si se eliminó exitosamente
     */
    boolean eliminarCliente(int idCliente) throws ReglaNegocioException;

    /**
     * Busca (READ) un cliente por su ID.
     * @param idCliente ID del cliente
     * @return DTO del cliente o null si no existe
     */
    ClienteDto buscarPorId(int idCliente);

    /**
     * Busca (READ) un cliente por su número de identificación.
     * @param identificacion identificación del cliente
     * @return DTO del cliente o null si no existe
     */
    ClienteDto buscarPorIdentificacion(String identificacion);

    /**
     * Lista todos los clientes registrados (READ).
     * @return Lista de DTOs de clientes
     */
    List<ClienteDto> listarClientes();

    /**
     * Filtra clientes por nombre, correo o identificación (READ).
     * @param texto texto de búsqueda
     * @return Lista de DTOs de clientes que coinciden con el texto
     */
    List<ClienteDto> buscarClientes(String texto);

    /**
     * Inicia sesión con las credenciales del cliente.
     * @param correo Correo electrónico
     * @param contrasena Contraseña
     * @return true si las credenciales son válidas
     */
    boolean iniciarSesion(String correo, String contrasena);
}
