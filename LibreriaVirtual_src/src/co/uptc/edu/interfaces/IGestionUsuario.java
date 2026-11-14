package co.uptc.edu.interfaces;

import java.util.List;

import co.uptc.edu.model.Usuario;

/**
 * Contrato de persistencia de los usuarios.
 * La capa de negocio (GestionUsuarios) solo conoce esta interfaz, de modo que se puede
 * guardar en memoria, en un archivo JSON o en la base de datos sin modificarla.
 */
public interface IGestionUsuario {

    void guardar(Usuario usuario);

    /**
     * Reemplaza los datos del usuario identificado por correoAnterior.
     * Se recibe el correo anterior porque el correo también puede cambiar.
     */
    void actualizar(String correoAnterior, Usuario usuario);

    void eliminar(String correo);

    /** Devuelve el usuario con ese correo o null si no existe. */
    Usuario buscar(String correo);

    List<Usuario> listar();
}
