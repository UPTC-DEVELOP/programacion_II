package co.edu.uptc.persistence.mariadb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import co.edu.uptc.exception.PersistenciaException;
import co.edu.uptc.model.Carrito;
import co.edu.uptc.model.ItemCarrito;
import co.edu.uptc.persistence.CarritoRepository;
import co.edu.uptc.persistence.Conexion;

/** Persistencia MariaDB del carrito y sus líneas. */
public class CarritoMariaDbRepository implements CarritoRepository {

    @Override
    public Optional<Carrito> buscarPorCliente(String correo) {
        String sql = "SELECT c.id, i.isbn, i.titulo, i.precio_base, i.descuento, "
                + "i.impuesto_iva, i.cantidad "
                + "FROM carrito c LEFT JOIN item_carrito i ON i.carrito_id = c.id "
                + "WHERE c.correo_cliente = ? ORDER BY i.isbn";

        try (Connection conexion = obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                Carrito carrito = null;
                while (resultado.next()) {
                    if (carrito == null) {
                        carrito = new Carrito(correo);
                    }
                    String isbn = resultado.getString("isbn");
                    if (isbn != null) {
                        carrito.agregar(new ItemCarrito(
                                isbn,
                                resultado.getString("titulo"),
                                resultado.getDouble("precio_base"),
                                resultado.getDouble("descuento"),
                                resultado.getDouble("impuesto_iva"),
                                resultado.getInt("cantidad")));
                    }
                }
                return Optional.ofNullable(carrito);
            }
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo cargar el carrito de " + correo, e);
        }
    }

    @Override
    public void guardar(Carrito carrito) {
        String buscarCarrito = "SELECT id FROM carrito WHERE correo_cliente = ? "
                + "ORDER BY id LIMIT 1 FOR UPDATE";
        String insertarCarrito = "INSERT INTO carrito "
                + "(correo_cliente, fecha_actualizacion) VALUES (?, CURRENT_TIMESTAMP)";
        String actualizarFecha = "UPDATE carrito SET fecha_actualizacion = CURRENT_TIMESTAMP "
                + "WHERE id = ?";
        String borrarItems = "DELETE FROM item_carrito WHERE carrito_id = ?";
        String insertarItem = "INSERT INTO item_carrito "
                + "(carrito_id, isbn, titulo, precio_base, descuento, impuesto_iva, cantidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = obtenerConexion()) {
            boolean autoCommitOriginal = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                long carritoId;
                try (PreparedStatement sentencia = conexion.prepareStatement(buscarCarrito)) {
                    sentencia.setString(1, carrito.getCorreoCliente());
                    try (ResultSet resultado = sentencia.executeQuery()) {
                        if (resultado.next()) {
                            carritoId = resultado.getLong("id");
                            try (PreparedStatement actualizar = conexion.prepareStatement(actualizarFecha)) {
                                actualizar.setLong(1, carritoId);
                                actualizar.executeUpdate();
                            }
                        } else {
                            try (PreparedStatement insertar = conexion.prepareStatement(
                                    insertarCarrito, Statement.RETURN_GENERATED_KEYS)) {
                                insertar.setString(1, carrito.getCorreoCliente());
                                insertar.executeUpdate();
                                try (ResultSet claves = insertar.getGeneratedKeys()) {
                                    if (!claves.next()) {
                                        throw new SQLException("No se pudo obtener el id del carrito");
                                    }
                                    carritoId = claves.getLong(1);
                                }
                            }
                        }
                    }
                }

                try (PreparedStatement sentencia = conexion.prepareStatement(borrarItems)) {
                    sentencia.setLong(1, carritoId);
                    sentencia.executeUpdate();
                }

                try (PreparedStatement sentencia = conexion.prepareStatement(insertarItem)) {
                    for (ItemCarrito item : carrito.getItems()) {
                        sentencia.setLong(1, carritoId);
                        sentencia.setString(2, item.getIsbn());
                        sentencia.setString(3, item.getTitulo());
                        sentencia.setDouble(4, item.getPrecioBase());
                        sentencia.setDouble(5, item.getPorcentajeDescuento());
                        sentencia.setDouble(6, item.getImpuestoIVA());
                        sentencia.setInt(7, item.getCantidad());
                        sentencia.addBatch();
                    }
                    sentencia.executeBatch();
                }
                conexion.commit();
                conexion.setAutoCommit(autoCommitOriginal);
            } catch (SQLException | RuntimeException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo guardar el carrito de " + carrito.getCorreoCliente(), e);
        }
    }

    @Override
    public void eliminar(String correo) {
        String buscarId = "SELECT id FROM carrito WHERE correo_cliente = ?";
        String borrarItems = "DELETE FROM item_carrito WHERE carrito_id = ?";
        String borrarCarrito = "DELETE FROM carrito WHERE id = ?";

        try (Connection conexion = obtenerConexion();
             PreparedStatement buscar = conexion.prepareStatement(buscarId)) {
            buscar.setString(1, correo);
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) {
                    return;
                }
                long id = resultado.getLong("id");
                try (PreparedStatement items = conexion.prepareStatement(borrarItems);
                     PreparedStatement carrito = conexion.prepareStatement(borrarCarrito)) {
                    items.setLong(1, id);
                    items.executeUpdate();
                    carrito.setLong(1, id);
                    carrito.executeUpdate();
                }
            }
        } catch (SQLException | RuntimeException e) {
            throw error("No se pudo eliminar el carrito de " + correo, e);
        }
    }

    private Connection obtenerConexion() throws SQLException {
        Connection conexion = Conexion.obtenerConexion();
        if (conexion == null) {
            throw new SQLException("Conexion.obtenerConexion() devolvió null");
        }
        return conexion;
    }

    private static PersistenciaException error(String mensaje, Exception causa) {
        return new PersistenciaException(mensaje, causa);
    }
}
