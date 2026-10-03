//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

/// Representa el panel de lista con botones de acción y la tabla de datos para la
/// interacción con el usuario.
public class PanelLista extends PanelBase {
  
  private ModeloTablaLista modeloTabla;
  private TablaBase tablaLista;

  
  /// Función que maneja el evento cuando el usuario selecciona una fila de la tabla visual.
  private AlSeleccionarFila funcionFilaSeleccionada;
  
  /// Función que maneja el evento cuando usuario activa una fila de la tabla visual.
  private AlActivarFila funcionFilaActivada;
  
  public PanelLista(
    ModeloTablaLista modelo
  ) {
    super();
    this.modeloTabla = modelo;
    this.tablaLista = new TablaBase(this.modeloTabla).construir();
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout());
    
    // indicar que la tabla visual solo seleccionará filas
    // this.tablaLista.setRowSelectionAllowed(true);
    // this.tablaLista.setColumnSelectionAllowed(false);
    this.tablaLista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
    this.tablaLista.getSelectionModel()
      .addListSelectionListener((e) -> {
        // activar cuando la tabla esté reposo, o sea que el usuario no esté editando o moviendo
        // celdas. 
        if (e.getValueIsAdjusting()) { return;}
        
        // invocar la función que manejará el evento de la fila seleccionada
        if (this.funcionFilaSeleccionada != null) {
       // llamar a la función manejadora del evento de fila seleccionada y enviarle el índice de la fila
          this.funcionFilaSeleccionada.aceptar(getIndiceFilaSeleccionada());
        }

      });
    
    // Crear un manejador para los eventos de mouse que el usuario haga sobre una fila
    this.tablaLista.addMouseListener(new MouseAdapter() {
      
      @Override
      public void mouseClicked(MouseEvent e) {
        // se captura el evento si el usuario hace doble click con el botón izquierdo del ratón
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
          // la tabla visual reporta el índice de fila donde el usuario hizo click.
          // pero, el índice visual no siempre es igual al índice del modelo lógico
          int indiceFilaVisual = PanelLista.this.tablaLista.rowAtPoint(e.getPoint());
          // si hubo una fila seleccionada y si hay función manejadora
          if (indiceFilaVisual >= 0 && PanelLista.this.funcionFilaActivada != null) {
            // el índice de la fila visual debe ser convertido al índice correspondiente del modelo
            // lógico de datos de la tabla
            int indiceFila = PanelLista.this.tablaLista.convertRowIndexToModel(indiceFilaVisual);
            // llamar a la función que manejará el evento
            PanelLista.this.funcionFilaActivada.aceptar(indiceFila);
          }
        }
      }
    });
    
    // la tabla siempre debe alojarse dentro de un JScrollPane
    JScrollPane scrpn = new JScrollPane(this.tablaLista);
    add(scrpn, BorderLayout.CENTER);
  }
  
  /// Actualiza la tabla que le muestra la lista de objetos de negocio al usuario.
  /// 
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.modeloTabla.setDatos(datos);
  }
  
  /// Obtiene el valor de los datos lógicos de la celda especificada.
  /// 
  /// @param indiceFila    Índice de la fila
  /// @param indiceColumna Índice de la columna.
  /// @return El valor de la celda en el modelo lógico de la tabla.
  public Object getValorCelda(int indiceFila, int indiceColumna) {
    return this.modeloTabla.getValueAt(indiceFila, indiceColumna);
  }
  
  /// Obtiene el valor lógico de la columna especificada de la fila actualmente
  /// seleccionada.
  /// 
  /// @param indiceColumna Índice de la columna.
  /// @return El valor de la celda en el modelo lógico de la tabla.
  public Object getValorCeldaSeleccionada(int indiceColumna) {
    int fila = getIndiceFilaSeleccionada();
    return (fila < 0) ? null : this.modeloTabla.getValueAt(fila, indiceColumna);
  }
  
  /// Obtiene el índice de la fila seleccionada respecto al modelo lógico de datos de la
  /// tabla.
  /// 
  /// @return índice de la fila seleccionada en el modelo lógico. -1 si no hay selección.
  public int getIndiceFilaSeleccionada() {
    int indicefilaVista = this.tablaLista.getSelectedRow();
    // devolver -1 : si el usuario no tiene seleccionado una fila
    // devolver el índice visual pero convertido al índice del modelo lógico.
    return (indicefilaVista < 0) ? -1 : this.tablaLista.convertRowIndexToModel(indicefilaVista);
  }
  

  public AlSeleccionarFila getFuncionFilaSeleccionada() { return this.funcionFilaSeleccionada; }
  
  public void setFuncionFilaSeleccionada(AlSeleccionarFila funcionFilaSeleccionada) {
    this.funcionFilaSeleccionada = funcionFilaSeleccionada;
  }
  
  public void setFuncionFilaActivada(AlActivarFila funcion) { this.funcionFilaActivada = funcion; }
  
}
