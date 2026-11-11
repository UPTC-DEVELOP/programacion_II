package persistencia;
import configuracion.ConfiguracionBD;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import modelo.ItemCarrito;

/** Persistencia de carrito con transacciones JDBC y sentencias parametrizadas. */
public final class PersistenciaCarritoJdbc {
    private final String carritoId;

    public PersistenciaCarritoJdbc() {
        this("local");
    }

    public PersistenciaCarritoJdbc(String carritoId) {
        if (carritoId == null || carritoId.isBlank()
                || carritoId.length() > 120) {
            throw new IllegalArgumentException("Carrito invalido");
        }
        this.carritoId = carritoId;
    }
    public List<PersistenciaCarrito.Registro> leer() throws IOException {
        List<PersistenciaCarrito.Registro> registros = new ArrayList<>();
        String sql = "SELECT titulo, autor, formato, precio, cantidad FROM carrito_items WHERE carrito_id = ? ORDER BY id";
        try (Connection cn = ConfiguracionBD.conexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, carritoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) registros.add(new PersistenciaCarrito.Registro(rs.getString(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5)));
            }
        } catch (SQLException ex) { throw new IOException("No se pudo consultar carrito MySQL: " + ex.getMessage(), ex); }
        return registros;
    }
    public void guardar(List<ItemCarrito> items) throws IOException {
        try (Connection cn = ConfiguracionBD.conexion()) {
            cn.setAutoCommit(false);
            try {
                try (PreparedStatement d = cn.prepareStatement("DELETE FROM carrito_items WHERE carrito_id = ?")) {
                    d.setString(1, carritoId);d.executeUpdate();
                }
                try (PreparedStatement i = cn.prepareStatement("INSERT INTO carrito_items (carrito_id,titulo,autor,formato,precio,cantidad) VALUES (?,?,?,?,?,?)")) {
                    for (ItemCarrito item:items) {
                        i.setString(1,carritoId);i.setString(2,item.getLibro().getTitulo());i.setString(3,item.getLibro().getAutor());
                        i.setString(4,item.getLibro().getFormato());i.setInt(5,item.getLibro().getPrecio());i.setInt(6,item.getCantidad());i.addBatch();
                    }
                    i.executeBatch();
                }
                cn.commit();
            } catch (SQLException|RuntimeException e) {
                try { cn.rollback(); } catch (SQLException rollback) {e.addSuppressed(rollback);}
                throw e;
            }
        } catch (SQLException ex) { throw new IOException("No se pudo guardar carrito MySQL: " + ex.getMessage(), ex); }
    }
}
