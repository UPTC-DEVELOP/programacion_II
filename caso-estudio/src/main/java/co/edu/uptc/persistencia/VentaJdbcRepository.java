package co.edu.uptc.persistencia;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.ICompraRepository;
import co.edu.uptc.interfaces.IVentaRepository;
import co.edu.uptc.modelo.Venta;
import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
import co.edu.uptc.modelo.dto.CompraDtos.ItemCompraDto;
import co.edu.uptc.modelo.dto.CompraDtos.LineaFacturaDto;
import co.edu.uptc.modelo.dto.CompraDtos.SolicitudCompraDto;
import co.edu.uptc.modelo.enums.EstadoVenta;
import co.edu.uptc.persistencia.conexion.ConexionDB;
/**/
/**
 * IMPLEMENTA IVentaRepository Y GESTIONA LA PERSISTENCIA DE VENTAS EN POSTGRESQL
 *  @author: Equipo de desarrollo grupo 7 UPTC
 *  @modificacion:Brayan Panqueva
 */
public class VentaJdbcRepository implements IVentaRepository, ICompraRepository {
    
    /*modificacion:Brayan Panqueva
    */
    private static final String CONSULTA_VENTAS =
            "SELECT b.id_venta, b.id_cliente, c.nombre_completo, b.fecha_compra, b.total_pagar, " +
            "COALESCE(array_agg(sd.isbn_libro) FILTER(WHERE sd.isbn_libro IS NOT NULL), '{}') isbns " +
            "FROM buys b LEFT JOIN clients c ON c.id_cliente=b.id_cliente " +
            "LEFT JOIN sale_detais sd ON sd.id_venta=b.id_venta " +
            "GROUP BY b.id_venta, c.nombre_completo ORDER BY b.fecha_compra DESC";

    private static final String CONSULTA_FACTURA =
            "SELECT v.id_venta, v.fecha_compra, c.nombre_completo, c.correo_electronico, c.tipo_cliente, " +
            "v.subtotal, v.total_impuestos, v.descuento, v.total_pagar, d.isbn_libro, b.titulo, " +
            "d.cantidad, d.precio_unitario, d.subtotal AS detalle_subtotal, b.porcentaje_iva " +
            "FROM buys v JOIN clients c ON c.id_cliente=v.id_cliente " +
            "JOIN sale_detais d ON v.id_venta=d.id_venta JOIN books b ON b.isbn=d.isbn_libro " +
            "WHERE v.id_venta=? ORDER BY d.id_detalle";

    @Override public List<Venta> listarTodas() { return consultar(CONSULTA_VENTAS); }
    
    @Override public List<Venta> listarRecientes(int cantidad) {
        return cantidad <= 0 ? List.of() : consultar(CONSULTA_VENTAS + " LIMIT " + cantidad);
    }
    
    @Override public int contarVentas() {
        return entero("SELECT COUNT(*) FROM buys", "contar ventas");
    }
    
