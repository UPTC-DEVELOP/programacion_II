package co.edu.uptc.persistence;

import co.edu.uptc.model.Cliente;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarioMemoria implements IRepositorioUsuario {
    private List<Cliente> listaUsuarios = new ArrayList<>();

    @Override
    public void crearUsuario(Cliente cliente) {
        listaUsuarios.add(cliente);
    }

    @Override
    public Cliente leerUsuario(Object object) {
        return listaUsuarios.stream()
                .filter(u -> u.getId().equals(object))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Cliente> obtenerTodosLosUsuarios() {
        return listaUsuarios;
    }

    @Override
    public void actualizarUsuario(Cliente clienteActualizado) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getId().equals(clienteActualizado.getId())) {
                // Reemplaza el objeto en la posición exacta de la lista
                listaUsuarios.set(i, clienteActualizado);
                return;
            }
        }
    }

    @Override
    public void eliminarUsuario(String id) {
        listaUsuarios.removeIf(u -> u.getId().equals(id));
    }

    @Override
    public Cliente leerUsuario(String id) {
        return leerUsuario((Object) id);
    }
}
