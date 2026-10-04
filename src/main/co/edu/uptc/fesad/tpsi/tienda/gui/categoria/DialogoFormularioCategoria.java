package main.co.edu.uptc.fesad.tpsi.tienda.gui.categoria;

import java.awt.Window;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.DialogoFormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.FormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;

public class DialogoFormularioCategoria
  extends DialogoFormularioElementoBase {
  
  protected DialogoFormularioCategoria(
    Window propietario,
    String titulo,
    Categoria categoria,
    Evento evento
  ) {
    super(
      propietario,
      titulo,
      categoria,
      evento
    );
  }
  
  @Override
  protected FormularioElementoBase crearFormulario() {
    
    return (FormularioCategoria) new FormularioCategoria(
      getEvento()
    ).construir();
  }
  
  @Override
  protected String getEventoGuardar() { return ControladorCategoria.CATEGORIA_GUARDAR; }
  
  @Override
  protected String getEventoEliminar() { return ControladorCategoria.CATEGORIA_ELIMINAR; }
  
  @Override
  protected String describirElemento() {
    
    return String.format(
      "la categoría '%s'",
      ((Categoria) getElemento()).getNombre()
    );
  }
}