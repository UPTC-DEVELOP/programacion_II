package com.letsread.repository;

import com.letsread.model.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de persistencia de clientes (C-R-U-D).
 * Implementaciones: ClienteJsonRepositorio y ClienteBdRepositorio.
 */
public interface ClienteRepositorio {
    void guardar(Cliente cliente);                    // Create
    Optional<Cliente> buscarPorCorreo(String correo); // Read (uno)
    List<Cliente> listar();                           // Read (todos)
    void actualizar(Cliente cliente);                 // Update
    boolean eliminar(String correo);                  // Delete
}