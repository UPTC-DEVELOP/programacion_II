package co.edu.uptc.tienda.modelo.enums;

import co.edu.uptc.tienda.modelo.Administrador;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.ClientePremium;
import co.edu.uptc.tienda.modelo.ClienteRegular;

public enum TipoCliente {

	REGULAR {
		@Override
		public Cliente crear(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
				String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
				String direccion, String contrasenia) {
			return new ClienteRegular(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion,
					identificacion, correoElectronico, celular, direccion, 0, contrasenia, null, 0);
		}
	},
	PREMIUM {
		@Override
		public Cliente crear(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
				String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
				String direccion, String contrasenia) {
			return new ClientePremium(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion,
					identificacion, correoElectronico, celular, direccion, 0, contrasenia, null, 0);
		}
	},

	ADMIN {
		@Override
		public Cliente crear(String primerNombre, String otrosNombres, String primerApellido, String otrosApellidos,
				String tipoIdentificacion, String identificacion, String correoElectronico, String celular,
				String direccion, String contrasenia) {
			return new Administrador(primerNombre, otrosNombres, primerApellido, otrosApellidos, tipoIdentificacion,
					identificacion, correoElectronico, celular, direccion, 0, contrasenia, null, 0);
		}
	};

	public abstract Cliente crear(String primerNombre, String otrosNombres, String primerApellido,
			String otrosApellidos, String tipoIdentificacion, String identificacion, String correoElectronico,
			String celular, String direccion, String contrasenia);

}
