package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CarritoExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCarrito;
import co.edu.uptc.tienda.modelo.RegistroCarrito;

public class CarritoBD implements IPersistenciaCarrito {

    @Override
    public void guardar(List<RegistroCarrito> registros) throws CarritoExcepcion {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                sincronizar(conexion, registros);
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new CarritoExcepcion("No se pudo guardar el carrito en la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    private void sincronizar(Connection conexion, List<RegistroCarrito> registros) throws SQLException {
        List<RegistroCarrito> guardados = consultar(conexion);
        for (RegistroCarrito registro : registros) {
            RegistroCarrito guardado = buscar(guardados, registro.getIdCliente(), registro.getIsbn());
            if (guardado == null) {
                insertar(conexion, registro);
            } else if (guardado.getCantidad() != registro.getCantidad()) {
                actualizar(conexion, registro);
            }
        }
        for (RegistroCarrito guardado : guardados) {
            if (buscar(registros, guardado.getIdCliente(), guardado.getIsbn()) == null) {
                eliminar(conexion, guardado);
            }
        }
    }

    @Override
    public List<RegistroCarrito> leer() throws CarritoExcepcion {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            return consultar(conexion);
        } catch (SQLException e) {
            throw new CarritoExcepcion("No se pudo leer el carrito de la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    private void insertar(Connection conexion, RegistroCarrito registro) throws SQLException {
        String sql = "INSERT INTO carrito (id_cliente, isbn, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, registro.getIdCliente());
            sentencia.setString(2, registro.getIsbn());
            sentencia.setInt(3, registro.getCantidad());
            sentencia.executeUpdate();
        }
    }

    private List<RegistroCarrito> consultar(Connection conexion) throws SQLException {
        List<RegistroCarrito> registros = new ArrayList<>();
        String sql = "SELECT id_cliente, isbn, cantidad FROM carrito";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                registros.add(new RegistroCarrito(resultado.getString("id_cliente"),
                        resultado.getString("isbn"), resultado.getInt("cantidad")));
            }
        }
        return registros;
    }

    private void actualizar(Connection conexion, RegistroCarrito registro) throws SQLException {
        String sql = "UPDATE carrito SET cantidad = ? WHERE id_cliente = ? AND isbn = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, registro.getCantidad());
            sentencia.setString(2, registro.getIdCliente());
            sentencia.setString(3, registro.getIsbn());
            sentencia.executeUpdate();
        }
    }

    private void eliminar(Connection conexion, RegistroCarrito registro) throws SQLException {
        String sql = "DELETE FROM carrito WHERE id_cliente = ? AND isbn = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, registro.getIdCliente());
            sentencia.setString(2, registro.getIsbn());
            sentencia.executeUpdate();
        }
    }

    private RegistroCarrito buscar(List<RegistroCarrito> registros, String idCliente, String isbn) {
        for (RegistroCarrito registro : registros) {
            if (registro.getIdCliente().equals(idCliente) && registro.getIsbn().equals(isbn)) {
                return registro;
            }
        }
        return null;
    }
}
