package com.tiendavirtual.repositorio;

import com.tiendavirtual.modelo.Cliente;
import com.tiendavirtual.util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepositoryMySQL implements ClienteRepository {

    @Override
    public boolean guardar(Cliente c) {
        String sql = "INSERT INTO clientes "
                + "(tipo_documento, numero_documento, nombre, apellido, correo, telefono, contrasenia) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getTipoDocumento());
            ps.setInt(2, c.getNumeroDocumento());
            ps.setString(3, c.getNombre());
            ps.setString(4, c.getApellido());
            ps.setString(5, c.getCorreo());
            ps.setString(6, c.getTelefono());
            ps.setString(7, c.getContrasenia());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Cliente c) {
        String sql = "UPDATE clientes SET tipo_documento=?, nombre=?, apellido=?, "
                + "correo=?, telefono=?, contrasenia=? WHERE numero_documento=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getTipoDocumento());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getApellido());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getContrasenia());
            ps.setInt(7, c.getNumeroDocumento());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int numeroDocumento) {
        String sql = "DELETE FROM clientes WHERE numero_documento=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numeroDocumento);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Cliente buscarPorDocumento(int numeroDocumento) {
        String sql = "SELECT * FROM clientes WHERE numero_documento=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, numeroDocumento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirCliente(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + e.getMessage(), e);
        }

        return null;
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                clientes.add(convertirCliente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar clientes: " + e.getMessage(), e);
        }

        return clientes;
    }

    @Override
    public Cliente iniciarSesion(String correo, String contrasenia) {
        String sql = "SELECT * FROM clientes WHERE correo=? AND contrasenia=?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasenia);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return convertirCliente(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }

        return null;
    }

    private Cliente convertirCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("tipo_documento"),
                rs.getInt("numero_documento"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("correo"),
                rs.getString("telefono"),
                rs.getString("contrasenia")
        );
    }
}