    @Override public double sumarIngresos() {
        try (Connection c=ConexionDB.obtenerConexion(); PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(total_pagar),0) FROM buys"); ResultSet r=p.executeQuery()) {
            r.next(); return r.getDouble(1);
        } catch (SQLException e) { throw error(e, "sumar ingresos"); }
    }

    @Override
    public int finalizarCompra(SolicitudCompraDto solicitud) {
        if (solicitud == null || solicitud.items() == null || solicitud.items().isEmpty()) {
            throw new PersistenciaException("No hay líneas de compra para persistir.");
        }
        Connection c = null;
        try {
            c = ConexionDB.obtenerConexion();
            c.setAutoCommit(false);
            int idVenta;
            
            // CORREGIDO: Se incluyen explícitamente los valores de la solicitud garantizando 
            // que el descuento y subtotales viajen correctamente a la tabla buys.
            String queryCompra = "INSERT INTO buys(id_cliente, subtotal, total_impuestos, descuento, total_pagar, metodo_pago) " +
                                 "VALUES(?, ?, ?, ?, ?, ?) RETURNING id_venta";
            
            try (PreparedStatement p = c.prepareStatement(queryCompra)) {
                p.setInt(1, solicitud.idCliente());
                p.setDouble(2, solicitud.subtotal());
                p.setDouble(3, solicitud.impuestos());
                p.setDouble(4, solicitud.descuento()); 
                p.setDouble(5, solicitud.total());
                p.setString(6, solicitud.metodoPago());
                
                try (ResultSet r = p.executeQuery()) { 
                    if (r.next()) {
                        idVenta = r.getInt(1); 
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la venta generada.");
                    }
                }
            }
            
            /*modificacion:Brayan Panqueva*/
            String queryDetalles = "INSERT INTO sale_detais(id_venta, isbn_libro, cantidad, precio_unitario, subtotal) VALUES(?, ?, ?, ?, ?)";
            try (PreparedStatement p = c.prepareStatement(queryDetalles)) {
                for (ItemCompraDto item : solicitud.items()) {
                    p.setInt(1, idVenta); 
                    p.setString(2, item.isbn()); 
                    p.setInt(3, item.cantidad());
                    p.setDouble(4, item.precioUnitario()); 
                    p.setDouble(5, item.subtotal()); 
                    p.addBatch();
                }
                p.executeBatch();
            }
            
            // Limpieza del carrito del cliente tras finalizar la compra
            try (PreparedStatement p = c.prepareStatement("DELETE FROM shopping_carts WHERE id_cliente=?")) {
                p.setInt(1, solicitud.idCliente()); 
                p.executeUpdate();
            }
            
            c.commit();
            return idVenta;
        } catch (SQLException e) {
            if (c != null) {
                try { 
                    c.rollback(); 
                } catch (SQLException ignored) { }
            }
            throw error(e, "finalizar la compra; la transacción fue revertida");
        } finally {
            ConexionDB.cerrarConexion(c);
        }
    }

    @Override
    public FacturaDto consultarFactura(int idVenta) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(CONSULTA_FACTURA)) {
            p.setInt(1, idVenta);
            try (ResultSet r = p.executeQuery()) {
                FacturaDto factura = null; 
                List<LineaFacturaDto> lineas = new ArrayList<>();
                
                while (r.next()) {
                    if (factura == null) {
                        Timestamp fecha = r.getTimestamp("fecha_compra");
                        factura = new FacturaDto(
                            r.getInt("id_venta"), 
                            fecha == null ? "" : fecha.toLocalDateTime().toString(),
                            r.getString("nombre_completo"), 
                            r.getString("correo_electronico"),
                            r.getString("tipo_cliente"), 
                            r.getDouble("subtotal"), 
                            r.getDouble("total_impuestos"),
                            r.getDouble("descuento"), 
                            r.getDouble("total_pagar"), 
                            lineas
                        );
                    }
                    lineas.add(new LineaFacturaDto(
                        r.getString("isbn_libro"), 
                        r.getString("titulo"),
                        r.getInt("cantidad"), 
                        r.getDouble("precio_unitario"),
                        r.getDouble("detalle_subtotal"), 
                        r.getInt("porcentaje_iva")
                    ));
                }
                return factura;
            }
        } catch (SQLException e) { 
            throw error(e, "consultar la factura"); 
        }
    }

    private List<Venta> consultar(String sql) {
        List<Venta> out = new ArrayList<>();
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                Array array = r.getArray("isbns"); 
                List<String> isbns = new ArrayList<>();
                if (array != null) {
                    for (Object isbn : (Object[]) array.getArray()) {
                        isbns.add(String.valueOf(isbn));
                    }
                }
                Timestamp fecha = r.getTimestamp("fecha_compra");
                out.add(new Venta(
                    r.getInt("id_venta"), 
                    r.getInt("id_cliente"), 
                    r.getString("nombre_completo"),
                    fecha == null ? null : fecha.toLocalDateTime().toLocalDate(), 
                    isbns,
                    r.getDouble("total_pagar"), 
                    EstadoVenta.COMPLETADA
                ));
            }
            return out;
        } catch (SQLException e) { 
            throw error(e, "consultar ventas"); 
        }
    }

    private int entero(String sql, String operacion) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(sql); ResultSet r = p.executeQuery()) {
            r.next(); 
            return r.getInt(1);
        } catch (SQLException e) { 
            throw error(e, operacion); 
        }
    }
    
    private PersistenciaException error(SQLException e, String operacion) {
        return new PersistenciaException("No se pudo " + operacion + " en PostgreSQL: " + e.getMessage(), e);
    }
}
