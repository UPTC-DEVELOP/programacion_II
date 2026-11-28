package co.edu.uptc.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.config.ConexionBD;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.model.Cliente;


public class ClienteBD implements IClienteRepositorio {

    @Override
    public void guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (cedula, nombre, apellido, telefono, "
                + "correo, direccion, tipo_cliente) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getCedula());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getApellido());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getCorreo());
            ps.setString(6, cliente.getDireccion());
            ps.setString(7, cliente.getTipoCliente());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, telefono = ?, "
                + "correo = ?, direccion = ?, tipo_cliente = ? WHERE cedula = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getApellido());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getCorreo());
            ps.setString(5, cliente.getDireccion());
            ps.setString(6, cliente.getTipoCliente());
            ps.setString(7, cliente.getCedula());
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(String cedula) {
        String sql = "DELETE FROM clientes WHERE cedula = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cedula);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> listar() {
        String sql = "SELECT cedula, nombre, apellido, telefono, correo, direccion, "
                + "tipo_cliente FROM clientes ORDER BY nombre, apellido";
        List<Cliente> clientes = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(construirCliente(rs));
            }
            return clientes;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar los clientes: " + e.getMessage(), e);
        }
    }

    private Cliente construirCliente(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getString("cedula"),
            rs.getString("nombre"),
            rs.getString("apellido"),
            rs.getString("telefono"),
            rs.getString("correo"),
            rs.getString("direccion"),
            rs.getString("tipo_cliente"));
    }
}
