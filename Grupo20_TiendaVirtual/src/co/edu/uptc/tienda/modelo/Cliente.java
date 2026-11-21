package co.edu.uptc.tienda.modelo;

public class Cliente {

    private String id;
    private String nombreCompleto;
    private String correoElectronico;
    private String direccionEnvio;
    private String celular;
    private String tipoCliente;

    public Cliente(String id, String nombreCompleto, String correoElectronico,
            String direccionEnvio, String celular, String tipoCliente) {

        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correoElectronico = correoElectronico;
        this.direccionEnvio = direccionEnvio;
        this.celular = celular;
        this.tipoCliente = tipoCliente;
    }

    public double obtenerPorcentajeDescuento() {
        return "Premium".equalsIgnoreCase(tipoCliente == null ? null : tipoCliente.trim()) ? 10 : 0;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getCorreoElectronico() {
		return correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	public String getDireccionEnvio() {
		return direccionEnvio;
	}

	public void setDireccionEnvio(String direccionEnvio) {
		this.direccionEnvio = direccionEnvio;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(String tipoCliente) {
		this.tipoCliente = tipoCliente;
	}
}