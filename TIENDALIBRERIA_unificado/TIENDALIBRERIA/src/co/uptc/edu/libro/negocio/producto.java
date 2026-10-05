package co.uptc.edu.libro.negocio;

public class producto {

	private String nombre;
	private double precioBase;
	private int stockDisdponible;
	private double descuento;
	private double iva;

	public producto(String nombre, double precioBase, int stockDisdponible, double descuento, double iva) {
		super();
		this.nombre = nombre;
		this.precioBase = precioBase;
		this.stockDisdponible = stockDisdponible;
		this.descuento = descuento;
		this.iva = iva;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}

	public int getStockDisdponible() {
		return stockDisdponible;
	}

	public void setStockDisdponible(int stockDisdponible) {
		this.stockDisdponible = stockDisdponible;
	}

	public double getDescuento() {
		return descuento;
	}

	public void setDescuento(double descuento) {
		this.descuento = descuento;
	}

	public double getIva() {
		return iva;
	}

	public void setIva(double iva) {
		this.iva = iva;
	}

	@Override
	public String toString() {
		return "producto [nombre=" + nombre + ", precioBase=" + precioBase + ", stockDisdponible=" + stockDisdponible
				+ ", descuento=" + descuento + ", iva=" + iva + "]";
	}

}
