//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.ItemSelectable;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.swing.JFormattedTextField;
import javax.swing.text.JTextComponent;

import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un validador que verifica si los campos de un formulario cumplen con alguna
/// condición antes de enviar los datos del elemento al controlador respectivo.
///
/// Las validaciones se pueden encadenar de manera similar al patrón de diseño Builder.
public class ValidadorFormulario {
  
  /// Resultado de la validación.
  private final ResultadoValidacion errores;
  
  /// Crea un nuevo objeto [ValidadorFormulario].
  public ValidadorFormulario() {
    this.errores = new ResultadoValidacion();
  }
  
  /// Patrón de expresión regular que  obliga a los nombres de archivo de imagen a empezar con una letra o número, solo puede tener letras minúsculas, números, punto (.), guion (-) y guion bajo (_), sin tildes, y sin espacios, y máximo 255 caracteres.
  private static final Pattern PATRON_NOMBRE_ARCHIVO = Pattern.compile("[a-z0-9][a-z0-9._-]{0,254}");
  
  /// Patrón de expresión regular para rechazar nombres de archivos que no sean admitidos en Windows.
  private static final Pattern PATRON_NOMBRE_RESERVADO =
    Pattern.compile("(con|prn|aux|nul|com[1-9]|lpt[1-9])(\\..*)?");
  
  
  /// Obtiene el resultado de la validación con todos los errores encontrados.
  ///
  /// @return [ResultadoValidacion] con los errores encontrados en el formulario.
  public ResultadoValidacion getErrores() { return this.errores; }
  
  /// Valida si el campo de texto no tiene contenido.
  /// 
  /// @param atributo    Nombre del atributo.
  /// @param nombreCampo Nombre del campo que el usuario verá.
  /// @param campo       Campo de texto que contiene el valor del atributo.
  /// @return Este mismo objeto [ValidadorFormulario] 
  public ValidadorFormulario textoNoVacio(String atributo, String nombreCampo, JTextComponent campo) {
    String texto = campo.getText() != null ? campo.getText()
      .trim() : "";
    
    // no cumple si el texto es vacío
    if (texto.isBlank()) {
      this.errores.agregarAtributo(atributo, String.format("El campo '%s' no puede estar vacío.", nombreCampo));
    }
    
    return this;
  }
  
