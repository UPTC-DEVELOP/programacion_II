package negocio;

/**
 * Catalogo de metodos de pago disponibles.
 */
public class MetodoPago {

    private String nombre;
    private String descripcion;

    public MetodoPago() {
    }

    public MetodoPago(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
