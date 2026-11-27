package co.edu.uptc.tienda.persistencia;

import co.edu.uptc.tienda.conexion.Conexion;
import co.edu.uptc.tienda.modelo.Cliente;
import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.modelo.ItemCarrito;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.enums.TipoCliente;
import co.edu.uptc.tienda.modelo.enums.TipoLibro;
import co.edu.uptc.tienda.negocio.IGestionFactura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaseDeDatosFactura implements IGestionFactura {

	private static final String SEPARADOR_LIBROS = ", ";

	@Override
	public boolean procesarYGuardarFactura(Factura factura) {
		Conexion conexion = new Conexion();
		conexion.conexion();

		String sql = "INSERT INTO factura VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			for (ItemCarrito item : factura.getItems()) {
				pstmt.setInt(1, 0);
				pstmt.setInt(2, factura.getNumero());
				pstmt.setTimestamp(3, Timestamp.valueOf(factura.getFecha()));
				pstmt.setString(4, factura.getCliente().getIdentificacion());
				pstmt.setString(5, factura.getCliente().getPrimerNombre());
				pstmt.setString(6, factura.getCliente().getCorreoElectronico());
				pstmt.setString(7, factura.getCliente().getTipoCliente().name());

				pstmt.setLong(8, item.getLibro().getIdLibro());
				pstmt.setString(9, item.getLibro().getIsbn());
				pstmt.setString(10, item.getLibro().getTitulo());
				pstmt.setString(11, item.getLibro().getTipoLibro().name());
				pstmt.setInt(12, item.getCantidad());
				pstmt.setDouble(13, item.getLibro().getPrecioFinal());

				pstmt.addBatch();
			}

			pstmt.executeBatch();
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int obtenerSiguienteNumeroFactura() {
		String sql = "SELECT COALESCE(MAX(numero_factura), 0) + 1 AS siguiente FROM factura";
		Conexion conn = new Conexion();
		conn.conexion();

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {
			if (rs.next()) {
				return rs.getInt("siguiente");
			}
		} catch (Exception e) {
			System.err.println("Error al obtener siguiente número de factura -> " + e.getMessage());
		} finally {
			conn.desconectar();
		}
		return 1;
	}

	@Override
	public List<Factura> listarFacturas() {
		List<Factura> facturas = new ArrayList<>();
		Map<Integer, Factura> mapaFacturas = new LinkedHashMap<>();
		Conexion conn = new Conexion();
		conn.conexion();

		String sentenciaSQL = "SELECT * FROM factura ORDER BY fecha_factura DESC, numero_factura DESC, id_factura ASC";

		try (PreparedStatement pstmt = conn.getConnection().prepareStatement(sentenciaSQL);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				int numero = rs.getInt("numero_factura");
				Factura factura = mapaFacturas.get(numero);

				ItemCarrito item = mapearItemCarrito(rs);
				double montoFila = rs.getDouble("precio_final");

				if (factura == null) {
					LocalDateTime fecha = rs.getTimestamp("fecha_factura") != null
							? rs.getTimestamp("fecha_factura").toLocalDateTime()
							: LocalDateTime.now();
					Cliente cliente = mapearCliente(rs);

					List<ItemCarrito> items = new ArrayList<>();
					items.add(item);

					factura = new Factura(numero, fecha, cliente, items, montoFila, 0.0, montoFila);
					mapaFacturas.put(numero, factura);
				} else {
					factura.getItems().add(item);
					factura.setSubtotal(factura.getSubtotal() + montoFila);
					factura.setTotal(factura.getTotal() + montoFila);
				}
			}

			facturas.addAll(mapaFacturas.values());

		} catch (Exception e) {
			System.err.println("Error al listar facturas -> " + e.getMessage());
		} finally {
			conn.desconectar();
		}

		return facturas;
	}

	private ItemCarrito mapearItemCarrito(ResultSet rs) throws SQLException {
		String tipoLibroStr = rs.getString("tipo_libro");
		TipoLibro tipoLibro = TipoLibro.valueOf(tipoLibroStr.trim().toUpperCase());

		String isbn = rs.getString("isbn");
		String titulo = rs.getString("titulo");
		List<String> autores = new ArrayList<>(); // Si no viene en la tabla factura, queda vacía o se consulta aparte
		String fechaPublicacion = "";
		String genero = "";
		String editorial = "";
		int numeroPaginas = 0;
		double precioBase = rs.getDouble("precio_final");
		int stockDisponible = 0;

		Libro libro = tipoLibro.crear(isbn, titulo, autores, fechaPublicacion, genero, editorial, numeroPaginas,
				precioBase, stockDisponible);
		libro.setIdLibro(rs.getLong("id_libro"));

		int cantidad = rs.getInt("stock_comprado");
		return new ItemCarrito(libro, cantidad);
	}

	private Cliente mapearCliente(ResultSet rs) throws SQLException {
		String tipoStr = rs.getString("tipo_cliente");
		TipoCliente tipoCliente = (tipoStr != null && !tipoStr.isBlank())
				? TipoCliente.valueOf(tipoStr.trim().toUpperCase())
				: TipoCliente.REGULAR;

		String nombre = rs.getString("cliente_nombre");
		String correo = rs.getString("cliente_correo");
		String identificacion = rs.getString("cliente_id");

		// Ajusta qué campos van en primerNombre / identificacion según cómo los guardes
		Cliente cliente = tipoCliente.crear(nombre != null ? nombre : "", "", "", "", "",
				identificacion != null ? identificacion : "", correo != null ? correo : "", "", "", "");
		return cliente;
	}

	@Override
	public Factura busquedaPorFiltro(String filtro) {
		// TODO Auto-generated method stub
		return null;
	}
}