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

// Persistencia MySQL transaccional para registrar compras, descontar stock y consultar historial
public class RepositorioCompraMySQL implements IRepositorioCompra {

    // Registra la compra, sus ítems y el descuento de stock en una única transacción
    @Override
    public boolean registrarCompra(Cliente cliente, Carrito carrito, String metodoPago) {
        if (cliente == null || esVacio(cliente.getId()) || carrito == null || carrito.estaVacio()) {
            return false;
        }

        String clienteId = cliente.getId().trim();
        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                int compraId = insertarCompra(conn, clienteId, carrito.calcularTotalFinal(cliente));
                guardarItemsYStock(conn, compraId, carrito);
                vaciarCarrito(conn, clienteId);
                conn.commit();
                return true;
            } catch (SQLException e) {
                rollbackSilencioso(conn);
                System.err.println("Rollback en compra de cliente " + clienteId + ": " + e.getMessage());
                return false;
            } finally {
                restaurarAutoCommit(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión en RepositorioCompraMySQL: " + e.getMessage());
            return false;
        }
    }

    // Consulta el historial de compras del cliente ordenado de la más reciente a la más antigua
    @Override
    public List<Compra> obtenerComprasPorCliente(String clienteId) {
        List<Compra> compras = new ArrayList<>();
        if (esVacio(clienteId)) {
            return compras;
        }

        String sql = "SELECT c.id, c.cliente_id, c.fecha, c.total, u.nombre_completo "
                + "FROM compras c LEFT JOIN usuarios u ON c.cliente_id = u.id "
                + "WHERE c.cliente_id = ? ORDER BY c.fecha DESC, c.id DESC";

        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    compras.add(mapearCompra(conn, rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar compras de " + clienteId + ": " + e.getMessage());
        }
        return compras;
    }

    // Verifica si el ISBN tiene al menos una venta registrada en cualquier compra de MySQL
    @Override
    public boolean existeVentaDeLibro(String isbn) {
        if (esVacio(isbn)) {
            return false;
        }

        String sql = "SELECT 1 FROM compra_items WHERE libro_isbn = ? LIMIT 1";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, isbn.trim());
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar ventas del libro " + isbn + ": " + e.getMessage());
            return false;
        }
    }

    // Mapea una fila de compras junto con sus ítems asociados
    private Compra mapearCompra(Connection conn, ResultSet rs) throws SQLException {
        int idCompra = rs.getInt("id");
        Compra compra = new Compra();
        compra.setIdCompra(String.valueOf(idCompra));
        compra.setClienteId(rs.getString("cliente_id"));
        compra.setNombreCliente(rs.getString("nombre_completo"));

        Timestamp ts = rs.getTimestamp("fecha");
        compra.setFecha(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());

        List<ItemCarrito> items = obtenerItems(conn, idCompra);
        compra.setItems(items);
        calcularTotalesCompra(compra, items, rs.getDouble("total"));
        return compra;
    }

    // Calcula desglose de subtotal, IVA y descuento a partir de los ítems y el total en BD
    private void calcularTotalesCompra(Compra compra, List<ItemCarrito> items, double totalBd) {
        Carrito aux = new Carrito(compra.getClienteId());
        aux.setItems(items);
        double bruto = aux.calcularTotal();
        compra.setSubtotalSinIva(aux.calcularSubtotalSinIva());
        compra.setTotalImpuestos(aux.calcularTotalImpuestos());
        compra.setDescuentoAplicado(bruto > totalBd ? (bruto - totalBd) : 0.0);
        compra.setTotalPagado(totalBd);
    }

    // Recupera los ítems de una compra uniendo con la tabla de libros
    private List<ItemCarrito> obtenerItems(Connection conn, int compraId) throws SQLException {
        List<ItemCarrito> items = new ArrayList<>();
        String sql = "SELECT ci.libro_isbn, ci.cantidad, ci.precio_unitario, "
                + "l.titulo, l.autor, l.anio, l.categoria, l.editorial, l.paginas, "
                + "l.precio, l.stock, l.formato, l.porcentaje_iva "
                + "FROM compra_items ci INNER JOIN libros l ON ci.libro_isbn = l.isbn "
                + "WHERE ci.compra_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, compraId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new ItemCarrito(
                            mapearLibro(rs),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario")));
                }
            }
        }
        return items;
    }

    // Construye un objeto Libro a partir de las columnas seleccionadas del detalle de compra
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

    // Inserta el encabezado de la compra y retorna la clave primaria autogenerada
    private int insertarCompra(Connection conn, String clienteId, double total) throws SQLException {
        String sql = "INSERT INTO compras (cliente_id, total) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, clienteId);
            ps.setDouble(2, total);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("No se obtuvo ID de compra.");
            }
        }
    }

    // Inserta los ítems comprados y descuenta stock verificando disponibilidad en BD
    private void guardarItemsYStock(Connection conn, int compraId, Carrito carrito) throws SQLException {
        String sqlItem = "INSERT INTO compra_items (compra_id, libro_isbn, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        String sqlStock = "UPDATE libros SET stock = stock - ? WHERE isbn = ? AND stock >= ?";

        try (PreparedStatement psItem = conn.prepareStatement(sqlItem);
             PreparedStatement psStock = conn.prepareStatement(sqlStock)) {
            List<ItemCarrito> items = carrito.getItems();
            for (int i = 0; i < items.size(); i++) {
                ItemCarrito it = items.get(i);
                if (it == null || it.getLibro() == null || esVacio(it.getLibro().getIsbn())) {
                    continue;
                }
                String isbn = it.getLibro().getIsbn().trim();
                double precioUnitario = it.getPrecioUnitarioOverride() != null
                        ? it.getPrecioUnitarioOverride()
                        : it.getLibro().getPrecioVenta();

                psItem.setInt(1, compraId);
                psItem.setString(2, isbn);
                psItem.setInt(3, it.getCantidad());
                psItem.setDouble(4, precioUnitario);
                psItem.addBatch();

                psStock.setInt(1, it.getCantidad());
                psStock.setString(2, isbn);
                psStock.setInt(3, it.getCantidad());
                psStock.addBatch();
            }
            psItem.executeBatch();
            int[] filasActualizadas = psStock.executeBatch();
            for (int i = 0; i < filasActualizadas.length; i++) {
                if (filasActualizadas[i] == 0) {
                    throw new SQLException("Stock insuficiente.");
                }
            }
        }
    }

    // Limpia los ítems del carrito del cliente dentro de la transacción de compra
    private void vaciarCarrito(Connection conn, String clienteId) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM carrito_items WHERE cliente_id = ?")) {
            ps.setString(1, clienteId);
            ps.executeUpdate();
        }
    }

    private void rollbackSilencioso(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.rollback();
        } catch (SQLException ex) {
            System.err.println("Error en rollback de compra: " + ex.getMessage());
        }
    }

    private void restaurarAutoCommit(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            if (!conn.isClosed()) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.err.println("Error restaurando autoCommit en compra: " + ex.getMessage());
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}