package com.letsread.repository;

import com.letsread.model.Cliente;
import com.letsread.model.ClienteFactory;
import com.letsread.persistence.ConexionJDBC;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Guarda los clientes en la tabla clientes de MariaDB usando JDBC.
 * Usa la misma ConexionJDBC que la sección de libros (no se modifica).
 */
public class ClienteBdRepositorio implements ClienteRepositorio {

    private static final String SELECT =
        "SELECT correo, tipo, nombre_completo, direccion, telefono, clave FROM clientes";

    private static final String INSERT =
        "INSERT INTO clientes (correo, tipo, nombre_completo, direccion, telefono, clave) "
        + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
        "UPDATE clientes SET tipo = ?, nombre_completo = ?, direccion = ?, telefono = ?, clave = ? "
        + "WHERE correo = ?";

    private static final String DELETE = "DELETE FROM clientes WHERE correo = ?";

    @Override
    public void guardar(Cliente cliente) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(INSERT)) {
            ps.setString(1, cliente.getCorreo());
            ps.setString(2, cliente.getTipo());
            ps.setString(3, cliente.getNombreCompleto());
            ps.setString(4, cliente.getDireccion());
            ps.setString(5, cliente.getTelefono());
            ps.setString(6, cliente.getClave());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new IllegalStateException("Error al guardar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorCorreo(String correo) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT + " WHERE correo = ?")) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al buscar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(SELECT);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al listar clientes: " + e.getMessage(), e);
        }
        return clientes;
    }

    @Override
    public void actualizar(Cliente cliente) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, cliente.getTipo());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getClave());
            ps.setString(6, cliente.getCorreo());
            if (ps.executeUpdate() == 0) {
                throw new IllegalArgumentException("No existe un cliente con el correo " + cliente.getCorreo());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al actualizar el cliente: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(String correo) {
        try (Connection con = ConexionJDBC.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setString(1, correo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Error al eliminar el cliente: " + e.getMessage(), e);
        }
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        return ClienteFactory.crear(
            rs.getString("tipo"),
            rs.getString("nombre_completo"),
            rs.getString("correo"),
            rs.getString("direccion"),
            rs.getString("telefono"),
            rs.getString("clave"));
    }
}