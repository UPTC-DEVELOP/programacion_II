package co.edu.uptc.modelo;

import java.sql.Timestamp;

public abstract class Cliente extends Persona {
	

    private int idCliente;
    private TipoCliente tipoCliente;
    private String contrasenia;
    private Timestamp fechaRegistro;
    private int intentosFallidos;
    
  //SUPER 
    
    
    public Cliente(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
			String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
			String direccion, int idCliente, TipoCliente tipoCliente, String contrasenia, Timestamp fechaRegistro,
			int intentosFallidos) {
    	//agrega datos a la clase persona
		super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion,
				correoElectronico, celular, direccion);
		this.idCliente = idCliente;
		this.tipoCliente = tipoCliente;
		this.contrasenia = contrasenia;
		this.fechaRegistro = fechaRegistro;
		this.intentosFallidos = intentosFallidos;
	}

    //set y gett
 

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public String getContrasenia() {
		return contrasenia;
	}

	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	public Timestamp getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Timestamp fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public int getIntentosFallidos() {
		return intentosFallidos;
	}

	public void setIntentosFallidos(int intentosFallidos) {
		this.intentosFallidos = intentosFallidos;
		
	
	}

	
	//Descuento 
	  public abstract double calcularDescuento(double subtotal);
	  
}