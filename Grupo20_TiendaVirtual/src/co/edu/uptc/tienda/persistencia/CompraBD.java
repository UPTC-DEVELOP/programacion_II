package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCompra;
import co.edu.uptc.tienda.modelo.Compra;
import co.edu.uptc.tienda.modelo.DetalleCompra;

public class CompraBD implements IPersistenciaCompra {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void guardar(Compra compra) throws CompraExcepcion {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                if (!existe(conexion, compra.getNumero())) {
                    insertarCompra(conexion, compra);
                    for (DetalleCompra detalle : compra.getDetalles()) {
                        insertarDetalle(conexion, compra.getNumero(), detalle);
                    }
                }
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new CompraExcepcion("No se pudo guardar la compra en la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    @Override
    public List<Compra> leer() throws CompraExcepcion {
        try (Connection conexion = ConexionBD.obtenerConexion()) {
            Map<Integer, List<DetalleCompra>> detalles = consultarDetalles(conexion);
            List<Compra> compras = new ArrayList<>();
            String sql = "SELECT numero, id_cliente, nombre_cliente, fecha, metodo_pago, porcentaje_descuento "
                    + "FROM compra ORDER BY numero";
            try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                    ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    int numero = resultado.getInt("numero");
                    compras.add(new Compra(numero, resultado.getString("id_cliente"),
                            resultado.getString("nombre_cliente"),
                            resultado.getTimestamp("fecha").toLocalDateTime().format(FORMATO),
                            resultado.getString("metodo_pago"), resultado.getDouble("porcentaje_descuento"),
                            detalles.getOrDefault(numero, new ArrayList<>())));
                }
            }
            return compras;
        } catch (SQLException e) {
            throw new CompraExcepcion("No se pudieron leer las compras de la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    private boolean existe(Connection conexion, int numero) throws SQLException {
        String sql = "SELECT 1 FROM compra WHERE numero = ?";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, numero);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private void insertarCompra(Connection conexion, Compra compra) throws SQLException {
        String sql = "INSERT INTO compra (numero, id_cliente, nombre_cliente, fecha, metodo_pago, subtotal, "
                + "impuestos, porcentaje_descuento, descuento, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, compra.getNumero());
            sentencia.setString(2, compra.getIdCliente());
            sentencia.setString(3, compra.getNombreCliente());
            sentencia.setTimestamp(4, Timestamp.valueOf(compra.getFecha()));
            sentencia.setString(5, compra.getMetodoPago());
            sentencia.setDouble(6, compra.getSubtotal());
            sentencia.setDouble(7, compra.getImpuestos());
            sentencia.setDouble(8, compra.getPorcentajeDescuento());
            sentencia.setDouble(9, compra.getDescuento());
            sentencia.setDouble(10, compra.getTotal());
            sentencia.executeUpdate();
        }
    }

    private void insertarDetalle(Connection conexion, int numero, DetalleCompra detalle) throws SQLException {
        String sql = "INSERT INTO detalle_compra (numero_compra, isbn, titulo, cantidad, precio_unitario, "
                + "iva_porcentaje) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, numero);
            sentencia.setString(2, detalle.getIsbn());
            sentencia.setString(3, detalle.getTitulo());
            sentencia.setInt(4, detalle.getCantidad());
            sentencia.setDouble(5, detalle.getPrecioUnitario());
            sentencia.setDouble(6, detalle.getIvaPorcentaje());
            sentencia.executeUpdate();
        }
    }

    private Map<Integer, List<DetalleCompra>> consultarDetalles(Connection conexion) throws SQLException {
        Map<Integer, List<DetalleCompra>> detalles = new LinkedHashMap<>();
        String sql = "SELECT numero_compra, isbn, titulo, cantidad, precio_unitario, iva_porcentaje "
                + "FROM detalle_compra ORDER BY numero_compra";
        try (PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                detalles.computeIfAbsent(resultado.getInt("numero_compra"), numero -> new ArrayList<>())
                        .add(new DetalleCompra(resultado.getString("isbn"), resultado.getString("titulo"),
                                resultado.getInt("cantidad"), resultado.getDouble("precio_unitario"),
                                resultado.getDouble("iva_porcentaje")));
            }
        }
        return detalles;
    }
}
