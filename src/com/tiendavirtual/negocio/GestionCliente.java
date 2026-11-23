package com.tiendavirtual.negocio;

import java.util.List;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.repositorio.ClienteRepository;
import com.tiendavirtual.repositorio.ClienteRepositoryMySQL;

public class GestionCliente {

    private ClienteRepository repositorio;

    public GestionCliente() {
        repositorio = new ClienteRepositoryMySQL();
    }

    public boolean registrarCliente(Cliente cliente) {

        if (!camposCompletos(cliente)) {
            return false;
        }

        if (existeCorreo(cliente.getCorreo())) {
            return false;
        }

        if (existeDocumento(cliente.getNumeroDocumento())) {
            return false;
        }

        if (!contraseniaValida(cliente.getContrasenia())) {
            return false;
        }

        return repositorio.guardar(cliente);
    }

    public Cliente iniciarSesion(String correo, String contrasenia) {
        return repositorio.iniciarSesion(correo, contrasenia);
    }

    public boolean actualizarCliente(Cliente actualizado) {

        if (!camposCompletos(actualizado)) {
            return false;
        }

        Cliente existente =
                repositorio.buscarPorDocumento(actualizado.getNumeroDocumento());

        if (existente == null) {
            return false;
        }

        for (Cliente otro : repositorio.listar()) {
            if (otro.getNumeroDocumento() != actualizado.getNumeroDocumento()
                    && otro.getCorreo().equalsIgnoreCase(actualizado.getCorreo())) {
                return false;
            }
        }

        if (actualizado.getContrasenia() != null
                && !actualizado.getContrasenia().isEmpty()
                && !contraseniaValida(actualizado.getContrasenia())) {
            return false;
        }

        if (actualizado.getContrasenia() == null
                || actualizado.getContrasenia().isEmpty()) {
            actualizado.setContrasenia(existente.getContrasenia());
        }

        return repositorio.actualizar(actualizado);
    }

    public boolean eliminarCliente(int numeroDocumento) {
        return repositorio.eliminar(numeroDocumento);
    }

    public List<Cliente> listarClientes() {
        return repositorio.listar();
    }

    private boolean existeCorreo(String correo) {
        for (Cliente cliente : repositorio.listar()) {
            if (cliente.getCorreo().equalsIgnoreCase(correo)) {
                return true;
            }
        }
        return false;
    }

    private boolean existeDocumento(int numeroDocumento) {
        return repositorio.buscarPorDocumento(numeroDocumento) != null;
    }

    private boolean contraseniaValida(String contrasenia) {
        return contrasenia != null && contrasenia.length() >= 6;
    }

    private boolean camposCompletos(Cliente cliente) {
        return cliente != null
                && cliente.getNombre() != null && !cliente.getNombre().isEmpty()
                && cliente.getApellido() != null && !cliente.getApellido().isEmpty()
                && cliente.getCorreo() != null && !cliente.getCorreo().isEmpty()
                && cliente.getTelefono() != null && !cliente.getTelefono().isEmpty();
    }

public Cliente buscarPorDocumento(int numeroDocumento) {
    return repositorio.buscarPorDocumento(numeroDocumento);
}
}