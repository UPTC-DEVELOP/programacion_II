package co.edu.uptc.tienda.modelo;

import java.sql.Timestamp;

import co.edu.uptc.tienda.modelo.enums.TipoCliente;

public class ClientePremium extends Cliente {
	
	//SUPER el hijo pasa los datos a la clase padre para que haga su parte 
	
	public ClientePremium(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
			String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
			String direccion, int idCliente, TipoCliente tipoCliente, String contrasenia, Timestamp fechaRegistro,
			int intentosFallidos) {
		super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion, correoElectronico,
				celular, direccion, idCliente, tipoCliente, contrasenia, fechaRegistro, intentosFallidos);
		// TODO Auto-generated constructor stub
	}


    @Override
    public double calcularDescuento(double subtotal) {
        // Ejecución del beneficio de descuento (10%) exclusivo para PREMIUM
        return subtotal * 0.10; 
    }
}