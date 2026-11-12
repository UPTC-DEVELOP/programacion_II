//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import co.edu.uptc.tiendalibros.modelo.Dinero;

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
  /// @throws SQLException Si ocurre un error al leer y convertir el valor.
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
  /// @throws SQLException Si ocurre un error al leer y convertir el valor.
  public static void escribirLongNulo(PreparedStatement comando, int indiceParametro, Long valor) throws SQLException {
    if (valor == null) {
      // el valor es null. Se escribe un NULL según los tipos SQL de Java
      comando.setNull(indiceParametro, Types.BIGINT);
    } else {
      // el valor no es null. Se escribe el valor directamente
      comando.setLong(indiceParametro, valor);
    }
  }
  
  /// Lee una cantidad de dinero de una columna DECIMAL que NO admite NULL.
  ///
  /// La moneda del dinero no se guarda en la base de datos. Moneda viene de la moneda
  /// predeterminada [Dinero#MONEDA].
  ///
  /// @throws SQLException Si ocurre un error al leer el valor, o si la celda es NULL.
  public static Dinero leerDinero(ResultSet fila, String nombreColumna) throws SQLException {
    // si en SQL la celda es NULL, JDBC devuelve null
    BigDecimal cantidad = fila.getBigDecimal(nombreColumna);
    // no se puede convertir un null en Dinero
    if (cantidad == null) {
      throw new SQLException(
        String.format("La columna '%s' es NULL, pero se esperaba una cantidad de dinero.", nombreColumna)
      );
    }
    return new Dinero(cantidad);
  }
  
  /// Escribe una cantidad de dinero en un parámetro DECIMAL de un comando SQL.
  ///
  /// `valor` no debe ser nulo y está en la moneda predeterminada [Dinero#MONEDA], porque la
  /// base de datos solo guarda la cantidad, no la moneda.
  /// 
  /// Por ejemplo, si la base de datos ya está guardando en pesos COP el valor en dólares USD
  /// no se puede guardar
  ///
  /// @throws IllegalArgumentException Si `valor` es nulo o no está en la moneda
  ///                                  predeterminada.
  /// @throws SQLException             Si ocurre un error al escribir el valor.
  /// 
  public static void escribirDinero(PreparedStatement comando, int indiceParametro, Dinero valor)
    throws SQLException {
    // el valor dinero que se quiere guardar no puede ser nulo
    if (valor == null) {
      throw new IllegalArgumentException("ValoresSQL: el valor de dinero no puede ser nulo.");
    }
    
    // tener cuidado en guardar dinero que sea de la misma moneda
    if (Dinero.MONEDA.equals(valor.getMoneda()) == false) {
      throw new IllegalArgumentException(
        String.format(
          "ValoresSQL: la base de datos solo guarda %s y el valor está en %s.",
          Dinero.MONEDA.getCurrencyCode(),
          valor.getMoneda()
            .getCurrencyCode()
        )
      );
    }
    comando.setBigDecimal(indiceParametro, valor.getCantidad());
  }
  
}