  /// Valida si el campo tiene un valor que representa un número entero.
  ///
  /// @param atributo    Nombre del atributo.
  /// @param nombreCampo Nombre del campo que el usuario verá.
  /// @param campo       Campo de texto que contiene el valor del atributo.
  /// @return Este mismo objeto [ValidadorFormulario].
  public ValidadorFormulario numeroEntero(String atributo, String nombreCampo, JTextComponent campo) {
    // obtener el texto del campo que no sea nulo
    String valorTexto = campo.getText() != null ? campo.getText()
      .trim() : "";
    
    // no se cumple si el texto es vacío
    if (valorTexto.isBlank()) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' está vacío y debe ser un número entero.", nombreCampo)
      );
      return this;
    }
    
    // intentar convertir el texto a un número entero
    try {
      Integer.parseInt(valorTexto);
    } catch (NullPointerException | IllegalArgumentException excepcion) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' debe ser un número entero.", nombreCampo)
      );
      return this;
    }
    
    // aquí, se retorna sin errores de validación
    return this;
  }
  
  /// Valida si el campo tiene un valor que representa un número decimal.
  ///
  /// @param atributo    Nombre del atributo.
  /// @param nombreCampo Nombre del campo que el usuario verá.
  /// @param campo       Campo de texto que contiene el valor del atributo.
  /// @return Este mismo objeto [ValidadorFormulario].
  public ValidadorFormulario numeroDecimal(String atributo, String nombreCampo, JTextComponent campo) {
    // obtener el texto del campo que no sea nulo
    String valorTexto = campo.getText() != null ? campo.getText()
      .trim() : "";
    
    // no se cumple si el texto es vacío
    if (valorTexto.isBlank()) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' está vacío y debe ser un número decimal.", nombreCampo)
      );
      return this;
    }
    
    // intentar convertir el texto a un número decimal
    try {
      Double.parseDouble(valorTexto);
    } catch (NullPointerException | IllegalArgumentException excepcion) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' debe ser un número decimal.", nombreCampo)
      );
      return this;
    }
    
    // aquí, se retorna sin errores de validación
    return this;
  }
  
  /// Valida si el campo tiene un valor que representa una cantidad de dinero.
  ///
  /// @param atributo    Nombre del atributo.
  /// @param nombreCampo Nombre del campo que el usuario verá.
  /// @param campo       Campo de texto que contiene el valor del atributo.
  /// @return Este mismo objeto [ValidadorFormulario].
  public ValidadorFormulario numeroDinero(String atributo, String nombreCampo, JFormattedTextField campo) {
    Object valor = campo.getValue();
    
    // no se cumple si el valor es nulo
    if (valor == null) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' está vacío y debe ser un número decimal.", nombreCampo)
      );
      return this;
    }
    
    // no se cumple si el valor es un número entero
    if (!(valor instanceof Number)) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' debe ser un número decimal.", nombreCampo)
      );
    }
    
    return this;
  }
  
  /// Valida si una lista de selección tenga un ítem seleccionado diferente a un nulo.
  ///
  /// El primer ítem de la lista es null ("— Seleccione —"), así que null significa que el
  /// usuario no eligió nada.
  ///
  /// @param atributo Nombre del atributo.
  /// @param campo    Campo lista que quiere validar.
  /// @param mensaje  Mensaje de error para la validación.
  public ValidadorFormulario unaOpcionSeleccionada(String atributo, ItemSelectable campo, String mensaje) {
    // el campo debe tener un ítem seleccionado.
    // esta validación serviría para un JComboBox y un JList
    Object[] seleccionados = campo.getSelectedObjects();
    boolean sinSeleccion = (seleccionados == null)
      || (seleccionados.length == 0)
      || (seleccionados[0] == null);
    
    if (sinSeleccion) {
      this.errores.agregarAtributo(atributo, mensaje);
    }
    return this;
  }
  
  
  public ValidadorFormulario nombreImagenValido(String atributo, String nombreCampo, JTextComponent campo) {
    
    String nombreArchivo = campo.getText() != null ? campo.getText()
      .trim() : "";
    
    // no cumple si el nombre del archivo de imagen está vacío
    if (nombreArchivo.isEmpty()) {
      return this;
    }
    
    // obligar al usuario a que escriba el nombre del archivo de la imagen debe estar en minúsculas. Locale.ROOT es un idioma neutral, y entonces toLowerCase() siempre cambia todo a minúsculas
    if (nombreArchivo.equals(nombreArchivo.toLowerCase(Locale.ROOT)) == false) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' debe estar escrito en minúsculas.", nombreCampo)
      );
      // return this;
    }
    
    // validar que el nombre del archivo cumpla lo exigido
    if (ValidadorFormulario.PATRON_NOMBRE_ARCHIVO.matcher(nombreArchivo).matches() == false) {
      this.errores.agregarAtributo(atributo, String.format("El campo '%s' debe empezar con una letra o número, solo puede tener letras minúsculas, números, punto (.), guion (-) y guion bajo (_), sin tildes, sin espacios, y máximo 255 caracteres.", nombreCampo) );
      // return this;
    }
    
    // el nombre de archivo tampoco puede terminar con un punto 
    if (nombreArchivo.endsWith(".")) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' no puede terminar en punto.", nombreCampo)
      );
      // return this;
    }
    
    // el nombre de archivo no puede ser un nombre reservado del sistema operativo
    if (ValidadorFormulario.PATRON_NOMBRE_RESERVADO.matcher(nombreArchivo)
      .matches()) {
      this.errores.agregarAtributo(
        atributo,
        String.format("El campo '%s' tiene un nombre que Windows reserva (con|prn|aux|nul|com|lpt).", nombreCampo));
      
    }
    
    return this;
    
  }
}
