package co.edu.uptc.tiendalibros.gui.autor;

import java.util.List;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.Autor;
import co.edu.uptc.tiendalibros.negocio.GestionAutor;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ReglaNegocioException;

public class ControladorAutor extends ControladorBase {
  
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
    EnrutadorEventos enrutador,
    PanelAutores panel,
    GestionAutor gestor
  ) {
    super(enrutador);
    this.panelAutores = panel;
    this.gestorAutor = gestor;
  }
  
  /// Muestra el panel de autores.
  public void mostrarInicio() {
    listar();
  }
  
  /// Carga la lista de autores y las visualiza en la tabla visual de datos de usuario.
  public void listar() {
    // cargar la lista de autores
    List<Autor> autores = this.gestorAutor.listar();
    
    // convertir la lista de autores en un array de objetos para el modelo de datos de la
    // tabla.
    
    int cantidadAtributos = 3; // el autor tiene id , nombre , apellidos
    
    // array contenedor de dos dimensiones:
    // - dimensión 0: los índices de las filas
    // - dimensión 1: un array de los atributos
    Object[][] filas = new Object[autores.size()][cantidadAtributos];
    
    // recorrer la lista de autores y crear un array Object que contenga los atributos, y
    // asignar el array Object a una posición del array contenedor
    for (int i = 0; i < autores.size(); i++) {
      Autor autor = autores.get(i);
      
      filas[i] = new Object[] { autor.getId(), autor.getNombre(), autor.getApellidos() };
      
    }
    
    // pedirle al panel de autores que actualice la lista de autores.
    this.panelAutores.listar(filas);
  }
  
  public void nuevo() {
    // mostrar un objeto autor predeterminado
    ElementoVistaAutor elementoVista = new ElementoVistaAutor();
    this.panelAutores.mostrarFormulario(elementoVista);
  }
  
  /// Busca el autor con el ID dado y prepara el formulario para editarlo.
  ///
  /// @param id ID del autor que se quiere editar.
  public void editar(Object id) {
    if (id == null || !(id instanceof Long idAutor)) {
      return;
    }
    
    List<Autor> autores = this.gestorAutor.buscarPorId(idAutor);
    
    if (!autores.isEmpty()) {
      ElementoVistaAutor elementoVista = new ElementoVistaAutor(
        autores.get(0),
        new ResultadoValidacion()
      );
      this.panelAutores.mostrarFormulario(elementoVista);
    } else {
      mostrarAviso(
        Aviso.advertencia(
          "El autor ya no existe en el repositorio de datos."
        )
      );
      listar();
    }
  }
  
  public void guardar(Object elemento) {
    if (elemento == null || !(elemento instanceof Autor autor)) {
    return;
  }
    
  String accion = autor.getId() == null ? "creó" : "actualizó";
    
  // GestionAutor.guardar() lanza ReglaNegocioException
  // no se toca su implementación interna — responsabilidad del compañero
    try {
      this.gestorAutor.guardar(autor);
      
      this.panelAutores.cerrarFormulario();
      listar();
      mostrarAviso(
        Aviso.exito(
          String.format(
            "Se %s el autor '%s %s'.",
            accion,
            autor.getNombre(),
            autor.getApellidos()
          )
        )
      );
      
    } catch (ReglaNegocioException excepcion) {
      
      ResultadoValidacion errores = new ResultadoValidacion();
      errores.agregarGeneral(excepcion.getMessage());
      
      ElementoVistaAutor elementoVista = new ElementoVistaAutor(autor, errores);
      this.panelAutores.mostrarFormulario(elementoVista);
      mostrarAviso(Aviso.advertencia(excepcion.getMessage()));
    }
  }
  
  /// Elimina el autor con el id dado.
  ///
  /// @param elemento ID del autor que se quiere eliminar. Es de tipo Long.
  public void eliminar(Object id) {
    if (id == null || !(id instanceof Long idAutor)) {
    return;
   }
    
    boolean eliminado = this.gestorAutor.eliminar(idAutor);
    
   if (eliminado) {
     mostrarAviso(Aviso.exito("Se eliminó el autor."));
   } else {
     mostrarAviso(Aviso.advertencia("No se pudo eliminar el autor."));
    }
    
    this.panelAutores.cerrarFormulario();
    listar();
  }
  
  /// @see co.edu.uptc.tiendalibros.interfaces.IControlador#registrarEventos(co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos)
  @Override
  public void registrarEventos() {
    // asociar el evento cuando se abre el panel de autores.
    getEnrutador().registrarEvento(
      ControladorAutor.AUTOR_INICIO,
      (elemento) -> mostrarInicio()
    );
    
    getEnrutador().registrarEvento(
      ControladorAutor.AUTOR_LISTAR,
      (elemento) -> listar()
    );
    
    getEnrutador()
      .registrarEvento(ControladorAutor.AUTOR_NUEVO, (elemento) -> nuevo());
    
    // elemento es el ID del autor que se quiere mostrar para editar
    getEnrutador()
      .registrarEvento(ControladorAutor.AUTOR_EDITAR, (elemento) -> editar(elemento));
    
    getEnrutador()
      .registrarEvento(ControladorAutor.AUTOR_GUARDAR, (autor) -> guardar(autor));
    
    // elemento es el ID del autor que se quiere eliminar
    getEnrutador()
      .registrarEvento(ControladorAutor.AUTOR_ELIMINAR, (elemento) -> eliminar(elemento));
    
  }
}
