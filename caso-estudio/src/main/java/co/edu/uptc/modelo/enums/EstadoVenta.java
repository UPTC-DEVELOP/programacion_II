package co.edu.uptc.modelo.enums;

/**
 * ENUMERACIÓN EstadoVenta  (paquete: modelo.enums)
 * ---------------------------------------------------------------------------
 * Define el estado de una venta registrada en la tienda.
 *
 * ¿POR QUÉ UN ENUM Y NO UN STRING?
 *  - Seguridad de tipos: el compilador obliga a usar solo estos valores
 *    conocidos, eliminando errores por typos ("completada" vs "Completada").
 *  - Fácil de extender: agregar un estado nuevo es agregar una constante,
 *    sin tocar la lógica que la consume (OCP).
 *  - La tabla "Ventas Recientes" del dashboard y la columna "Estado" de
 *    Reportes muestran directamente el nombreMostrar de esta enumeración.
 */
public enum EstadoVenta {

    COMPLETADA("Completada"),
    PENDIENTE("Pendiente"),
    CANCELADA("Cancelada");

    private final String nombreMostrar;

    EstadoVenta(String nombreMostrar) {
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
