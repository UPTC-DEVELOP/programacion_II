package co.edu.uptc.tienda.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.interfaz.IPersistenciaCalificacion;
import co.edu.uptc.tienda.modelo.Calificacion;

public class CalificacionBD implements IPersistenciaCalificacion {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void guardar(Calificacion calificacion) throws LibroExcepcion {
        String sql = "INSERT INTO calificacion (isbn, id_cliente, puntaje, comentario, fecha) VALUES (?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE puntaje = VALUES(puntaje), comentario = VALUES(comentario), "
                + "fecha = VALUES(fecha)";
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, calificacion.getIsbn());
            sentencia.setString(2, calificacion.getIdCliente());
            sentencia.setInt(3, calificacion.getPuntaje());
            sentencia.setString(4, calificacion.getComentario());
            sentencia.setTimestamp(5, Timestamp.valueOf(calificacion.getFecha()));
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new LibroExcepcion("No se pudo guardar la calificación en la base de datos: "
                    + ConexionBD.explicarError(e));
        }
    }

    @Override
    public List<Calificacion> leer() throws LibroExcepcion {
        List<Calificacion> calificaciones = new ArrayList<>();
        String sql = "SELECT isbn, id_cliente, puntaje, comentario, fecha FROM calificacion ORDER BY fecha";
        try (Connection conexion = ConexionBD.obtenerConexion();
                PreparedStatement sentencia = conexion.prepareStatement(sql);
                ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                calificaciones.add(new Calificacion(resultado.getString("isbn"), resultado.getString("id_cliente"),
                        resultado.getInt("puntaje"), resultado.getString("comentario"),
                        resultado.getTimestamp("fecha").toLocalDateTime().format(FORMATO)));
            }
        } catch (SQLException e) {
            throw new LibroExcepcion("No se pudieron leer las calificaciones de la base de datos: "
                    + ConexionBD.explicarError(e));
        }
        return calificaciones;
    }
}
