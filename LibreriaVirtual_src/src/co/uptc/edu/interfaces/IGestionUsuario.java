package co.uptc.edu.interfaces;

import java.util.List;

import co.uptc.edu.model.Usuario;

public interface IGestionUsuario {

    void guardar(Usuario usuario);

    void actualizar(String correoAnterior, Usuario usuario);

    void eliminar(String correo);

    //Devuelve el usuario con ese correo o null si no existe
    Usuario buscar(String correo);

    List<Usuario> listar();
}
