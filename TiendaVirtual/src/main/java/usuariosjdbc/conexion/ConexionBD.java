package usuariosjdbc.conexion;
import configuracion.ConfiguracionBD;
import java.sql.Connection;
import java.sql.SQLException;
public class ConexionBD {
    public Connection obtenerConexion() throws SQLException { return ConfiguracionBD.conexion(); }
}
