package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import co.edu.uptc.caso.estudio.model.Cliente; 

public class RepositorioCompraMySQL implements IRepositorioCompra {

    @Override
    public boolean registrarCompra(Cliente cliente, Carrito carrito, String metodoPago) {
        if (cliente == null || cliente.getId() == null || carrito == null || carrito.estaVacio()) {
            return false;
        }

        String clienteId = cliente.getId();
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                int compraId = insertarCabeceraCompra(conn, clienteId, carrito.calcularTotalFinal(cliente));
                registrarDetallesYDescontarStock(conn, compraId, carrito);
                vaciarCarritoCliente(conn, clienteId);

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Rollback en compra de cliente " + clienteId + ": " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión en RepositorioCompraMySQL: " + e.getMessage());
            return false;
        }
    }

    private int insertarCabeceraCompra(Connection conn, String clienteId, double total) throws SQLException {
        String sql = "INSERT INTO compras (cliente_id, total) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clienteId);
            ps.setDouble(2, total);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("No se pudo obtener el ID generado de la compra.");
            }
        }
    }

    private void registrarDetallesYDescontarStock(Connection conn, int compraId, Carrito carrito) throws SQLException {
        String sqlItem = "INSERT INTO compra_items (compra_id, libro_isbn, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE libros SET stock = stock - ? WHERE isbn = ? AND stock >= ?";

        try (PreparedStatement psItem = conn.prepareStatement(sqlItem);
             PreparedStatement psStock = conn.prepareStatement(sqlStock)) {

            for (ItemCarrito it : carrito.getItems()) {
                if (it.getLibro() == null) continue;

                psItem.setInt(1, compraId);
                psItem.setString(2, it.getLibro().getIsbn());
                psItem.setInt(3, it.getCantidad());
                psItem.setDouble(4, it.getLibro().getPrecioVenta());
                psItem.addBatch();

                psStock.setInt(1, it.getCantidad());
                psStock.setString(2, it.getLibro().getIsbn());
                psStock.setInt(3, it.getCantidad());
                psStock.addBatch();
            }

            psItem.executeBatch();
            int[] resultadosStock = psStock.executeBatch();
            for (int filasAfectadas : resultadosStock) {
                if (filasAfectadas == 0) {
                    throw new SQLException("Stock insuficiente para completar la compra.");
                }
            }
        }
    }

    private void vaciarCarritoCliente(Connection conn, String clienteId) throws SQLException {
        String sql = "DELETE FROM carrito_items WHERE cliente_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId);
            ps.executeUpdate();
        }
    }
}