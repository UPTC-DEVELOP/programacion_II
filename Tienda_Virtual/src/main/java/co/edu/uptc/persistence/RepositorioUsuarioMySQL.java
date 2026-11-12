package co.edu.uptc.persistence;

import co.edu.uptc.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuarioMySQL implements IRepositorioUsuario {

    @Override
    public boolean guardarUsuario(Cliente c) {
        String sql = "INSERT INTO usuarios (id, nombre_completo, correo, direccion_envio, departamento, ciudad, telefono, tipo_cliente, porcentaje_descuento, contrasena_hash) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getId());
            ps.setString(2, c.getNombreCompleto());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getDireccionEnvio());
            ps.setString(5, c.getDepartamento());
            ps.setString(6, c.getCiudad());
            ps.setString(7, c.getTelefono());
            ps.setString(8, c.getTipoCliente());
            ps.setDouble(9, c.getPorcentajeDescuento());
            ps.setString(10, c.getContrasenaHash());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Cliente buscarPorId(String id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM usuarios WHERE correo = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearCliente(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cliente> obtenerTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) lista.add(mapearCliente(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public boolean actualizarUsuario(Cliente c) {
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
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(String id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = ConexionMySQL.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getString("id"));
        c.setNombreCompleto(rs.getString("nombre_completo"));
        c.setCorreo(rs.getString("correo"));
        c.setDireccionEnvio(rs.getString("direccion_envio"));
        c.setDepartamento(rs.getString("departamento"));
        c.setCiudad(rs.getString("ciudad"));
        c.setTelefono(rs.getString("telefono"));
        c.setTipoCliente(rs.getString("tipo_cliente"));
        c.setContrasenaHash(rs.getString("contrasena_hash"));
        return c;
    }
}
