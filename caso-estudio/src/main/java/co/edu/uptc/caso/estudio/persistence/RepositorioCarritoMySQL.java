package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Carrito;
import co.edu.uptc.caso.estudio.model.FabricaLibros;
import co.edu.uptc.caso.estudio.model.ItemCarrito;
import co.edu.uptc.caso.estudio.model.Libro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RepositorioCarritoMySQL implements IRepositorioCarrito {

    private final IRepositorioLibros repoLibros;

    public RepositorioCarritoMySQL() {
        this(new RepositorioLibrosMySQL());
    }

    public RepositorioCarritoMySQL(IRepositorioLibros repoLibros) {
        this.repoLibros = repoLibros;
    }

    @Override
    public Carrito obtenerCarritoPorCliente(String clienteId) {
        Carrito carrito = new Carrito(clienteId);
        if (clienteId == null) return carrito;

        String sql = "SELECT ci.cantidad, l.* FROM carrito_items ci "
                   + "INNER JOIN libros l ON ci.libro_isbn = l.isbn "
                   + "WHERE ci.cliente_id = ?";

        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId);
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
                            rs.getDouble("porcentaje_iva")
                    );
                    carrito.agregarItem(libro, rs.getInt("cantidad"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo carrito de MySQL: " + e.getMessage());
        }
        return carrito;
    }

    @Override
    public boolean guardarCarrito(Carrito carrito) {
        if (carrito == null || carrito.getClienteId() == null) return false;

        String sqlDelete = "DELETE FROM carrito_items WHERE cliente_id = ?";
        String sqlInsert = "INSERT INTO carrito_items (cliente_id, libro_isbn, cantidad) VALUES (?, ?, ?)";

        try (Connection conn = ConexionMySQL.obtenerConexion()) {
            conn.setAutoCommit(false);
            try {
                try (PreparedStatement psDel = conn.prepareStatement(sqlDelete)) {
                    psDel.setString(1, carrito.getClienteId());
                    psDel.executeUpdate();
                }

                if (!carrito.estaVacio()) {
                    try (PreparedStatement psIns = conn.prepareStatement(sqlInsert)) {
                        for (ItemCarrito item : carrito.getItems()) {
                            if (item.getLibro() != null) {
                                psIns.setString(1, carrito.getClienteId());
                                psIns.setString(2, item.getLibro().getIsbn());
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
                conn.rollback();
                System.err.println("Rollback guardando carrito en MySQL: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión en RepositorioCarritoMySQL: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean vaciarCarrito(String clienteId) {
        if (clienteId == null) return false;
        String sql = "DELETE FROM carrito_items WHERE cliente_id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error vaciando carrito en MySQL: " + e.getMessage());
            return false;
        }
    }
}