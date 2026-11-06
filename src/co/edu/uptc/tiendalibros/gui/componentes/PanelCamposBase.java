//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un panel que contiene campos de un formulario.
///
/// @param <EV>  Tipo de Elemento Vista que se quiere mostrar en el panel.
/// @param <DTO> Tipo de Objeto que el panel genera para transferir datos
public abstract class PanelCamposBase<EV extends ElementoVista<?>, DTO> extends PanelBase {
  
  /// Elemento vista que este formulario muestra
  private EV elementoVista;
  
  /// Asociación lógica de atributos, campos y notas de error.
  private AsociacionCamposFormulario asociacionCampos;
  
  public PanelCamposBase(AsociacionCamposFormulario asociacionCampos) {
    this.asociacionCampos = asociacionCampos;
  }
  
  /// Prepara los campos del panel para visualizar el elemento vista especificado.
  ///
  /// @param elementoVista Elemento vista para preparar los campos del panel.
  public abstract void prepararCampos(EV elementoVista);
  
  // /// Obtiene el elemento principal que el panel maneja.
  // ///
  // /// @return Elemento principal que el panel maneja.
  // public T getElemento() { return this.elementoVista != null ?
  // this.elementoVista.getElemento() : null; }
  
  /// Valida los campos del panel según las validaciones necesarias.
  ///
  /// @return Resultados con los errores de validación de los campos del panel.
  public abstract ResultadoValidacion validarCampos();
  
  /// Crea un objeto con los datos ingresados por el usuario.
  public abstract DTO capturarDatos();
  
  /// Obtiene el elemento vista que el panel maneja.
  ///
  /// @return Elemento que el panel maneja.
  protected EV getElementoVista() {
    return this.elementoVista;
  }
  
  /// Establece el elemento vista que el panel maneja.
  ///
  /// @param elemento Elemento vista que el panel maneja.
  protected void setElementoVista(EV elementoVista) { this.elementoVista = elementoVista; }
  
  /// Asocia de manera lógica un atributo con su campo y su nota de error.
  public void asociarCampo(String atributo, JComponent componente, JLabel notaError) {
    this.asociacionCampos.asociar(atributo, componente, notaError);
  }
  
  /// Agrega el componente a este panel usando el objeto [RestriccionesCelda].
  ///
  /// Es útil porque a veces se olvida convertir el objeto [RestriccionesCelda] al objeto
  /// [GridBagConstraints] requerido para un [GridBagLayout].
  public void agregar(JComponent componente, RestriccionesCelda rc) {
    add(componente, rc.aGBC());
  }
  
}
