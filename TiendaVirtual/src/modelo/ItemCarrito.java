package modelo;

import excepciones.ExcepcionValidacion;

public class ItemCarrito {
    private Libro libro;
    private int cantidad;

    public ItemCarrito(Libro libro, int cantidad) throws ExcepcionValidacion {
        setLibro(libro);
        setCantidad(cantidad);
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) throws ExcepcionValidacion {
        if (libro == null) {
            throw new ExcepcionValidacion("Debe seleccionar un libro.");
        }
        this.libro = libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) throws ExcepcionValidacion {
        if (cantidad <= 0) {
            throw new ExcepcionValidacion("La cantidad debe ser mayor que cero.");
        }
        this.cantidad = cantidad;
    }

    public int getSubtotal() {
        return libro.getPrecio() * cantidad;
    }
}
