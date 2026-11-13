package co.edu.uptc.persistence;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.ItemCarrito;
import java.sql.*;

public class RepositorioCompraMySQL implements IRepositorioCompra {

    @Override
    public boolean registrarCompra(String clienteId, Carrito carrito) {
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                int compraId;
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO compras (cliente_id, total) VALUES (?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, clienteId);
                    ps.setDouble(2, carrito.calcularTotal());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        compraId = rs.getInt(1);
                    }
                }

                try (PreparedStatement psItem = conn.prepareStatement(
                        "INSERT INTO compra_items (compra_id, libro_isbn, cantidad, precio_unitario) VALUES (?, ?, ?, ?)");
                     PreparedStatement psStock = conn.prepareStatement(
                        "UPDATE libros SET stock = GREATEST(stock - ?, 0) WHERE isbn = ?")) {
                    for (ItemCarrito it : carrito.getItems()) {
                        if (it.getLibro() == null) continue;
                        psItem.setInt(1, compraId);
                        psItem.setString(2, it.getLibro().getIsbn());
                        psItem.setInt(3, it.getCantidad());
                        psItem.setDouble(4, it.getLibro().getPrecioVenta());
                        psItem.addBatch();

                        psStock.setInt(1, it.getCantidad());
                        psStock.setString(2, it.getLibro().getIsbn());
                        psStock.addBatch();
                    }
                    psItem.executeBatch();
                    psStock.executeBatch();
                }

                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM carrito_items WHERE cliente_id = ?")) {
                    ps.setString(1, clienteId);
                    ps.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}