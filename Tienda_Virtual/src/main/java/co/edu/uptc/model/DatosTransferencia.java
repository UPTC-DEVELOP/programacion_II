package main.java.co.edu.uptc.model;

public class DatosTransferencia {
    public static final String NEQUI = "Nequi";

    private String banco;
    private String numeroTarjeta;
    private String codigoSeguridad;
    private String fechaExpiracion; // formato MM/AA
    private String telefonoNequi;

    public DatosTransferencia() {}

    public DatosTransferencia(String banco, String numeroTarjeta, String codigoSeguridad,
                              String fechaExpiracion, String telefonoNequi) {
        this.banco = banco;
        this.numeroTarjeta = numeroTarjeta;
        this.codigoSeguridad = codigoSeguridad;
        this.fechaExpiracion = fechaExpiracion;
        this.telefonoNequi = telefonoNequi;
    }

    public static String[] bancos() {
        return new String[]{"Bancolombia", "Davivienda", "Banco de Bogotá", "BBVA", "Banco de Occidente",
                "Banco Popular", "Banco AV Villas", "Banco Caja Social", "Scotiabank Colpatria", "Itaú", NEQUI};
    }

    public boolean esNequi() {
        return NEQUI.equalsIgnoreCase(banco);
    }
// proteccion de visibilidad de la tarjeta 
    public String getTarjetaEnmascarada() {
        String digitos = numeroTarjeta == null ? "" : numeroTarjeta.replaceAll("\\D", "");
        String ultimos = digitos.length() > 4 ? digitos.substring(digitos.length() - 4) : digitos;
        return "**** **** **** " + ultimos;
    }

    public String getBanco() { return banco; }
    public void setBanco(String banco) { this.banco = banco; }
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    public String getCodigoSeguridad() { return codigoSeguridad; }
    public void setCodigoSeguridad(String codigoSeguridad) { this.codigoSeguridad = codigoSeguridad; }
    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    public String getTelefonoNequi() { return telefonoNequi; }
    public void setTelefonoNequi(String telefonoNequi) { this.telefonoNequi = telefonoNequi; }
}