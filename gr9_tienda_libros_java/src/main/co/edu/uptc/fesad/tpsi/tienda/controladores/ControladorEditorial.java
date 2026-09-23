//
package main.co.edu.uptc.fesad.tpsi.tienda.controladores;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.PanelEditoriales;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.Editorial;

//
public class ControladorEditorial extends ControladorBase {
  private PanelEditoriales panelEditoriales;
  
  public static final String EDITORIAL_INICIO = "editorial.inicio";
  public static final String EDITORIAL_NUEVO = "editorial.nuevo";
  public static final String EDITORIAL_EDITAR = "editorial.editar";
  public static final String EDITORIAL_GUARDAR = "editorial.guardar";
  public static final String EDITORIAL_LISTAR = "editorial.listar";
  
  /// Lista de editoriales que actúa como la base de datos de editoriales.
  private List<Editorial> editoriales;
  
  /// este es un truco para generar ids
  private final AtomicLong contador = new AtomicLong(10);
  
  //
  public ControladorEditorial(
    EnrutadorEventos enrutador,
    PanelEditoriales panel
  ) {
    super(enrutador);
    this.panelEditoriales = panel;
    
    // lista de editoriales para probar la interfaz gráfica
    this.editoriales = new ArrayList<Editorial>();
    
    this.editoriales = new ArrayList<Editorial>() {
      {
        add(new Editorial(1L, "Oceano"));
        add(new Editorial(5L, "Planeta"));
        add(new Editorial(3L, "Pearson"));
        add(new Editorial(8L, "ECOE"));
        add(new Editorial(6L, "McGraw-Hill"));
      }
    };
    
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
    cargarDatos();
    
    // convertir la lista de editoriales en un array de objetos para el modelo de datos de la
    // tabla.
    
    int cantidadAtributos = 2; // la editorial tiene id y nombre
    
    // array contenedor de dos dimensiones:
    // - dimensión 0: los índices
    // - dimensión 1: un array de atributos
    Object[][] filas = new Object[this.editoriales.size()][cantidadAtributos];
    
    // recorrer la lista de editoriales y crear un array Object que contenga los atributos, y
    // asignar el array Object a una posición del array contenedor
    for (int i = 0; i < this.editoriales.size(); i++) {
      Editorial editorial = this.editoriales.get(i);
      
      filas[i] = new Object[] { editorial.getId(), editorial.getNombre() };
    }
    
    // pedirle al panel de editoriales que actualice la lista de editoriales.
    this.panelEditoriales.listar(filas);
  }
  
  public void nuevo() {
    // el formulario mostrará una editorial en blanco.
    this.panelEditoriales.prepararFormulario(null);
  }
  
  /// Busca la editorial con el ID dado y prepara el formulario para editarlo.
  /// 
  /// @param id ID de la editorial que se quiere editar.
  public void editar(Object id) {
    // si el id es nulo, no hay nada qué mostrar
    if (id == null) {
      return;
    }
    
    // obtener el id de la editorial
    Long idEditorial = (Long) id;
    
    // buscar la editorial que concida con el id
    Editorial editorial = null;
    for (Editorial editorialActual : this.editoriales) {
      if (editorialActual.getId()
        .equals(idEditorial)) {
        editorial = editorialActual;
        break;
      }
    }
    
    // si no se encontró la editorial, no hay nada qué mostrar
    if (editorial == null) {
      return;
    }
    
    // si se encontró la editorial, mostrarla
    this.panelEditoriales.prepararFormulario(editorial);
  }
  
  public void guardar(Object elemento) {
    // si el elemento es nulo, no se puede guardar nada
    if (elemento == null) {
      return;
    }
    
    // obtener la editorial que se quiere guardar
    Editorial editorial = (Editorial) elemento;
    
    // creo que aquí van más validaciones
    if (editorial.getNombre()
      .isEmpty()) {
      return;
    }
    
    // todo depende del id
    // si el id es nulo, la editorial no se ha guardado en la base de datos
    if (editorial.getId() == null) {
      // usar el truco para generar un id único
      Long idNuevo = this.contador.incrementAndGet();
      editorial.setId(idNuevo);
      // agregar la editorial a la base de datos
      this.editoriales.add(editorial);
    }
    // el id no es nulo, luego toca reemplazar la editorial en la base de datos
    else {
      for (int i = 0; i < this.editoriales.size(); i++) {
        if (this.editoriales.get(i)
          .getId()
          .equals(editorial.getId())) {
          // reemplazar el objeto editorial
          this.editoriales.set(i, editorial);
          break;
        }
      }
    }
    
    // se terminó de guardar, limpiar el formulario y actualizar la tabla de datos
    this.panelEditoriales.prepararFormulario(null);
    listar();
  }
  
  private void cargarDatos() {
    // TODO: cargar la lista de editoriales con editoriales de prueba
  }
  
}
