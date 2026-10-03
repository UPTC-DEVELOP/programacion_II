package co.edu.uptc.libreria.modelo;

public class ItemCarrito {
	
	private Libro libro;
	private int cantidad;
	private double precioUnitario;
	
	
	public ItemCarrito(Libro libro, int cantidad, double precioUnitario) {
		super();
		this.libro = libro;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
	}


	public Libro getLibro() {
		return libro;
	}



	public int getCantidad() {
		return cantidad;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public double getPrecioUnitario() {
		return precioUnitario;
	}


	public void setPrecioUnitario(double precioUnitario) {
		this.precioUnitario = precioUnitario;
	}
	
	public double getSubtotal() {
		return precioUnitario * cantidad;
	}
	

}
