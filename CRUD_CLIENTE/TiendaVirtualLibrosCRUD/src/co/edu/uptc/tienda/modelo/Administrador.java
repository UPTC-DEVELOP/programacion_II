package co.edu.uptc.tienda.modelo;

import java.sql.Timestamp;

import co.edu.uptc.tienda.modelo.enums.TipoCliente;

public class Administrador extends Cliente {

	public Administrador(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
			String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
			String direccion, int idCliente, String contrasenia, Timestamp fechaRegistro, int intentosFallidos) {
		super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion,
				correoElectronico, celular, direccion, idCliente, contrasenia, fechaRegistro, intentosFallidos);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TipoCliente getTipoCliente() {
		// TODO Auto-generated method stub
		return TipoCliente.ADMIN;
	}

	@Override
	public double calcularDescuento(double subtotal) {
		return 0.0;
	}

}
