//
package co.edu.uptc.tiendalibros.gui.libro;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.GestionCategoria;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.editorial.GestionEditorial;
import co.edu.uptc.tiendalibros.negocio.libro.GestionLibro;

/// Representa un controlador para los eventos y las solicitudes de un libro.
public class ControladorLibro extends ControladorBase {
  
  /// Ruta para el evento de la pantalla de inicio de los libros.
  public static final String LIBRO_INICIO = "libro.inicio";
  
  /// Ruta para el evento de crear un libro.
  public static final String LIBRO_CREAR = "libro.nuevo";
  
  /// Ruta para el evento de editar un libro.
  public static final String LIBRO_EDITAR = "libro.editar";
  
  /// Ruta para el evento de guardar un libro.
  public static final String LIBRO_GUARDAR = "libro.guardar";
  
  /// Ruta para el evento de listar en una tabla de datos los libros.
  public static final String LIBRO_LISTAR = "libro.listar";
  
  /// Ruta para el evento de eliminar un libro.
  public static final String LIBRO_ELIMINAR = "libro.eliminar";
  
  /// Panel de libros que este controlador controla.
  private PanelLibros panelLibros;
  
  /// Gestor de los libros
  private GestionLibro gestorLibro;
  
  /// Gestor de las categorías.
  private GestionCategoria gestorCategoria;
  
  /// Gestor de las editoriales.
  private GestionEditorial gestorEditorial;
  
  /// Crea un nuevo objeto [ControladorLibro][ControladorLibro].
  ///
  /// @param enrutador       Enrutador para comunicar eventos.
  /// @param panel           Panel para la gestión de libros.
  /// @param gestorLibro     Gestor de las operaciones de los libros.
  /// @param gestorCategoria Gestor de las operaciones de las categorías.
  /// @param gestorEditorial Gestor de las operaciones de las editoriales.
  public ControladorLibro(
    EnrutadorEventos enrutador,
    PanelLibros panel,
    GestionLibro gestorLibro,
    GestionCategoria gestorCategoria,
    GestionEditorial gestorEditorial
  ) {
    super(enrutador);
    this.panelLibros = panel;
    this.gestorLibro = gestorLibro;
    this.gestorCategoria = gestorCategoria;
    this.gestorEditorial = gestorEditorial;
  }
  
  /// Muestra el panel de libros.
  public void mostrarInicio() {
    listar();
  }
  
  /// Carga la lista de libros y los visualiza en la tabla visual de datos de usuario.
  public void listar() {
    // cargar la lista de libros
    List<Libro> libros = this.gestorLibro.listar();
    
    // convertir la lista de libros en un array de objetos para el modelo de datos de la tabla
    
    // un libro tiene 3 atributos (id, titulo,isbn)
    int cantidadAtributos = 3;
    
    // array contenedor de dos dimensiones:
    // - dimensión 0: los índices de las filas
    // - dimensión 1: un array de los atributos
    Object[][] filas = new Object[libros.size()][cantidadAtributos];
    
    // recorrer la lista de libros y crear un array Object que contenga los atributos, y
    // asignar el array Object a una posición del array contenedor
    for (int i = 0; i < libros.size(); i++) {
      Libro libro = libros.get(i);
      
      filas[i] = new Object[] { libro.getId(), libro.getTitulo(), libro.getIsbn() };
    }
    
    // pedirle al panel que actualice la tabla de libros.
    this.panelLibros.listar(filas);
  }
  
  /// Muestra el formulario para crear un libro.
  public void crear() {
    this.panelLibros.mostrarFormulario(crearVista(new LibroConFormatos(), new ResultadoValidacion()));
  }
  
  /// Busca el libro con el ID dado y prepara el formulario para editarlo.
  ///
  /// @param id ID del libro que se quiere editar.
  public void editar(Long id) {
    // si el ID es nulo, no se puede editar nada
    if (id == null) {
      return;
    }
    
    // se busca el libro con sus formatos. Se supone que el ID es único
    Optional<LibroConFormatos> libroConFormatos = this.gestorLibro.buscarLibroConFormatos(id);
    
    // si no se encontró el libro se debe avisar al usuario
    if (libroConFormatos.isEmpty()) {
      mostrarAviso(Aviso.advertencia("El libro ya no existe en el repositorio de datos."));
      // actualizar la lista tabla de datos
      listar();
      return;
    }
    
    // como apenas se abre el formulario no hay errores de validación
    this.panelLibros.mostrarFormulario(crearVista(libroConFormatos.get(), new ResultadoValidacion()));
  }
  
