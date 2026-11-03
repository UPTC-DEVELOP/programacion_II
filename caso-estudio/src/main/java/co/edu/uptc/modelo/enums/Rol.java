package co.edu.uptc.modelo.enums;

/**
 * ENUMERACIÓN Rol  (paquete: modelo.enums)
 * ---------------------------------------------------------------------------
 * Define el rol del usuario dentro de la tienda virtual.
 *  - ADMIN:   accede al panel de administración (CRUD de libros y clientes).
 *  - CLIENTE: accede al panel del cliente (catálogo y gestión de su perfil).
 */
public enum Rol {

    ADMIN("Administrador"),
    CLIENTE("Cliente");

    private final String nombreMostrar;

    Rol(String nombreMostrar) {
        this.nombreMostrar = nombreMostrar;
    }

    public String getNombreMostrar() {
        return nombreMostrar;
    }

    @Override
    public String toString() {
        return nombreMostrar;
    }
}
