package persistencia;

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
        ConfiguracionBD.crearCarpeta(ruta);
        crearTabla();
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
            throw new PersistenciaException("No se pudo crear la tabla clientes", e);
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
    public void insertar(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        String sql = "INSERT OR REPLACE INTO clientes "
                + "(correo, nombre, direccion, telefono, contrasena, tipo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getCorreo());
            ps.setString(2, cliente.getNombreCompleto());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getContrasena());
            ps.setString(6, cliente instanceof ClientePremium ? "premium" : "regular");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo insertar el cliente en la BD", e);
        }
    }

    @Override
    public void actualizar(Cliente cliente) {
        if (cliente == null) {
            return;
        }
        String sql = "UPDATE clientes SET nombre = ?, direccion = ?, telefono = ?, "
                + "contrasena = ?, tipo = ? WHERE correo = ?";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombreCompleto());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getContrasena());
            ps.setString(5, cliente instanceof ClientePremium ? "premium" : "regular");
            ps.setString(6, cliente.getCorreo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo actualizar el cliente en la BD", e);
        }
    }

    @Override
    public boolean eliminarPersistente(Cliente cliente) {
        if (cliente == null || cliente.getCorreo() == null) {
            return false;
        }
        String sql = "DELETE FROM clientes WHERE correo = ?";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, cliente.getCorreo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo eliminar el cliente de la BD", e);
        }
    }

    @Override
    public void cargar() {
        clientes.clear();
        String sql = "SELECT correo, nombre, direccion, telefono, contrasena, tipo "
                + "FROM clientes ORDER BY rowid ASC";
        try (Connection conexion = conectar();
                Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer los clientes de la BD", e);
        }
    }

    @Override
    public List<Cliente> listarUltimos(int limite) {
        if (limite <= 0) {
            return new ArrayList<Cliente>();
        }
        String sql = "SELECT correo, nombre, direccion, telefono, contrasena, tipo "
                + "FROM clientes ORDER BY rowid DESC LIMIT ?";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, limite);
            try (ResultSet rs = ps.executeQuery()) {
                List<Cliente> ultimos = new ArrayList<Cliente>();
                while (rs.next()) {
                    ultimos.add(mapearCliente(rs));
                }
                return ultimos;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron leer los ultimos clientes de la BD", e);
        }
    }

    @Override
    public List<Cliente> listarPagina(int inicio, int cantidad) {
        if (inicio < 0 || cantidad <= 0) {
            return new ArrayList<Cliente>();
        }
        String sql = "SELECT correo, nombre, direccion, telefono, contrasena, tipo "
                + "FROM clientes ORDER BY rowid ASC LIMIT ? OFFSET ?";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, cantidad);
            ps.setInt(2, inicio);
            try (ResultSet rs = ps.executeQuery()) {
                List<Cliente> pagina = new ArrayList<Cliente>();
                while (rs.next()) {
                    pagina.add(mapearCliente(rs));
                }
                return pagina;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo leer la pagina de clientes de la BD", e);
        }
    }

    @Override
    public int contar() {
        String sql = "SELECT COUNT(*) FROM clientes";
        try (Connection conexion = conectar();
                Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo contar los clientes de la BD", e);
        }
    }

    @Override
    public List<Cliente> buscar(String criterio) {
        List<Cliente> resultado = new ArrayList<Cliente>();
        if (criterio == null || criterio.isBlank()) {
            return resultado;
        }
        String sql = "SELECT correo, nombre, direccion, telefono, contrasena, tipo "
                + "FROM clientes WHERE LOWER(correo) = LOWER(?) ORDER BY rowid ASC";
        try (Connection conexion = conectar();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, criterio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearCliente(rs));
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudo buscar el cliente en la BD", e);
        }
    }

    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        String tipo = rs.getString("tipo");
        Cliente cliente = "premium".equalsIgnoreCase(tipo)
                ? new ClientePremium()
                : new ClienteRegular();
        cliente.setCorreo(rs.getString("correo"));
        cliente.setNombreCompleto(rs.getString("nombre"));
        cliente.setDireccion(rs.getString("direccion"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setContrasena(rs.getString("contrasena"));
        return cliente;
    }

    @Override
    public void guardar() {
        String insert = "INSERT INTO clientes (correo, nombre, direccion, telefono, contrasena, tipo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = conectar()) {
            conexion.setAutoCommit(false);

            try {
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
                try {
                    conexion.rollback();
                } catch (SQLException ignored) {
                }
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException(
                    "No se pudieron guardar los clientes en la BD", e);
        }
    }
}
