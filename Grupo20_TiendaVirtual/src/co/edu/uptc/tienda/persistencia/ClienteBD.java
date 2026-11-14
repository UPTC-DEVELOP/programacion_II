package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.ClienteExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCliente;
import co.edu.uptc.tienda.modelo.Cliente;

public class ClienteBD implements IPersistenciaCliente {

    @Override
    public void guardar(List<Cliente> clientes) throws ClienteExcepcion {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            List<Cliente> guardados = consultar(conexion);
            for (Cliente cliente : clientes) {
                if (buscar(guardados, cliente.getId()) == null) {
                    insertar(conexion, cliente);
                } else {
                    actualizar(conexion, cliente);
                }
            }
            for (Cliente guardado : guardados) {
                if (buscar(clientes, guardado.getId()) == null) {
                    eliminar(conexion, guardado.getId());
                }
            }
        } catch (SQLException e) {
            throw new ClienteExcepcion("No se pudieron guardar los clientes en la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    @Override
    public List<Cliente> leer() throws ClienteExcepcion {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            return consultar(conexion);
        } catch (SQLException e) {
            throw new ClienteExcepcion("No se pudieron leer los clientes de la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    private List<Cliente> consultar(Connection conexion) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, nombre_completo, correo_electronico, direccion_envio, celular, tipo_cliente FROM cliente";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                clientes.add(new Cliente(resultado.getString("id"),
                        resultado.getString("nombre_completo"),
                        resultado.getString("correo_electronico"),
                        resultado.getString("direccion_envio"),
                        resultado.getString("celular"),
                        resultado.getString("tipo_cliente")));
            }
        }
        return clientes;
    }

    private void insertar(Connection conexion, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (id, nombre_completo, correo_electronico, direccion_envio, celular, tipo_cliente) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, cliente.getId());
            sentencia.setString(2, cliente.getNombreCompleto());
            sentencia.setString(3, cliente.getCorreoElectronico());
            sentencia.setString(4, cliente.getDireccionEnvio());
            sentencia.setString(5, cliente.getCelular());
            sentencia.setString(6, cliente.getTipoCliente());
            sentencia.executeUpdate();
        }
    }

    private void actualizar(Connection conexion, Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nombre_completo = ?, correo_electronico = ?, direccion_envio = ?, "
                + "celular = ?, tipo_cliente = ? WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, cliente.getNombreCompleto());
            sentencia.setString(2, cliente.getCorreoElectronico());
            sentencia.setString(3, cliente.getDireccionEnvio());
            sentencia.setString(4, cliente.getCelular());
            sentencia.setString(5, cliente.getTipoCliente());
            sentencia.setString(6, cliente.getId());
            sentencia.executeUpdate();
        }
    }

    private void eliminar(Connection conexion, String id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, id);
            sentencia.executeUpdate();
        }
    }

    private Cliente buscar(List<Cliente> clientes, String id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                return cliente;
            }
        }
        return null;
    }
}
