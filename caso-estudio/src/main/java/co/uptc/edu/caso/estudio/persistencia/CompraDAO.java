package co.uptc.edu.caso.estudio.persistencia;

import co.uptc.edu.caso.estudio.negocio.Compra;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraDAO {

    public boolean guardar(Compra compra) {
        if (buscarPorId(compra.getIdCompra()) != null) {
            return false; // Ya existe ese ID
        }
        compra.calcularTotal();
        String sql = "INSERT INTO compra (id_compra, fecha, total, estado) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, compra.getIdCompra());
            ps.setTimestamp(2, new Timestamp(compra.getFecha().getTime()));
            ps.setDouble(3, compra.getTotal());
            ps.setString(4, compra.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar compra: " + e.getMessage());
            return false;
        }
    }

    public Compra buscarPorId(int id) {
        String sql = "SELECT * FROM compra WHERE id_compra = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Compra c = new Compra();
                    c.setIdCompra(rs.getInt("id_compra"));
                    c.setFecha(new java.util.Date(rs.getTimestamp("fecha").getTime()));
                    c.setTotal(rs.getDouble("total"));
                    c.setEstado(rs.getString("estado"));
                    return c;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar compra: " + e.getMessage());
        }
        return null;
    }

    public boolean actualizarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE compra SET estado = ? WHERE id_compra = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar compra: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM compra WHERE id_compra = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar compra: " + e.getMessage());
            return false;
        }
    }

    public List<Compra> obtenerTodas() {
        List<Compra> lista = new ArrayList<>();
        String sql = "SELECT * FROM compra";
        try (Connection con = ConexionBD.conectar();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Compra c = new Compra();
                c.setIdCompra(rs.getInt("id_compra"));
                c.setFecha(new java.util.Date(rs.getTimestamp("fecha").getTime()));
                c.setTotal(rs.getDouble("total"));
                c.setEstado(rs.getString("estado"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar compras: " + e.getMessage());
        }
        return lista;
    }
}
