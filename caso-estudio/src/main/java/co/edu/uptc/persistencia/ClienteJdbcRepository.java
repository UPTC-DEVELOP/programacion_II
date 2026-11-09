package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.IClienteRepositorio;
import co.edu.uptc.modelo.Cliente;
import co.edu.uptc.modelo.ClientePremium;
import co.edu.uptc.modelo.ClienteRegular;
import co.edu.uptc.modelo.enums.Rol;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.persistencia.conexion.ConexionDB;

/**
 * IMPLEMENTA IClienteRepositorio Y GESTIONA LA PERSISTENCIA DE CLIENTES EN POSTGRESQL
 *  @author: Equipo de desarrollo grupo 7 UPTC
 *  @modificacion:Brayan Panqueva
 */
public class ClienteJdbcRepository implements IClienteRepositorio {

    private static final String SELECT = "SELECT c.id_cliente,c.primer_nombre,c.otros_nombres,c.primer_apellido,c.otros_apellidos,c.tipo_identificacion,c.identificacion,c.correo_electronico,c.telefono_contacto,c.direccion_envio,c.tipo_cliente,c.contrasenia_hash,c.fecha_registro,c.intentos_fallidos,COALESCE(r.codigo,'USER') rol,COALESCE((SELECT COUNT(*) FROM buys b WHERE b.id_cliente=c.id_cliente),0) compras FROM clients c LEFT JOIN profiles_rols r ON r.id_rol=c.rol_id";

    /*modificacion:Brayan Panqueva*/
    @Override
    public void guardar(Cliente c) {
        String sql = "INSERT INTO clients (nombre_completo,correo_electronico,direccion_envio,telefono_contacto,tipo_cliente,primer_nombre,otros_nombres,primer_apellido,otros_apellidos,tipo_identificacion,identificacion,contrasenia_hash,fecha_registro,intentos_fallidos) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) RETURNING id_cliente,rol_id";
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            bind(ps, c);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c.setIdCliente(rs.getInt(1));
            
                }}
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo registrar el cliente ", e);
        }
    }

    @Override
    public void actualizar(Cliente c) {
        String sql = "UPDATE clients SET nombre_completo=?,correo_electronico=?,direccion_envio=?,telefono_contacto=?,tipo_cliente=?,primer_nombre=?,otros_nombres=?,primer_apellido=?,otros_apellidos=?,tipo_identificacion=?,identificacion=?,contrasenia_hash=?,intentos_fallidos=? WHERE id_cliente=?";
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement ps = cn.prepareStatement(sql)) {
            bindUpdate(ps, c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo actualizar el cliente ", e);
        }
    }

    private void bind(PreparedStatement p, Cliente c) throws SQLException {
        p.setString(1, c.getNombreCompleto());
        p.setString(2, c.getCorreoElectronico().trim().toLowerCase());
        p.setString(3, c.getDireccion());
        p.setString(4, c.getCelular());
        p.setString(5, c.getTipoCliente().getNombreMostrar());
        p.setString(6, c.getPrimerNombre());
        p.setString(7, c.getOtrosNombres());
        p.setString(8, c.getPrimerApellido());
        p.setString(9, c.getOtrosApellidos());
        p.setString(10, c.getTipoIdentificacion());
        p.setString(11, c.getIdentificacion());
        p.setString(12, c.getContrasenia());
        p.setTimestamp(13, c.getFechaRegistro() == null ? new Timestamp(System.currentTimeMillis()) : c.getFechaRegistro());
        p.setInt(14, c.getIntentosFallidos());
    }

    private void bindUpdate(PreparedStatement p, Cliente c) throws SQLException {
        bindUpdateBase(p, c);
        p.setInt(14, c.getIdCliente());
    }

    private void bindUpdateBase(PreparedStatement p, Cliente c) throws SQLException {
        p.setString(1, c.getNombreCompleto());
        p.setString(2, c.getCorreoElectronico().trim().toLowerCase());
        p.setString(3, c.getDireccion());
        p.setString(4, c.getCelular());
        p.setString(5, c.getTipoCliente().getNombreMostrar());
        p.setString(6, c.getPrimerNombre());
        p.setString(7, c.getOtrosNombres());
        p.setString(8, c.getPrimerApellido());
        p.setString(9, c.getOtrosApellidos());
        p.setString(10, c.getTipoIdentificacion());
        p.setString(11, c.getIdentificacion());
        p.setString(12, c.getContrasenia());
        p.setInt(13, c.getIntentosFallidos());
    }

    @Override
    public void eliminar(String identificacion) {
        ejecutarDelete("identificacion", identificacion);
    }

    @Override
    public void eliminarPorId(int id) {
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement p = cn.prepareStatement("DELETE FROM clients WHERE id_cliente=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar el cliente", e);
        }
    }

    private void ejecutarDelete(String columna, String valor) {
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement p = cn.prepareStatement("DELETE FROM clients WHERE " + columna + "=?")) {
            p.setString(1, valor);
            p.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo eliminar el cliente", e);
        }
    }

    @Override
    public List<Cliente> listar() {
        return consultar(SELECT + " ORDER BY c.id_cliente");
    }

    @Override
    public Cliente buscar(String id) {
        return primero(SELECT + " WHERE c.identificacion=?", id);
    }

    @Override
    public Cliente buscarPorCorreo(String correo) {
        return primero(SELECT + " WHERE c.correo_electronico=lower(?)", correo);
    }

    @Override
    public Cliente buscarPorId(int id) {
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement p = cn.prepareStatement(SELECT + " WHERE c.id_cliente=?")) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el cliente", e);
        }
    }

    private Cliente primero(String sql, String val) {
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement p = cn.prepareStatement(sql)) {
            p.setString(1, val);
            try (ResultSet r = p.executeQuery()) {
                return r.next() ? map(r) : null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo consultar el cliente", e);
        }
    }

    private List<Cliente> consultar(String sql) {
        List<Cliente> out = new ArrayList<>();
        try (Connection cn = ConexionDB.obtenerConexion(); PreparedStatement p = cn.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                out.add(map(r));
            
            }return out;
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron consultar los clientes", e);
        }
    }

    private Cliente map(ResultSet r) throws SQLException {
        TipoCliente tipo = "Premium".equalsIgnoreCase(r.getString("tipo_cliente")) ? TipoCliente.PREMIUM : TipoCliente.REGULAR;
        Rol rol = "ADMIN".equalsIgnoreCase(r.getString("rol")) ? Rol.ADMIN : Rol.CLIENTE;
        Cliente c = tipo == TipoCliente.PREMIUM ? new ClientePremium(r.getString("primer_nombre"), r.getString("otros_nombres"), r.getString("primer_apellido"), r.getString("otros_apellidos"), r.getString("tipo_identificacion"), r.getString("identificacion"), r.getString("correo_electronico"), r.getString("telefono_contacto"), r.getString("direccion_envio"), r.getInt("id_cliente"), tipo, r.getString("contrasenia_hash"), r.getTimestamp("fecha_registro"), r.getInt("intentos_fallidos"), rol) : new ClienteRegular(r.getString("primer_nombre"), r.getString("otros_nombres"), r.getString("primer_apellido"), r.getString("otros_apellidos"), r.getString("tipo_identificacion"), r.getString("identificacion"), r.getString("correo_electronico"), r.getString("telefono_contacto"), r.getString("direccion_envio"), r.getInt("id_cliente"), tipo, r.getString("contrasenia_hash"), r.getTimestamp("fecha_registro"), r.getInt("intentos_fallidos"), rol);
        c.setComprasRealizadas(r.getInt("compras"));
        return c;
    }
}
