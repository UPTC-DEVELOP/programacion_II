//
package co.edu.uptc.tiendalibros.gui.editorial;

import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.editorial.GestionEditorial;

/// Representa un controlador para los eventos y las solicitudes de una editorial.
public class ControladorEditorial extends ControladorBase {
  
  /// Ruta para el evento de la pantalla de inicio de las editoriales.
  public static final String EDITORIAL_INICIO = "editorial.inicio";
  
  /// Ruta para el evento de crear una editorial.
  public static final String EDITORIAL_CREAR = "editorial.nuevo";
  
  /// Ruta para el evento de editar una editorial.
  public static final String EDITORIAL_EDITAR = "editorial.editar";
  
  /// Ruta para el evento de guardar una editorial.
  public static final String EDITORIAL_GUARDAR = "editorial.guardar";
  
  /// Ruta para el evento de listar en una tabla de datos las editoriales.
  public static final String EDITORIAL_LISTAR = "editorial.listar";
  
  /// Ruta para el evento de eliminar una editorial.
  public static final String EDITORIAL_ELIMINAR = "editorial.eliminar";
  
  /// Panel de editoriales que este controlador controla.
  private PanelEditoriales panelEditoriales;
  
  /// Gestor de editoriales.
  private GestionEditorial gestorEditorial;
  
  /// Crea un nuevo objeto [ControladorEditorial][ControladorEditorial].
  ///
  /// @param enrutador Enrutador para comunicar eventos.
  /// @param panel     Panel para la gestión de editoriales.
  /// @param gestor    Gestor de las operaciones de los editoriales.
  public ControladorEditorial(
    EnrutadorEventos enrutador,
    PanelEditoriales panel,
    GestionEditorial gestor
  ) {
    super(enrutador);
    this.panelEditoriales = panel;
    this.gestorEditorial = gestor;
  }
  
  /// Muestra el panel de editoriales.
  public void mostrarInicio() {
    listar();
  }
  
  /// Carga la lista de editoriales y las visualiza en la tabla visual de datos de usuario.
  public void listar() {
    // cargar la lista de editoriales
    List<Editorial> editoriales = this.gestorEditorial.listar();
    
    // convertir la lista de editoriales en un array de objetos para el modelo de datos de la
    // tabla.
    
    // una editorial tiene 2 atributos: (id, nombre)
    int cantidadAtributos = 2;
    
    // array contenedor de dos dimensiones:
    // - dimensión 0: los índices de las filas
    // - dimensión 1: un array de los atributos
    Object[][] filas = new Object[editoriales.size()][cantidadAtributos];
    
    // recorrer la lista de editoriales y crear un array Object que contenga los atributos, y
    // asignar el array Object a una posición del array contenedor
    for (int i = 0; i < editoriales.size(); i++) {
      Editorial editorial = editoriales.get(i);
      
      filas[i] = new Object[] { editorial.getId(), editorial.getNombre() };
    }
    
    // pedirle al panel que actualice la tabla de editoriales
    this.panelEditoriales.listar(filas);
  }
  
  /// Muestra el formulario para crear una editorial.
  public void crear() {
    // mostrar un objeto editorial predeterminado
    ElementoVistaEditorial elementoVista = new ElementoVistaEditorial();
    this.panelEditoriales.mostrarFormulario(elementoVista);
  }
  
  /// Busca la editorial con el ID dado y prepara el formulario para editarla.
  ///
  /// @param id ID de la editorial que se quiere editar.
  public void editar(Long id) {
    // si el ID es nulo, no se puede editar nada
    if (id == null) {
      return;
    }
    
    // se busca editoriales con ese ID. Se supone que el ID es único
    Optional<Editorial> editorial = this.gestorEditorial.buscarPorId(id);
    
    // si no se encontró la editorial se debe avisar al usuario
    if (editorial.isEmpty()) {
      mostrarAviso(Aviso.advertencia("La editorial ya no existe en el repositorio de datos."));
      // actualizar la lista tabla de datos
      listar();
      return;
    }
    
    // crear el elemento vista con la editorial. Como apenas se abre el formulario no hay
    // errores de validación
    ElementoVistaEditorial elementoVista = new ElementoVistaEditorial(editorial.get(), new ResultadoValidacion());
    
    this.panelEditoriales.mostrarFormulario(elementoVista);
    
  }
  
