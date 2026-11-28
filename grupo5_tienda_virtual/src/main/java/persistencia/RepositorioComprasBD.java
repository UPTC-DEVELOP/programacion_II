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
        String sql = "SELECT id, fecha, metodo_pago, correo_cliente, total "
                + "FROM compras ORDER BY id";
        try (Connection conexion = conectar();
                Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            List<Compra> leidas = new ArrayList<Compra>();
            while (rs.next()) {
                leidas.add(mapearCompra(rs));
            }
            cargarDetalles(conexion, leidas);
            compras.addAll(leidas);
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer las compras de la BD", e);
        }
    }

    @Override
    public List<Compra> listarUltimos(int limite) {
        if (limite <= 0) {
            return new ArrayList<Compra>();
        }
        String sql = "SELECT id, fecha, metodo_pago, correo_cliente, total "
                + "FROM compras ORDER BY id DESC LIMIT ?";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                List<Compra> ultimas = new ArrayList<Compra>();
                while (rs.next()) {
                    ultimas.add(mapearCompra(rs));
                }
                cargarDetalles(conexion, ultimas);
                return ultimas;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer las ultimas compras de la BD", e);
        }
    }

    @Override
    public List<Compra> listarPagina(int inicio, int cantidad) {
        if (inicio < 0 || cantidad <= 0) {
            return new ArrayList<Compra>();
        }
        String sql = "SELECT id, fecha, metodo_pago, correo_cliente, total "
                + "FROM compras ORDER BY id ASC LIMIT ? OFFSET ?";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, inicio);
            try (ResultSet rs = ps.executeQuery()) {
                List<Compra> pagina = new ArrayList<Compra>();
                while (rs.next()) {
                    pagina.add(mapearCompra(rs));
                }
                cargarDetalles(conexion, pagina);
                return pagina;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo leer la pagina de compras de la BD", e);
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM compras";
        try (Connection conexion = conectar();
                Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo contar las compras de la BD", e);
        }
    }

    @Override
    public List<Compra> buscar(String criterio) {
        List<Compra> resultado = new ArrayList<Compra>();
        if (criterio == null || criterio.isBlank()) {
            return resultado;
        }
        String sql = "SELECT id, fecha, metodo_pago, correo_cliente, total "
                + "FROM compras WHERE LOWER(correo_cliente) = LOWER(?) "
                + "ORDER BY id DESC LIMIT 10";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, criterio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearCompra(rs));
                }
            }
            cargarDetalles(conexion, resultado);
            return resultado;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron buscar las compras en la BD", e);
        }
    }

    private Compra mapearCompra(ResultSet rs) throws SQLException {
        Compra compra = new Compra();
        compra.setId(rs.getLong("id"));
        String fecha = rs.getString("fecha");
        if (fecha != null && !fecha.isBlank()) {
            compra.setFecha(LocalDate.parse(fecha));
        }
        compra.setMetodoPago(rs.getString("metodo_pago"));
        compra.setCorreoCliente(rs.getString("correo_cliente"));
        compra.setTotal(rs.getDouble("total"));
        return compra;
    }

    private void cargarDetalles(Connection conexion, List<Compra> comprasLeidas)
            throws SQLException {
        String sql = "SELECT isbn, titulo, cantidad, precio_unitario, porcentaje_iva "
                + "FROM detalle_compra WHERE compra_id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (Compra compra : comprasLeidas) {
                ps.setLong(1, compra.getId());
                try (ResultSet detalle = ps.executeQuery()) {
                    while (detalle.next()) {
                        Libro libro = new Libro();
                        libro.setIsbn(detalle.getString("isbn"));
                        libro.setTitulo(detalle.getString("titulo"));
                        libro.setPrecio(detalle.getDouble("precio_unitario"));
                        libro.setPorcentajeIva(detalle.getDouble("porcentaje_iva"));
                        libro.setCantidadDisponible(0);
                        compra.agregarItem(new ItemCompra(libro, detalle.getInt("cantidad")));
                    }
                }
            }
        }
    }

    @Override
    public void insertar(Compra compra) {
        if (compra == null) {
            return;
        }
        String sqlCompra = "INSERT INTO compras "
                + "(id, fecha, metodo_pago, correo_cliente, total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = conectar()) {
            conexion.setAutoCommit(false);
            try {
                if (compra.getId() == 0) {
                    compra.setId(siguienteId(conexion));
                }
                try (PreparedStatement ps = conexion.prepareStatement(sqlCompra)) {
                    ps.setLong(1, compra.getId());
                    ps.setString(2, compra.getFecha() != null ? compra.getFecha().toString() : null);
                    ps.setString(3, compra.getMetodoPago());
                    ps.setString(4, compra.getCorreoCliente());
                    ps.setDouble(5, compra.getTotal());
                    ps.executeUpdate();
                }
                insertarDetalles(conexion, compra);
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo insertar la compra en la BD", e);
        }
    }

    @Override
    public void actualizar(Compra compra) {
        if (compra == null) {
            return;
        }
        String sqlCompra = "UPDATE compras SET fecha = ?, metodo_pago = ?, "
                + "correo_cliente = ?, total = ? WHERE id = ?";
        try (Connection conexion = conectar()) {
            conexion.setAutoCommit(false);
            try {
                int filas;
                try (PreparedStatement ps = conexion.prepareStatement(sqlCompra)) {
                    ps.setString(1, compra.getFecha() != null ? compra.getFecha().toString() : null);
                    ps.setString(2, compra.getMetodoPago());
                    ps.setString(3, compra.getCorreoCliente());
                    ps.setDouble(4, compra.getTotal());
                    ps.setLong(5, compra.getId());
                    filas = ps.executeUpdate();
                }
                if (filas == 0) {
                    conexion.rollback();
                    return;
                }
                borrarDetalles(conexion, compra.getId());
                insertarDetalles(conexion, compra);
                conexion.commit();
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo actualizar la compra en la BD", e);
        }
    }

    @Override
    public boolean eliminarPersistente(Compra compra) {
        if (compra == null) {
            return false;
        }
        String sqlCompra = "DELETE FROM compras WHERE id = ?";
        try (Connection conexion = conectar()) {
            conexion.setAutoCommit(false);
            try {
                borrarDetalles(conexion, compra.getId());
                int filas;
                try (PreparedStatement ps = conexion.prepareStatement(sqlCompra)) {
                    ps.setLong(1, compra.getId());
                    filas = ps.executeUpdate();
                }
                conexion.commit();
                return filas > 0;
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar la compra de la BD", e);
        }
    }

    private long siguienteId(Connection conexion) throws SQLException {
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM compras";
        try (Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 1;
        }
    }

    private void borrarDetalles(Connection conexion, long idCompra) throws SQLException {
        String sql = "DELETE FROM detalle_compra WHERE compra_id = ?";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setLong(1, idCompra);
            ps.executeUpdate();
        }
    }

    private void insertarDetalles(Connection conexion, Compra compra) throws SQLException {
        String sql = "INSERT INTO detalle_compra "
                + "(compra_id, isbn, titulo, cantidad, precio_unitario, porcentaje_iva) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        if (compra.getItems() == null) {
            return;
        }
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (ItemCompra item : compra.getItems()) {
                if (item == null || item.getLibro() == null) {
                    continue;
                }
                Libro libro = item.getLibro();
                ps.setLong(1, compra.getId());
                ps.setString(2, libro.getIsbn());
                ps.setString(3, libro.getTitulo());
                ps.setInt(4, item.getCantidad());
                ps.setDouble(5, libro.getPrecio());
                ps.setDouble(6, libro.getPorcentajeIva());
                ps.addBatch();
            }
            ps.executeBatch();
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
