package co.edu.uptc.libreria.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.libreria.interfaces.IGestionCliente;
import co.edu.uptc.libreria.modelo.Cliente;
import co.edu.uptc.libreria.modelo.enums.TipoCliente;

public class JdbcCliente implements IGestionCliente {

	@Override
	public void guardar(Cliente cliente) {
		String consulta = "INSERT INTO clientes (correo, nombre_completo, direccion_envio, telefono, tipo) VALUES (?, ?, ?, ?, ?)";
		ejecutarActualizacion(consulta, "registrar",
				cliente.getCorreo(),
				cliente.getNombreCompleto(),
				cliente.getDireccionEnvio(),
				cliente.getTelefono(),
				cliente.getTipo().name());
	}

	@Override
	public void actualizar(Cliente cliente) {
		String consulta = "UPDATE clientes SET nombre_completo = ?, direccion_envio = ?, telefono = ?, tipo = ? WHERE correo = ?";
		ejecutarActualizacion(consulta, "actualizar",
				cliente.getNombreCompleto(),
				cliente.getDireccionEnvio(),
				cliente.getTelefono(),
				cliente.getTipo().name(),
				cliente.getCorreo());
	}

	@Override
	public void eliminar(String correo) {
		String consulta = "DELETE FROM clientes WHERE correo = ?";
		ejecutarActualizacion(consulta, "eliminar", correo);
	}

	@Override
	public Cliente buscar(String correo) {
		Cliente cliente = null;
		try (
				Connection conex = ConexionBD.getConnection();
				PreparedStatement estatuto = conex.prepareStatement("SELECT * FROM clientes WHERE correo = ?")) {

			estatuto.setString(1, correo);
			try (ResultSet rs = estatuto.executeQuery()) {
				if (rs.next()) {
					cliente = armarCliente(rs);
				}
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new IllegalStateException("No se pudo consultar el cliente");
		}
		return cliente;
	}

	@Override
	public List<Cliente> listar() {
		ConexionBD conex = new ConexionBD();
		List<Cliente> clientes = new ArrayList<Cliente>();
		try {
			PreparedStatement estatuto = conex.getConnection().prepareStatement("SELECT * FROM clientes");
			ResultSet rs = estatuto.executeQuery();
			while (rs.next()) {
				clientes.add(armarCliente(rs));
			}
			rs.close();
			estatuto.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new IllegalStateException("No se pudo listar los clientes");
		} finally {
		}
		return clientes;
	}

	private void ejecutarActualizacion(String consulta, String accion, String... parametros) {
		ConexionBD conex = new ConexionBD();
		try {
			PreparedStatement estatuto = conex.getConnection().prepareStatement(consulta);
			for (int i = 0; i < parametros.length; i++) {
				estatuto.setString(i + 1, parametros[i]);
			}
			estatuto.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new IllegalStateException("No se pudo " + accion + " el cliente");
		} finally {
		}
	}

	private Cliente armarCliente(ResultSet rs) throws SQLException {
		Cliente c = new Cliente();
		c.setNombreCompleto(rs.getString("nombre_completo"));
		c.setCorreo(rs.getString("correo"));
		c.setDireccionEnvio(rs.getString("direccion_envio"));
		c.setTelefono(rs.getString("telefono"));
		c.setTipo(TipoCliente.valueOf(rs.getString("tipo")));
		return c;
	}
}