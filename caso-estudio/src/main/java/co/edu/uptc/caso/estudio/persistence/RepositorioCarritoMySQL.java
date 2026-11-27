package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.FabricaLibros;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Persistencia MySQL para los ítems del carrito de cada cliente
public class RepositorioCarritoMySQL implements IRepositorioCarrito {

    public RepositorioCarritoMySQL() {
    }

    public RepositorioCarritoMySQL(IRepositorioLibros repoLibros) {
        this();
    }

    // Consulta los ítems actuales del carrito junto con los datos del libro en MySQL
    @Override
    public Carrito obtenerCarritoPorCliente(String clienteId) {
        Carrito carrito = new Carrito(clienteId);
        if (esVacio(clienteId)) {
            return carrito;
        }

        String sql = "SELECT ci.cantidad, l.* FROM carrito_items ci "
                + "INNER JOIN libros l ON ci.libro_isbn = l.isbn "
                + "WHERE ci.cliente_id = ?";

        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId.trim());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro libro = FabricaLibros.crear(
                            rs.getString("isbn"),
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getInt("anio"),
                            rs.getString("categoria"),
                            rs.getString("editorial"),
                            rs.getInt("paginas"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getString("formato"),
                            rs.getDouble("porcentaje_iva"));
                    carrito.agregarItem(libro, rs.getInt("cantidad"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo carrito de MySQL: " + e.getMessage());
        }
        return carrito;
    }

    // Reemplaza el contenido del carrito en MySQL dentro de una transacción atómica
    @Override
    public boolean guardarCarrito(Carrito carrito) {
        if (carrito == null || esVacio(carrito.getClienteId())) {
            return false;
        }

        String clienteId = carrito.getClienteId().trim();
        String sqlDelete = "DELETE FROM carrito_items WHERE cliente_id = ?";
        String sqlInsert = "INSERT INTO carrito_items (cliente_id, libro_isbn, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                    psDel.setString(1, clienteId);
                    psDel.executeUpdate();
                }

                if (!carrito.estaVacio() && carrito.getItems() != null) {
                    try (PreparedStatement psIns = conn.prepareStatement(sqlInsert)) {
                        for (ItemCarrito item : carrito.getItems()) {
                            if (item != null && item.getLibro() != null
                                    && !esVacio(item.getLibro().getIsbn()) && item.getCantidad() > 0) {
                                psIns.setString(1, clienteId);
                                psIns.setString(2, item.getLibro().getIsbn().trim());
                                psIns.setInt(3, item.getCantidad());
                                psIns.addBatch();
                            }
                        }
                        psIns.executeBatch();
                    }
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                rollbackSilencioso(conn);
                System.err.println("Rollback guardando carrito en MySQL: " + e.getMessage());
                return false;
            } finally {
                restaurarAutoCommit(conn);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión en RepositorioCarritoMySQL: " + e.getMessage());
            return false;
        }
    }

    // Elimina todos los ítems del carrito del cliente en MySQL
    @Override
    public boolean vaciarCarrito(String clienteId) {
        if (esVacio(clienteId)) {
            return false;
        }
        String sql = "DELETE FROM carrito_items WHERE cliente_id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error vaciando carrito en MySQL: " + e.getMessage());
            return false;
        }
    }

    private void rollbackSilencioso(Connection conn) {
        if (conn == null) {
            return;
        }
        try {
            conn.rollback();
        } catch (SQLException ex) {
            System.err.println("Error en rollback de carrito: " + ex.getMessage());
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
            System.err.println("Error restaurando autoCommit en carrito: " + ex.getMessage());
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}