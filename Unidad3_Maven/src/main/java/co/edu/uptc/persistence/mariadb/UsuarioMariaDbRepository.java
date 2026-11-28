package co.edu.uptc.persistence.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Administrador;
import co.edu.uptc.model.Cliente;
import co.edu.uptc.model.ClientePremium;
import co.edu.uptc.model.ClienteRegular;
import co.edu.uptc.model.Rol;
import co.edu.uptc.model.Usuario;
import co.edu.uptc.persistence.Conexion;
import co.edu.uptc.persistence.UsuarioRepository;

/**
 * Repositorio JDBC para usuarios y clientes.
 *
 * La contraseña recibida debe ser ya un hash SHA-256 generado por
 * AutenticacionService; esta clase nunca almacena la contraseña en texto plano.
 */
public class UsuarioMariaDbRepository implements UsuarioRepository {

    private static final String SELECT_COLUMNAS =
            "SELECT nombre, correo, hash, rol, tipo_usuario, direccion, telefono "
                    + "FROM usuario";

    @Override
    public List<Usuario> listar() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = SELECT_COLUMNAS + " ORDER BY nombre";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                usuarios.add(mapearUsuario(resultado));
            }
            return usuarios;
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo listar los usuarios", e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        String sql = SELECT_COLUMNAS + " WHERE LOWER(correo) = LOWER(?)";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo.trim());
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearUsuario(resultado));
                }
                return Optional.empty();
            }
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo buscar el usuario " + correo, e);
        }
    }

    @Override
    public void guardar(Usuario usuario) {
        String sql = "INSERT INTO usuario "
                + "(nombre, correo, hash, rol, tipo_usuario, direccion, telefono) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre), hash = VALUES(hash), "
                + "rol = VALUES(rol), tipo_usuario = VALUES(tipo_usuario), "
                + "direccion = VALUES(direccion), telefono = VALUES(telefono)";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, usuario.getNombreCompleto());
            sentencia.setString(2, usuario.getCorreo().trim().toLowerCase());
            sentencia.setString(3, usuario.getContrasenaHash());
            sentencia.setString(4, usuario.getRol().name());
            sentencia.setString(5, tipoUsuario(usuario));
            sentencia.setString(6, direccion(usuario));
            sentencia.setString(7, telefono(usuario));
            sentencia.executeUpdate();
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo guardar el usuario " + usuario.getCorreo(), e);
        }
    }

    private Connection obtenerConexion() throws SQLException {
        Connection conexion = Conexion.obtenerConexion();
        if (conexion == null) {
            throw new SQLException("Conexion.obtenerConexion() devolvió null");
        }
        return conexion;
    }

    private static Usuario mapearUsuario(ResultSet resultado) throws SQLException {
        String nombre = resultado.getString("nombre");
        String correo = resultado.getString("correo");
        String hash = resultado.getString("hash");
        Rol rol = Rol.valueOf(resultado.getString("rol"));

        if (rol == Rol.ADMINISTRADOR) {
            return new Administrador(nombre, correo, hash);
        }

        String tipo = resultado.getString("tipo_usuario");
        String direccion = resultado.getString("direccion");
        String telefono = resultado.getString("telefono");
        if ("PREMIUM".equalsIgnoreCase(tipo)) {
            return new ClientePremium(nombre, correo, hash, direccion, telefono);
        }
        return new ClienteRegular(nombre, correo, hash, direccion, telefono);
    }

    private static String tipoUsuario(Usuario usuario) {
        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            return "ADMINISTRADOR";
        }
        return usuario instanceof ClientePremium ? "PREMIUM" : "REGULAR";
    }

    private static String direccion(Usuario usuario) {
        return usuario instanceof Cliente ? ((Cliente) usuario).getDireccionEnvio() : null;
    }

    private static String telefono(Usuario usuario) {
        return usuario instanceof Cliente ? ((Cliente) usuario).getTelefono() : null;
    }

    private static PersistenciaException error(String mensaje, Exception causa) {
        return new PersistenciaException(mensaje, causa);
    }
}
