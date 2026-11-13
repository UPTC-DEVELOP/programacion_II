package usuariosjdbc.repositorio;
import java.sql.*;
import usuariosjdbc.conexion.ConexionBD;
import usuariosjdbc.modelo.Usuario;

public class RepositorioUsuarios {
    private final ConexionBD db = new ConexionBD();
    public void guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (correo,nombre,direccion,telefono,tipo_cliente,contrasena) VALUES (?,?,?,?,?,?)";
        try (Connection c=db.obtenerConexion(); PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,usuario.getCorreo());ps.setString(2,usuario.getNombre());ps.setString(3,usuario.getDireccion());
            ps.setString(4,usuario.getTelefono());ps.setString(5,usuario.getTipoCliente());ps.setString(6,usuario.getContrasena());
            ps.executeUpdate(); System.out.println("Usuario guardado correctamente");
        } catch(SQLException ex) { throw new IllegalStateException("Error al guardar usuario: "+ex.getMessage(),ex); }
    }
    public Usuario buscarUsuario(String correo) {
        try (Connection c=db.obtenerConexion(); PreparedStatement ps=c.prepareStatement("SELECT nombre,correo,direccion,telefono,tipo_cliente,contrasena FROM usuarios WHERE correo=?")) {
            ps.setString(1,correo);
            try(ResultSet rs=ps.executeQuery()) { return rs.next() ? new Usuario(rs.getString("nombre"),rs.getString("correo"),rs.getString("direccion"),rs.getString("telefono"),rs.getString("tipo_cliente"),rs.getString("contrasena")):null; }
        } catch(SQLException ex) { throw new IllegalStateException("Error al consultar usuario: "+ex.getMessage(),ex); }
    }
    public void actualizarUsuario(Usuario u) {
        String sql="UPDATE usuarios SET nombre=?,direccion=?,telefono=?,tipo_cliente=?,contrasena=? WHERE correo=?";
        try(Connection c=db.obtenerConexion();PreparedStatement ps=c.prepareStatement(sql)) {
            ps.setString(1,u.getNombre());ps.setString(2,u.getDireccion());ps.setString(3,u.getTelefono());ps.setString(4,u.getTipoCliente());ps.setString(5,u.getContrasena());ps.setString(6,u.getCorreo());
            if(ps.executeUpdate()==0) throw new IllegalArgumentException("Usuario no encontrado: "+u.getCorreo());
        } catch(SQLException ex) {throw new IllegalStateException("Error al actualizar usuario: "+ex.getMessage(),ex);}
    }
    public void eliminarUsuario(String correo) {
        try(Connection c=db.obtenerConexion();PreparedStatement ps=c.prepareStatement("DELETE FROM usuarios WHERE correo=?")) {
            ps.setString(1,correo);ps.executeUpdate();
        } catch(SQLException ex) {throw new IllegalStateException("Error al eliminar usuario: "+ex.getMessage(),ex);}
    }
    public java.util.ArrayList<Usuario> listarUsuarios() {
        java.util.ArrayList<Usuario> usuarios = new java.util.ArrayList<>();
        String sql = "SELECT nombre,correo,direccion,telefono,tipo_cliente,contrasena FROM usuarios ORDER BY nombre,correo";
        try (Connection c = db.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new Usuario(rs.getString("nombre"), rs.getString("correo"),
                    rs.getString("direccion"), rs.getString("telefono"),
                    rs.getString("tipo_cliente"), rs.getString("contrasena")));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Error al listar usuarios: " + ex.getMessage(), ex);
        }
        return usuarios;
    }
}
