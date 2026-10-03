/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;

///
public abstract class FormularioElementoBase extends PanelBase {
  
  /// ID del elemento que este formulario maneja.
  /// - ID con valor numérico: el elemento existe en el repositorio de datos
  /// - ID con valor null: la editorial todavía no existe en el repositorio de datos.
  private Long idElemento = null;
  
  /// Crea un nuevo objeto [FormularioElementoBase][FormularioElementoBase].
  protected FormularioElementoBase(Evento evento) {
    super(evento);
  }
  
  /// Inicializar el formulario.
  /// 
  /// @param titulo Título que se mostrará en la zona del formulario.
  public void inicializarComponente(String titulo) {
    setLayout(new BorderLayout(5, 5));
    Border borde = BorderFactory.createTitledBorder(titulo);
    setBorder(borde);
    

  }
  
  public abstract void preparar(Object elemento);
  
  public abstract Object capturarDatos();
  
  public Long getIDElemento() { return this.idElemento; }
  
  protected void setIDElemento(Long id) { this.idElemento = id; }


}
