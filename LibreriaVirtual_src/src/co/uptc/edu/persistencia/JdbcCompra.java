package co.uptc.edu.persistencia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import co.uptc.edu.interfaces.IGestionCompra;
import co.uptc.edu.model.ItemCarrito;
import co.uptc.edu.model.Usuario;

/**
 * Historial de compras en las tablas "compras" y "detalle_compra" de MariaDB.
 * La compra y sus renglones se guardan en una sola transacción: o queda todo o no queda nada.
 */
public class JdbcCompra implements IGestionCompra {

    private static final String METODO_PAGO = "Pago simulado";

    @Override
    public void registrar(Usuario comprador, List<ItemCarrito> items, double subtotal, double iva, double total) {
        String sqlCompra = "INSERT INTO compras (usuario_id, metodo_pago, subtotal, iva, total) "
                + "VALUES ((SELECT id FROM usuarios WHERE correo = ?), ?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_compra (compra_id, isbn, titulo_libro, cantidad, precio_unitario, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection()) {
            conexion.setAutoCommit(false);
            try (PreparedStatement compra = conexion.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement detalle = conexion.prepareStatement(sqlDetalle)) {
                compra.setString(1, comprador == null ? null : comprador.getCorreo());
                compra.setString(2, METODO_PAGO);
                compra.setBigDecimal(3, dinero(subtotal));
                compra.setBigDecimal(4, dinero(iva));
                compra.setBigDecimal(5, dinero(total));
                compra.executeUpdate();

                int idCompra;
                try (ResultSet llaves = compra.getGeneratedKeys()) {
                    if (!llaves.next()) {
                        throw new SQLException("La base de datos no devolvió el número de la compra.");
                    }
                    idCompra = llaves.getInt(1);
                }
                for (ItemCarrito item : items) {
                    detalle.setInt(1, idCompra);
                    detalle.setString(2, item.getLibro().getIsbn());
                    detalle.setString(3, item.getLibro().getTituloLibro());
                    detalle.setInt(4, item.getCantidad());
                    detalle.setBigDecimal(5, dinero(item.getLibro().getPrecioVenta()));
                    detalle.setBigDecimal(6, dinero(item.getSubtotal()));
                    detalle.executeUpdate();
                }
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            throw new IllegalStateException("No se pudo registrar la compra en la base de datos.", e);
        }
    }

    private static BigDecimal dinero(double valor) {
        return BigDecimal.valueOf(valor).setScale(2, RoundingMode.HALF_UP);
    }
}
