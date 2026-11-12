package co.edu.uptc.model;

// El cliente regular no recibe descuento por tipo, pero puede usar todos los servicios de compra.
public class ClienteRegular extends Cliente {
    public ClienteRegular(String nombre, String correo, String hash, String direccion, String telefono) {
        super(nombre, correo, hash, direccion, telefono);
    }
    @Override public double calcularDescuento(double totalCompra) { return 0.0; }
}
