package co.edu.uptc.model;

// Administrador representa al usuario que gestiona catálogo y consulta ventas globales.
public class Administrador extends Usuario {
    public Administrador(String nombre, String correo, String hash) {
        super(nombre, correo, hash, Rol.ADMINISTRADOR);
    }
}
