package co.edu.uptc.modelo;

import java.sql.Timestamp;

import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;

/**
 * Cliente premium: obtiene un 10% de descuento sobre el subtotal (RF06).
 */
public class ClientePremium extends Cliente {

	private static final double PORCENTAJE_DESCUENTO = 0.10;

	public ClientePremium(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
						  String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
						  String direccion, int idCliente, TipoCliente tipoCliente, String contrasenia,
						  Timestamp fechaRegistro, int intentosFallidos, Rol rol) {
		super(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion, identificacion,
				correoElectronico, celular, direccion, idCliente, tipoCliente, contrasenia, fechaRegistro,
				intentosFallidos, rol);
	}

	@Override
	public TipoCliente getTipoCliente() {
		return TipoCliente.PREMIUM;
	}

	@Override
	public double calcularDescuento(double subtotal) {
		if (subtotal <= 0) {
			return 0.0;
		}
		return subtotal * PORCENTAJE_DESCUENTO;
	}
}
