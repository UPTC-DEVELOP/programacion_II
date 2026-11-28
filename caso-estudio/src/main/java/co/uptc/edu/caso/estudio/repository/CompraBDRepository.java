package co.uptc.edu.caso.estudio.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.uptc.edu.caso.estudio.negocio.Compra;
import co.uptc.edu.caso.estudio.negocio.DetalleCompra;
import co.uptc.edu.caso.estudio.negocio.Libro;
import co.uptc.edu.caso.estudio.persistencia.ConexionBD;

public class CompraBDRepository implements CompraRepository {

    @Override
    public void guardar(Compra compra) {
        compra.calcularTotal();
        String sqlCompra = "INSERT INTO compra (id_compra, fecha, total, estado) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_compra (id_compra, isbn_libro, titulo_libro, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.conectar()) {
            con.setAutoCommit(false);

            try (PreparedStatement psCompra = con.prepareStatement(sqlCompra)) {
                psCompra.setInt(1, compra.getIdCompra());
                psCompra.setTimestamp(2, new Timestamp(compra.getFecha().getTime()));
                psCompra.setDouble(3, compra.getTotal());
                psCompra.setString(4, compra.getEstado());
                psCompra.executeUpdate();
            }

            if (compra.getDetalles() != null) {
                try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalle)) {
                    for (DetalleCompra d : compra.getDetalles()) {
                        psDetalle.setInt(1, compra.getIdCompra());
                        psDetalle.setString(2, d.getLibro() != null ? d.getLibro().getIsbn() : "");
                        psDetalle.setString(3, d.getLibro() != null ? d.getLibro().getTitulo() : "");
                        psDetalle.setInt(4, d.getCantidad());
                        psDetalle.setDouble(5, d.getPrecioUnitario());
                        psDetalle.setDouble(6, d.calcularSubtotal());
                        psDetalle.addBatch();
                    }
                    psDetalle.executeBatch();
                }
            }

            con.commit();
            System.out.println("Compra y detalles guardados correctamente en MariaDB.");
        } catch (SQLException e) {
            System.err.println("Error al guardar compra en BD: " + e.getMessage());
        }
    }

    @Override
    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT * FROM compra";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Compra c = new Compra();
                c.setIdCompra(rs.getInt("id_compra"));
                c.setFecha(new java.util.Date(rs.getTimestamp("fecha").getTime()));
                c.setTotal(rs.getDouble("total"));
                c.setEstado(rs.getString("estado"));
                c.setDetalles(cargarDetalles(con, c.getIdCompra()));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras en BD: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Compra buscarPorId(int idCompra) {
        String sql = "SELECT * FROM compra WHERE id_compra = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Compra c = new Compra();
                    c.setIdCompra(rs.getInt("id_compra"));
                    c.setFecha(new java.util.Date(rs.getTimestamp("fecha").getTime()));
                    c.setTotal(rs.getDouble("total"));
                    c.setEstado(rs.getString("estado"));
                    c.setDetalles(cargarDetalles(con, c.getIdCompra()));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar compra en BD: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void actualizar(Compra compra) {
        String sql = "UPDATE compra SET estado = ?, total = ? WHERE id_compra = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, compra.getEstado());
            ps.setDouble(2, compra.getTotal());
            ps.setInt(3, compra.getIdCompra());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar compra en BD: " + e.getMessage());
        }
    }

    @Override
    public void eliminar(int idCompra) {
        String sql = "DELETE FROM compra WHERE id_compra = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCompra);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar compra en BD: " + e.getMessage());
        }
    }

    private List<DetalleCompra> cargarDetalles(Connection con, int idCompra) throws SQLException {
        List<DetalleCompra> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_compra WHERE id_compra = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCompra);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Libro libro = new Libro();
                    libro.setIsbn(rs.getString("isbn_libro"));
                    libro.setTitulo(rs.getString("titulo_libro"));
                    libro.setPrecioVenta(rs.getDouble("precio_unitario"));

                    DetalleCompra d = new DetalleCompra(
                            rs.getInt("id_detalle"),
                            rs.getInt("cantidad"),
                            rs.getDouble("precio_unitario"),
                            libro
                    );
                    detalles.add(d);
                }
            }
        }
        return detalles;
    }
}
