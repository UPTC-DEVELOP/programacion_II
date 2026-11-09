//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa las opciones para configurar una base de datos.
/// 
/// Las opciones se cargan desde un archivo `.properties`.
public class ConfiguracionBaseDatos {
  
  /// Ruta predeterminada del archivo `.properties`.
  public static final Path RUTA_PREDETERMINADA = Path.of("config", "bd.properties");
  
  /// URL de la conexión con el servidor de la base de datos.
  private final String url;
  
  /// Nombre de usuario para conectarse a la base de datos.
  private final String usuario;
  
  /// Contraseña del usuario para conectarse en la base de datos.
  private final String contrasena;
  
  /// Crea un nuevo objeto [ConfiguracionBaseDatos] con los datos especificados.
  /// 
  /// @param url        Dirección URL de la conexión con el servidor de la base de datos.
  /// @param usuario    Nombre de usuario para conectarse a la base de datos.
  /// @param contrasena Contraseña del usuario para conectarse en la base de datos.
  public ConfiguracionBaseDatos(
    String url,
    String usuario,
    String contrasena
  ) {
    this.url = url;
    this.usuario = usuario;
    this.contrasena = contrasena;
  }
  
  public static ConfiguracionBaseDatos cargar(Path ruta) throws ErrorTecnicoSistemaException {
    // crear el objeto propiedades para cargar los datos de conexión
    Properties propiedades = new Properties();
    
    // intentar cargar y leer las propiedades de los datos de conexión
    try (Reader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
      propiedades.load(lector);
    } catch (IOException excepcion) {
      throw new ErrorTecnicoSistemaException(
        String.format(
          "No se pudo leer el archivo de configuración: '%s'",
          ruta.toAbsolutePath()
        ), excepcion
      );
    }
    
    // se pudo cargar el archivo properties, pero falta revisar si tiene los valores
    // necesarios
    
    String valorURL = propiedades.getProperty("db.url");
    String valorUsuario = propiedades.getProperty("db.usuario");
    String valorContrasena = propiedades.getProperty("db.contrasena");
    
    if ((valorURL == null) || (valorURL.isBlank())
      || (valorUsuario == null) || (valorUsuario.isBlank())
      || (valorContrasena == null) || (valorContrasena.isBlank())) {
      // los valores necesarios no están, por lo que se lanza una excepción
      throw new ErrorTecnicoSistemaException(
        String.format(
          "El archivo de configuración '%s' debe tener los valores 'db.url', 'db.usuario' y 'db.contrasena'",
          ruta.toAbsolutePath()
        ), null
      );
    }
    
    // si cargar las propiedades fue exitosa, se devuelve la configuración
    return new ConfiguracionBaseDatos(valorURL.trim(), valorUsuario.trim(), valorContrasena.trim());
  }
  
  /// Obtiene la URL de la conexión con el servidor de la base de datos.
  public String getUrl() { return this.url; }
  
  /// Obtiene el nombre de usuario para conectarse a la base de datos.
  public String getUsuario() { return this.usuario; }
  
  /// Obtiene la contraseña del usuario para conectarse en la base de datos.
  public String getContrasena() { return this.contrasena; }
  
}
