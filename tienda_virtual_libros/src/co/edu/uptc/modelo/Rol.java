package co.edu.uptc.modelo;

/**
 * ENUMERACIÓN Rol  (paquete: modelo)
 * ---------------------------------------------------------------------------
 * Perfil con el que un usuario ingresa al sistema. Decide qué módulo se abre
 * después del login (administrador o cliente).
 */
public enum Rol {

    CLIENTE("Cliente"),
    ADMINISTRADOR("Administrador");

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
