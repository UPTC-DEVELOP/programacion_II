//
package co.edu.uptc.tiendalibros.gui.libro;

import javax.swing.JButton;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.InfoColumnaTabla;
import co.edu.uptc.tiendalibros.gui.componentes.PanelGestionElementoBase;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

// ElementoVistaLibroConFormatos, DatosEntradaLibro están en el mismo paquete
/// Representa un panel para la gestión de los libros.
public class PanelLibros extends PanelGestionElementoBase<ElementoVistaLibroConFormatos> {
  
  /// Botón para crear un libro.
  private JButton btnCrear;
  
  /// Botón para editar un libro.
  private JButton btnEditar;
  
  /// Crea un nuevo objeto [PanelLibros] para la gestión de libros.
  public PanelLibros(Evento evento) {
    super(evento);
    // asignar el índice de la columna ID en la tabla de datos
    setColumnaID(0);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente();
    // nota: no invocar setLayout() porque borra todo lo hecho por la clase padre, y no se
    // verá nada.
    
  }
  
  @Override
  public String getTituloDialogoFormulario(ElementoVistaLibroConFormatos elementoVista) {
    return elementoVista.getElemento().getId() == null ? "Crear Libro" : "Editar Libro";
  }

  @Override
  public FormularioElementoBase<ElementoVistaLibroConFormatos, DatosEntradaLibro> crearFormulario() {
    return new FormularioLibro(getEvento());
  }
  
  @Override
  public void configurarBotones() {
    // crear y configurar el botón crear y su evento para crear un libro
    this.btnCrear = new JButton("Crear Nuevo");
    this.btnCrear.setActionCommand(ControladorLibro.LIBRO_CREAR);
    this.btnCrear.addActionListener(getEvento());
    FabricaEstilos.fabrica(this.btnCrear)
      .botonCrear()
      .tamanioPreferido();
    
    // editar necesita el ID como dato, por eso se usa listener en lugar de actioncommand
    this.btnEditar = new JButton("Editar");
    this.btnEditar.setEnabled(false);
    FabricaEstilos.fabrica(this.btnEditar)
      .botonEditar()
      .tamanioPreferido();
    
    this.btnEditar.addActionListener((e) -> {
      int indiceFila = getPanelTabla().getIndiceFilaSeleccionada();
      solicitarEdicion(indiceFila);
    });
    
    // agregar los botones de acción a la barra de acciones
    getBarraAcciones().agregarBotonAccion(this.btnCrear);
    getBarraAcciones().agregarBotonAccion(this.btnEditar);
    
    // asignar la función que manejará el evento cuando el usuario seleccione una fila en la
    // tabla visual
    getPanelTabla()
      .setFuncionFilaSeleccionada((indiceFila) -> {
        // la tabla visual reporta el índice de la fila seleccionada
        // índice >= 0: el usuario seleccionó una fila
        // índice < 0: el usuario no ha seleccionado ninguna fila
        // entonces, se habilita o deshabilita el botón Editar dependiendo si el usuario
        // seleccionó o no una fila
        this.btnEditar.setEnabled(indiceFila >= 0);
      });
  }
  
  @Override
  public String getRutaEventoSolicitarEdicion() {
    // devolver la ruta que el controlador usa para editar
    return ControladorLibro.LIBRO_EDITAR;
  }
  
  @Override
  public InfoColumnaTabla[] getInfoColumnas() {
    // crear la información de las columnas del modelo de tabla de datos
    InfoColumnaTabla[] infoColumnas = new InfoColumnaTabla[] {
      // Infocolumna: (indice, encabezado, identificador, esEditable, esVisible)
      new InfoColumnaTabla(0, "ID", "id", false, true),
      new InfoColumnaTabla(1, "Título", "titulo", false, true),
      new InfoColumnaTabla(2, "ISBN", "isbn", false, true),
    };
    
    return infoColumnas;
  }
  
}
