package main.co.edu.uptc.fesad.tpsi.tienda.gui.autor;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.GestionAutor;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.excepciones.ReglaNegocioException;

public class ControladorAutor implements IControlador {
  
  public static final String AUTOR_INICIO = "autor.inicio";
  public static final String AUTOR_NUEVO = "autor.nuevo";
  public static final String AUTOR_EDITAR = "autor.editar";
  public static final String AUTOR_GUARDAR = "autor.guardar";
  public static final String AUTOR_LISTAR = "autor.listar";
  public static final String AUTOR_ELIMINAR = "autor.eliminar";
  
  private PanelAutores panelAutores;
  private GestionAutor gestorAutor;
  
  //
  public ControladorAutor(
    PanelAutores panel,
    GestionAutor gestor
  ) {
    this.panelAutores = panel;
    this.gestorAutor = gestor;
  }
  
  /// Muestra el panel de editoriales.
  public void mostrarInicio() {
    listar();
  }
  
  /// Carga la lista de editoriales y las visualiza en la tabla visual de datos de usuario.
  public void listar() {
    // cargar la lista de editoriales
    List<Autor> autores = this.gestorAutor.listar();
    
    // convertir la lista de editoriales en un array de objetos para el modelo de datos de la
    // tabla.
    
    int cantidadAtributos = 3; // la editorial tiene id y nombre
    
    // array contenedor de dos dimensiones:
    // - dimensión 0: los índices de las filas
    // - dimensión 1: un array de los atributos
    Object[][] filas = new Object[autores.size()][cantidadAtributos];
    
    // recorrer la lista de editoriales y crear un array Object que contenga los atributos, y
    // asignar el array Object a una posición del array contenedor
    for (int i = 0; i < autores.size(); i++) {
      Autor autor = autores.get(i);
      
      filas[i] = new Object[] { autor.getId(), autor.getNombre(), autor.getApellidos() };
      
    }
    
    // pedirle al panel de editoriales que actualice la lista de editoriales.
    this.panelAutores.listar(filas);
  }
  
  public void nuevo() {
    // mostrar un objeto editorial predeterminado
    this.panelAutores.mostrarFormulario(new Autor());
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
    Long idAutor = (Long) id;
    
    // se busca editoriales con ese ID. Se supone que el ID es único
    List<Autor> autores = this.gestorAutor.buscarPorId(idAutor);
    
    // si se encontró la editorial, mostrarla
    if (autores.size() > 0) {
      this.panelAutores.mostrarFormulario(autores.get(0));
      
    } else {
      // si no se encontró la editorial, avisarle al usuario
      this.panelAutores.mostrarError("El autor ya no existe en el repositorio de datos.");
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
    if (!(elemento instanceof Autor)) {
      return;
    }
    
    // obtener la editorial
    Autor autor = (Autor) elemento;
    
    try {
      this.gestorAutor.guardar(autor);
    } catch (ReglaNegocioException excepcion) {
      // en la interfaz gráfica se espera a que el usuario corrija el error
      this.panelAutores.mostrarError(excepcion.getMessage());
      
      return;
    }
    
    // se terminó de guardar: cerrar el formulario y actualizar la tabla de datos
    this.panelAutores.cerrarFormulario();
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
    Long idAutor = (Long) id;
    
    boolean eliminado = this.gestorAutor.eliminar(idAutor);
    
    // Si no se pudo eliminar la editorial, avisar al usuario
    if (!eliminado) {
      this.panelAutores.mostrarError("No se pudo eliminar el autor.");
    }
    
    // continuar cerrando el formulario y actualizando la tabla visual de datos
    
    // limpiar el formulario y actualizar la tabla visual
    this.panelAutores.cerrarFormulario();
    listar();
  }
  
  /// @see main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador#registrarEventos(main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos)
  @Override
  public void registrarEventos(EnrutadorEventos enrutador) {
    // asociar el evento cuando se abre el panel de editoriales.
    enrutador.registrarEvento(
      ControladorAutor.AUTOR_INICIO,
      (elemento) -> mostrarInicio()
    );
    
    enrutador.registrarEvento(
      ControladorAutor.AUTOR_LISTAR,
      (elemento) -> listar()
    );
    
    enrutador
      .registrarEvento(ControladorAutor.AUTOR_NUEVO, (elemento) -> nuevo());
    
    // elemento es el ID de la editorial que se quiere mostrar para editar
    enrutador
      .registrarEvento(ControladorAutor.AUTOR_EDITAR, (elemento) -> editar(elemento));
    
    enrutador
      .registrarEvento(ControladorAutor.AUTOR_GUARDAR, (autor) -> guardar(autor));
    
    // elemento es el ID de la editorial que se quiere eliminar
    enrutador
      .registrarEvento(ControladorAutor.AUTOR_ELIMINAR, (elemento) -> eliminar(elemento));
    
  }
}
