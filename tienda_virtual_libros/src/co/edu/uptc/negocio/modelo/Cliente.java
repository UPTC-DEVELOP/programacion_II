package co.edu.uptc.tienda.modelo;

import java.sql.Timestamp;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;

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
		//asigna la fehca cuando se ingreso a la tienda
		this.fechaRegistro = fechaRegistro;
		this.intentosFallidos = intentosFallidos;
	}

   
    public abstract double calcularDescuento(double subtotal);

    public int getIdCliente() { return idCliente; }
    public String getContrasenia() { return contrasenia; }
    public TipoCliente getTipoCliente() { return tipoCliente; }
    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }
}