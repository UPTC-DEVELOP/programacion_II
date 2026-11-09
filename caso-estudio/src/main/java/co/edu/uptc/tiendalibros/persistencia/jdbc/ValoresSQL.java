//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/// Representa la conversión de valores SQL en valores Java, especialmente la conversión
/// de valores nulos.
public final class ValoresSQL {
  
  private ValoresSQL() {
  }
  
  /// Lee un valor numérico [Long] en una celda que puede ser NULL.
  ///
  /// @param fila          Fila actual del resultado.
  /// @param nombreColumna Nombre de la columna.
  /// @return El valor numérico en la celda, o `null` si la celda es NULL en la base de
  ///         datos.
  /// @throws SQLException Si ocurre un error al leer y convetir el valor.
  public static Long leerLongNulo(ResultSet fila, String nombreColumna) throws SQLException {
    // si en SQL la celda es NULL, JDBC devuelve 0, y eso debe convertir a un null propio de
    // Java
    long valor = fila.getLong(nombreColumna);
    if (fila.wasNull()) {
      // verificación: si al leer esa columna, SQL dijo NULL, se devuelve un null de Java
      return null;
    }
    
    // aquí, se leyó la celda en SQL y sí es un número en la base de datos
    return valor;
  }
  
  /// Escribe un valor numérico [Long] que puede ser nulo en un parámetro de un comando SQL.
  /// 
  /// @param comando         Comando SQL preparado.
  /// @param indiceParametro Índice o posición del parámetro en el comando SQL. Índice basado
  ///                        en 1.
  /// @param valor           Valor numérico que se quiere escribir. Un valor `null` guarda
  ///                        `NULL` en la base de datos.
  /// @throws SQLException Si ocurre un error al leer y convetir el valor.
  public static void escribirLongNulo(PreparedStatement comando, int indiceParametro, Long valor) throws SQLException {
    if (valor == null) {
      // el valor es null. Se escribe un NULL según los tipos SQL de Java
      comando.setNull(indiceParametro, Types.BIGINT);
    } else {
      // el valor no es null. Se escribe el valor directamente
      comando.setLong(indiceParametro, valor);
    }
  }
  
}
