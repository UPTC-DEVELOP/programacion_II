package com.tiendavirtual.repositorio;

import java.util.List;
import com.tiendavirtual.modelo.Cliente;

public interface ClienteRepository {

    boolean guardar(Cliente cliente);

    boolean actualizar(Cliente cliente);

    boolean eliminar(int numeroDocumento);

    Cliente buscarPorDocumento(int numeroDocumento);

    List<Cliente> listar();

    Cliente iniciarSesion(String correo, String contrasenia);
}