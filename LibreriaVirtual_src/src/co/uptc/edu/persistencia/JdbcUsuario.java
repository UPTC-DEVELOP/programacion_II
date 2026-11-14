package co.uptc.edu.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.uptc.edu.interfaces.IGestionUsuario;
import co.uptc.edu.model.Rol;
import co.uptc.edu.model.Usuario;

/**
 * Persistencia de usuarios en la tabla "usuarios" de MariaDB.
 * Todas las consultas usan parámetros (PreparedStatement) para evitar inyección SQL.
 */
public class JdbcUsuario implements IGestionUsuario {

    private static final String COLUMNAS =
            "nombre, apellido, correo, direccion, telefono, tipo_usuario, salt, hash_contrasena";

    @Override
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (" + COLUMNAS + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignarDatos(sentencia, usuario);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw error("registrar el usuario", e);
        }
    }

    @Override
    public void actualizar(String correoAnterior, Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, apellido = ?, correo = ?, direccion = ?, telefono = ?, "
                + "tipo_usuario = ?, salt = ?, hash_contrasena = ? WHERE correo = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            asignarDatos(sentencia, usuario);
            sentencia.setString(9, correoAnterior);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw error("actualizar el usuario", e);
        }
    }

    @Override
    public void eliminar(String correo) {
        String sql = "DELETE FROM usuarios WHERE correo = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo);
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw error("eliminar el usuario", e);
        }
    }

    @Override
    public Usuario buscar(String correo) {
        String sql = "SELECT " + COLUMNAS + " FROM usuarios WHERE correo = ?";
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? armarUsuario(resultado) : null;
            }
        } catch (SQLException e) {
            throw error("consultar el usuario", e);
        }
    }

    @Override
    public List<Usuario> listar() {
        String sql = "SELECT " + COLUMNAS + " FROM usuarios ORDER BY id";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                usuarios.add(armarUsuario(resultado));
            }
        } catch (SQLException e) {
            throw error("listar los usuarios", e);
        }
        return usuarios;
    }

    // ---------- Auxiliares ----------
    private static void asignarDatos(PreparedStatement sentencia, Usuario u) throws SQLException {
        sentencia.setString(1, u.getNombre());
        sentencia.setString(2, u.getApellido());
        sentencia.setString(3, u.getCorreo());
        sentencia.setString(4, u.getDireccion());
        sentencia.setString(5, u.getTelefono());
        sentencia.setString(6, u.getTipoUsuario().name());
        sentencia.setBytes(7, u.getSalt());
        sentencia.setBytes(8, u.getHashContrasena());
    }

    private static Usuario armarUsuario(ResultSet r) throws SQLException {
        return Usuario.reconstruir(r.getString("nombre"), r.getString("apellido"), r.getString("correo"),
                r.getString("direccion"), r.getString("telefono"), Rol.valueOf(r.getString("tipo_usuario")),
                r.getBytes("salt"), r.getBytes("hash_contrasena"));
    }

    private static IllegalStateException error(String accion, SQLException causa) {
        System.err.println("Error de base de datos: " + causa.getMessage());
        return new IllegalStateException("No se pudo " + accion + " en la base de datos.", causa);
    }
}
