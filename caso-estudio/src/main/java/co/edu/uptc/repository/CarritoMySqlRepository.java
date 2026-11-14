package co.edu.uptc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.config.ConexionBD;
import co.edu.uptc.model.Carrito;

public class CarritoMySqlRepository {

    // CREATE: Insertar un producto en el carrito
    public void guardar(Carrito item) {
        String sql = "INSERT INTO carrito (id_producto, titulo, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getIdProducto());
            ps.setString(2, item.getTitulo());
            ps.setInt(3, item.getCantidad());
            ps.setDouble(4, item.getPrecioUnitario());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar en carrito MySQL: " + e.getMessage(), e);
        }
    }

    // READ: Listar todos los productos del carrito
    public List<Carrito> listar() {
        String sql = "SELECT id_producto, titulo, cantidad, precio_unitario FROM carrito";
        List<Carrito> items = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(new Carrito(
                    rs.getString("id_producto"),
                    rs.getString("titulo"),
                    rs.getInt("cantidad"),
                    rs.getDouble("precio_unitario")
                ));
            }
            return items;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar el carrito desde MySQL: " + e.getMessage(), e);
        }
    }

    // UPDATE: Actualizar la cantidad de un producto existente
    public void actualizarCantidad(String idProducto, int nuevaCantidad) {
        String sql = "UPDATE carrito SET cantidad = ? WHERE id_producto = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, nuevaCantidad);
            ps.setString(2, idProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cantidad en carrito MySQL: " + e.getMessage(), e);
        }
    }

    // DELETE: Eliminar un producto específico
    public void eliminar(String idProducto) {
        String sql = "DELETE FROM carrito WHERE id_producto = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, idProducto);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar item del carrito MySQL: " + e.getMessage(), e);
        }
    }

    // DELETE ALL: Vaciar todo el carrito
    public void vaciar() {
        String sql = "DELETE FROM carrito";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al vaciar el carrito en MySQL: " + e.getMessage(), e);
        }
    }
}