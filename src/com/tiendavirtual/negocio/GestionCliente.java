package com.tiendavirtual.negocio;

import java.util.List;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.repositorio.ClienteRepository;
import com.tiendavirtual.repositorio.ClienteRepositoryMySQL;
import com.tiendavirtual.util.Contrasenias;

public class GestionCliente {

    private ClienteRepository repositorio;

    public GestionCliente() {
        repositorio = new ClienteRepositoryMySQL();
    }

    public GestionCliente(ClienteRepository repositorio) {
        this.repositorio = repositorio;
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

        cliente.setContrasenia(Contrasenias.hashear(cliente.getContrasenia()));
        return repositorio.guardar(cliente);
    }

    public Cliente iniciarSesion(String correo, String contrasenia) {
        if (correo == null || contrasenia == null) {
            return null;
        }

        Cliente cliente = repositorio.buscarPorCorreo(correo.trim());

        if (cliente == null
                || !Contrasenias.verificar(contrasenia, cliente.getContrasenia())) {
            return null;
        }

        if (!Contrasenias.esHash(cliente.getContrasenia())) {
            // Contraseña antigua en texto plano: se convierte a hash.
            try {
                cliente.setContrasenia(Contrasenias.hashear(contrasenia));
                repositorio.actualizar(cliente);
            } catch (RuntimeException e) {
                // Si aún no se ejecutó el script SQL, el inicio de sesión sigue funcionando.
            }
        }

        return cliente;
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

        String nueva = actualizado.getContrasenia();

        if (nueva == null || nueva.isEmpty()
                || nueva.equals(existente.getContrasenia())) {
            // Sin cambio de contraseña: se conserva la guardada.
            actualizado.setContrasenia(existente.getContrasenia());
        } else if (contraseniaValida(nueva)) {
            actualizado.setContrasenia(Contrasenias.hashear(nueva));
        } else {
            return false;
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