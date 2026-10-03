package co.edu.uptc.model;

public class ClientePremium extends Cliente {
    private double porcentajeDescuento;

    public ClientePremium(String nombreCompleto, String correo, String direccionEnvio, String telefono, double porcentajeDescuento) {
        super(nombreCompleto, correo, direccionEnvio, telefono);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return totalCompra * (porcentajeDescuento / 100.0);
    }

    public double getPorcentajeDescuento() { return porcentajeDescuento; }
}