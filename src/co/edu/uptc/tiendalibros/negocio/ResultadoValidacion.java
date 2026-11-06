//
package co.edu.uptc.tiendalibros.negocio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionReglaNegocioException;

/// Representa un conjunto de resultados de validación.
///
/// Acumula los errores de validación de atributos y generales.
public final class ResultadoValidacion {
  
  /// Colección de los errores de validación por atributo.
  private final Map<String, List<String>> porAtributo;
  
  /// Colección de los errores de validación generales.
  private final List<String> generales;
  
  /// Crea un nuevo objeto [ResultadoValidacion] sin contener errores e validación.
  public ResultadoValidacion() {
    this.porAtributo = new LinkedHashMap<>();
    this.generales = new ArrayList<>();
  }
  
  /// Agrega un error de validación de atributo.
  ///
  /// @param atributo Nombre del atributo que tiene el error de validación.
  /// @param mensaje  Mensaje que describe el error de validación del atributo.
  public void agregarAtributo(String atributo, String mensaje) {
    // si es la primera vez que se crea el error para el atributo, se debe crear su lista de
    // errores
    if (this.porAtributo.get(atributo) == null) {
      this.porAtributo.put(atributo, new ArrayList<String>());
    }
    // agregar el error a la colección
    this.porAtributo.get(atributo)
      .add(mensaje);
  }
  
  /// Agrega un error de validación general.
  ///
  /// @param mensaje Mensaje que describe el error de validación general.
  public void agregarGeneral(String mensaje) {
    this.generales.add(mensaje);
  }
  
  /// Determina si hay errores de validación de atributos y generales.
  ///
  /// @return `true` si hay errores de validación de atributos y generales.
  public boolean hayErrores() {
    return (this.porAtributo.size() > 0) || (this.generales.size() > 0);
  }
  
  /// Determina si hay errores de validación de atributos.
  ///
  /// @return `true` si hay errores de validación de atributos.
  public boolean hayErroresPorAtributo() {
    return this.porAtributo.size() > 0;
  }
  
  /// Determina si hay errores de validación generales.
  ///
  /// @return `true` si hay errores de validación generales.
  public boolean hayErroresGenerales() {
    return this.generales.size() > 0;
  }
  
  /// Obtiene la colección de errores de validación de atributo.
  ///
  /// @return Una copia de la colección de errores de validación de atributo.
  public Map<String, List<String>> getPorAtributo() { return Collections.unmodifiableMap(this.porAtributo); }
  
  /// Obtiene la colección de errores de validación generales.
  ///
  /// @return Una copia de la colección de validación generales.
  public List<String> getGenerales() { return List.copyOf(this.generales); }
  
  /// Hace la unión de este objeto con otro objeto [(ResultadoValidacion].
  ///
  /// @param otro El otro objeto que representa errores de validación.
  public void union(ResultadoValidacion otro) {
    // unir los errores de validación de atributos
    for (Map.Entry<String, List<String>> entrada : otro.porAtributo.entrySet()) {
      for (String mensaje : entrada.getValue()) {
        agregarAtributo(entrada.getKey(), mensaje);
      }
    }
    // unir los errores de validación generales
    for (String mensaje : otro.generales) {
      agregarGeneral(mensaje);
    }
  }
  
  /// Agrega una excepción de violación de regla de negocio. La excepción debe saber cómo
  /// reportar el error.
  public void agregarViolacion(ViolacionReglaNegocioException violacion) {
    // cada violación decide cómo reportarse a este error
    violacion.reportarEn(this);
  }
  
}
