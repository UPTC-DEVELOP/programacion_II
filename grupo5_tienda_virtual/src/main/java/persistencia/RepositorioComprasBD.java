package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Compra;
import model.ItemCompra;
import model.Libro;

public class RepositorioComprasBD implements IRepositorio<Compra> {

    private static final String RUTA_POR_DEFECTO = ConfiguracionBD.RUTA_BD;

    private final String url;
    private final List<Compra> compras;

    public RepositorioComprasBD() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioComprasBD(String ruta) {
        this.url = ConfiguracionBD.JDBC_PREFIJO + ruta;
        this.compras = new ArrayList<Compra>();
        ConfiguracionBD.crearCarpeta(ruta);
        crearTablas();
        cargar();
    }

    @Override
    public List<Compra> listar() {
        return compras;
    }

    @Override
    public void agregar(Compra compra) {
        if (compra.getId() == 0) {
            compra.setId(siguienteId());
        }
        compras.add(compra);
    }

    @Override
    public boolean eliminar(Compra compra) {
        return compras.remove(compra);
    }

    @Override
    public void cargar() {
        compras.clear();

        String sqlCompras = "SELECT id, fecha, metodo_pago, correo_cliente, total "
                + "FROM compras ORDER BY id";
        String sqlDetalle = "SELECT isbn, titulo, cantidad, precio_unitario, porcentaje_iva "
                + "FROM detalle_compra WHERE compra_id = ?";

        try (Connection conexion = conectar()) {

            try (Statement st = conexion.createStatement();
                    ResultSet rs = st.executeQuery(sqlCompras)) {
                while (rs.next()) {
                    Compra compra = new Compra();
                    compra.setId(rs.getLong("id"));
                    String fecha = rs.getString("fecha");
                    if (fecha != null && !fecha.isBlank()) {
                        compra.setFecha(LocalDate.parse(fecha));
                    }
                    compra.setMetodoPago(rs.getString("metodo_pago"));
                    compra.setCorreoCliente(rs.getString("correo_cliente"));
                    compra.setTotal(rs.getDouble("total"));
                    compras.add(compra);
                }
            }

            try (PreparedStatement ps = conexion.prepareStatement(sqlDetalle)) {
                for (Compra compra : compras) {
                    ps.setLong(1, compra.getId());
                    try (ResultSet detalle = ps.executeQuery()) {
                        while (detalle.next()) {
                            Libro libro = new Libro();
                            libro.setIsbn(detalle.getString("isbn"));
                            libro.setTitulo(detalle.getString("titulo"));
                            libro.setPrecio(detalle.getDouble("precio_unitario"));
                            libro.setPorcentajeIva(detalle.getDouble("porcentaje_iva"));
                            libro.setCantidadDisponible(0);

                            ItemCompra item = new ItemCompra(
                                    libro, detalle.getInt("cantidad"));
                            compra.agregarItem(item);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer las compras de la BD", e);
        }
    }

    @Override
    public void guardar() {
        String borrarDetalle = "DELETE FROM detalle_compra";
        String borrarCompras = "DELETE FROM compras";
        String insertarCompra = "INSERT INTO compras "
                + "(id, fecha, metodo_pago, correo_cliente, total) VALUES (?, ?, ?, ?, ?)";
        String insertarDetalle = "INSERT INTO detalle_compra "
                + "(compra_id, isbn, titulo, cantidad, precio_unitario, porcentaje_iva) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = conectar()) {
            conexion.setAutoCommit(false);

            try {
                try (Statement st = conexion.createStatement()) {
                    st.executeUpdate(borrarDetalle);
                    st.executeUpdate(borrarCompras);
                }

                try (PreparedStatement ps = conexion.prepareStatement(insertarCompra)) {
                    for (Compra compra : compras) {
                        ps.setLong(1, compra.getId());
                        ps.setString(2, compra.getFecha() != null
                                ? compra.getFecha().toString() : null);
                        ps.setString(3, compra.getMetodoPago());
                        ps.setString(4, compra.getCorreoCliente());
                        ps.setDouble(5, compra.getTotal());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                try (PreparedStatement ps = conexion.prepareStatement(insertarDetalle)) {
                    for (Compra compra : compras) {
                        if (compra.getItems() == null) {
                            continue;
                        }
                        for (ItemCompra item : compra.getItems()) {
                            Libro libro = item.getLibro();
                            if (libro == null) {
                                continue;
                            }
                            ps.setLong(1, compra.getId());
                            ps.setString(2, libro.getIsbn());
                            ps.setString(3, libro.getTitulo());
                            ps.setInt(4, item.getCantidad());
                            ps.setDouble(5, libro.getPrecio());
                            ps.setDouble(6, libro.getPorcentajeIva());
                            ps.addBatch();
                        }
                    }
                    ps.executeBatch();
                }

                conexion.commit();
            } catch (SQLException e) {
                try {
                    conexion.rollback();
                } catch (SQLException ignored) {
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron guardar las compras en la BD", e);
        }
    }

    private void crearTablas() {
        String sqlCompras = "CREATE TABLE IF NOT EXISTS compras ("
                + "id INTEGER PRIMARY KEY,"
                + "fecha TEXT,"
                + "metodo_pago TEXT,"
                + "correo_cliente TEXT,"
                + "total REAL)";

        String sqlDetalle = "CREATE TABLE IF NOT EXISTS detalle_compra ("
                + "compra_id INTEGER,"
                + "isbn TEXT,"
                + "titulo TEXT,"
                + "cantidad INTEGER,"
                + "precio_unitario REAL,"
                + "porcentaje_iva REAL)";

        try (Connection conexion = conectar();
                Statement st = conexion.createStatement()) {
            st.execute(sqlCompras);
            st.execute(sqlDetalle);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudieron crear las tablas de compras", e);
        }
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private long siguienteId() {
        long maximo = 0;
        for (Compra compra : compras) {
            maximo = Math.max(maximo, compra.getId());
        }
        return maximo + 1;
    }
}
