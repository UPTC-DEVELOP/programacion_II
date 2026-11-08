//
package co.edu.uptc.tiendalibros.persistencia.conexiones;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa una conexión con una base de datos relacional para ejecutar comandos y
/// consultas SQL.
public class ConexionBaseDatos implements AutoCloseable {
  
  /// Tiempo máximo de espera para conectarse al servidor de la base de datos.
  private static final int TIEMPO_MAXIMO_ESPERA = 15;
  
  /// Objeto conexión que el driver SQL usa.
  private Connection conexion;
  
  /// Opciones para conectarse al servidor de la base de datos.
  private ConfiguracionBaseDatos configuracion;
  
  //
  public ConexionBaseDatos(ConfiguracionBaseDatos configuracion) {
    this.configuracion = configuracion;
  }
  
  /// Abre una conexión nueva con la base de datos.
  /// 
  /// La conexión estará abierta hasta que se cierre explícitamente.
  /// 
  /// Se recomienda usar el try-with-resources.
  public Connection abrir() throws SQLException {
    // establecer en el driver el tiempo máximo de espera para conectarse a la base de datos
    DriverManager.setLoginTimeout(ConexionBaseDatos.TIEMPO_MAXIMO_ESPERA);
    // pedirle al driver que cree la conexión con la base de datos.
    this.conexion = DriverManager
      .getConnection(this.configuracion.getUrl(), this.configuracion.getUsuario(), this.configuracion.getContrasena());
    return this.conexion;
  }
  
  @Override
  public void close() {
    try {
      // intentar cerrar el objeto conexión.
      if (this.conexion != null && !this.conexion.isClosed()) {
        this.conexion.close();
      }
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException(
        "Error al cerrar la conexión con la base de datos.", excepcion
      );
    }
    this.conexion = null;
  }
  
  /// Prueba si hay una conexión con la base de datos.
  /// 
  /// @return Un texto con información sobre el SGBD, la base deatos, el usuario y el driver.
  /// @throws ErrorTecnicoSistemaException Si ocurre un error al intentar conectarse con la
  ///                                      bae de datos.
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
}
