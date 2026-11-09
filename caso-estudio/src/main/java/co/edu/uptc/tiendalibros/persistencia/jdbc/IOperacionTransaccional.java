//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

/// Representa una operación de la base de datos que se ejecuta dentro de una transacción.
/// 
/// @param <R> Tipo de resultado de la operación.
@FunctionalInterface
public interface IOperacionTransaccional<R> {
  
  /// Ejecuta la operación con la conexión especificada, que ya tiene la transacción
  /// iniciada.
  ///
  /// @param conexion Conexión con la transacción abierta.
  /// @return El resultado de la operación.
  R ejecutar(Connection conexion) throws SQLException;
}
