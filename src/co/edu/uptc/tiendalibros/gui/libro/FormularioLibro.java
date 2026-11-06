//
package co.edu.uptc.tiendalibros.gui.libro;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.PanelCamposBase;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un formulario para crear y editar un libro.
public class FormularioLibro extends FormularioElementoBase<ElementoVistaLibroConFormatos, DatosEntradaLibro> {
  
  /// Panel de campos del libro
  private PanelCamposLibro panelCamposLibro;
  
  /// Panel de campos del formato impreso del libro.
  private PanelCamposFormatoLibro panelFormatoImpreso;
  
  /// Panel de campos del formato digital del libro.
  private PanelCamposFormatoLibro panelFormatoDigital;
  
  /// Lista de los paneles de campos que tiene el formulario. El ? puede representar que es
  /// un panel de libro, un panel de formato de libro, o un panel de autorlibro
  private final List<PanelCamposBase<?, ?>> listaPaneles = new ArrayList<>();
  
  /// Crea un nuevo objeto [FormularioLibro] para un libro.
  ///
  /// @param evento Objeto que maneja eventos.
  public FormularioLibro(
    Evento evento
  ) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    
    super.inicializarComponente("Formulario Libro");
    
    getPanelContenido().setLayout(new BorderLayout());
    getPanelContenido().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
    // el panel contenedor debe permitir barras de desplazamiento en el formulario
    JPanel panelContenedor = crearPanelContenedor();
    JScrollPane scrlContenedor = new JScrollPane(panelContenedor);
    scrlContenedor.setBorder(null);
    scrlContenedor.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    scrlContenedor.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
    
    getPanelContenido().add(scrlContenedor, BorderLayout.CENTER);
  }
  
  private JPanel crearPanelContenedor() {
    JPanel panelContenedor = new JPanel();
    panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
    panelContenedor.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    
    // panel de los campos del libro
    this.panelCamposLibro = (PanelCamposLibro) new PanelCamposLibro(getAsociacionCampos()).construir();
    this.panelCamposLibro.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    this.listaPaneles.add(this.panelCamposLibro);
    panelContenedor.add(this.panelCamposLibro);
    panelContenedor.add(Box.createVerticalStrut(8));
    
    // panel de los campos del formato impreso de libro
    this.panelFormatoImpreso = (PanelCamposFormatoLibro) new PanelCamposFormatoLibro(
      getAsociacionCampos(), TipoFormatoLibro.IMPRESO
    )
      .construir();
    this.panelFormatoImpreso.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    this.listaPaneles.add(this.panelFormatoImpreso);
    panelContenedor.add(this.panelFormatoImpreso);
    panelContenedor.add(Box.createVerticalStrut(8));
    
    // panel de los campos del formato digital del libro
    this.panelFormatoDigital = (PanelCamposFormatoLibro) new PanelCamposFormatoLibro(
      getAsociacionCampos(), TipoFormatoLibro.DIGITAL
    ).construir();
    this.panelFormatoDigital.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    this.listaPaneles.add(this.panelFormatoDigital);
    panelContenedor.add(this.panelFormatoDigital);
    panelContenedor.add(Box.createVerticalStrut(8));
    
    return panelContenedor;
    
  }
  
  @Override
  public void prepararCampos(ElementoVistaLibroConFormatos elementoVista) {
    
    this.panelCamposLibro.prepararCampos(elementoVista);
    this.panelFormatoImpreso.prepararCampos(elementoVista.getFormatoImpreso());
    this.panelFormatoDigital.prepararCampos(elementoVista.getFormatoDigital());
    
  }
  
  @Override
  public ResultadoValidacion validarFormulario() {
    // validar si los campos de cada panel del formulario tienen errores
    ResultadoValidacion errores = new ResultadoValidacion();
    
    for (PanelCamposBase<?, ?> panel : this.listaPaneles) {
      errores.union(panel.validarCampos());
    }
    
    return errores;
  }
  
  @Override
  public DatosEntradaLibro capturarDatos() {
    // ahora, el formulario reunirá todos los datos de sus paneles para formar un libro
    // compuesto consolidado
    Libro libro = this.panelCamposLibro.capturarDatos();
    
    return DatosEntradaLibro.builder()
      .id(libro.getId())
      .isbn(libro.getIsbn())
      .titulo(libro.getTitulo())
      .anioPublicacion(libro.getAnioPublicacion())
      .idCategoria(libro.getIdCategoria())
      .idEditorial(libro.getIdEditorial())
      .imagenLibro(libro.getImagenLibro())
      .formatoImpreso(this.panelFormatoImpreso.capturarDatos())
      .formatoDigital(this.panelFormatoDigital.capturarDatos())
      .construir();
  }
  
  @Override
  protected String describirElemento() {
    LibroConFormatos libroConFormatos = getElementoVista().getElemento();
    return String.format(
      "el libro '%s'",
      libroConFormatos.getLibro()
        .getTitulo()
    );
  }
  
  @Override
  protected String getRutaEventoGuardar() { return ControladorLibro.LIBRO_GUARDAR; }
  
  @Override
  protected String getRutaEventoEliminar() { return ControladorLibro.LIBRO_ELIMINAR; }
}
