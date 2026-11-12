package co.edu.uptc.model;

// Cliente Premium aplica su descuento sin que CompraService tenga que revisar el tipo.
public class ClientePremium extends Cliente {
    private static final double PORCENTAJE_DESCUENTO = 10.0;
    public ClientePremium(String nombre, String correo, String hash, String direccion, String telefono) {
        super(nombre, correo, hash, direccion, telefono);
    }
    @Override public double calcularDescuento(double totalCompra) {
        return totalCompra * PORCENTAJE_DESCUENTO / 100.0;
    }
}
