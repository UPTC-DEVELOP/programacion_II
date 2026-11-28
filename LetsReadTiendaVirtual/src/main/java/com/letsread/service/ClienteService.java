package com.letsread.service;

import com.letsread.model.Cliente;
import com.letsread.model.ClienteFactory;
import com.letsread.persistence.ArchivoTxtDAO;
import com.letsread.repository.ClienteRepositorio;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso de Clientes (C-R-U-D). Depende de la interfaz ClienteRepositorio
 * (inyectada desde Main), no de JSON ni de la BD concretos: principio DIP de SOLID.
 */
public class ClienteService {
    private final ClienteRepositorio repositorio;

    public ClienteService(ClienteRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    /** C - Registrar cliente. */
    public Cliente registrar(String tipo, String nombre, String correo,
                             String direccion, String telefono, String clave) {
        validarObligatorio(nombre, "El nombre");
        validarObligatorio(correo, "El correo");
        validarObligatorio(direccion, "La dirección");
        validarObligatorio(telefono, "El teléfono");
        validarObligatorio(clave, "La clave");
        String correoNorm = correo.trim().toLowerCase();
        if (!correoNorm.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("El correo no tiene un formato válido.");
        }
        if (repositorio.buscarPorCorreo(correoNorm).isPresent()) {
            throw new IllegalArgumentException("Ese correo ya está registrado.");
        }
        Cliente cliente = ClienteFactory.crear(tipo, nombre.trim(), correoNorm,
                direccion.trim(), telefono.trim(), clave);
        repositorio.guardar(cliente);
        ArchivoTxtDAO.registrarOperacion("Cliente registrado: " + correoNorm + " (" + cliente.getTipo() + ")");
        return cliente;
    }

    /** R - Consultar un cliente. */
    public Optional<Cliente> buscar(String correo) {
        if (correo == null) {
            return Optional.empty();
        }
        return repositorio.buscarPorCorreo(correo.trim().toLowerCase());
    }

    /** R - Listar todos los clientes. */
    public List<Cliente> listar() {
        return repositorio.listar();
    }

    /** U - Actualizar datos (el correo identifica al cliente y no cambia). */
    public Cliente actualizar(String correo, String tipo, String nombre, String direccion,
                              String telefono, String clave) {
        Cliente actual = buscar(correo)
                .orElseThrow(() -> new IllegalArgumentException("No existe un cliente con ese correo."));
        validarObligatorio(nombre, "El nombre");
        validarObligatorio(direccion, "La dirección");
        validarObligatorio(telefono, "El teléfono");
        validarObligatorio(clave, "La clave");
        // Se recrea con el tipo elegido porque el tipo (Regular/Premium) puede cambiar.
        Cliente nuevo = ClienteFactory.crear(tipo, nombre.trim(), actual.getCorreo(),
                direccion.trim(), telefono.trim(), clave);
        repositorio.actualizar(nuevo);
        ArchivoTxtDAO.registrarOperacion("Cliente actualizado: " + actual.getCorreo());
        return nuevo;
    }

    /** D - Eliminar cliente. */
    public void eliminar(String correo) {
        if (correo == null || !repositorio.eliminar(correo.trim().toLowerCase())) {
            throw new IllegalArgumentException("No existe un cliente con ese correo.");
        }
        ArchivoTxtDAO.registrarOperacion("Cliente eliminado: " + correo.trim().toLowerCase());
    }

    /** Inicio de sesión: devuelve el cliente si correo y clave coinciden. */
    public Optional<Cliente> autenticar(String correo, String clave) {
        return buscar(correo).filter(c -> c.getClave().equals(clave));
    }

    private void validarObligatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
    }
}