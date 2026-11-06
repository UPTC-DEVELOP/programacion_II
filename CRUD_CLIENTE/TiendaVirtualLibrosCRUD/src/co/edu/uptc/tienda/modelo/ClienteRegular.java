package co.edu.uptc.tienda.modelo;

import java.sql.Timestamp;

import co.edu.uptc.tienda.modelo.enums.TipoCliente;

public class ClienteRegular extends Cliente {

	public ClienteRegular(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
			String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
			String direccion, int idCliente, String contrasenia, Timestamp fechaRegistro, int intentosFallidos) {

		super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion,
				correoElectronico, celular, direccion, idCliente, contrasenia, fechaRegistro, intentosFallidos);
	}

	@Override
	public TipoCliente getTipoCliente() {
		return TipoCliente.REGULAR;
	}

	@Override
	public double calcularDescuento(double subtotal) {
		return 0;
	}
}
