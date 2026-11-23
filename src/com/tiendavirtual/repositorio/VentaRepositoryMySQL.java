package com.tiendavirtual.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiendavirtual.modelo.Venta;
import com.tiendavirtual.util.Conexion;

public class VentaRepositoryMySQL implements VentaRepository {

    @Override
    public void guardarVenta(Venta venta) {

        String sql = "INSERT INTO ventas "
                   + "(id_venta, id_cliente, fecha, total) "
                   + "VALUES (?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, venta.getId());
            ps.setInt(2, venta.getIdCliente());
            ps.setString(3, venta.getFecha());
            ps.setDouble(4, venta.getMontoTotal());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al registrar la venta: " + e.getMessage(), e);
        }
    }

    @Override
    public Venta buscarPorId(String id) {

        String sql = "SELECT * FROM ventas WHERE id_venta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return convertirVenta(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al consultar la venta: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Venta> obtenerTodas() {

        List<Venta> ventas = new ArrayList<>();

        String sql = "SELECT * FROM ventas";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ventas.add(convertirVenta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al listar las ventas: " + e.getMessage(), e);
        }

        return ventas;
    }

    @Override
    public void eliminarVenta(String id) {

        String sql = "DELETE FROM ventas WHERE id_venta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al eliminar la venta: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarVenta(Venta venta) {

        String sql = "UPDATE ventas SET "
                   + "id_cliente = ?, fecha = ?, total = ? "
                   + "WHERE id_venta = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, venta.getIdCliente());
            ps.setString(2, venta.getFecha());
            ps.setDouble(3, venta.getMontoTotal());
            ps.setString(4, venta.getId());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new RuntimeException(
                    "No se encontró la venta.");
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al actualizar la venta: " + e.getMessage(), e);
        }
    }

    private Venta convertirVenta(ResultSet rs) throws SQLException {

        return new Venta(
            rs.getString("id_venta"),
            rs.getString("fecha"),
            rs.getDouble("total"),
            rs.getInt("id_cliente")
        );
    }
}