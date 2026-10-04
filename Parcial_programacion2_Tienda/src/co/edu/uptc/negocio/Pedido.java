package co.edu.uptc.negocio;


public class Pedido {
    private String idPedido;
    private Producto producto;
    private int cantidad;
    private double totalPagar;

    public Pedido(String idPedido, Producto producto, int cantidad, double totalPagar) {
        this.idPedido = idPedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.totalPagar = totalPagar;
    }
//getters and setters
    public String getIdPedido() {
    	return idPedido; }
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public double getTotalPagar() { return totalPagar; }
    
}

