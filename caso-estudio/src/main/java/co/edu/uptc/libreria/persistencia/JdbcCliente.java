package co.edu.uptc.libreria.persistencia;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.libreria.interfaces.IGestionCliente;
import co.edu.uptc.libreria.modelo.Cliente;
import co.edu.uptc.libreria.modelo.enums.TipoCliente;

public class JdbcCliente implements IGestionCliente {

	@Override
	public void guardar(Cliente cliente) {
		String consulta = "INSERT INTO cliente (correo, nombre_completo, direccion_envio, telefono, tipo) VALUES ('"
				+ cliente.getCorreo() + "', '"
				+ cliente.getNombreCompleto() + "', '"
				+ cliente.getDireccionEnvio() + "', '"
				+ cliente.getTelefono() + "', '"
				+ cliente.getTipo().name() + "')";
		ejecutarActualizacion(consulta, "registrar");
	}

	@Override
	public void actualizar(Cliente cliente) {
		String consulta = "UPDATE cliente SET nombre_completo = '" + cliente.getNombreCompleto()
				+ "', direccion_envio = '" + cliente.getDireccionEnvio()
				+ "', telefono = '" + cliente.getTelefono()
				+ "', tipo = '" + cliente.getTipo().name()
				+ "' WHERE correo = '" + cliente.getCorreo() + "'";
		ejecutarActualizacion(consulta, "actualizar");
	}

	@Override
	public void eliminar(String correo) {
		String consulta = "DELETE FROM cliente WHERE correo = '" + correo + "'";
		ejecutarActualizacion(consulta, "eliminar");
	}

	@Override
	public Cliente buscar(String correo) {
		Cliente cliente = null;
		try (
				Connection conex = ConexionBD.getConnection();
			     Statement estatuto = conex.createStatement();
			     ResultSet rs = estatuto.executeQuery("SELECT * FROM cliente WHERE correo = '" + correo + "'")) {
				
				if (rs.next()) {
					cliente = armarCliente(rs);
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
			Statement estatuto = conex.getConnection().createStatement();
			ResultSet rs = estatuto.executeQuery("SELECT * FROM cliente");
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

	private void ejecutarActualizacion(String consulta, String accion) {
		ConexionBD conex = new ConexionBD();
		try {
			Statement estatuto = conex.getConnection().createStatement();
			estatuto.executeUpdate(consulta);
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