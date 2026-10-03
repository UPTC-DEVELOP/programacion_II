/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial;

import java.awt.Window;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.DialogoFormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.FormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Editorial;

///
public class DialogoFormularioEditorial extends DialogoFormularioElementoBase {


  protected DialogoFormularioEditorial(
    Window propietario,
    String titulo,
    Editorial editorial,
    Evento evento
  ) {
    super(propietario, titulo, editorial, evento);
    
  }


  @Override
  protected FormularioElementoBase crearFormulario() {
    return (FormularioEditorial) new FormularioEditorial(getEvento()).construir();
  }
  
  @Override
  protected String getEventoGuardar() { return ControladorEditorial.EDITORIAL_GUARDAR; }
  
  @Override
  protected String getEventoEliminar() { return ControladorEditorial.EDITORIAL_ELIMINAR; }
  
  @Override
  protected String describirElemento() {
    return String.format("la editorial '%s'", ((Editorial) getElemento()).getNombre());
  }
  

}
