//
package main.co.edu.uptc.fesad.tpsi.tienda.controladores;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.PanelEditoriales;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.Editorial;
import main.co.edu.uptc.fesad.tpsi.tienda.persistencia.IRepositorioEditorial;

//
public class ControladorEditorial extends ControladorBase {
  

  public static final String EDITORIAL_INICIO = "editorial.inicio";
  public static final String EDITORIAL_NUEVO = "editorial.nuevo";
  public static final String EDITORIAL_EDITAR = "editorial.editar";
  public static final String EDITORIAL_GUARDAR = "editorial.guardar";
  public static final String EDITORIAL_LISTAR = "editorial.listar";
  public static final String EDITORIAL_ELIMINAR = "editorial.eliminar";
  
  private PanelEditoriales panelEditoriales;
  private IRepositorioEditorial repositorio;
  
  //
  public ControladorEditorial(
    EnrutadorEventos enrutador,
    PanelEditoriales panel,
    IRepositorioEditorial repositorio
  ) {
    super(enrutador);
    this.panelEditoriales = panel;
    this.repositorio = repositorio;
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.controladores.ControladorBase#inicializar()
  @Override
  public void inicializar() {
    // TODO Auto-generated method stub
    
  }
  
  /// Muestra el panel de editoriales.
  public void mostrarInicio() {
    this.panelEditoriales.setVisible(true);
    listar();
  }
  
  /// Carga la lista de editoriales y las visualiza en la tabla visual de datos de usuario.
  public void listar() {
    // cargar la lista de editoriales
    List<Editorial> editoriales = this.repositorio.listar();
    
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
    // enviando un objeto nulo, el formulario mostrará una editorial predeterminada en blanco.
    this.panelEditoriales.prepararFormulario(null);
  }
  
  /// Busca la editorial con el ID dado y prepara el formulario para editarlo.
  /// 
  /// @param id ID de la editorial que se quiere editar.
  public void editar(Object id) {
    // si el ID es nulo, no se puede editar nada
    if (id == null) {
      return;
    }
    
    // convertir el id de la editorial
    Long idEditorial = (Long) id;
    
    // se busca editoriales con ese ID. Se supone que el ID es único
    List<Editorial> editoriales = this.repositorio.buscarPorId(idEditorial);
    
    // si se encontró la editorial, mostrarla
    if (editoriales.size() > 0) {
      this.panelEditoriales.prepararFormulario(editoriales.get(0));
    } else {
      this.panelEditoriales.prepararFormulario(null);
    }
  }
  
  public void guardar(Object elemento) {
    // si el objeto es nulo, no se puede guardar nada
    if (elemento == null) {
      return;
    }
    
    // obtener la editorial
    Editorial editorial = (Editorial) elemento;
    
    try {
      this.repositorio.guardar(editorial);
    } catch (IllegalArgumentException ex) {
      // TODO: hacer algo con las posibles excepciones
      return;
    }
    
    // se terminó de guardar, limpiar el formulario y actualizar la tabla de datos
    // null hace que el formulario se limpie
    this.panelEditoriales.prepararFormulario(null);
    listar();
  }
  
  /// Elimina una editorial con el id dado.
  /// 
  /// @param elemento ID de la editorial que se quiere eliminar. Es de tipo Long.
  public void eliminar(Object elemento) {
    // si el objeto es nulo, no se puede eliminar nada
    if (elemento == null) {
      return;
    }
    
    boolean eliminado = this.repositorio.eliminar((Long) elemento);
    
    // limpiar el formulario y actualizar la tabla visual
    // null hace que el formulario se limpie
    if (eliminado) {
    this.panelEditoriales.prepararFormulario(null);
      listar();
    }
  }
  
  
}
