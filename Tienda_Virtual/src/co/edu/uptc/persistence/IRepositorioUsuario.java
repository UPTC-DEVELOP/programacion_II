package co.edu.uptc.persistence;

import co.edu.uptc.model.Cliente; // Reutilizando la clase existente
import java.util.List;

public interface IRepositorioUsuario {
    void crearUsuario(Cliente cliente);        // Create
    Cliente leerUsuario(String id);            // Read
    List<Cliente> obtenerTodosLosUsuarios();   // Read All
    void actualizarUsuario(Cliente cliente);   // Update
    void eliminarUsuario(String id);           // Delete
	Cliente leerUsuario(Object object);
}
