package co.edu.uptc.persistence;

import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.model.Libro;
import java.sql.*;

public class RepositorioCarritoMySQL implements IRepositorioCarrito {

    private final RepositorioLibrosMySQL repoLibros = new RepositorioLibrosMySQL();

    @Override
    public Carrito obtenerCarritoPorCliente(String clienteId) {
        Carrito carrito = new Carrito(clienteId);
        String sql = "SELECT libro_isbn, cantidad FROM carrito_items WHERE cliente_id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro libro = repoLibros.buscarPorIsbn(rs.getString("libro_isbn"));
                    if (libro != null) {
                        carrito.agregarItem(libro, rs.getInt("cantidad"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carrito;
    }

    @Override
    public boolean guardarCarrito(Carrito carrito) {
        if (carrito == null || carrito.getClienteId() == null) return false;
        vaciarCarrito(carrito.getClienteId());
        String sql = "INSERT INTO carrito_items (cliente_id, libro_isbn, cantidad) VALUES (?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (ItemCarrito item : carrito.getItems()) {
                if (item.getLibro() != null) {
                    ps.setString(1, carrito.getClienteId());
                    ps.setString(2, item.getLibro().getIsbn());
                    ps.setInt(3, item.getCantidad());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public boolean vaciarCarrito(String clienteId) {
        String sql = "DELETE FROM carrito_items WHERE cliente_id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, clienteId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}