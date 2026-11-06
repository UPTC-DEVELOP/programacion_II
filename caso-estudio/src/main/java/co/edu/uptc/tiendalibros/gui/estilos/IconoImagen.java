//
package co.edu.uptc.tiendalibros.gui.estilos;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.swing.ImageIcon;

/// Representa la agrupación de íconos para la interfaz gráfica.
public enum IconoImagen {
  
  APLICACION("aplicacion-1.png"),
  CREAR("nuevo-1.png"),
  GUARDAR("disquete-1.png"),
  EDITAR("editar-1.png"),
  ELIMINAR("caneca-1.png"),
  LOGIN("login-1.png"),
  CATALOGO("catalogo-1.png"),
  VENTAS("ventas-1.png"),
  CLIENTES("clientes-1.png"),
  ADMINISTRACION("administracion-1.png"),
  EDITORIAL("editorial-1.png"),
  AUTOR("autor-1.png"),
  CATEGORIA("categoria-1.png"),
  LIBRO("libro-1.png");
  
  /// Tamaño del lado predeterminado que tendrá la imagen del ícono. Se mide en pixeles.
  private static final int LADO_PREDETERMINADO = 20;
  
  /// Colección de imágenes de diferente tamaño que tendrá el ícono.
  ///
  /// Ejemplo: 16 px -> login.png; 24 px -> login.png.
  ///
  /// Es una caché para optimizar la carga de las imágenes del ícono en memoria. O sea,
  /// precarga una imagen de 16 px, 20 px, 24px, etc
  private final Map<Integer, Optional<ImageIcon>> iconosCargados = new HashMap<>();
  
  /// Nombre del archivo de imagen para el ícono.
  private String nombreArchivo;
  
  /// Crea un nuevo objeto [IconoImagen].
  ///
  /// @param nombreArchivo Nombre del archivo con la imagen del ícono.
  private IconoImagen(String nombreArchivo) {
    this.nombreArchivo = nombreArchivo;
  }
  
  /// Obtiene el ícono con el tamaño de lado predeterminado.
  ///
  /// @return El ícono imagen si el ícono se carga desde el disco exitosamente.
  public Optional<ImageIcon> icono() {
    return icono(IconoImagen.LADO_PREDETERMINADO);
  }
  
  /// Obtiene el ícono con el tamaño de lado especificado.
  ///
  /// @param lado Tamaño del lado del ícono. Lado = Alto = Ancho.
  /// @return El ícono imagen si el ícono se carga desde el disco exitosamente.
  public Optional<ImageIcon> icono(int lado) {
    if (this.iconosCargados.containsKey(lado) == false) {
      this.iconosCargados.put(lado, ManejadorRecursos.obtenerIcono(this.nombreArchivo, lado, lado));
    }
    return this.iconosCargados.get(lado);
  }

  /// Precarga en memoria todos los íconos.
  public static void precargar() {
    for (IconoImagen icono : IconoImagen.values()) {
      icono.icono();
    }
  }
}
