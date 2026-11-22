package co.edu.uptc.caso.estudio.model;

public class ClienteRegular extends Cliente {

    public ClienteRegular() {
        setTipoCliente("REGULAR");
        setPorcentajeDescuento(0.0);
    }

    public ClienteRegular(String id, String nombreCompleto, String correo, String direccionEnvio,
                          String departamento, String ciudad, String telefono) {
        super(id, nombreCompleto, correo, direccionEnvio, departamento, ciudad, telefono, "REGULAR");
        setPorcentajeDescuento(0.0);
    }

    @Override
    public double calcularDescuento(double totalCompra) {
        return 0.0;
    }
}