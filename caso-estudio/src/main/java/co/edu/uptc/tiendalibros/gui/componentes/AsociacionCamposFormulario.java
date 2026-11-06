//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.util.LinkedHashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;

/// Representa una colección de asociaciones lógicas, pero no estructurales, entre un
/// atributos, campos de datos y notas de error.
public class AsociacionCamposFormulario {
  
  private LinkedHashMap<String, CamposAsociados> asociacion;
  
  public AsociacionCamposFormulario() {
    this.asociacion = new LinkedHashMap<>();
  }
  
  public void asociar(String atributo, JComponent campo, JLabel notaError) {
    this.asociacion.put(atributo, new CamposAsociados(atributo, campo, notaError));
  }
  
  public LinkedHashMap<String, CamposAsociados> getAsociacionCampos() { return this.asociacion; }
  
  /// Representa una asociación lógica, pero no estructural, de un atributo con su campo de
  /// datos y su nota de error.
  public record CamposAsociados(String atributo, JComponent campo, JLabel notaError) {
  }
  
}
