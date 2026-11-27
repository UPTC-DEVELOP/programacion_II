package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.conexion.Conexion;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;
import co.edu.uptc.tienda.negocio.IRepositorioCliente;
import co.edu.uptc.tienda.negocio.NegocioException;

/**
 * Repositorio de clientes en la Base de Datos MySQL (tabla clientes). Cumple el
 * mismo contrato que ClienteJSON, por eso el negocio no cambia al usarlo.
 * La tabla se crea con el script sql/crear_tabla_clientes.sql
 */
public class BaseDeDatosCliente implements IRepositorioCliente {

	private static final String COLUMNAS = "tipo_cliente, primer_nombre, otros_nombres, primer_apellido, "
			+ "otros_apellidos, tipo_identificacion, identificacion, correo_electronico, celular, direccion, "
			+ "contrasenia, fecha_registro, intentos_fallidos";

	private static final String SQL_INSERTAR = "INSERT INTO clientes (" + COLUMNAS
			+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_LISTAR = "SELECT * FROM clientes ORDER BY id_cliente";
	private static final String SQL_BUSCAR = "SELECT * FROM clientes WHERE identificacion = ?";
	private static final String SQL_ACTUALIZAR = "UPDATE clientes SET tipo_cliente = ?, primer_nombre = ?, "
			+ "otros_nombres = ?, primer_apellido = ?, otros_apellidos = ?, tipo_identificacion = ?, "
			+ "correo_electronico = ?, celular = ?, direccion = ?, contrasenia = ?, intentos_fallidos = ? "
			+ "WHERE identificacion = ?";
	private static final String SQL_ELIMINAR = "DELETE FROM clientes WHERE identificacion = ?";

	@Override
	public void guardar(Cliente cliente) {
		Conexion conexion = new Conexion();
		try (PreparedStatement pstmt = abrir(conexion).prepareStatement(SQL_INSERTAR,
				PreparedStatement.RETURN_GENERATED_KEYS)) {

			llenarDatos(pstmt, cliente);
			pstmt.setTimestamp(12, cliente.getFechaRegistro());
			pstmt.setInt(13, cliente.getIntentosFallidos());
			pstmt.executeUpdate();

			// El id lo genera MySQL (AUTO_INCREMENT)
			try (ResultSet llaves = pstmt.getGeneratedKeys()) {
				if (llaves.next()) {
					cliente.setIdCliente(llaves.getInt(1));
				}
			}

		} catch (SQLException e) {
			throw new NegocioException("No se pudo guardar el cliente en la Base de Datos: " + e.getMessage());
		} finally {
			conexion.desconectar();
		}
	}

	@Override
	public List<Cliente> listar() {
		List<Cliente> clientes = new ArrayList<>();
		Conexion conexion = new Conexion();
		try (PreparedStatement pstmt = abrir(conexion).prepareStatement(SQL_LISTAR);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				clientes.add(mapearCliente(rs));
			}

		} catch (SQLException | NegocioException e) {
			// Igual que BaseDeDatosLibro: si no hay conexión la tabla se muestra vacía
			System.err.println("Error al listar los clientes de la Base de Datos -> " + e.getMessage());
		} finally {
			conexion.desconectar();
		}
		return clientes;
	}

	@Override
	public Cliente buscarPorIdentificacion(String identificacion) {
		Conexion conexion = new Conexion();
		try (PreparedStatement pstmt = abrir(conexion).prepareStatement(SQL_BUSCAR)) {

			pstmt.setString(1, identificacion);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next() ? mapearCliente(rs) : null;
			}

		} catch (SQLException e) {
			throw new NegocioException("No se pudo consultar el cliente en la Base de Datos: " + e.getMessage());
		} finally {
			conexion.desconectar();
		}
	}

	@Override
	public boolean actualizar(Cliente cliente) {
		Conexion conexion = new Conexion();
		try (PreparedStatement pstmt = abrir(conexion).prepareStatement(SQL_ACTUALIZAR)) {

			// La identificación es la clave: no se modifica, va en el WHERE
			pstmt.setString(1, cliente.getTipoCliente().name());
			pstmt.setString(2, cliente.getPrimerNombre());
			pstmt.setString(3, cliente.getOtrosNombres());
			pstmt.setString(4, cliente.getPrimerApellido());
			pstmt.setString(5, cliente.getOtrosApellidos());
			pstmt.setString(6, cliente.getTipoIdentificacion());
			pstmt.setString(7, cliente.getCorreoElectronico());
			pstmt.setString(8, cliente.getCelular());
			pstmt.setString(9, cliente.getDireccion());
			pstmt.setString(10, cliente.getContrasenia());
			pstmt.setInt(11, cliente.getIntentosFallidos());
			pstmt.setString(12, cliente.getIdentificacion());

			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new NegocioException("No se pudo actualizar el cliente en la Base de Datos: " + e.getMessage());
		} finally {
			conexion.desconectar();
		}
	}

	@Override
	public boolean eliminar(String identificacion) {
		Conexion conexion = new Conexion();
		try (PreparedStatement pstmt = abrir(conexion).prepareStatement(SQL_ELIMINAR)) {

			pstmt.setString(1, identificacion);
			return pstmt.executeUpdate() > 0;

		} catch (SQLException e) {
			throw new NegocioException("No se pudo eliminar el cliente en la Base de Datos: " + e.getMessage());
		} finally {
			conexion.desconectar();
		}
	}

	// Abre la conexión o avisa con un mensaje claro si la Base de Datos no responde
	private Connection abrir(Conexion conexion) {
		Connection conn = conexion.conexion();
		if (conn == null) {
			throw new NegocioException("No hay conexión con la Base de Datos. Revise internet y el archivo .properties.");
		}
		return conn;
	}

	// Columnas 1 a 11, comunes al INSERT
	private void llenarDatos(PreparedStatement pstmt, Cliente cliente) throws SQLException {
		pstmt.setString(1, cliente.getTipoCliente().name());
		pstmt.setString(2, cliente.getPrimerNombre());
		pstmt.setString(3, cliente.getOtrosNombres());
		pstmt.setString(4, cliente.getPrimerApellido());
		pstmt.setString(5, cliente.getOtrosApellidos());
		pstmt.setString(6, cliente.getTipoIdentificacion());
		pstmt.setString(7, cliente.getIdentificacion());
		pstmt.setString(8, cliente.getCorreoElectronico());
		pstmt.setString(9, cliente.getCelular());
		pstmt.setString(10, cliente.getDireccion());
		pstmt.setString(11, cliente.getContrasenia());
	}

	// Convierte una fila de la tabla en un Cliente usando la fábrica de TipoCliente
	private Cliente mapearCliente(ResultSet rs) throws SQLException {
		Cliente cliente = TipoCliente.valueOf(rs.getString("tipo_cliente")).crear(rs.getString("primer_nombre"),
				rs.getString("otros_nombres"), rs.getString("primer_apellido"), rs.getString("otros_apellidos"),
				rs.getString("tipo_identificacion"), rs.getString("identificacion"),
				rs.getString("correo_electronico"), rs.getString("celular"), rs.getString("direccion"),
				rs.getString("contrasenia"));

		cliente.setIdCliente(rs.getInt("id_cliente"));
		cliente.setFechaRegistro(rs.getTimestamp("fecha_registro"));
		cliente.setIntentosFallidos(rs.getInt("intentos_fallidos"));
		return cliente;
	}
}
