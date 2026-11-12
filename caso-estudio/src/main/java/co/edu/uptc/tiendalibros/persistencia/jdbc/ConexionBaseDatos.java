//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa una conexión con una base de datos relacional para ejecutar comandos y
/// consultas SQL.
public class ConexionBaseDatos {
  
  /// Tiempo máximo de espera para conectarse al servidor de la base de datos.
  private static final int TIEMPO_MAXIMO_ESPERA = 5;
  
  /// Opciones para conectarse al servidor de la base de datos.
  private ConfiguracionBaseDatos configuracion;
  
  //
  public ConexionBaseDatos(ConfiguracionBaseDatos configuracion) {
    this.configuracion = configuracion;
    
    // establecer en el driver el tiempo máximo de espera para conectarse a la base de datos
    DriverManager.setLoginTimeout(ConexionBaseDatos.TIEMPO_MAXIMO_ESPERA);
  }
  
  /// Abre una conexión nueva con la base de datos.
  /// 
  /// Se crea una nueva conexión que se mantiene abierta y quien la use debe cerrarla
  /// explícitamente.
  /// 
  /// Se recomienda usar el try-with-resources.
  public Connection abrir() throws SQLException {
    
    // pedirle al driver que cree la conexión con la base de datos.
    return DriverManager
      .getConnection(this.configuracion.getUrl(), this.configuracion.getUsuario(), this.configuracion.getContrasena());
  }
  
  /// Prueba si hay una conexión con la base de datos.
  /// 
  /// @return Un texto con información sobre el SGBD, la base deatos, el usuario y el driver.
  /// @throws ErrorTecnicoSistemaException Si ocurre un error al intentar conectarse con la
  ///                                      base de datos.
  public String probar() throws ErrorTecnicoSistemaException {
    // abrir la conexión con la base de datos usando try-with-resources
    try (Connection conexion = this.abrir()) {
      
      // verificar si la conexión es válida
      if (conexion.isValid(ConexionBaseDatos.TIEMPO_MAXIMO_ESPERA) == false) {
        throw new SQLException("La base de datos no respondió a tiempo.");
      }
      // obtener los metadatos de la conexión con la base de datos
      DatabaseMetaData metaData = conexion.getMetaData();
      
      return String.format(
        "SGBD: %s\tVersión:%s%nBase de datos: %s%nUsuario: %s%nDriver: %s",
        metaData.getDatabaseProductName(),
        metaData.getDatabaseMajorVersion(),
        conexion.getCatalog(),
        metaData.getUserName(),
        metaData.getDriverName()
      );
      
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException("No se pudo conectar con la base de datos.", excepcion);
    }
  }

  /// Ejecuta una operación de la base de datos dentro una transacción.
  /// 
  /// Si la operación termina bien, confirma los cambios ( `commit`).
  /// 
  /// Si la operación falla, deshace los cambios (`rollback`).
  /// 
  /// @param operacion Operación que se ejecuta en la transacción.
  /// @return El resultado de la operación.
  public <R> R ejecutarEnTransaccion(IOperacionTransaccional<R> operacion) {
    // ejecutar la transacción en una conexión
    try (Connection conexion = abrir()) {
      // iniciar la transacción: los cambios no se confirman hasta el commit explícito
      conexion.setAutoCommit(false);
      
      try {
        // ejecutar la operación usando esta misma conexión
        R resultado = operacion.ejecutar(conexion);
        // si la operación fue exitosa, se confirman los cambios (`commit`).
        conexion.commit();
        return resultado;
      } catch (SQLException | RuntimeException excepcion) {
        // algo en la operación falla, se deshacen los cambios (`rollback`)
        conexion.rollback();
        throw excepcion;
      }
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException(
        "Error de la base de datos al ejecutar la operación de transacción.", excepcion
      );
    }
  }
}
