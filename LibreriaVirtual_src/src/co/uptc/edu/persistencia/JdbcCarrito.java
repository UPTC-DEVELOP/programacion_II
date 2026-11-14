package co.uptc.edu.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.uptc.edu.interfaces.IGestionCarrito;
import co.uptc.edu.model.ItemCarrito;

//Carrito de cada usuario en la tabla "items_carrito" de MariaDB
public class JdbcCarrito implements IGestionCarrito {

    private static final String ID_USUARIO = "(SELECT id FROM usuarios WHERE correo = ?)";

    @Override
    public void guardar(String correo, List<ItemCarrito> items) {
        String sqlBorrar = "DELETE FROM items_carrito WHERE usuario_id = " + ID_USUARIO;
        String sqlInsertar = "INSERT INTO items_carrito (usuario_id, isbn, cantidad) VALUES (" + ID_USUARIO + ", ?, ?)";
        try (Connection conexion = ConexionBD.getConnection()) {
            conexion.setAutoCommit(false);
            try (PreparedStatement borrar = conexion.prepareStatement(sqlBorrar);
                 PreparedStatement insertar = conexion.prepareStatement(sqlInsertar)) {
                borrar.setString(1, correo);
                borrar.executeUpdate();
                for (ItemCarrito item : items) {
                    insertar.setString(1, correo);
                    insertar.setString(2, item.getLibro().getIsbn());
                    insertar.setInt(3, item.getCantidad());
                    insertar.executeUpdate();
                }
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw error("guardar el carrito", e);
        }
    }

    @Override
    public Map<String, Integer> cargar(String correo) {
        String sql = "SELECT isbn, cantidad FROM items_carrito WHERE usuario_id = " + ID_USUARIO + " ORDER BY id";
        Map<String, Integer> carrito = new LinkedHashMap<>();
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo);
            try (ResultSet r = sentencia.executeQuery()) {
                while (r.next()) {
                    carrito.put(r.getString("isbn"), r.getInt("cantidad"));
                }
            }
        } catch (SQLException e) {
            throw error("consultar el carrito", e);
        }
        return carrito;
    }

    private static IllegalStateException error(String accion, SQLException causa) {
        System.err.println("Error de base de datos: " + causa.getMessage());
        return new IllegalStateException("No se pudo " + accion + " en la base de datos.", causa);
    }
}
