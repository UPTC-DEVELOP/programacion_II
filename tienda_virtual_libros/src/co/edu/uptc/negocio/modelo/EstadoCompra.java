package co.edu.uptc.negocio.modelo;

/**
 * Estados posibles de una compra en el sistema (RF09).
 */

public enum EstadoCompra {

	 PENDIENTE("Pendiente"),
	    CONFIRMADA("Confirmada"),
	    CANCELADA("Cancelada");

	    private final String nombreMostrar;

	    EstadoCompra(String nombreMostrar) {
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
