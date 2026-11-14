package co.edu.uptc.tienda.persistencia;

import co.edu.uptc.tienda.conexion.Conexion;
import co.edu.uptc.tienda.modelo.Factura;
import co.edu.uptc.tienda.modelo.ItemCarrito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BaseDeDatosFactura {

	public boolean registrarFactura(Factura factura) {
		Conexion conexion = new Conexion();
		conexion.conexion();

		String sql = "INSERT INTO factura (numero_factura, fecha_factura, cliente_id, cliente_nombre, "
				+ "cliente_correo, tipo_cliente, id_libro, isbn, titulo, tipo_libro, stock_comprado, precio_final) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = conexion.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

			// Recorremos los ítems (libros) que están en la factura
			for (ItemCarrito item : factura.getItems()) {
				pstmt.setInt(1, factura.getNumero());
				pstmt.setTimestamp(2, Timestamp.valueOf(factura.getFecha()));
				pstmt.setString(3, factura.getCliente().getIdentificacion());
				pstmt.setString(4, factura.getCliente().getPrimerNombre());
				pstmt.setString(5, factura.getCliente().getCorreoElectronico());
				pstmt.setString(6, factura.getCliente().getTipoCliente().name());

				pstmt.setLong(7, item.getLibro().getIdLibro());
				pstmt.setString(8, item.getLibro().getIsbn());
				pstmt.setString(9, item.getLibro().getTitulo());
				pstmt.setString(10, item.getLibro().getTipoLibro().name());
				pstmt.setInt(11, item.getCantidad());
				pstmt.setDouble(12, item.getLibro().getPrecioFinal());

				pstmt.addBatch(); // Agregamos a lote para eficiencia
			}

			pstmt.executeBatch(); // Ejecuta todas las inserciones juntas
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

}
