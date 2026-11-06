//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.SwingUtilities;

import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa un panel base para la gestión de una entidad o elemento de negocio.
///
/// @param <EV> Tipo de ElementoVista que este panel maneja.
public abstract class PanelGestionElementoBase<EV extends ElementoVista<?>> extends PanelBase {

  /// Índice de la columna que representa el ID en el modelo lógico de la tabla de datos.
  private int columnaID;

  /// Representa una barra acciones para la gestión de la entidad.
  private PanelBarraAcciones barraAcciones;

  /// Panel con la tabla visual de datos que muestra la lista de entidades.
  private PanelTablaLista panelTabla;

  /// Diálogo que muestra el formulario de crear o editar una entidad de negocio.
  private DialogoFormularioElemento<EV> dialogo;
  
  /// Crea un nuevo objeto [PanelGestionEntidadBase][PanelGestionElementoBase] para la
  /// gestión de una entidad.
  public PanelGestionElementoBase(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout());
    
    // agregar la barra de acciones
    this.barraAcciones = (PanelBarraAcciones) new PanelBarraAcciones(getEvento()).construir();
    add(this.barraAcciones, BorderLayout.NORTH);
    
    // agregar el panel de tabla de datos
    this.panelTabla = crearPanelTabla();
    add(this.panelTabla, BorderLayout.CENTER);
    
    // agregar y configurar los botones de acción
    configurarBotones();
  }
  
  public PanelTablaLista crearPanelTabla() {
    // crear el modelo de tabla de datos
    ModeloTablaLista modeloTabla = new ModeloTablaLista(getInfoColumnas());
    
    // crear el panel con la tabla de datos
    PanelTablaLista elPanelTabla = (PanelTablaLista) new PanelTablaLista(modeloTabla).construir();
    
    // asignar la función que manejará el evento cuando el usuario haga doble click en una
    // fila de la tabla visual
    elPanelTabla.setFuncionFilaActivada((indiceFila) -> {
      // entonces al hacer doble click en una fila se solicita editar el elemento de esa fila
      solicitarEdicion(indiceFila);
    });
    
    return elPanelTabla;
  }
  
  /// Configura los los botones de acción del panel.
  public abstract void configurarBotones();
  
  /// Actualiza la tabla de datos.
  ///
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.panelTabla.listar(datos);
  }
  
  /// Envía un evento solicitando que se edite la fila Editorial seleccionada.
  public void solicitarEdicion(int indiceFila) {
    // obtener el id de la editorial. Se supone que en el modelo de datos es la columna 0
    Object id = this.panelTabla.getValorCelda(indiceFila, getColumnaID());
    // si se obtuvo un ID no nulo, pedir que se maneje el evento de editar con ese ID
    if (id != null) {
      getEvento().manejar(getRutaEventoSolicitarEdicion(), id);
    }
  }
  
  /// Muestra el formulario con los datos del elemento dado.
  ///
  /// @param elementoVista Elemento vista que se quiere mostrar en el formulario.
  public void mostrarFormulario(EV elementoVista) {
    // primero, preguntar si ya hay una ventana de diálogo abierto
    if (this.dialogo != null) {
      // el diálogo ya está abierto y, por tanto, se pide actualizar su contenido
      this.dialogo.actualizar(elementoVista);
      return;
    }

    // obtener título del diálogo del formulario
    String titulo = getTituloDialogoFormulario(elementoVista);

    // obtener el componente o ventana padre para el diálogo
    Window propietario = SwingUtilities.getWindowAncestor(this);

    // crear el diálogo que contendrá el formulario para crear o editar un elemento
    this.dialogo = new DialogoFormularioElemento<EV>(
      propietario,
      titulo,
      crearFormulario().construir(),
      getEvento()
    ).construir();

    // se muestra el diálogo y toca esperar hasta que el usuario cierre el diálogo
    this.dialogo.mostrar(elementoVista);

    // cuando el usuario cierre el diálogo, anularlo
    this.dialogo = null;
  }
  
  /// Cierra el formulario.
  public void cerrarFormulario() {
    // cerrar el diálogo si fue creado
    if (this.dialogo != null) {
      this.dialogo.cerrar();
    }
  }
  
  /// Obtiene el título que se debe mostrar en el formulario.
  ///
  /// @param elementoVista Elemento vista con los datos para determinar el título.
  /// @return Título que se debe mostrar en el formulario.
  public abstract String getTituloDialogoFormulario(EV elementoVista);

  /// Crea el formulario que se debe mostrar.
  ///
  /// @return Formulario que se debe mostrar.
  public abstract FormularioElementoBase<EV, ?> crearFormulario();
  
  /// Obtiene el índice de la columna que representa el ID en el modelo lógico de la tabla
  /// de datos.
  ///
  /// @return Índice de la columna que representa el ID.
  public int getColumnaID() { return this.columnaID; }
  
  /// Establece el índice de la columna que representa el ID en el modelo lógico de la tabla
  /// de datos.
  ///
  /// @param columnaID Índice de la columna que representa el ID.
  public void setColumnaID(int columnaID) { this.columnaID = columnaID; }
  
  /// Obtiene el panel de la tabla de datos.
  ///
  /// @return El panel de la tabla de datos.
  public PanelTablaLista getPanelTabla() { return this.panelTabla; }
  
  public DialogoFormularioElemento<EV> getDialogo() { return this.dialogo; }

  public void setDialogo(DialogoFormularioElemento<EV> dialogo) { this.dialogo = dialogo; }
  
  /// Obtiene la ruta que manejará el evento de solicitar edición.
  ///
  /// @return Ruta que manejará el evento de solicitar edición.
  public abstract String getRutaEventoSolicitarEdicion();
  
  /// Obtiene el panel que representa una barra de botones de acción.
  public PanelBarraAcciones getBarraAcciones() { return this.barraAcciones; }
  
  /// Obtiene la información de metadatos de las columnas de la tabla de datos. Esta
  /// información se usa para el modelo lógico y la presentación visual de la tabla de datos.
  ///
  /// @return Información de metadatos de las columnas de la tabla de datos.
  public abstract InfoColumnaTabla[] getInfoColumnas();
}
