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
            List<RegistroCarrito> guardados = consultar(conexion);
            for (RegistroCarrito registro : registros) {
                RegistroCarrito guardado = buscar(guardados, registro.getIsbn());
                if (guardado == null) {
                    insertar(conexion, registro);
                } else if (!sonIguales(guardado, registro)) {
                    actualizar(conexion, registro);
                }
            }
            for (RegistroCarrito guardado : guardados) {
                if (buscar(registros, guardado.getIsbn()) == null) {
                    eliminar(conexion, guardado.getIsbn());
                }
            }
        } catch (SQLException e) {
            throw new CarritoExcepcion("No se pudo guardar el carrito en la base de datos: "
                    + ConexionBD.explicarError(e));
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
        String sql = "INSERT INTO carrito (isbn, id_cliente, cantidad) VALUES (?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, registro.getIsbn());
            sentencia.setString(2, registro.getIdCliente());
            sentencia.setInt(3, registro.getCantidad());
            sentencia.executeUpdate();
        }
    }

    private List<RegistroCarrito> consultar(Connection conexion) throws SQLException {
        List<RegistroCarrito> registros = new ArrayList<>();
        String sql = "SELECT isbn, id_cliente, cantidad FROM carrito";
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
        String sql = "UPDATE carrito SET id_cliente = ?, cantidad = ? WHERE isbn = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, registro.getIdCliente());
            sentencia.setInt(2, registro.getCantidad());
            sentencia.setString(3, registro.getIsbn());
            sentencia.executeUpdate();
        }
    }

    private void eliminar(Connection conexion, String isbn) throws SQLException {
        String sql = "DELETE FROM carrito WHERE isbn = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, isbn);
            sentencia.executeUpdate();
        }
    }

    private RegistroCarrito buscar(List<RegistroCarrito> registros, String isbn) {
        for (RegistroCarrito registro : registros) {
            if (registro.getIsbn().equals(isbn)) {
                return registro;
            }
        }
        return null;
    }

    private boolean sonIguales(RegistroCarrito uno, RegistroCarrito otro) {
        String cliente = uno.getIdCliente();
        boolean mismoCliente = (cliente == null) ? otro.getIdCliente() == null : cliente.equals(otro.getIdCliente());
        return mismoCliente && uno.getCantidad() == otro.getCantidad();
    }
}