  /// Crea un objeto vista que se envía al lormulario.
  ///
  /// @param libroConFormatos Libro con sus dos formatos que se quiere mostrar.
  /// @param errores          Errores de validación que se quieren mostrar. Vacío si no hay
  ///                         errores.
  /// @return Objeto vista con el libro, sus formatos, las categorías y las editoriales.
  private ElementoVistaLibroConFormatos crearVista(LibroConFormatos libroConFormatos, ResultadoValidacion errores) {
    return new ElementoVistaLibroConFormatos(
      libroConFormatos.getLibro(),
      new ElementoVistaFormatoLibro(libroConFormatos.getFormatoImpreso()),
      new ElementoVistaFormatoLibro(libroConFormatos.getFormatoDigital()),
      this.gestorCategoria.listar(),
      this.gestorEditorial.listar(),
      errores
    );
  }
  
  /// Guarda el libro con sus formatos en el repositorio de datos.
  ///
  /// @param datosEntrada DTO con los datos ingresados por el usuario en el formulario.
  public void guardar(DatosEntradaLibro datosEntrada) {
    if (datosEntrada == null) {
      return;
    }
    
    // transformar los datos de entrada en el libro con sus formatos que recibe el gestor
    LibroConFormatos libroConFormatos = datosEntrada.aLibroConFormatos();
    
    String accion = libroConFormatos.getId() == null ? "creó" : "actualizó";
    Respuesta<LibroConFormatos> respuesta = this.gestorLibro.guardar(libroConFormatos);
    
    switch (respuesta) {
      case RespuestaExitosa<LibroConFormatos> exitosa -> {
        // el gestor devuelve el libro guardado con sus formatos y los IDs asignados: se muestra
        // sin cerrar el formulario
        LibroConFormatos guardado = exitosa.getElemento()
          .orElse(libroConFormatos);
        this.panelLibros.mostrarFormulario(crearVista(guardado, new ResultadoValidacion()));
        listar();
        mostrarAviso(
          Aviso.exito(
            String.format(
              "Se %s el libro '%s'.",
              accion,
              guardado.getLibro()
                .getTitulo()
            )
          )
        );
      }
      
      case RespuestaFallida<LibroConFormatos> fallida -> {
        // mostrar de nuevo lo que escribió el usuario, junto con los errores
        this.panelLibros.mostrarFormulario(crearVista(libroConFormatos, fallida.getErrores()));
        mostrarAviso(Aviso.advertencia(fallida.getMensaje()));
      }
    }
  }
  
  /// Elimina un libro con el ID especificado.
  ///
  /// @param id ID del libro que se quiere eliminar.
  ///
  public void eliminar(Long id) {
    // si el ID es nulo no hacer nada
    if (id == null) {
      return;
    }
    
    // intentar eliminar el libro
    Respuesta<Libro> respuesta = this.gestorLibro.eliminar(id);
    // procesar la respuesta
    
    // avisar al usuario el resultado de la operación eliminar
    switch (respuesta) {
      // si la respuesta devuelve el libro eliminado, entonces se puede mostrar el título del
      // libro eliminado
      case RespuestaExitosa<Libro> _       -> {
        String titulo = respuesta.getElemento()
          .isPresent()
            ? respuesta.getElemento()
              .get()
              .getTitulo()
            : "";
        
        // cerrar el formulario
        this.panelLibros.cerrarFormulario();
        // actualizar la tabla de datos
        listar();
        mostrarAviso(Aviso.exito(String.format("Se eliminó el libro '%s'.", titulo)));
      }
      
      case RespuestaFallida<Libro> fallida -> {
        // mostrar los errores que ocurrieron al eliminar
        mostrarAviso(Aviso.advertencia(fallida.getMensaje()));
      }
    }
    
  }
  
  @Override
  public void registrarEventos() {
    // asociar la ruta del evento cuando se abre el panel de libros
    getEnrutador().registrarEvento(ControladorLibro.LIBRO_INICIO, (elemento) -> mostrarInicio());
    
    // asociar la ruta del evento para listar los libros en la tabla de datos
    getEnrutador().registrarEvento(ControladorLibro.LIBRO_LISTAR, (elemento) -> listar());
    
    // asociar la ruta del evento cuando se desee crear un libro
    getEnrutador().registrarEvento(ControladorLibro.LIBRO_CREAR, (elemento) -> crear());
    
    // asociar la ruta del evento cuando se desee editar un libro
    getEnrutador().registrarEvento(ControladorLibro.LIBRO_EDITAR, (id) -> editar((Long) id));
    
    // asociar la ruta del evento cuando se desee guardar el libro
    getEnrutador().registrarEvento(ControladorLibro.LIBRO_GUARDAR, (datos) -> guardar((DatosEntradaLibro) datos));
    
    // asociar la ruta del evento cuando se desee eliminar el libro
    getEnrutador().registrarEvento(ControladorLibro.LIBRO_ELIMINAR, (id) -> eliminar((Long) id));
    
  }
  
}
