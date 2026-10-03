package main.co.edu.uptc.fesad.tpsi.tienda.categoria;

import java.util.List;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.GestionCategoria;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.excepciones.ReglaNegocioException;

public class ControladorCategoria
  implements IControlador {
  
  public static final String CATEGORIA_INICIO = "categoria.inicio";
  
  public static final String CATEGORIA_NUEVO = "categoria.nuevo";
  
  public static final String CATEGORIA_EDITAR = "categoria.editar";
  
  public static final String CATEGORIA_GUARDAR = "categoria.guardar";
  
  public static final String CATEGORIA_LISTAR = "categoria.listar";
  
  public static final String CATEGORIA_ELIMINAR = "categoria.eliminar";
  
  private PanelCategorias panelCategorias;
  
  private GestionCategoria gestorCategoria;
  
  public ControladorCategoria(
    PanelCategorias panel,
    GestionCategoria gestor
  ) {
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
    
    this.panelCategorias.mostrarFormulario(
      new Categoria()
    );
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
    
    if (categorias.size() > 0) {
      
      this.panelCategorias.mostrarFormulario(
        categorias.get(0)
      );
      
    } else {
      
      this.panelCategorias.mostrarError(
        "La categoría ya no existe en el repositorio de datos."
      );
      
      listar();
    }
  }
  
  public void guardar(Object elemento) {
    
    if (elemento == null) {
      return;
    }
    
    if (!(elemento instanceof Categoria)) {
      return;
    }
    
    Categoria categoria = (Categoria) elemento;
    
    try {
      
      this.gestorCategoria.guardar(
        categoria
      );
      
    } catch (ReglaNegocioException excepcion) {
      
      this.panelCategorias.mostrarError(
        excepcion.getMessage()
      );
      
      return;
    }
    
    this.panelCategorias.cerrarFormulario();
    listar();
  }
  
  public void eliminar(Object id) {
    
    if (id == null) {
      return;
    }
    
    if (!(id instanceof Long)) {
      return;
    }
    
    Long idCategoria = (Long) id;
    
    boolean eliminado = this.gestorCategoria.eliminar(
      idCategoria
    );
    
    if (!eliminado) {
      
      this.panelCategorias.mostrarError(
        "No se pudo eliminar la categoría."
      );
    }
    
    this.panelCategorias.cerrarFormulario();
    listar();
  }
  
  @Override
  public void registrarEventos(
    EnrutadorEventos enrutador
  ) {
    
    enrutador.registrarEvento(
      ControladorCategoria.CATEGORIA_INICIO,
      (elemento) -> mostrarInicio()
    );
    
    enrutador.registrarEvento(
      ControladorCategoria.CATEGORIA_LISTAR,
      (elemento) -> listar()
    );
    
    enrutador.registrarEvento(
      ControladorCategoria.CATEGORIA_NUEVO,
      (elemento) -> nuevo()
    );
    
    enrutador.registrarEvento(
      ControladorCategoria.CATEGORIA_EDITAR,
      (elemento) -> editar(elemento)
    );
    
    enrutador.registrarEvento(
      ControladorCategoria.CATEGORIA_GUARDAR,
      (categoria) -> guardar(categoria)
    );
    
    enrutador.registrarEvento(
      ControladorCategoria.CATEGORIA_ELIMINAR,
      (elemento) -> eliminar(elemento)
    );
  }
}