/// 
package main.co.edu.uptc.fesad.tpsi.tienda.gui.autor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.DialogoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.InfoColumnaTabla;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.ModeloTablaLista;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelLista;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;

///
public class PanelAutores extends PanelBase {
  
  /// Índice de la columna que representa el ID en el modelo lógico de la tabla de datos.
  private static final int COLUMNA_ID = 0;
  
  private JButton btnCrear;
  
  private JButton btnEditar;
  
  private PanelLista panelLista;
  
  DialogoBase dialogo;
  
  /// Crea un nuevo objeto [PanelAutores][PanelAutores].
  /// 
  public PanelAutores(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout());
    
    // crear una barra con botones de acciones
    JPanel barraAcciones = new JPanel();
    barraAcciones.setLayout(new FlowLayout(FlowLayout.LEFT, 4, 8));
    
    // crear y configurar el botón crear y su evento para crear un autor
    this.btnCrear = new JButton("Crear Nuevo");
    this.btnCrear.setActionCommand(ControladorAutor.AUTOR_NUEVO);
    this.btnCrear.addActionListener(getEvento());
    
    // editar necesita el ID como dato, por eso se usa listener en lugar de actioncommand
    this.btnEditar = new JButton("Editar");
    this.btnEditar.setEnabled(false);
    this.btnEditar.addActionListener((e) -> solicitarEdicion());
    
    // agregar los botones de acción a la barra
    barraAcciones.add(this.btnCrear);
    barraAcciones.add(this.btnEditar);
    
    add(barraAcciones, BorderLayout.NORTH);
    
    // crear la información de las columnas del modelo de tabla de datos
    InfoColumnaTabla[] infoColumnas = new InfoColumnaTabla[] {
      // Infocolumna (indice, encabezado, identificador, esEditable, esVisible)
      new InfoColumnaTabla(0, "ID", "id", false, true),
      new InfoColumnaTabla(1, "Nombre", "nombre", false, true),
      new InfoColumnaTabla(2, "Apellidos", "apellidos", false, true),
    };
    
    // crear el modelo de tabla de datos
    ModeloTablaLista modeloTabla = new ModeloTablaLista(infoColumnas);
    
    // crear el panel con la tabla de datos
    this.panelLista = (PanelLista) new PanelLista(modeloTabla).construir();
    
    // Asignar las funciones que manejarán los eventos
    
    // asignar la función que manejará el evento cuando el usuario seleccione una fila en la
    // tabla visual
    this.panelLista
      .setFuncionFilaSeleccionada((indiceFila) -> {
        // la tabla visual reporta el índice de la fila seleccionada
        // índice >= 0: el usuario seleccionó una fila
        // índice < 0: el usuario no ha seleccionado ninguna fila
        // entonces, se habilita o deshabilita el botón Editar dependiendo si el usuario
        // seleccionó o no una fila
        this.btnEditar.setEnabled(indiceFila >= 0);
      });
    
    // asignar la función que manejará el evento cuando el usuario haga doble click en una
    // fila de la tabla visual
    this.panelLista.setFuncionFilaActivada((indiceFila) -> {
      // doble click en la fila hace lo mismo que el botón Editar
      solicitarEdicion();
    });
    
    add(this.panelLista, BorderLayout.CENTER);
    
  }
  
  /// Envía un evento solicitando que se edite la fila Autor seleccionada.
  public void solicitarEdicion() {
    // obtener el id del autor. Se supone que en el modelo de datos es la columna 0
    Object id = this.panelLista.getValorCeldaSeleccionada(PanelAutores.COLUMNA_ID);
    // si se obtuvo un ID no nulo, pedir que se maneje el evento de editar con ese ID
    if (id != null) {
      getEvento().manejar(ControladorAutor.AUTOR_EDITAR, id);
    }
  }
  
  /// Actualiza la tabla de autores.
  /// 
  /// @param datos Datos que se quieren visualizar en la tabla.
  public void listar(Object[][] datos) {
    this.panelLista.listar(datos);
  }
  
  /// Muestra el formulario.
  public void mostrarFormulario(Autor autor) {
    // mostrar el título del diálogo
    // ID = null: se pide crear un nuevo autorl para que el repositorio la guarde después
    // ID != null: se pide editar un autor ya existente en el repositorio
    String titulo = autor.getId() == null ? "Crear Autor" : "Editar Autor";
    
    // obtener el componente o ventana padre para el diálogo
    Window propietario = SwingUtilities.getWindowAncestor(this);
    
    // crear el diálogo que contendrá el formulario para crear o editar el autor
    this.dialogo = new DialogoFormularioAutor(propietario, titulo, autor, getEvento()).construir();
    this.dialogo.mostrar();
    
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
  
  /// Muestra un mensaje de error al usuario.
  public void mostrarError(String mensaje) {
    // primero, se determina si el mensaje aparecerá sobre el diálogo o sobre este panel
    Component padre = (this.dialogo != null) ? this.dialogo : this;
    
    // segundo, se muestra el mensaje de error al usuario.
    JOptionPane.showMessageDialog(padre, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
  }
  
  public void mostrarDialogo(String titulo, Object elemento) throws Exception {
    throw new Exception("mostrar dialogo no implementado");
    // // obtiene el objeto que sea un JFrame actuando como la ventana que está mostrando este
    // // panel. Esto reemplaza un getVentanaPrincipal()
    // Window propietario = SwingUtilities.getWindowAncestor(this);
    // // pasando el objeto propietario el diálogo sabe cuál ventana JFrame es su padre
    // this.dialogo = new DialogoFormularioAutor(propietario, titulo, elemento,
    // getEvento()).construir();
    // this.dialogo.mostrar();
    
  }
}
