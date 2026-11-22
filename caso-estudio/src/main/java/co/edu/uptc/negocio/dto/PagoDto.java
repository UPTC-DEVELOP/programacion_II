package co.edu.uptc.negocio.dto;

public class PagoDto {
    private final String metodo;
    private final String banco;
    private final String numeroTarjeta;
    private final String expiracion;
    private final String cvv;
    private final String numeroNequi;

    public PagoDto(String metodo, String banco, String numeroTarjeta, String expiracion, String cvv, String numeroNequi) {
        this.metodo = metodo;
        this.banco = banco;
        this.numeroTarjeta = numeroTarjeta;
        this.expiracion = expiracion;
        this.cvv = cvv;
        this.numeroNequi = numeroNequi;
    }

    public String getMetodo() { return metodo; }
    public String getBanco() { return banco; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public String getExpiracion() { return expiracion; }
    public String getCvv() { return cvv; }
    public String getNumeroNequi() { return numeroNequi; }
}
