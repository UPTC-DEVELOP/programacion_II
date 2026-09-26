package co.uptc.edu.negocio;

import java.util.Date;

public class Pago {

	private int idPago;
    private String metodoPago;
    private Date fechaPago;
    private Double monto;
    private String estado;

    public Pago() {}

    public Pago(String metodoPago, Double monto) {
        this.idPago = (int) (Math.random() * 9000) + 1000;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.fechaPago = new Date();
        this.estado = "Pendiente";
    }

    // Métodos del diagrama de negocio
    public boolean validarPago() {
        return monto != null && monto > 0 && metodoPago != null && !metodoPago.isEmpty();
    }

    public boolean procesarPago() {
        if (validarPago()) {
            this.estado = "Aprobado";
            System.out.println("Pago #" + idPago + " procesado exitosamente por monto: $" + monto);
            return true;
        }
        this.estado = "Rechazado";
        return false;
    }

    // Getters y Setters
    public int getIdPago() { return idPago; }
    public String getMetodoPago() { return metodoPago; }
    public Date getFechaPago() { return fechaPago; }
    public Double getMonto() { return monto; }
    public String getEstado() { return estado; }
	
}
