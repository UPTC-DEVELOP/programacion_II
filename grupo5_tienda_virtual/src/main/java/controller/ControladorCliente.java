package controller;

import java.util.List;

import model.Cliente;
import model.ClientePremium;
import model.ClienteRegular;
import persistencia.IRepositorio;
import persistencia.PersistenciaException;
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
    private final ControladorOperaciones operaciones;

    public ControladorCliente(IRepositorio<Cliente> repositorio) {
        this(repositorio, null);
    }

    public ControladorCliente(IRepositorio<Cliente> repositorio,
            ControladorOperaciones operaciones) {
        this.repositorio = repositorio;
        this.operaciones = operaciones;
    }

    @Override
    public boolean registrar(Cliente cliente) {
        if (cliente == null || cliente.getCorreo() == null
                || cliente.getCorreo().trim().isEmpty()
                || buscar(cliente.getCorreo()) != null) {
            return false;
        }
        try {
            repositorio.insertar(cliente);
            registrarOperacion("Cliente registrado: " + cliente.getCorreo());
            return true;
        } catch (PersistenciaException e) {
            return false;
        }
    }

    @Override
    public boolean registrarCliente(String tipo, String nombreCompleto, String correo,
            String direccion, String telefono, String contrasena) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()
                || correo == null || correo.trim().isEmpty()
                || contrasena == null || contrasena.isEmpty()) {
            return false;
        }

        Cliente cliente;
        if ("Premium".equalsIgnoreCase(tipo != null ? tipo.trim() : "")) {
            cliente = new ClientePremium(nombreCompleto.trim(), correo.trim(), contrasena);
        } else {
            cliente = new ClienteRegular(nombreCompleto.trim(), correo.trim(), contrasena);
        }
        cliente.setDireccion(direccion != null ? direccion.trim() : "");
        cliente.setTelefono(telefono != null ? telefono.trim() : "");
        return registrar(cliente);
    }

    @Override
    public List<Cliente> listar() {
        return repositorio.listarUltimos(10);
    }

    @Override
    public Cliente buscar(String correo) {
        if (correo == null) {
            return null;
        }
        List<Cliente> encontrados = repositorio.buscar(correo);
        for (Cliente cliente : encontrados) {
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
        try {
            actual.setNombreCompleto(nombreCompleto);
            actual.setDireccion(direccion);
            actual.setTelefono(telefono);
            repositorio.actualizar(actual);
            registrarOperacion("Cliente actualizado: " + correo);
            return true;
        } catch (PersistenciaException e) {
            return false;
        }
    }

    @Override
    public boolean eliminar(String correo) {
        Cliente actual = buscar(correo);
        if (actual == null) {
            return false;
        }
        try {
            boolean eliminado = repositorio.eliminarPersistente(actual);
            if (eliminado) {
                registrarOperacion("Cliente eliminado: " + correo);
            }
            return eliminado;
        } catch (PersistenciaException e) {
            return false;
        }
    }

    private void registrarOperacion(String descripcion) {
        if (operaciones != null) {
            operaciones.registrar(descripcion);
        }
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
        return repositorio.contar();
    }
}
