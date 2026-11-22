package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.Cliente;
import co.edu.uptc.caso.estudio.model.FabricaClientes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarioMySQL implements IRepositorioUsuario {

    @Override
    public boolean guardarUsuario(Cliente c) {
        if (c == null || c.getId() == null || c.getCorreo() == null) return false;
        String sql = "INSERT INTO usuarios (id, nombre_completo, correo, direccion_envio, departamento, ciudad, telefono, tipo_cliente, porcentaje_descuento, contrasena_hash) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getId());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getCorreo().trim());
            ps.setString(4, c.getDireccionEnvio());
            ps.setString(5, c.getDepartamento());
            ps.setString(6, c.getCiudad());
            ps.setString(7, c.getTelefono());
            ps.setString(8, c.getTipoCliente());
            ps.setDouble(9, c.getPorcentajeDescuento());
            ps.setString(10, c.getContrasenaHash());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error guardando usuario en MySQL: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Cliente buscarPorId(String id) {
        if (id == null) return null;
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando usuario por ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        if (correo == null) return null;
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error buscando usuario por correo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error listando usuarios de MySQL: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarUsuario(Cliente c) {
        if (c == null || c.getId() == null) return false;
        String sql = "UPDATE usuarios SET nombre_completo=?, direccion_envio=?, departamento=?, ciudad=?, telefono=?, tipo_cliente=?, porcentaje_descuento=?, contrasena_hash=? WHERE id=?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombreCompleto());
            ps.setString(2, c.getDireccionEnvio());
            ps.setString(3, c.getDepartamento());
            ps.setString(4, c.getCiudad());
            ps.setString(5, c.getTelefono());
            ps.setString(6, c.getTipoCliente());
            ps.setDouble(7, c.getPorcentajeDescuento());
            ps.setString(8, c.getContrasenaHash());
            ps.setString(9, c.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizando usuario en MySQL: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(String id) {
        if (id == null) return false;
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error eliminando usuario en MySQL: " + e.getMessage());
            return false;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return FabricaClientes.crear(
                rs.getString("id"),
                rs.getString("nombre_completo"),
                rs.getString("correo"),
                rs.getString("direccion_envio"),
                rs.getString("departamento"),
                rs.getString("ciudad"),
                rs.getString("telefono"),
                rs.getString("tipo_cliente"),
                rs.getString("contrasena_hash")
        );
    }
}