  /// Guarda la editorial en el repositorio de datos.
  ///
  /// @param editorial Editorial que se quiere guardar.
  public void guardar(Editorial editorial) {
    // si el objeto es nulo, no se puede guardar nada
    if (editorial == null) {
      return;
    }
    
    // texto que indica si se crea o se actualiza la editorial
    String accion = editorial.getId() == null ? "creó" : "actualizó";
    
    // intentar guardar la editorial
    Respuesta<Editorial> respuesta = this.gestorEditorial.guardar(editorial);
    
    // avisar al usuario el resultado de la operación de guardar
    switch (respuesta) {
      case RespuestaExitosa<Editorial> _       -> {
        // se terminó de guardar: cerrar el formulario
        this.panelEditoriales.cerrarFormulario();
        // actualizar la tabla de datos
        listar();
        // mostrar el aviso exitoso
        mostrarAviso(Aviso.exito(String.format("Se %s la editorial '%s'.", accion, editorial.getNombre())));
      }
      case RespuestaFallida<Editorial> fallida -> {
        // crear un elemento vista con la editorial y los errores de validación
        ElementoVistaEditorial elementoVista = new ElementoVistaEditorial(
          editorial,
          fallida.getErrores()
        );
        this.panelEditoriales.mostrarFormulario(elementoVista);
        mostrarAviso(Aviso.advertencia(fallida.getMensaje()));
      }
      
    }
    
  }
  
  /// Elimina una editorial con el ID especificado.
  ///
  /// @param id ID de la editorial que se quiere eliminar.
  public void eliminar(Long id) {
    // si el ID es nulo no hacer nada
    if (id == null) {
      return;
    }
    
    // intentar eliminar la editorial
    Respuesta<Editorial> respuesta = this.gestorEditorial.eliminar(id);
    
    switch (respuesta) {
      case RespuestaExitosa<Editorial> _       -> {
        // si la respuesta devuelve la editorial eliminada, entonces se puede mostrar el nombre de
        // la editorial eliminada
        String nombre = respuesta.getElemento()
          .isPresent()
            ? respuesta.getElemento()
              .get()
              .getNombre()
            : "";
        mostrarAviso(Aviso.exito(String.format("Se eliminó la editorial '%s'.", nombre)));
        
        // cerrar el formulario
        this.panelEditoriales.cerrarFormulario();
        // actualizar la tabla de datos
        listar();
      }
      case RespuestaFallida<Editorial> fallida -> {
        // crear el elemento vista con la editorial y los errores
        // no debería suceder, pero si la respuesta envía un objeto vacío, se crea una
        // editorial nueva para enviar al formulario
        Editorial editorialConError = respuesta.getElemento()
          .orElse(new Editorial());
        
        ElementoVistaEditorial elementoVista = new ElementoVistaEditorial(
          editorialConError,
          fallida.getErrores()
        );
        // actualizar la vista y mostrar errores
        this.panelEditoriales.mostrarFormulario(elementoVista);
        mostrarAviso(Aviso.advertencia(fallida.getMensaje()));
        
      }
    }
    
  }
  
  @Override
  public void registrarEventos() {
    
    // asociar la ruta del evento cuando se abre el panel de editoriales.
    getEnrutador().registrarEvento(
      ControladorEditorial.EDITORIAL_INICIO,
      (elemento) -> mostrarInicio()
    );
    
    // asociar la ruta del evento para listar las editoriales en la tabla de datos
    getEnrutador().registrarEvento(
      ControladorEditorial.EDITORIAL_LISTAR,
      (elemento) -> listar()
    );
    
    // asociar la ruta del evento cuando se desee crear una editorial
    getEnrutador()
      .registrarEvento(ControladorEditorial.EDITORIAL_CREAR, (elemento) -> crear());
    
    // elemento es el ID de la editorial que se quiere mostrar para editar
    getEnrutador()
      .registrarEvento(ControladorEditorial.EDITORIAL_EDITAR, (id) -> editar((Long) id));
    
    // asociar la ruta del evento cuando se desee guardar una editorial
    getEnrutador()
      .registrarEvento(ControladorEditorial.EDITORIAL_GUARDAR, (editorial) -> guardar((Editorial) editorial));
    
    // elemento es el ID de la editorial que se quiere eliminar
    getEnrutador()
      .registrarEvento(ControladorEditorial.EDITORIAL_ELIMINAR, (id) -> eliminar((Long) id));
    
  }
  
}
