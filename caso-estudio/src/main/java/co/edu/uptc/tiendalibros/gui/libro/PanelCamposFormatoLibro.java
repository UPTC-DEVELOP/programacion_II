package co.edu.uptc.tiendalibros.gui.libro;

import java.awt.GridBagLayout;
import java.math.BigDecimal;
import java.util.Optional;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import co.edu.uptc.tiendalibros.gui.componentes.AsociacionCamposFormulario;
import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.componentes.PanelCamposBase;
import co.edu.uptc.tiendalibros.gui.componentes.RestriccionesCelda;
import co.edu.uptc.tiendalibros.gui.componentes.ValidadorFormulario;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.FormatosDinero;
import co.edu.uptc.tiendalibros.modelo.Dinero;
import co.edu.uptc.tiendalibros.modelo.EstadoEnCatalogo;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.modelo.TipoIVA;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

public class PanelCamposFormatoLibro
  extends PanelCamposBase<ElementoVistaFormatoLibro, Optional<DatosEntradaFormatoLibro>> {
  
  /// Casilla para indicar si el libro se ofrece en este formato.
  private JCheckBox chkOfrecido;
  
  /// Campo para mostrar el tipo de formato.
  private JTextField txtTipoFormato;
  
  /// Campo para mostrar el inventario de este formato
  private JTextField txtInventario;
  
  /// Campo para mostrar la cantidad de páginas
  private JTextField txtCantidadPaginas;
  
  /// Campo para mostrar el precio base
  private JFormattedTextField txtPrecioBase;
  
  /// Campo para mostrar el precio de venta
  private JTextField txtPrecioVenta;
  
  /// Opción IVA 19%.
  private JRadioButton rbIva19;
  
  /// Opción IVA 5%.
  private JRadioButton rbIva5;
  
  /// Campo
  private ButtonGroup grupoOpcionesIva;
  
  // Panel para el grupo de opciones del IVA
  private PanelBase panelOpcionesIva;
  
  /// Componente para mostrar errores del inventario
  private JLabel notaErrorInventario;
  
  /// Componente para mostrar errores de la cantidad de páginas
  private JLabel notaErrorCantidadPaginas;
  
  /// Componente para mostrar errores del precio base
  private JLabel notaErrorPrecioBase;
  
  /// Componente para mostrar errores del tipo de IVA
  private JLabel notaErrorTipoIVA;
  
  /// Tipo de formato de libro que este panel maneja
  private TipoFormatoLibro tipoFormato;
  
  /// Crea un nuevo objeto [PanelCamposFormatoLibro] para el formato de libro especificado.
  public PanelCamposFormatoLibro(
    AsociacionCamposFormulario asociacionCampos,
    TipoFormatoLibro tipoFormato
  ) {
    super(asociacionCampos);
    this.tipoFormato = tipoFormato;
  }
  
  @Override
  protected void inicializarComponente() {
    // determinar el título según el tipo de formato de libro para distinguirlos visualmente
    String nombreFormato = switch (this.tipoFormato) {
      case IMPRESO -> "Impreso";
      case DIGITAL -> "Digital";
    };
    String tituloPanel = String.format("Formato %s", nombreFormato);
    
    FabricaEstilos.fabrica(this)
      .bordeSeccion(tituloPanel);
    setBackground(null);
    setLayout(new GridBagLayout());
    
    // crear la casilla para ofrecer el formato. Al cambiar su estado seleccionado, se
    // habilitan o deshabilitan los campos del formato
    String textoCasilla = String.format("Ofrecer el libro en formato %s", nombreFormato.toLowerCase());
    this.chkOfrecido = new JCheckBox(textoCasilla);
    this.chkOfrecido.addActionListener((e) -> habilitarCampos(this.chkOfrecido.isSelected()));
    
    // crear el campo de tipo de formato de libro
    this.txtTipoFormato = new JTextField();
    FabricaEstilos.fabrica(this.txtTipoFormato)
      .soloLectura()
      .transparente(0);
    
    // crear el campo de inventario
    this.txtInventario = new JTextField();
    FabricaEstilos.fabrica(this.txtInventario)
      .campoTexto(10);
    
    // crear el campo de cantidad de páginas
    this.txtCantidadPaginas = new JTextField();
    FabricaEstilos.fabrica(this.txtCantidadPaginas)
      .campoTexto(10);
    
    // crear el campo de precio base
    this.txtPrecioBase = new JFormattedTextField();
    this.txtPrecioBase.setValue(BigDecimal.ZERO);
    this.txtPrecioBase.setFormatterFactory(FormatosDinero.crearFabricaFormatoPredeterminado());
    FabricaEstilos.fabrica(this.txtPrecioBase)
      .campoTexto(20);
    
    // crear el campo de precio final
    this.txtPrecioVenta = new JTextField();
    FabricaEstilos.fabrica(this.txtPrecioVenta)
      .campoTexto(20)
      .soloLectura();
    
    // crear el grupo de opciones del IVA
    this.rbIva19 = new JRadioButton("19%", true);
    this.rbIva5 = new JRadioButton("5%", false);
    this.grupoOpcionesIva = new ButtonGroup();
    this.grupoOpcionesIva.add(this.rbIva19);
    this.grupoOpcionesIva.add(this.rbIva5);
    this.panelOpcionesIva = new PanelBase().construir();
    this.panelOpcionesIva.add(this.rbIva19);
    this.panelOpcionesIva.add(this.rbIva5);
    
    // crear las notas de errores para cada campo
    this.notaErrorInventario = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorInventario)
      .notaError();
    this.notaErrorCantidadPaginas = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorCantidadPaginas)
      .notaError();
    this.notaErrorPrecioBase = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorPrecioBase)
      .notaError();
    this.notaErrorTipoIVA = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorTipoIVA)
      .notaError();
    
    // asociar atributos, campos y notas de error
    
    asociarCampo(obtenerAtributoFormato("inventario"), this.txtInventario, this.notaErrorInventario);
    asociarCampo(
      obtenerAtributoFormato("cantidadPaginas"),
      this.txtCantidadPaginas,
      this.notaErrorCantidadPaginas
    );
    asociarCampo(obtenerAtributoFormato("precioBase"), this.txtPrecioBase, this.notaErrorPrecioBase);
    asociarCampo(obtenerAtributoFormato("tipoIVA"), this.panelOpcionesIva, this.notaErrorTipoIVA);
    
    // ubicar los componentes en un layout
    ubicarComponentes();
    
    // agregar eventos cuando el usuario cambie el precio base
    this.txtPrecioBase.getDocument()
      .addDocumentListener(new DocumentListener() {
        
        @Override
        public void removeUpdate(DocumentEvent e) {
          actualizarPrecioVenta();
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {
          actualizarPrecioVenta();
        }
        
        @Override
        public void changedUpdate(DocumentEvent e) {
          actualizarPrecioVenta();
        }
      });
    
    // agregar eventos cuando el usuario cambie el tipo de IVA
    this.rbIva19.addActionListener((e) -> actualizarPrecioVenta());
    this.rbIva5.addActionListener((e) -> actualizarPrecioVenta());
    
  }
  
  /// Obtiene un nombre compuesto para asociar un los campos de un atributo según el formato
  /// del libro.
  ///
  /// Ejemplo: formato impreso: "impreso.inventario"
  ///
  /// Ejemplo: formato digital: "digital.precioBase"
  private String obtenerAtributoFormato(String atributo) {
    String nombreFormato = switch (this.tipoFormato) {
      case IMPRESO -> "impreso";
      case DIGITAL -> "digital";
    };
    
    return String.format("%s.%s", nombreFormato, atributo);
  }
  
  @Override
  public void prepararCampos(ElementoVistaFormatoLibro elementoVista) {
    setElementoVista(elementoVista);
    
    // obtener el objeto formato de libro que es el elemento principal que maneja este panel
    FormatoLibro formatoLibro = elementoVista.getElemento();
    
    // marcar la casilla según si el libro ofrece el formato, y habilitar o deshabilitar los
    // campos según corresponda
    this.chkOfrecido.setSelected(elementoVista.esOfrecido());
    habilitarCampos(elementoVista.esOfrecido());
    
    // mostrar los atributos del formato del libro
    this.txtTipoFormato.setText(
      formatoLibro.getTipoFormato()
        .toString()
    );
    this.txtInventario.setText(String.valueOf(formatoLibro.getInventario()));
    this.txtCantidadPaginas.setText(String.valueOf(formatoLibro.getCantidadPaginas()));
    this.txtPrecioBase.setValue(
      formatoLibro.getPrecioBase()
        .getCantidad()
    );
    this.txtPrecioVenta.setText(
      formatoLibro.getTipoIVA()
        .calcularPrecioFinal(formatoLibro.getPrecioBase())
        .formatear()
    );
    
    // establecer la opción de IVA según el formato del libro
    TipoIVA tipoIVA = formatoLibro.getTipoIVA();
    this.rbIva19.setSelected(tipoIVA == TipoIVA.IVA_19);
    this.rbIva5.setSelected(tipoIVA == TipoIVA.IVA_5);
    
  }
  
  /// Habilita o deshabilita los campos que el usuario llena solo cuando el libro ofrece el
  /// formato.
  ///
  /// @param habilitar `true` para habilitar los campos, `false` para deshabilitarlos.
  private void habilitarCampos(boolean habilitar) {
    this.txtInventario.setEnabled(habilitar);
    this.txtCantidadPaginas.setEnabled(habilitar);
    this.txtPrecioBase.setEnabled(habilitar);
    this.rbIva19.setEnabled(habilitar);
    this.rbIva5.setEnabled(habilitar);
    this.txtPrecioVenta.setEnabled(habilitar);
  }
  
  public void ubicarComponentes() {
    // crear las etiquetas de los campos
    // JLabel lblTipoFormato = new JLabel("Tipo Formato:");
    JLabel lblInventario = new JLabel("Inventario:");
    JLabel lblCantidadPaginas = new JLabel("Páginas:");
    JLabel lblPrecioBase = new JLabel("Precio Base:");
    JLabel lblTipoIva = new JLabel("Tipo IVA:");
    JLabel lblPrecioVenta = new JLabel("Precio Venta:");
    
    // crear una restricción base para todos los componentes del formulario
    RestriccionesCelda base = new RestriccionesCelda().insets(4, 4, 0, 4)
      .anclarIzquierda();
    
    // ubicar la etiqueta tipo de formato
    // agregar(
    // lblTipoFormato,
    // base.clonar()
    // .gridy(0)
    // .gridx(0)
    // );
    // // ubicar el campo tipo de formato de libro
    // agregar(
    // this.txtTipoFormato,
    // base.clonar()
    // .gridy(0)
    // .gridx(1)
    // .expandirHorizontal()
    // );
    
    // ubicar la casilla de ofrecer el formato, que ocupa toda la primera fila
    agregar(
      this.chkOfrecido,
      base.clonar()
        .gridy(0)
        .gridx(0)
        .gridwidth(4)
    );
    
    // ubicar la etiqueta inventario
    agregar(
      lblInventario,
      base.clonar()
        .gridy(1)
        .gridx(0)
    );
    // ubicar el campo inventario
    agregar(
      this.txtInventario,
      base.clonar()
        .gridy(1)
        .gridx(1)
    );
    
    // ubicar la etiqueta páginas
    agregar(
      lblCantidadPaginas,
      base.clonar()
        .gridy(1)
        .gridx(2)
    );
    // ubicar el campo páginas
    agregar(
      this.txtCantidadPaginas,
      base.clonar()
        .gridy(1)
        .gridx(3)
        .expandirHorizontal()
    );
    
    // ubicar la nota de errores del inventario
    agregar(
      this.notaErrorInventario,
      base.clonar()
        .gridy(2)
        .gridx(1)
    );
    // ubicar la nota de errores de las páginas
    agregar(
      this.notaErrorCantidadPaginas,
      base.clonar()
        .gridy(2)
        .gridx(3)
    );
    
    // ubicar la etiqueta de precio base
    agregar(
      lblPrecioBase,
      base.clonar()
        .gridy(3)
        .gridx(0)
    );
    // ubicar el campo precio base
    agregar(
      this.txtPrecioBase,
      base.clonar()
        .gridy(3)
        .gridx(1)
    );
    // ubicar la nota de error de precio base
    agregar(
      this.notaErrorPrecioBase,
      base.clonar()
        .gridy(4)
        .gridx(1)
        .expandirHorizontal()
    );
    
    // ubicar la etiqueta de tipo de IVA
    agregar(
      lblTipoIva,
      base.clonar()
        .gridy(5)
        .gridx(0)
    );
    // ubicar el panel de opciones del IVA
    agregar(
      this.panelOpcionesIva,
      base.clonar()
        .gridy(5)
        .gridx(1)
        .expandirHorizontal()
    );
    // ubicar la nota de errores del IVA
    agregar(
      this.notaErrorTipoIVA,
      base.clonar()
        .gridy(6)
        .gridx(1)
        .expandirHorizontal()
    );
    
    // ubicar la etiqueta de precio de venta
    agregar(
      lblPrecioVenta,
      base.clonar()
        .gridy(7)
        .gridx(0)
    );
    // ubicar el campo de precio de venta
    agregar(
      this.txtPrecioVenta,
      base.clonar()
        .gridy(7)
        .gridx(1)
        .expandirHorizontal()
    );
    
  }
  
  @Override
  public ResultadoValidacion validarCampos() {
    
    // si el libro no ofrece este formato, sus campos no se deben validar
    if (this.chkOfrecido.isSelected() == false) {
      return new ResultadoValidacion();
    }
    
    return new ValidadorFormulario()
      .numeroEntero(obtenerAtributoFormato("inventario"), "inventario", this.txtInventario)
      .numeroEntero(obtenerAtributoFormato("cantidadPaginas"), "páginas", this.txtCantidadPaginas)
      .numeroDinero(obtenerAtributoFormato("precioBase"), "precio base", this.txtPrecioBase)
      .getErrores();
  }
  
  @Override
  public Optional<DatosEntradaFormatoLibro> capturarDatos() {
    // si el libro no ofrece este formato, no hay datos que capturar
    if (!this.chkOfrecido.isSelected()) {
      return Optional.empty();
    }
    
    // obtener el formato del libro el cual es el objeto principal manejado por el panel
    FormatoLibro formatoLibro = getElementoVista().getElemento();
    // obtener el id del formato del libro
    Long idFormatoLibro = formatoLibro.getId();
    
    // obtener el tipo de formato
    TipoFormatoLibro tipoFormato = formatoLibro.getTipoFormato();
    
    // obtener el estado
    EstadoEnCatalogo estadoLibroCatalogo = formatoLibro.getEstadoEnCatalogo();
    
    // obtener el inventario
    int inventario = Integer.valueOf(
      this.txtInventario.getText()
        .trim()
    );
    
    // obtener la cantidad de páginas
    int cantidadPaginas = Integer.valueOf(
      this.txtCantidadPaginas.getText()
        .trim()
    );
    
    // obtener el precio base
    Dinero precioBase = Dinero.desde(this.txtPrecioBase.getValue());
    
    // obtener el tipo del IVA
    TipoIVA tipoIVA = obtenerTipoIva();
    
    // obtener un nuevo objeto formato de libro a partir de estos valores
    DatosEntradaFormatoLibro datos = DatosEntradaFormatoLibro.builder()
      .id(idFormatoLibro)
      .idLibro(formatoLibro.getIdLibro())
      .tipoFormato(tipoFormato)
      .inventario(inventario)
      .cantidadPaginas(cantidadPaginas)
      .precioBase(precioBase)
      .tipoIVA(tipoIVA)
      .estadoLibroCatalogo(estadoLibroCatalogo)
      .construir();
    
    return Optional.of(datos);
  }
  
  /// Obtiene el tipo de IVA seleccionado.
  private TipoIVA obtenerTipoIva() {
    // Devolver el tipo de IVA o un valor predeterminado
    if (this.rbIva19.isSelected()) {
      return TipoIVA.IVA_19;
    }
    if (this.rbIva5.isSelected()) {
      return TipoIVA.IVA_5;
    }
    // el valor predeterminado es 19%
    return TipoIVA.IVA_19;
  }
  
  /// Calcula el precio de venta cuando el usuario cambia el precio base y lo muestra
  /// al usuario.
  private void actualizarPrecioVenta() {
    
    // crear un objeto Dinero
    Dinero precioBase = Dinero.desde(this.txtPrecioBase.getValue());
    // obtener el tipo de IVA
    TipoIVA tipoIVA = obtenerTipoIva();
    // calcular el precio de venta
    Dinero precioVenta = precioBase.sumar(tipoIVA.calcularIVA(precioBase));
    this.txtPrecioVenta.setText(precioVenta.formatear());
    
  }
  
}
