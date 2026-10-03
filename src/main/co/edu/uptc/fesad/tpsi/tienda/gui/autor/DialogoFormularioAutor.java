package main.co.edu.uptc.fesad.tpsi.tienda.gui.autor;

import java.awt.Window;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.DialogoFormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.FormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;

public class DialogoFormularioAutor extends DialogoFormularioElementoBase {
  
  protected DialogoFormularioAutor(
    Window propietario,
    String titulo,
    Autor autor,
    Evento evento
  ) {
    super(propietario, titulo, autor, evento);
    
  }
  
  @Override
  protected FormularioElementoBase crearFormulario() {
    return (FormularioAutor) new FormularioAutor(getEvento()).construir();
  }
  
  @Override
  protected String getEventoGuardar() { return ControladorAutor.AUTOR_GUARDAR; }
  
  @Override
  protected String getEventoEliminar() { return ControladorAutor.AUTOR_ELIMINAR; }
  
  @Override
  protected String describirElemento() {
    Autor autor = (Autor) getElemento();
    return String.format("El autor '%s' '%s'", autor.getNombre(), autor.getApellidos());
  }
  
}
