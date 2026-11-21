/// 
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.catalogo.GestionExploradorCatalogo;

///
public class ControladorExploradorCatalogo extends ControladorBase {
  
  /// Ruta para el evento de la pantalla de inicio del explorador de libros
  public static final String CATALOGO_INICIO = "exploradorCatalogo.inicio";  
  
  /// Ruta para el evento de listar los libros en una lista de fichas de libros
  public static final String CATALOGO_LISTAR = "exploradorCatalogo.listar";
  
  /// Ruta para el evento de ver los detalles de un libro mostrado en una ficha.
  public static final String CATALOGO_VER_DETALLES = "exploradorCatalogo.verDetalles";
  
  /// Ruta para el evento de agregar el formato de un libro al carrito de compras.
  public static final String CATALOGO_AGREGAR_AL_CARRITO = "exploradorCatalogo.agregarAlCarrito";
  
  /// Panel que muestra el catálogo de libros y que este controlador controla.
  private final PanelExploradorCatalogo panelExplorador;
  
  /// Gestor del explorador del catálogo de libros.
  private final GestionExploradorCatalogo gestorExplorador;
  
  /// Crea el controlador del explorador del catálogo.
  ///
  /// @param enrutador        Enrutador de eventos.
  /// @param panel            Panel que muestra las fichas de los libros.
  /// @param gestorExplorador Gestor que entrega los libros comprables.
  public ControladorExploradorCatalogo(
    EnrutadorEventos enrutador,
    PanelExploradorCatalogo panel,
    GestionExploradorCatalogo gestorExplorador
  ) {
    super(enrutador);
    this.panelExplorador = panel;
    this.gestorExplorador = gestorExplorador;
  }
  
  /// Muestra el panel del explorador del catálogo de libros.
  public void mostrarInicio() {
    listar();
  }
  
  /// Carga la lista de libros y los visualiza en el explorador del catálogo de libros
  public void listar() {
    // cargar la lista de libros
    List<LibroConFormatos> libros = this.gestorExplorador.listar();
    
    List<ElementoVistaFichaLibro> evFichas = new ArrayList<>(libros.size());
    
    for (LibroConFormatos libroConFormatos : libros) {
      evFichas.add(new ElementoVistaFichaLibro(libroConFormatos));
    }
    
    this.panelExplorador.listar(evFichas);
  }
  
  /// Maneja el clic en "Agregar al Carrito" de un formato.
  ///
  /// `elemento` es el id (`Long`) del formato que se quiere agregar.
  ///
  /// TODO: cuando exista el carrito, aquí se agregará el formato. Por ahora solo avisa al
  /// usuario.
  ///
  /// @param elemento Id del formato de libro que se quiere agregar al carrito.
  public void agregarAlCarrito(Object elemento) {
    if (elemento instanceof Long idFormato) {
      mostrarAviso(
        Aviso.informacion(
          String.format(
            "El carrito de compras estará disponible próximamente (formato %d).",
            idFormato
          )
        )
      );
    }
  }
  
  /// Maneja el clic en "Ver Detalles" de un libro.
  ///
  /// `elemento` es el id (`Long`) del libro del que se quieren ver los detalles.
  ///
  /// TODO: la pantalla de detalles se diseña en una iteración posterior.
  ///
  /// @param elemento Id del libro.
  public void verDetalles(Object elemento) {
    if (elemento instanceof Long idLibro) {
      mostrarAviso(
        Aviso.informacion(
          String.format(
            "Los detalles del libro estarán disponibles próximamente (libro %d).",
            idLibro
          )
        )
      );
    }
  }
  
  @Override
  public void registrarEventos() {
    // asociar la ruta del evento cuando se abre el panel del explorador del catálogo de
    // libros
    getEnrutador().registrarEvento(ControladorExploradorCatalogo.CATALOGO_INICIO, (elemento) -> mostrarInicio());
    
    // asociar la ruta del evento para crear la lista de fichas de libros
    getEnrutador().registrarEvento(ControladorExploradorCatalogo.CATALOGO_LISTAR, (elemento) -> listar());
    
    // asociar la ruta del evento cuando se hace clic en "Agregar al Carrito"
    getEnrutador().registrarEvento(
      ControladorExploradorCatalogo.CATALOGO_AGREGAR_AL_CARRITO,
      (elemento) -> agregarAlCarrito(elemento)
    );
    
    // asociar la ruta del evento cuando se hace clic en "Ver Detalles"
    getEnrutador().registrarEvento(
      ControladorExploradorCatalogo.CATALOGO_VER_DETALLES,
      (elemento) -> verDetalles(elemento)
    );
  }
}
