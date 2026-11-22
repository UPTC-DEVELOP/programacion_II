//
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.componentes.RestriccionesCelda;
import co.edu.uptc.tiendalibros.gui.estilos.AlmacenImagenesCubiertas;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.estilos.PaletaColores;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa una ficha que muestra los datos de un libro en el catálogo.
public class FichaLibroCatalogo extends PanelBase {
  
  // campos y componentes del libro
  private JLabel campoCubierta;
  private JTextField campoTitulo;
  private JTextField campoIsbn;
  private JLabel campoCategoria;
  private JLabel campoEditorial;
  private JLabel campoAnio;
  
  private JLabel lblFormatoImpreso;
  private JLabel lblPrecioVentaImpreso;
  private JLabel campoPrecioImpreso;
  private JButton btnAgregarImpreso;
  
  private JLabel lblFormatoDigital;
  private JLabel lblPrecioVentaDigital;
  private JLabel campoPrecioDigital;
  private JButton btnAgregarDigital;
  
  private JButton btnVerDetalles;
  
  public FichaLibroCatalogo(Evento evento) {
    super(evento);
  }
  
  @Override
  protected void inicializarComponente() {
    super.inicializarComponente();
    
    setLayout(new BorderLayout(14, 0));
    // crear borders para la ficha
    setBorder(
      BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PaletaColores.BORDE_SECCION.color(), 1),
        BorderFactory.createEmptyBorder(8, 8, 8, 8)
      )
    );
    
    // alinear el contenido de la ficha a la izquierda
    setAlignmentX(LEFT_ALIGNMENT);
    
    // agregar la cubierta del libro
    this.campoCubierta = new JLabel("Sin cubierta", SwingConstants.CENTER);
    FabricaEstilos.fabrica(this.campoCubierta)
      .cubiertaLibro();
    
    add(this.campoCubierta, BorderLayout.WEST);
    
    // construir el panel de datos del libro y agregarlo a la ficha
    add(construirPanelDatosLibro(), BorderLayout.CENTER);
  }
  
  @Override
  public Dimension getMaximumSize() {
    // ancho libre. altura la necesaria
    return new Dimension(Integer.MAX_VALUE, getPreferredSize().height);
  }
  
  private JPanel construirPanelDatosLibro() {
    // crear los campos que muestran los datoos del libro
    this.campoTitulo = new JTextField();
    FabricaEstilos.fabrica(this.campoTitulo)
      .tituloLibroFicha();
    
    this.campoIsbn = new JTextField();
    FabricaEstilos.fabrica(this.campoIsbn)
      .campoTextoNoEditable()
      .campoSinBorde();
    
    this.campoAnio = new JLabel();
    FabricaEstilos.fabrica(this.campoAnio)
      .datoFicha();
    
    this.campoCategoria = new JLabel();
    FabricaEstilos.fabrica(this.campoCategoria)
      .datoFicha();
    
    this.campoEditorial = new JLabel();
    FabricaEstilos.fabrica(this.campoEditorial)
      .datoFicha();
    
    this.campoPrecioImpreso = new JLabel();
    FabricaEstilos.fabrica(this.campoPrecioImpreso)
      .datoFicha();
    
    this.campoPrecioDigital = new JLabel();
    FabricaEstilos.fabrica(this.campoPrecioDigital)
      .datoFicha();
    
    // crear los botones de acción
    this.btnAgregarImpreso = new JButton("Agregar al carrito");
    FabricaEstilos.fabrica(this.btnAgregarImpreso)
      .botonAgregarAlCarrito();
    
    this.btnAgregarDigital = new JButton("Agregar al carrito");
    FabricaEstilos.fabrica(this.btnAgregarDigital)
      .botonAgregarAlCarrito();
    
    this.btnVerDetalles = new JButton("Ver Detalles");
    FabricaEstilos.fabrica(this.btnVerDetalles)
      .botonVerDetalles();
    
    PanelBase panelCampos = new PanelBase().construir();
    panelCampos.setLayout(new GridBagLayout());
    panelCampos.setOpaque(false);
    ubicarCampos(panelCampos);
    
    return panelCampos;
  }
  
  /// Muestra en la ficha los datos del elemento vista y configura los botones de acción.
  public void mostrar(ElementoVistaFichaLibro ficha) {
    // mostrar la imagen de la cubierta
    mostrarCubierta(ficha);
    // se muestra el valor de un atributo en su campo respectivo
    this.campoTitulo.setText(ficha.getTitulo());
    this.campoIsbn.setText(ficha.getIsbn());
    this.campoAnio.setText(ficha.getAnioPublicacion());
    this.campoCategoria.setText(ficha.getNombreCategoria());
    this.campoEditorial.setText(ficha.getNombreEditorial());
    
    // mostrar los campos de cada formato
    mostrarFormatoImpreso(ficha);
    mostrarFormatoDigital(ficha);
    
    // mostrar el botón ver detalles teniendo en cuenta el ID del libro
    Long idLibro = ficha.getIdLibro();
    this.btnVerDetalles.addActionListener(
      (e) -> getEvento().manejar(ControladorExploradorCatalogo.CATALOGO_VER_DETALLES, idLibro)
    );
  }
  
  /// Muestra los campos del formato impreso si el libro ofrece el formato impreso.
  private void mostrarFormatoImpreso(ElementoVistaFichaLibro ficha) {
    // si el libro ofrece el formato impreso, mostrar el precio y el botón de agregar
    if (ficha.tieneFormatoImpreso() == false) {
      this.lblFormatoImpreso.setVisible(false);
      this.lblPrecioVentaImpreso.setVisible(false);
      this.campoPrecioImpreso.setVisible(false);
      this.btnAgregarImpreso.setVisible(false);
      // no hacer más
      return;
    }
    // mostrar el precio
    this.campoPrecioImpreso.setText(ficha.getTextoPrecioImpreso());
    // obtener el id del formato. Un formato es lo que en realidad se compra
    Long idFormato = ficha.getIdFormatoImpreso();
    // crear el manejador del evento click en agregar el formato al carrito de compras
    this.btnAgregarImpreso.addActionListener(
      (e) -> getEvento().manejar(ControladorExploradorCatalogo.CATALOGO_AGREGAR_AL_CARRITO, idFormato)
    );
  }
  
  /// Muestra los campos del formato digital si el libro ofrece el formato digital.
  private void mostrarFormatoDigital(ElementoVistaFichaLibro ficha) {
    // si el libro ofrece el formato digital, mostrar el precio y el botón de agregar
    if (ficha.tieneFormatoDigital() == false) {
      this.lblFormatoDigital.setVisible(false);
      this.lblPrecioVentaDigital.setVisible(false);
      this.campoPrecioDigital.setVisible(false);
      this.btnAgregarDigital.setVisible(false);
      // no hacer más
      return;
    }
    // mostrar el precio
    this.campoPrecioDigital.setText(ficha.getTextoPrecioDigital());
    // obtener el id del formato. Un formato es lo que en realidad se compra
    Long idFormato = ficha.getIdFormatoDigital();
    // crear el manejador del evento click en agregar el formato al carrito de compras
    this.btnAgregarDigital.addActionListener(
      (e) -> getEvento().manejar(ControladorExploradorCatalogo.CATALOGO_AGREGAR_AL_CARRITO, idFormato)
    );
  }
  
  /// Ubica los campos en el panel especificado.
  private void ubicarCampos(JPanel panel) {
    
    // crear las etiquetas de los campos
    JLabel lblIsbn = new JLabel("ISBN:");
    FabricaEstilos.fabrica(lblIsbn)
      .etiquetaFicha();
    JLabel lblAnio = new JLabel("Año:");
    FabricaEstilos.fabrica(lblAnio)
      .etiquetaFicha();
    JLabel lblCategoria = new JLabel("Categoría:");
    FabricaEstilos.fabrica(lblCategoria)
      .etiquetaFicha();
    JLabel lblEditorial = new JLabel("Editorial:");
    FabricaEstilos.fabrica(lblEditorial)
      .etiquetaFicha();
    
    this.lblFormatoImpreso = new JLabel("Formato Impreso:");
    FabricaEstilos.fabrica(this.lblFormatoImpreso)
      .etiquetaFicha();
    this.lblPrecioVentaImpreso = new JLabel("Precio de Venta:");
    FabricaEstilos.fabrica(this.lblPrecioVentaImpreso)
      .etiquetaFicha();
    
    this.lblFormatoDigital = new JLabel("Formato Digital:");
    FabricaEstilos.fabrica(this.lblFormatoDigital)
      .etiquetaFicha();
    this.lblPrecioVentaDigital = new JLabel("Precio de Venta:");
    FabricaEstilos.fabrica(this.lblPrecioVentaDigital)
      .etiquetaFicha();
    
    // fila 0: título
    agregar(
      panel,
      this.campoTitulo,
      new RestriccionesCelda()
        .gridy(0)
        .gridx(0)
        .gridwidth(5)
        .anclarIzquierda()
    );
    
    // fila 1: ISBN y año
    agregar(
      panel,
      lblIsbn,
      new RestriccionesCelda().gridx(0)
        .gridy(1)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.campoIsbn,
      new RestriccionesCelda().gridx(1)
        .gridy(1)
        .anclarIzquierda()
    );
    agregar(
      panel,
      lblAnio,
      new RestriccionesCelda().gridx(2)
        .gridy(1)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.campoAnio,
      new RestriccionesCelda().gridx(3)
        .gridy(1)
        .anclarIzquierda()
    );
    
    // fila 2: categoría y editorial
    agregar(
      panel,
      lblCategoria,
      new RestriccionesCelda().gridx(0)
        .gridy(2)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.campoCategoria,
      new RestriccionesCelda().gridx(1)
        .gridy(2)
        .anclarIzquierda()
    );
    agregar(
      panel,
      lblEditorial,
      new RestriccionesCelda().gridx(2)
        .gridy(2)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.campoEditorial,
      new RestriccionesCelda().gridx(3)
        .gridy(2)
        .anclarIzquierda()
    );
    
    // fila 3: formato impreso
    agregar(
      panel,
      this.lblFormatoImpreso,
      new RestriccionesCelda().gridx(0)
        .gridy(3)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.lblPrecioVentaImpreso,
      new RestriccionesCelda().gridx(1)
        .gridy(3)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.campoPrecioImpreso,
      new RestriccionesCelda().gridx(2)
        .gridy(3)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.btnAgregarImpreso,
      new RestriccionesCelda().gridx(3)
        .gridy(3)
        .anclarIzquierda()
    );
    // fila 4: formato digital
    agregar(
      panel,
      this.lblFormatoDigital,
      new RestriccionesCelda().gridx(0)
        .gridy(4)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.lblPrecioVentaDigital,
      new RestriccionesCelda().gridx(1)
        .gridy(4)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.campoPrecioDigital,
      new RestriccionesCelda().gridx(2)
        .gridy(4)
        .anclarIzquierda()
    );
    agregar(
      panel,
      this.btnAgregarDigital,
      new RestriccionesCelda().gridx(3)
        .gridy(4)
        .anclarIzquierda()
    );
    
    // fila 5: ver detalles
    agregar(
      panel,
      this.btnVerDetalles,
      new RestriccionesCelda().gridx(0)
        .gridy(5)
        .gridwidth(5)
    );
    
    // columna 4: agregar una columna de relleno para que el gridbaglayout alinee los campos a
    // la izquierda. Sin esa columna de rellena los campos quedan centrados
    agregar(
      panel,
      new JLabel(),
      new RestriccionesCelda()
        .gridy(0)
        .gridx(4)
        .weightx(1.0)
    );
  }
  
  /// Agrega el componente al panel usando el objeto [RestriccionesCelda].
  ///
  /// Es útil porque a veces se olvida convertir el objeto [RestriccionesCelda] al objeto
  /// [GridBagConstraints] requerido para un [GridBagLayout].
  /// 
  /// @param panel      Panel en el que se quiere agregar el componente.
  /// @param componente Componente que se quiere agregar al panel.
  /// @param rc         Objeto [RestriccionesCelda] para ubicar el componente.
  private void agregar(JPanel panel, JComponent componente, RestriccionesCelda rc) {
    rc.insets(2, 4, 2, 8);
    panel.add(componente, rc.aGBC());
  }
  
  /// Muestra la cubierta del libro.
  /// 
  /// Si la imagen de la cubierta no existe o no se puede cargar, se deja el texto
  /// "Sin cubierta".
  private void mostrarCubierta(ElementoVistaFichaLibro ficha) {
    AlmacenImagenesCubiertas.mostrarEnEtiqueta(this.campoCubierta, ficha.getNombreImagenCubierta());
  }
}
