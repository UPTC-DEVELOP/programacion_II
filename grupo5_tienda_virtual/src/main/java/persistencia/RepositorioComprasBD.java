package persistencia;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Compra;
import model.ItemCompra;
import model.Libro;

public class RepositorioComprasBD implements IRepositorio<Compra> {

    private final String url;
    private final List<Compra> compras;

    public RepositorioComprasBD() {
        this(ConfiguracionBD.RUTA_BD);
    }

    public RepositorioComprasBD(String ruta) {
        this.url = ConfiguracionBD.JDBC_PREFIJO + ruta;
        this.compras = new ArrayList<Compra>();
        crearCarpeta(ruta);
        crearTablas();
        cargar();
    }

    private void crearCarpeta(String ruta) {
        Path padre = Path.of(ruta).toAbsolutePath().getParent();
        if (padre != null) {
            try {
                Files.createDirectories(padre);
            } catch (IOException e) {
                System.err.println("No se pudo crear la carpeta de la BD: " + e.getMessage());
            }
        }
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void crearTablas() {
        String cabecera = "CREATE TABLE IF NOT EXISTS compras ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "fecha TEXT,"
                + "metodo_pago TEXT,"
                + "correo_cliente TEXT,"
                + "total REAL)";
        String detalle = "CREATE TABLE IF NOT EXISTS detalle_compra ("
                + "compra_id INTEGER,"
                + "isbn TEXT,"
                + "cantidad INTEGER,"
                + "precio_unitario REAL)";
        try (Connection c = conectar(); Statement st = c.createStatement()) {
            st.execute(cabecera);
            st.execute(detalle);
        } catch (SQLException e) {
            System.err.println("No se pudieron crear las tablas de compras: " + e.getMessage());
        }
    }

    @Override
    public List<Compra> listar() { return compras; }

    @Override
    public void agregar(Compra compra) { compras.add(compra); }

    @Override
    public boolean eliminar(Compra compra) { return compras.remove(compra); }

    @Override
    public void cargar() {
        compras.clear();
        Map<Integer, Compra> porId = new HashMap<Integer, Compra>();

        try (Connection c = conectar(); Statement st = c.createStatement()) {

            try (ResultSet rs = st.executeQuery(
                    "SELECT id, fecha, metodo_pago, correo_cliente, total FROM compras ORDER BY id")) {
                while (rs.next()) {
                    Compra compra = new Compra(rs.getString("metodo_pago"));
                    String fecha = rs.getString("fecha");
                    if (fecha != null) {
                        compra.setFecha(LocalDate.parse(fecha));
                    }
                    compra.setCorreoCliente(rs.getString("correo_cliente"));
                    compra.setTotal(rs.getDouble("total"));
                    compras.add(compra);
                    porId.put(rs.getInt("id"), compra);
                }
            }

            try (ResultSet rs = st.executeQuery(
                    "SELECT compra_id, isbn, cantidad, precio_unitario FROM detalle_compra")) {
                while (rs.next()) {
                    Compra compra = porId.get(rs.getInt("compra_id"));
                    if (compra == null) {
                        continue;
                    }
                    Libro libro = new Libro();
                    libro.setIsbn(rs.getString("isbn"));
                    libro.setPrecio(rs.getDouble("precio_unitario"));
                    compra.agregarItem(new ItemCompra(libro, rs.getInt("cantidad")));
                }
            }
        } catch (SQLException e) {
            System.err.println("No se pudieron leer las compras de la BD: " + e.getMessage());
        }
    }

    @Override
    public void guardar() {
        String insCab = "INSERT INTO compras (fecha, metodo_pago, correo_cliente, total) VALUES (?, ?, ?, ?)";
        String insDet = "INSERT INTO detalle_compra (compra_id, isbn, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try (Connection c = conectar()) {
            c.setAutoCommit(false);

            try (Statement st = c.createStatement()) {
                st.executeUpdate("DELETE FROM detalle_compra");
                st.executeUpdate("DELETE FROM compras");
                st.executeUpdate("DELETE FROM sqlite_sequence WHERE name = 'compras'");
            }

            try (PreparedStatement psCab = c.prepareStatement(insCab, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psDet = c.prepareStatement(insDet)) {

                for (Compra compra : compras) {
                    psCab.setString(1, compra.getFecha() == null ? null : compra.getFecha().toString());
                    psCab.setString(2, compra.getMetodoPago());
                    psCab.setString(3, compra.getCorreoCliente());
                    psCab.setDouble(4, compra.getTotal());
                    psCab.executeUpdate();

                    int compraId;
                    try (ResultSet claves = psCab.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se obtuvo el id de la compra");
                        }
                        compraId = claves.getInt(1);
                    }

                    for (ItemCompra item : compra.getItems()) {
                        psDet.setInt(1, compraId);
                        psDet.setString(2, item.getLibro().getIsbn());
                        psDet.setInt(3, item.getCantidad());
                        psDet.setDouble(4, item.getLibro().getPrecio());
                        psDet.addBatch();
                    }
                }
                psDet.executeBatch();
            }
            c.commit();
        } catch (SQLException e) {
            System.err.println("No se pudieron guardar las compras en la BD: " + e.getMessage());
        }
    }
}