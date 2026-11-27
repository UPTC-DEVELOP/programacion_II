import com.letsread.persistence.ConexionJDBC;
import java.sql.Connection;

public class PruebaConexion {
    public static void main(String[] args) {
        try (Connection con = ConexionJDBC.obtenerConexion()) {
            System.out.println("Conexión exitosa a: " + con.getCatalog());
        } catch (Exception e) {
            System.out.println("Error de conexión: " + e.getMessage());
        }
    }
}