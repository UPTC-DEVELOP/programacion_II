package co.edu.uptc.tiendalibros.gui.libro;

import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.componentes.ElementoVista;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.FormatoLibroBuilder;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;

/// Representa un objeto [FormatoLibro] adaptado para ser mostrado en la interfaz gráfica.
/// 
/// El formulario necesita un formato de libro para mostrar sus campos.
/// 
/// Esta vista muestra o bien el formato ofrecido por el libro, o bien un
/// formato predeterminado.
public class ElementoVistaFormatoLibro extends ElementoVista<FormatoLibro> {
  
  /// Indica si el libro ofrece este formato de libro.
  private final boolean ofrecido;
  
  /// Crea una vista para un formato de libro.
  /// 
  /// @param formatoLibro Formato que se muestra en los campos del formulario.
  /// @param ofrecido     `true` si el libro ofrece este formato de libro.
  public ElementoVistaFormatoLibro(
    FormatoLibro formatoLibro,
    boolean ofrecido
  ) {
    super(formatoLibro);
    this.ofrecido = ofrecido;
  }
  
  /// Crea la vista de un formato opcional del libro. Si el libro no ofrece el formato, la
  /// vista usa un formato con valores predeterminados del tipo indicado.
  ///
  /// @param formato     Formato que el libro ofrece. Vacío si no lo ofrece.
  /// @param tipoFormato Tipo del formato que la vista representa.
  /// @return Vista del formato.
  public static ElementoVistaFormatoLibro crear(Optional<FormatoLibro> formato, TipoFormatoLibro tipoFormato) {
    // si hay un formato de libro, se crea la vista con ese formato
    if (formato.isPresent()) {
      return new ElementoVistaFormatoLibro(formato.get(), true);
    }
    
    // no hubo formato de libro, se crea la vista con un formato predeterminado.
    FormatoLibro predeterminado = new FormatoLibroBuilder().tipoFormatoLibro(tipoFormato)
      .construir();
    return new ElementoVistaFormatoLibro(predeterminado, false);
  }
  
  /// Indica si el libro ofrece este formato.
  ///
  /// @return `true` si el libro ofrece este formato.
  public boolean esOfrecido() {
    return this.ofrecido;
  }
  
}