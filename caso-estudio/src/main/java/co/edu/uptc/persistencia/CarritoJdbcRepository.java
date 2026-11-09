package co.edu.uptc.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.ICarritoRepository;
import co.edu.uptc.modelo.CarritoCompras;
import co.edu.uptc.modelo.ItemCarrito;
import co.edu.uptc.modelo.enums.TipoCliente;
import co.edu.uptc.persistencia.conexion.ConexionDB;

/*implementa ICarritoRepository y gestiona la persistencia de carrito en postgreSQL
* @author: Equipo de desarrollo grupo 7 UPTC
*/

public class CarritoJdbcRepository implements ICarritoRepository {

    @Override
    public void guardarCarrito(CarritoCompras carrito) {
        if (carrito == null) {
            return;
        }

        try (Connection c = ConexionDB.obtenerConexion()) {
            c.setAutoCommit(false);
            int id = carrito.getIdCarrito();
            String q = "INSERT INTO shopping_carts(id_cliente) VALUES(?) ON CONFLICT(id_cliente) DO UPDATE SET fecha_creacion=shopping_carts.fecha_creacion RETURNING id_carrito";
            
            try (PreparedStatement p = c.prepareStatement(q)) {
                p.setInt(1, carrito.getIdCliente());
                try (ResultSet r = p.executeQuery()) {
                    if (r.next()) {
                        id = r.getInt(1);
                    }
                }
            }

            try (PreparedStatement d = c.prepareStatement("DELETE FROM cart_items WHERE id_carrito=?")) {
                d.setInt(1, id);
                d.executeUpdate();
            }

            try (PreparedStatement p = c.prepareStatement("INSERT INTO cart_items(id_carrito,isbn_libro,cantidad,precio_unitario,subtotal) VALUES(?,?,?,?,?)")) {
                for (ItemCarrito i : carrito.obtenerItems()) {
                    if (i.getCantidad() <= 0) {
                        continue;
                    }
                    p.setInt(1, id);
                    p.setString(2, i.getIsbn());
                    p.setInt(3, i.getCantidad());
                    p.setDouble(4, i.getPrecioUnitario());
                    p.setDouble(5, i.getSubtotal());
                    p.addBatch();
                }
                p.executeBatch();
            }

            c.commit();
            carrito.setIdCarrito(id);

        } catch (SQLException e) {
            // Nota: El objeto 'c' se maneja dentro del try-with-resources, 
            // pero para hacer el rollback seguro si falla, lo ideal es capturar el error.
            throw error(e, "persistir el carrito");
        }
    }

    @Override
    public CarritoCompras obtenerCarritoPorCliente(int id) {
        String q = "SELECT sc.id_carrito,sc.id_cliente,c.tipo_cliente,ci.isbn_libro,ci.cantidad,ci.precio_unitario,b.titulo,b.porcentaje_iva FROM shopping_carts sc JOIN clients c ON c.id_cliente=sc.id_cliente LEFT JOIN cart_items ci ON ci.id_carrito=sc.id_carrito LEFT JOIN books b ON b.isbn=ci.isbn_libro WHERE sc.id_cliente=? ORDER BY ci.id_item";
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement(q)) {
            p.setInt(1, id);
            try (ResultSet r = p.executeQuery()) {
                CarritoCompras out = null;
                while (r.next()) {
                    if (out == null) {
                        out = new CarritoCompras(r.getInt("id_carrito"), r.getInt("id_cliente"));
                        out.setTipoCliente("Premium".equalsIgnoreCase(r.getString("tipo_cliente")) ? TipoCliente.PREMIUM : TipoCliente.REGULAR);
                    }
                    if (r.getString("isbn_libro") != null) {
                        double tasa = r.getInt("porcentaje_iva") / 100.0;
                        ItemCarrito item = new ItemCarrito(r.getString("isbn_libro"), r.getInt("cantidad"), r.getDouble("precio_unitario"), tasa);
                        item.setTitulo(r.getString("titulo"));
                        out.agregarItem(item);
                    }
                }
                return out;
            }
        } catch (SQLException e) {
            throw error(e, "consultar el carrito");
        }
    }

    @Override
    public void eliminarCarrito(int id) {
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("DELETE FROM shopping_carts WHERE id_cliente=?")) {
            p.setInt(1, id);
            p.executeUpdate();
        } catch (SQLException e) {
            throw error(e, "eliminar el carrito");
        }
    }

    @Override
    public List<CarritoCompras> listarTodos() {
        List<CarritoCompras> out = new ArrayList<>();
        try (Connection c = ConexionDB.obtenerConexion(); PreparedStatement p = c.prepareStatement("SELECT id_cliente FROM shopping_carts ORDER BY id_carrito"); ResultSet r = p.executeQuery()) {
            while (r.next()) {
                CarritoCompras x = obtenerCarritoPorCliente(r.getInt(1));
                if (x != null) {
                    out.add(x);
                }
            }
            return out;
        } catch (SQLException e) {
            throw error(e, "listar los carritos");
        }
    }

    private PersistenciaException error(SQLException e, String op) {
        return new PersistenciaException("No se pudo " + op + " en PostgreSQL: " + e.getMessage(), e);
    }
}