package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.Compra;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Compra> obtenerComprasPorCliente(String clienteId) {
        List<Compra> compras = new ArrayList<>();
        if (clienteId == null || clienteId.trim().isEmpty()) {
            return compras;
        }

        String sql = "SELECT c.id, c.cliente_id, c.fecha, c.total, u.nombre_completo "
                + "FROM compras c "
                + "LEFT JOIN usuarios u ON c.cliente_id = u.id "
                + "WHERE c.cliente_id = ? "
                + "ORDER BY c.fecha DESC, c.id DESC";

        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, clienteId.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Compra compra = mapearCompra(conn, rs);
                    compras.add(compra);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar compras del cliente " + clienteId + ": " + e.getMessage());
        }

        return compras;
    }

    private Compra mapearCompra(Connection conn, ResultSet rs) throws SQLException {
        int idCompraInt = rs.getInt("id");
        Compra compra = new Compra();
        compra.setIdCompra(String.valueOf(idCompraInt));
        compra.setClienteId(rs.getString("cliente_id"));
        compra.setNombreCliente(rs.getString("nombre_completo"));

        Timestamp ts = rs.getTimestamp("fecha");
        compra.setFecha(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

        List<ItemCarrito> items = obtenerItemsCompra(conn, idCompraInt);
        compra.setItems(items);

        double totalBd = rs.getDouble("total");
        aplicarDesgloseFinanciero(compra, items, totalBd);
        return compra;
    }

    private void aplicarDesgloseFinanciero(Compra compra, List<ItemCarrito> items, double totalBd) {
        Carrito auxiliar = new Carrito(compra.getClienteId());
        auxiliar.setItems(items);

        double subtotalSinIva = auxiliar.calcularSubtotalSinIva();
        double totalImpuestos = auxiliar.calcularTotalImpuestos();
        double totalBrutoConIva = auxiliar.calcularTotal();
        double descuento = totalBrutoConIva > totalBd ? (totalBrutoConIva - totalBd) : 0.0;

        compra.setSubtotalSinIva(subtotalSinIva);
        compra.setTotalImpuestos(totalImpuestos);
        compra.setDescuentoAplicado(descuento);
        compra.setTotalPagado(totalBd);
    }

    private List<ItemCarrito> obtenerItemsCompra(Connection conn, int compraId) throws SQLException {
        List<ItemCarrito> items = new ArrayList<>();
        String sql = "SELECT ci.libro_isbn, ci.cantidad, ci.precio_unitario, "
                + "l.titulo, l.autor, l.anio, l.categoria, l.editorial, l.paginas, "
                + "l.precio, l.stock, l.formato, l.porcentaje_iva "
                + "FROM compra_items ci "
                + "INNER JOIN libros l ON ci.libro_isbn = l.isbn "
                + "WHERE ci.compra_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, compraId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapearItemCompra(rs));
                }
            }
        }
        return items;
    }

    private ItemCarrito mapearItemCompra(ResultSet rs) throws SQLException {
        Libro libro = mapearLibro(rs);
        int cantidad = rs.getInt("cantidad");
        double precioUnitario = rs.getDouble("precio_unitario");
        return new ItemCarrito(libro, cantidad, precioUnitario);
    }

    private Libro mapearLibro(ResultSet rs) throws SQLException {
        Libro libro = new Libro();
        libro.setIsbn(rs.getString("libro_isbn"));
        libro.setTitulo(rs.getString("titulo"));
        libro.setAutor(rs.getString("autor"));
        libro.setAnio(rs.getInt("anio"));
        libro.setCategoria(rs.getString("categoria"));
        libro.setEditorial(rs.getString("editorial"));
        libro.setPaginas(rs.getInt("paginas"));
        libro.setPrecioVenta(rs.getDouble("precio"));
        libro.setStock(rs.getInt("stock"));
        libro.setFormato(rs.getString("formato"));
        libro.setPorcentajeIva(rs.getDouble("porcentaje_iva"));
        return libro;
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
                if (it == null || it.getLibro() == null) continue;

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