package co.edu.uptc.modelo;

import java.sql.Timestamp;

import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * Cliente regular: no tiene descuentos especiales sobre el subtotal.
 */
public class ClienteRegular extends Cliente {

	public ClienteRegular(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
						  String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
						  String direccion, int idCliente, TipoCliente tipoCliente, String contrasenia,
						  Timestamp fechaRegistro, int intentosFallidos, Rol rol) {
		super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion,
				correoElectronico, celular, direccion, idCliente, tipoCliente, contrasenia, fechaRegistro,
				intentosFallidos, rol);
	}

	@Override
	public TipoCliente getTipoCliente() {
		return TipoCliente.REGULAR;
	}

	@Override
	public double calcularDescuento(double subtotal) {
		return 0.0;
	}
}
