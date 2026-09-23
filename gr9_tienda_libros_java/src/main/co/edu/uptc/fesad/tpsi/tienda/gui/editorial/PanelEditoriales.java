//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.ControladorEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.InfoColumnaTabla;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.ModeloTablaLista;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelMaestroDetalles;

//
public class PanelEditoriales extends PanelBase {
  
  PanelMaestroDetalles panelEditorialMaestroDetalles;
  
  FormularioEditorial formulario;
  
  /// Crea un nuevo objeto [PanelEditoriales][PanelEditoriales] con el enrutador de eventos
  /// dado.
  /// 
  /// @param enrutador Enrutador de los eventos.
  public PanelEditoriales(EnrutadorEventos enrutador) {
    super(enrutador);
  }
  
  @Override
  public void inicializarComponente() {
    
    // crear la información de las columnas del modelo de tabla de datos
    InfoColumnaTabla[] infoColumnas = new InfoColumnaTabla[] {
      // Infocolumna (indice, encabezado, identificador, esEditable, esVisible)
      new InfoColumnaTabla(0, "ID", "id", false, true),
      new InfoColumnaTabla(1, "Nombre", "nombre", false, true),
    };
    
    // crear el modelo de tabla de datos
    ModeloTablaLista modeloTabla = new ModeloTablaLista(infoColumnas);
    
    // crear el formulario de editorial
    this.formulario = (FormularioEditorial) new FormularioEditorial(getEnrutadorEventos()).construir();
    
    // crear el panel de maestro detalle
    this.panelEditorialMaestroDetalles = (PanelMaestroDetalles) new PanelMaestroDetalles(
      modeloTabla, this.formulario
    ).construir();
    
    // Asignar las funciones que manejarán los eventos
    
    // asignar la función que manejará el evento cuando el usuario haga click en el botón
    // Crear
    this.panelEditorialMaestroDetalles.getPanelLista()
      .setFuncionClickNuevo((elemento) -> getEnrutadorEventos().manejarEvento(ControladorEditorial.EDITORIAL_NUEVO));
    
    // asignar la función que manejeará el evento cuando el usuario seleccione una fila en la
    // tabla visual
    this.panelEditorialMaestroDetalles.getPanelLista()
      .setFuncionFilaSeleccionada((indiceFila) -> {
        // tenemos él índice de la fila, pero el controlador necesita el ID de la editorial
        // obtener el ID de la editorial y se supone que está en la columna 0
        Object id = this.panelEditorialMaestroDetalles.getPanelLista()
          .getValorCelda(indiceFila, 0);
        // invocar la función manejadora en el controlador enviando el ID de la editorial
        getEnrutadorEventos().manejarEvento(ControladorEditorial.EDITORIAL_EDITAR, id);
      });
    
    add(this.panelEditorialMaestroDetalles);
    
  }
  
  /// Actualiza la tabla de editoriales.
  /// 
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.panelEditorialMaestroDetalles.listar(datos);
  }
  
  public void prepararFormulario(Object elemento) {
    this.formulario.preparar(elemento);
  }
}
