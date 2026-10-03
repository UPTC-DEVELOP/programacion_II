//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Editorial;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.GestionEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.excepciones.ReglaNegocioException;

//
public class ControladorEditorial implements IControlador {
  

  public static final String EDITORIAL_INICIO = "editorial.inicio";
  public static final String EDITORIAL_NUEVO = "editorial.nuevo";
  public static final String EDITORIAL_EDITAR = "editorial.editar";
  public static final String EDITORIAL_GUARDAR = "editorial.guardar";
  public static final String EDITORIAL_LISTAR = "editorial.listar";
  public static final String EDITORIAL_ELIMINAR = "editorial.eliminar";
  
  private PanelEditoriales panelEditoriales;
  private GestionEditorial gestorEditorial;
  
  //
  public ControladorEditorial(
    PanelEditoriales panel,
    GestionEditorial gestor
  ) {
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
    
    int cantidadAtributos = 2; // la editorial tiene id y nombre
    
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
    
    // pedirle al panel de editoriales que actualice la lista de editoriales.
    this.panelEditoriales.listar(filas);
  }
  
  public void nuevo() {
    // mostrar un objeto editorial predeterminado
    this.panelEditoriales.mostrarFormulario(new Editorial());
  }
  
  /// Busca la editorial con el ID dado y prepara el formulario para editarlo.
  /// 
  /// @param id ID de la editorial que se quiere editar.
  public void editar(Object id) {
    // si el ID es nulo, no se puede editar nada
    if (id == null) {
      return;
    }
    
    // únicamente se espera un id numérico
    if (!(id instanceof Long)) {
      return;
    }
    
    // convertir el id de la editorial
    Long idEditorial = (Long) id;
    
    // se busca editoriales con ese ID. Se supone que el ID es único
    List<Editorial> editoriales = this.gestorEditorial.buscarPorId(idEditorial);
    
    // si se encontró la editorial, mostrarla
    if (editoriales.size() > 0) {
      this.panelEditoriales.mostrarFormulario(editoriales.get(0));
      
    } else {
      // si no se encontró la editorial, avisarle al usuario
      this.panelEditoriales.mostrarError("La editorial ya no existe en el repositorio de datos.");
      listar();
      return;
    }
  }
  
  public void guardar(Object elemento) {
    // si el objeto es nulo, no se puede guardar nada
    if (elemento == null) {
      return;
    }
    
    // únicamente se espera un elemento tipo Editorial
    if (!(elemento instanceof Editorial)) {
      return;
    }
    
    // obtener la editorial
    Editorial editorial = (Editorial) elemento;
    
    try {
      this.gestorEditorial.guardar(editorial);
    } catch (ReglaNegocioException excepcion) {
      // en la interfaz gráfica se espera a que el usuario corrija el error
      this.panelEditoriales.mostrarError(excepcion.getMessage());
      
      return;
    }
    
    // se terminó de guardar: cerrar el formulario y actualizar la tabla de datos
    this.panelEditoriales.cerrarFormulario();
    listar();
  }
  
  /// Elimina una editorial con el id dado.
  /// 
  /// @param elemento ID de la editorial que se quiere eliminar. Es de tipo Long.
  public void eliminar(Object id) {
    // si el ID es nulo, no se puede eliminar nada
    if (id == null) {
      return;
    }
    
    // únicamente se espera un id numérico
    if (!(id instanceof Long)) {
      return;
    }
    
    // convertir el id de la editorial
    Long idEditorial = (Long) id;
    
    boolean eliminado = this.gestorEditorial.eliminar(idEditorial);
    
    // Si no se pudo eliminar la editorial, avisar al usuario
    if (!eliminado) {
      this.panelEditoriales.mostrarError("No se pudo eliminar la editorial.");
    }
    
    // continuar cerrando el formulario y actualizando la tabla visual de datos
    
    // limpiar el formulario y actualizar la tabla visual
    this.panelEditoriales.cerrarFormulario();
    listar();
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador#registrarEventos(main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos)
  @Override
  public void registrarEventos(EnrutadorEventos enrutador) {
    // asociar el evento cuando se abre el panel de editoriales.
    enrutador.registrarEvento(
      ControladorEditorial.EDITORIAL_INICIO,
      (elemento) -> mostrarInicio()
    );
    
    enrutador.registrarEvento(
      ControladorEditorial.EDITORIAL_LISTAR,
      (elemento) -> listar()
    );
    
    enrutador
      .registrarEvento(ControladorEditorial.EDITORIAL_NUEVO, (elemento) -> nuevo());
    
    // elemento es el ID de la editorial que se quiere mostrar para editar
    enrutador
      .registrarEvento(ControladorEditorial.EDITORIAL_EDITAR, (elemento) -> editar(elemento));
    
    enrutador
      .registrarEvento(ControladorEditorial.EDITORIAL_GUARDAR, (editorial) -> guardar(editorial));
    
    // elemento es el ID de la editorial que se quiere eliminar
    enrutador
      .registrarEvento(ControladorEditorial.EDITORIAL_ELIMINAR, (elemento) -> eliminar(elemento));
    
  }
  
  
}
