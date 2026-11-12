package co.uptc.edu.negocio;

	import java.time.LocalDateTime;
	import java.time.format.DateTimeFormatter;

	public class Pedido {

	    private static int consecutivo = 1;

	    private final int numero;
	    private final Producto producto;
	    private final int cantidad;
	    private final double precioUnitario;
	    private final double total;
	    private final LocalDateTime fecha;

	    public Pedido(Producto producto, int cantidad) {
	        if (producto == null) {
	            throw new IllegalArgumentException(
	                    "El producto es obligatorio."
	            );
	        }

	        if (cantidad <= 0) {
	            throw new IllegalArgumentException(
	                    "La cantidad debe ser mayor que cero."
	            );
	        }

	        this.numero = consecutivo++;
	        this.producto = producto;
	        this.cantidad = cantidad;
	        this.precioUnitario = producto.calcularPrecioFinal();
	        this.total = precioUnitario * cantidad;
	        this.fecha = LocalDateTime.now();
	    }

	    public int getNumero() {
	        return numero;
	    }

	    public Producto getProducto() {
	        return producto;
	    }

	    public int getCantidad() {
	        return cantidad;
	    }

	    public double getPrecioUnitario() {
	        return precioUnitario;
	    }

	    public double getTotal() {
	        return total;
	    }

	    public String getFechaFormateada() {
	        DateTimeFormatter formato =
	                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	        return fecha.format(formato);
	    }
	
}
