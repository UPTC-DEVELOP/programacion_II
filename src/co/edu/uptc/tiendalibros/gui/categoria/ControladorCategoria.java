package co.edu.uptc.tiendalibros.gui.categoria;

import java.util.List;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.componentes.ControladorBase;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.negocio.GestionCategoria;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ReglaNegocioException;

public class ControladorCategoria
  extends ControladorBase {
  
  public static final String CATEGORIA_INICIO = "categoria.inicio";
  
  public static final String CATEGORIA_NUEVO = "categoria.nuevo";
  
  public static final String CATEGORIA_EDITAR = "categoria.editar";
  
  public static final String CATEGORIA_GUARDAR = "categoria.guardar";
  
  public static final String CATEGORIA_LISTAR = "categoria.listar";
  
  public static final String CATEGORIA_ELIMINAR = "categoria.eliminar";
  
  private PanelCategorias panelCategorias;
  
  private GestionCategoria gestorCategoria;
  
  public ControladorCategoria(
    EnrutadorEventos enrutador,
    PanelCategorias panel,
    GestionCategoria gestor
  ) {
    super(enrutador);
    this.panelCategorias = panel;
    this.gestorCategoria = gestor;
  }
  
  public void mostrarInicio() {
    listar();
  }
  
  public void listar() {
    
    List<Categoria> categorias = this.gestorCategoria.listar();
    
    int cantidadAtributos = 4;
    
    Object[][] filas = new Object[categorias.size()][cantidadAtributos];
    
    for (int i = 0; i < categorias.size(); i++) {
      
      Categoria categoria = categorias.get(i);
      
      filas[i] = new Object[] {
        categoria.getId(),
        categoria.getNombre(),
        categoria.getDescripcion(),
        categoria.getCodigo()
      };
    }
    
    this.panelCategorias.listar(filas);
  }
  
  public void nuevo() {
    
    ElementoVistaCategoria elementoVista = new ElementoVistaCategoria();
    this.panelCategorias.mostrarFormulario(elementoVista);
  }
  
  public void editar(Object id) {
    
    if (id == null) {
      return;
    }
    
    if (!(id instanceof Long)) {
      return;
    }
    
    Long idCategoria = (Long) id;
    
    List<Categoria> categorias = this.gestorCategoria.buscarPorId(
      idCategoria
    );
    
    if (!categorias.isEmpty()) {
      ElementoVistaCategoria elementoVista = new ElementoVistaCategoria(
        categorias.get(0),
        new ResultadoValidacion()
      );
      this.panelCategorias.mostrarFormulario(elementoVista);
    } else {
      mostrarAviso(
        Aviso.advertencia(
          "La categoría ya no existe en el repositorio de datos."
        )
      );
      listar();
    }
  }
  
  public void guardar(Object elemento) {
    
    if (elemento == null || !(elemento instanceof Categoria categoria)) {
      return;
    }
    
    String accion = categoria.getId() == null ? "creó" : "actualizó";
    
    try {
      this.gestorCategoria.guardar(categoria);
      
      this.panelCategorias.cerrarFormulario();
      listar();
      mostrarAviso(
        Aviso.exito(
          String.format("Se %s la categoría '%s'.", accion, categoria.getNombre())
        )
      );
      
    } catch (ReglaNegocioException excepcion) {
      
      ResultadoValidacion errores = new ResultadoValidacion();
      errores.agregarGeneral(excepcion.getMessage());
      
      ElementoVistaCategoria elementoVista = new ElementoVistaCategoria(
        categoria,
        errores
      );
      this.panelCategorias.mostrarFormulario(elementoVista);
      mostrarAviso(Aviso.advertencia(excepcion.getMessage()));
    }
  }
  
  public void eliminar(Object id) {
    if (id == null || !(id instanceof Long idCategoria)) {
      return;
    }
    
    boolean eliminado = this.gestorCategoria.eliminar(idCategoria);
    
    if (eliminado) {
      mostrarAviso(Aviso.exito("Se eliminó la categoría."));
    } else {
      mostrarAviso(Aviso.advertencia("No se pudo eliminar la categoría."));
    }
    
    this.panelCategorias.cerrarFormulario();
    listar();
  }
  
  @Override
  public void registrarEventos() {
    
    getEnrutador().registrarEvento(
      ControladorCategoria.CATEGORIA_INICIO,
      (elemento) -> mostrarInicio()
    );
    
    getEnrutador().registrarEvento(
      ControladorCategoria.CATEGORIA_LISTAR,
      (elemento) -> listar()
    );
    
    getEnrutador().registrarEvento(
      ControladorCategoria.CATEGORIA_NUEVO,
      (elemento) -> nuevo()
    );
    
    getEnrutador().registrarEvento(
      ControladorCategoria.CATEGORIA_EDITAR,
      (elemento) -> editar(elemento)
    );
    
    getEnrutador().registrarEvento(
      ControladorCategoria.CATEGORIA_GUARDAR,
      (categoria) -> guardar(categoria)
    );
    
    getEnrutador().registrarEvento(
      ControladorCategoria.CATEGORIA_ELIMINAR,
      (elemento) -> eliminar(elemento)
    );
  }
}