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
import java.util.ArrayList;
import java.util.List;

import model.Cliente;
import model.ClientePremium;
import model.ClienteRegular;

/**
 * Persistencia de clientes en una base de datos SQLite (driver importado con Maven).
 *
 * Implementa el mismo contrato IRepositorio&lt;Cliente&gt; que la version JSON, asi que
 * el controlador y las ventanas funcionan igual sin cambiar una linea: solo cambia
 * la implementacion que se inyecta (SOLID: Liskov + Inversion de dependencias).
 */
public class RepositorioClientesBD implements IRepositorio<Cliente> {

    private static final String RUTA_POR_DEFECTO = ConfiguracionBD.RUTA_BD;

    private final String url;
    private final List<Cliente> clientes;

    public RepositorioClientesBD() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioClientesBD(String ruta) {
        this.url = ConfiguracionBD.JDBC_PREFIJO + ruta;
        this.clientes = new ArrayList<Cliente>();
        crearCarpeta(ruta);
        crearTabla();
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

    private void crearTabla() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes ("
                + "correo TEXT PRIMARY KEY,"
                + "nombre TEXT,"
                + "direccion TEXT,"
                + "telefono TEXT,"
                + "contrasena TEXT,"
                + "tipo TEXT)";
        try (Connection conexion = conectar(); Statement st = conexion.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.err.println("No se pudo crear la tabla clientes: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> listar() {
        return clientes;
    }

    @Override
    public void agregar(Cliente cliente) {
        clientes.add(cliente);
    }

    @Override
    public boolean eliminar(Cliente cliente) {
        return clientes.remove(cliente);
    }

    @Override
    public void cargar() {
        clientes.clear();
        String sql = "SELECT correo, nombre, direccion, telefono, contrasena, tipo FROM clientes";
        try (Connection conexion = conectar();
                Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                String tipo = rs.getString("tipo");
                Cliente cliente = "premium".equalsIgnoreCase(tipo)
                        ? new ClientePremium()
                        : new ClienteRegular();
                cliente.setCorreo(rs.getString("correo"));
                cliente.setNombreCompleto(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setContrasena(rs.getString("contrasena"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("No se pudieron leer los clientes de la BD: " + e.getMessage());
        }
    }

    @Override
    public void guardar() {
        String insert = "INSERT INTO clientes (correo, nombre, direccion, telefono, contrasena, tipo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conectar()) {
            conexion.setAutoCommit(false);

            try (Statement st = conexion.createStatement()) {
                st.executeUpdate("DELETE FROM clientes");
            }

            try (PreparedStatement ps = conexion.prepareStatement(insert)) {
                for (Cliente cliente : clientes) {
                    ps.setString(1, cliente.getCorreo());
                    ps.setString(2, cliente.getNombreCompleto());
                    ps.setString(3, cliente.getDireccion());
                    ps.setString(4, cliente.getTelefono());
                    ps.setString(5, cliente.getContrasena());
                    ps.setString(6, cliente instanceof ClientePremium ? "premium" : "regular");
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conexion.commit();
        } catch (SQLException e) {
            System.err.println("No se pudieron guardar los clientes en la BD: " + e.getMessage());
        }
    }
}
