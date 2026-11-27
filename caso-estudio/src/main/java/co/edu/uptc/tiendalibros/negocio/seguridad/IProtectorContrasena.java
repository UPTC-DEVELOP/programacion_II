package co.edu.uptc.tiendalibros.negocio.seguridad;

import co.edu.uptc.tiendalibros.modelo.ContrasenaProtegida;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa una estrategia para proteger contraseñas y para verificarlas después.
/// 
/// Esta interfaz es para aplicar el patrón estrategia (strategy) para permitir que se
/// cambie el algoritmo interno de protección (encriptación) de contraseñas.
/// 
/// O sea, [GestionSeguridad] confía en que se use un buen algoritmo de encriptación, pero
/// sin conocer los detalles.
public interface IProtectorContrasena {
  
  /// Protege una contraseña: calcula su hash con una sal nueva y aleatoria.
  ///
  /// Nota: cada invocación de [#proteger(char\[\])] usa una sal distinta, por lo que invocar
  /// dos o más veces con la misma contraseña produce resultados distintos.
  ///
  /// @param contrasena Contraseña en claro.
  /// @return La contraseña protegida (encriptada) y lista para guardarla en la base de
  ///         datos.
  /// @throws ErrorTecnicoSistemaException Si ocurre un error al proteger (encriptar)
  ///                                      la contraseña.
  ContrasenaProtegida proteger(char[] contrasena);
  
  /// Verifica si una contraseña en claro (sin proteger) corresponde a una contraseña
  /// protegida.
  ///
  /// @param contrasena Contraseña en claro (sin proteger) que usuario ingresó.
  /// @param protegida  Contraseña protegida que se guardó en la base de datos.
  /// @return `true` si la contraseña en claro es la contraseña que se protegió.
  /// @throws ErrorTecnicoSistemaException Si ocurre un error al verificar la contraseña.
  boolean verificar(char[] contrasena, ContrasenaProtegida protegida);
}