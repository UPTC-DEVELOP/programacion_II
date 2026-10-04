package co.edu.uptc.controller;

import co.edu.uptc.persistence.IRepositorioUsuario;
import co.edu.uptc.model.Cliente;
import java.util.List;

public class UsuarioController {
    private IRepositorioUsuario repositorio;

    public UsuarioController(IRepositorioUsuario repositorio) {
        this.repositorio = repositorio;
    }

    public boolean agregarUsuario(Cliente cliente) {
        if (repositorio.leerUsuario(cliente.getId()) == null) {
            repositorio.crearUsuario(cliente);
            return true; 
        }
        return false;
    }

    public List<Cliente> obtenerUsuarios() {
        return repositorio.obtenerTodosLosUsuarios();
    }

    // === SOLUCIÓN: MÉTODO DE MODIFICACIÓN ENLAZADO ===
    public void modificarUsuario(Cliente cliente) {
        repositorio.actualizarUsuario(cliente);
    }

    public void darDeBajaUsuario(String id) {
        repositorio.eliminarUsuario(id);
    }
}
