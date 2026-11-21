//
package co.edu.uptc.tiendalibros.gui.catalogo;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;

//
public class ElementoVistaFichaLibro extends ElementoVista<LibroConFormatos> {
  
  public ElementoVistaFichaLibro(LibroConFormatos libroConFormatos) {
    super(libroConFormatos);
  }
  
  /// Obtiene el ID del libro.
  public Long getIdLibro() { return getElemento().getLibro()
    .getId(); }
  
  /// Obtiene el ISBN del libro.
  public String getIsbn() { return getElemento().getLibro()
    .getIsbn(); }
  
  /// Obtiene el título del libro.
  public String getTitulo() { return getElemento().getLibro()
    .getTitulo(); }
  
  /// Obtiene el año de publicación del libro.
  public String getAnioPublicacion() { return String.valueOf(
    getElemento().getLibro()
      .getAnioPublicacion()
  ); }
  
  /// Obtiene el nombre de la categoría del libro.
  public String getNombreCategoria() { return getElemento().getLibro()
    .getCategoria()
    .getNombre(); }
  
  /// Obtiene el nombre de la editorial del libro.
  public String getNombreEditorial() { return getElemento().getLibro()
    .getEditorial()
    .getNombre();
  }
  
  /// Obtiene el nombre del archivo de la cubierta. Nota: puede ser nulo o vacío.
  public String getNombreImagenCubierta() { return getElemento().getLibro()
    .getImagenLibro(); }
  
  /// Obtiene el ID del formato impreso del libro.
  public Long getIdFormatoImpreso() { return getElemento().getFormatoImpreso()
    .get()
    .getId(); }
  
  /// Obtiene el ID del formato digital del libro.
  public Long getIdFormatoDigital() { return getElemento().getFormatoDigital()
    .get()
    .getId(); }
  
  /// Precio de venta (con IVA incluido) del formato impreso ya formateado como moneda.
  public String getTextoPrecioImpreso() { return getElemento().getFormatoImpreso()
    .get()
    .calcularPrecioVenta()
    .formatear(); }
  
  /// Precio de venta (con IVA incluido) del formato digital ya formateado como moneda.
  public String getTextoPrecioDigital() { return getElemento().getFormatoDigital()
    .get()
    .calcularPrecioVenta()
    .formatear(); }
  
  /// Determina si el libro ofrece el formato impreso.
  public boolean tieneFormatoImpreso() {
    return getElemento().getFormatoImpreso()
      .isPresent();
  }
  
  /// Determina si el libro ofrece el formato digital.
  public boolean tieneFormatoDigital() {
    return getElemento().getFormatoDigital()
      .isPresent();
  }
  
}
