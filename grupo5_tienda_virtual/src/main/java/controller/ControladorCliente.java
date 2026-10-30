package controller;

import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import persistencia.IRepositorio;
import service.IServicioClientes;

/**
 * Logica de negocio de clientes.
 *
 * Implementa la interfaz IServicioClientes (la que usan las ventanas) y delega
 * el almacenamiento en un IRepositorio&lt;Cliente&gt; que se inyecta por constructor:
 * el controlador NO sabe si los datos viven en memoria, en JSON o en una base
 * de datos (SOLID: Dependency Inversion + Single Responsibility).
 */
public class ControladorCliente implements IServicioClientes {

    private final IRepositorio<Cliente> repositorio;

    public ControladorCliente(IRepositorio<Cliente> repositorio) {
        this.repositorio = repositorio;
        this.repositorio.cargar();
    }

    @Override
    public boolean registrar(Cliente cliente) {
        if (cliente == null || cliente.getCorreo() == null
                || cliente.getCorreo().trim().isEmpty()
                || buscar(cliente.getCorreo()) != null) {
            return false;
        }
        repositorio.agregar(cliente);
        repositorio.guardar();
        return true;
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<Cliente>(repositorio.listar());
    }

    @Override
    public Cliente buscar(String correo) {
        if (correo == null) {
            return null;
        }
        for (Cliente cliente : repositorio.listar()) {
            if (correo.equalsIgnoreCase(cliente.getCorreo())) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public boolean actualizarDatos(String correo, String nombreCompleto,
            String direccion, String telefono) {
        Cliente actual = buscar(correo);
        if (actual == null) {
            return false;
        }
        actual.setNombreCompleto(nombreCompleto);
        actual.setDireccion(direccion);
        actual.setTelefono(telefono);
        repositorio.guardar();
        return true;
    }

    @Override
    public boolean eliminar(String correo) {
        Cliente actual = buscar(correo);
        if (actual == null) {
            return false;
        }
        repositorio.eliminar(actual);
        repositorio.guardar();
        return true;
    }

    @Override
    public Cliente iniciarSesion(String correo, String contrasena) {
        Cliente cliente = buscar(correo);
        if (cliente != null && cliente.getContrasena() != null
                && cliente.getContrasena().equals(contrasena)) {
            return cliente;
        }
        return null;
    }

    @Override
    public int cantidad() {
        return repositorio.listar().size();
    }
}
