//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.ManejadorEvento;

/// Representa el panel de lista con botones de acción y la tabla de datos para la
/// interacción con el usuario.
public class PanelLista extends PanelBase {
  
  private ModeloTablaLista modeloTabla;
  private TablaBase tablaLista;
  
  /// Función que maneja el evento cuando el usuario da click en el botón de crear nuevo.
  private ManejadorEvento funcionClickNuevo;
  
  /// Función que maneja el evento cuando el usuario selecciona una fila de la tabla visual.
  private EnFilaCliqueable funcionFilaSeleccionada;
  
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
    
    // crear una barra con botones de acciones
    JPanel barraAcciones = new JPanel();
    barraAcciones.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
    
    JButton btnCrear = new JButton("Crear Nuevo");
    btnCrear.addActionListener((e) -> {
      // invocar al manejador del evento cuando el usuario hace click en el botón crear nuevo
      if (this.funcionClickNuevo != null) {
        // La función que maneja el evento tiene esta forma:
        // void aceptar(Object elemento);
        // pero, la función que maneje el evento click no necesita información de un elemento, por
        // eso
        // se envía null.
        this.funcionClickNuevo.aceptar(null);
      }
    });
    barraAcciones.add(btnCrear);
    
    add(barraAcciones, BorderLayout.NORTH);
    
    // crear la tabla visual que muestra el listado de elementos
    this.tablaLista.setRowSelectionAllowed(true);
    this.tablaLista.setColumnSelectionAllowed(false);
    
    this.tablaLista.getSelectionModel()
      .addListSelectionListener((e) -> {
        // activar cuando la tabla esté reposo, o sea que el usuario no esté editando o moviendo
        // celdas. Sólo cuando el usuario seleccionó la fila
        if (!e.getValueIsAdjusting() && this.tablaLista.getSelectedRow() >= 0) {
          // obtener el índice de la fila seleccionada
          int indiceFila = this.tablaLista.getSelectedRow();
          // llamar a la función manejadora del evento de fila seleccionada y enviarle el índice de
          // la fila
          this.funcionFilaSeleccionada.aceptar(indiceFila);
        }
      });
    
    JScrollPane scrpn = new JScrollPane(this.tablaLista);
    add(scrpn, BorderLayout.CENTER);
  }
  
  /// Actualiza la tabla que le muestra la lista de objetos de negocio al usuario.
  /// 
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.modeloTabla.setDatos(datos);
  }
  
  /// Obtiene el valor de los datos lǵoicos de la celda especificada.
  /// 
  /// @param indiceFila    Índice de la fila
  /// @param indiceColumna Índice de la columna.
  /// @return El valor de la celda en el modelo lógico de la tabla.
  public Object getValorCelda(int indiceFila, int indiceColumna) {
    return this.modeloTabla.getValueAt(indiceFila, indiceColumna);
  }
  
  public ManejadorEvento getFuncionClickNuevo() { return this.funcionClickNuevo; }
  
  public void setFuncionClickNuevo(ManejadorEvento funcionClickNuevo) { this.funcionClickNuevo = funcionClickNuevo; }
  
  public EnFilaCliqueable getFuncionFilaSeleccionada() { return this.funcionFilaSeleccionada; }
  
  public void setFuncionFilaSeleccionada(EnFilaCliqueable funcionFilaSeleccionada) {
    this.funcionFilaSeleccionada = funcionFilaSeleccionada;
  }
  
}
