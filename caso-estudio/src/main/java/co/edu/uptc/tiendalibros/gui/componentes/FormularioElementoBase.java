//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import co.edu.uptc.tiendalibros.gui.componentes.AsociacionCamposFormulario.CamposAsociados;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un formulario para mostrar y editar los detalles de un elemento.
///
/// @param <EV>  Tipo de Elemento Vista que se quiere mostrar en el formulario.
/// @param <DTO> Tipo de Objeto que el formulario genera para transferir datos.
public abstract class FormularioElementoBase<EV extends ElementoVista<?>, DTO> extends PanelBase {
  
  /// Elemento vista que este formulario muestra
  private EV elementoVista;
  
  /// Panel para mostrar el contenido del formulario.
  private PanelBase panelContenido;
  
  /// Asociación lógica de atributos, campos y notas de error.
  private AsociacionCamposFormulario asociacionCampos;
  
  /// Panel para mostrar los errores generales.
  private JLabel panelErroresGenerales;
  
  /// Panel para mostrar los botones de acción
  private PanelBase panelBotonesAccion;
  
  /// Botón para la acción de guardar.
  private JButton btnGuardar;
  
  /// Botón para la acción de eliminar.
  private JButton btnEliminar;
  
  /// Crea un nuevo objeto [FormularioElementoBase].
  ///
  /// @param evento Objeto que maneja eventos.
  protected FormularioElementoBase(
    Evento evento
  ) {
    super(evento);
    this.asociacionCampos = new AsociacionCamposFormulario();
  }
  
  /// Inicializar el formulario.
  ///
  /// @param titulo Título que se mostrará en el formulario.
  public void inicializarComponente(String titulo) {
    setLayout(new BorderLayout(5, 5));
    Border borde = BorderFactory.createTitledBorder(titulo);
    setBorder(borde);
    
    // crear el panel para mostrar los campos
    this.panelContenido = new PanelBase().construir();
    add(this.panelContenido, BorderLayout.CENTER);
    
    // crear el panel para mostrar los botones de acción
    this.panelBotonesAccion = new PanelBase().construir();
    this.panelBotonesAccion.setLayout(new FlowLayout(FlowLayout.RIGHT, 4, 4));
    
    // crear el botón eliminar
    this.btnEliminar = new JButton("Eliminar");
    FabricaEstilos.fabrica(this.btnEliminar)
      .botonEliminar()
      .tamanioPreferido();
    // crear el botón guardar
    this.btnGuardar = new JButton("Guardar");
    FabricaEstilos.fabrica(this.btnGuardar)
      .botonGuardar()
      .tamanioPreferido();
    
    // asignarle a cada botón sus listeners
    asignarListeners();
    
    // agregar cada botón de acción al panel de botones
    this.panelBotonesAccion.add(this.btnEliminar);
    this.panelBotonesAccion.add(this.btnGuardar);
    
    // TODO: establecer que la acción predeterminada es el botón guardar
    // getRootPane().setDefaultButton(this.btnGuardar);
    
    // crear el panel para mostrar los errores generales.
    this.panelErroresGenerales = new JLabel();
    this.panelErroresGenerales.setForeground(Color.RED);
    
    // crear el panel para contener los botones y los errores generales
    JPanel panelInferior = new JPanel(new BorderLayout(0, 2));
    panelInferior.add(this.panelBotonesAccion, BorderLayout.NORTH);
    panelInferior.add(this.panelErroresGenerales, BorderLayout.CENTER);
    
    add(panelInferior, BorderLayout.SOUTH);
  }
  
  /// Muestra el formulario con el elemento vista especificado.
  ///
  /// @param elementoVista Elemento vista que se quiere mostrar en el formulario.
  public void mostrar(EV elementoVista) {
    // establecer el elementoVista
    setElementoVista(elementoVista);
    
    // preparar los campos del formulario
    prepararCampos(elementoVista);
    
    // actualizar el estado de los botones.
    actualizarEstadoBotones();
    
    // mostrar en el formulario los errores
    mostrarErrores(elementoVista.getErrores());
  }
  
  /// Prepara los campos del formulario para visualizar el elemento vista especificado.
  ///
  /// @param elementoVista Elemento vista para preparar los campos del formulario.
  public abstract void prepararCampos(EV elementoVista);
  
  /// Obtiene un elemento con los datos ingresados o capturados en el formulario.
  ///
  /// @return Elemento cuyos atributos se completaron con los datos ingresados en
  ///         el formulario.
  public abstract DTO capturarDatos();
  
  /// Muestra los errores de validación del elemento mostrado en el formulario.
  ///
  /// @param errores Errores de validación de atributo.
  public void mostrarErrores(ResultadoValidacion errores) {
    // borrar los errores anteriores que se estaban mostrando.
    limpiarErrores();
    
    mostrarErroresDeAtributo(errores);
    
    // si hay errores de validación generales, mostrarlos.
    if (errores.hayErroresGenerales()) {
      StringBuilder textoErrorBuilder = new StringBuilder();
      textoErrorBuilder.append("<html>Errores Generales:<br>");
      
      for (String error : errores.getGenerales()) {
        textoErrorBuilder.append("• ")
          .append(error)
          .append("<br>");
      }
      
      textoErrorBuilder.append("</html>");
      this.panelErroresGenerales.setText(textoErrorBuilder.toString());
    }
    
  }
  
  /// Muestra los errores de validación de atributos visualmente en los campos y sus notas
  /// de error.
  public void mostrarErroresDeAtributo(ResultadoValidacion errores) {
    // si hay errores de validación de atributo, mostrarlos.
    if (errores.hayErroresPorAtributo()) {
      // vamos a recorrer la colección de errores de atributo
      for (Map.Entry<String, List<String>> atributoConErrores : errores.getPorAtributo()
        .entrySet()) {
        // obtener el atributo
        String atributo = atributoConErrores.getKey();
        // obtener la lista de errores
        List<String> listaErrores = atributoConErrores.getValue();
        
        // obtener la asociación del campo para el atributo
        AsociacionCamposFormulario.CamposAsociados camposAsociados = this.asociacionCampos.getAsociacionCampos()
          .get(atributo);
        
        // si se olvidó asociar al atributo con su campo y su nota de error, se continúa al
        // siguiente atributo
        if (camposAsociados == null) {
          continue;
        }
        
        // obtener el campo asociado al atributo
        JComponent campo = camposAsociados.campo();
        // obtener la nota de error asociada del atributo
        JLabel notaError = camposAsociados.notaError();
        
        if ((campo != null) && (notaError != null)) {
          // mostrar la lista de errores del atributo
          mostrarListaErroresEnCampo(campo, notaError, listaErrores);
        }
      }
    }
    
  }
  
  /// Genera una lista de viñetas que muestra los errores de validación.
  protected void mostrarListaErroresEnCampo(
    JComponent componente,
    JLabel notaError,
    List<String> listaErrores
  ) {
    FabricaEstilos.fabrica(componente)
      .error();
    FabricaEstilos.fabrica(notaError)
      .notaError();
    
    // constructor para crear la lista de errores
    StringBuilder textoBuilder = new StringBuilder();
    textoBuilder.append("<html><div style='width:200px;font-family:SansSerif'>");
    
    for (int i = 0; i < listaErrores.size(); i++) {
      String error = listaErrores.get(i);
      // agregar una viñeta por cada error
      textoBuilder.append("- ")
        .append(error);
      
      // agregar un salto de línea, excepto a la última línea
      if (i < listaErrores.size() - 1) {
        textoBuilder.append("<br>");
      }
    }
    
    textoBuilder.append("</div></html>");
    
    notaError.setText(textoBuilder.toString());
  }
  
  /// Obtiene el tamaño mínimo que debe tener la ventana de diálogo mostrará a este
  /// formulario.
  ///
  /// Tamaño predeterminado: ancho 800, alto 600.
  ///
  /// @return Tamaño mínimo que tendrá la ventana de diálogo.
  public Dimension getTamanoMinimoDialogo() { return new Dimension(800, 400); }
  
  /// Obtiene el panel usado para mostrar los campos del formulario. Este panel ofrece
  /// libertad para organizar los componentes hijos.
  ///
  public PanelBase getPanelCampos() { return this.panelContenido; }
  
  /// Limpia los errores mostrados en cada campo del formulario.
  public void limpiarErrores() {
    
    // limpiar los errores de atributo
    
    for (Entry<String, CamposAsociados> campoAsociado : this.asociacionCampos.getAsociacionCampos()
      .entrySet()) {
      // String atributo = campoAsociado.getKey();
      CamposAsociados asociacion = campoAsociado.getValue();
      
      // cambiar la apariencia de los campos y quitar el texto de las etiquetas de error que
      // tienen texto
      if (asociacion.notaError()
        .getText()
        .isEmpty() == false) {
        if (asociacion.campo() != null) {
          FabricaEstilos.fabrica(asociacion.campo())
            .quitarError();
        }
        
        asociacion.notaError()
          .setText("");
      }
    }
    
    // limpiar los errores generales si están tienen texto
    if (this.panelErroresGenerales.getText()
      .isEmpty() == false) {
      this.panelErroresGenerales.setText("");
    }
  }
  
  // Obtiene el elemento principal que el formulario maneja.
  //
  // @return Elemento principal que el formulario maneja.
  // public T getElemento() { return this.elementoVista != null ?
  // this.elementoVista.getElemento() : null; }
  
  /// Obtiene el elemento que el formulario maneja.
  ///
  protected EV getElementoVista() { return this.elementoVista; }
  
  /// Establece el elemento que el formulario maneja.
  ///
  /// @param elemento Elemento que el formulario maneja.
  protected void setElementoVista(EV elementoVista) { this.elementoVista = elementoVista; }
  
  /// Asigna los listeners que manejarán los eventos en el formulario.
  protected void asignarListeners() {
    // asignar el evento para guardar el elemento
    this.btnGuardar.addActionListener(
      (e) -> {
        guardar();
      }
    );
    
    // asignar el evento para eliminar el elemento
    this.btnEliminar.addActionListener((e) -> {
      if (confirmarEliminacion()) {
        // pedir que se elimine el elemento con el ID
        getEvento().manejar(
          getRutaEventoEliminar(),
          getElementoVista().getElemento()
            .getId()
        );
      }
    });
  }
  
  /// Guarda los datos del elemento de este formulario.
  ///
  protected void guardar() {
    // primero, revisar si se cumple la validación
    ResultadoValidacion errores = validarFormulario();
    
    if (errores.hayErrores()) {
      mostrarErrores(errores);
    } else {
      // pedir que se guarde el elemento con los datos ingresados por el usuario
      getEvento().manejar(getRutaEventoGuardar(), capturarDatos());
    }
    
  }
  
  /// Valida si los campos del formulario cumplen las reglas de validación esperadas.
  ///
  /// @return Resultado con los errores de validación del formulario.
  protected abstract ResultadoValidacion validarFormulario();
  
  /// Actualiza el estado de los botones de esta ventana de diálogo.
  protected void actualizarEstadoBotones() {
    
    // habilitar el botón eliminar sólo si el elemento ya existe en el repositorio de datos
    // porque tiene un ID que no es nulo. El id está anidado por eso toca verificar paso a
    // paso
    boolean esEdicion = (this.elementoVista != null)
      && (this.elementoVista.getElemento() != null)
      && (this.elementoVista.getElemento()
        .getId() != null);
    
    this.btnEliminar.setEnabled(esEdicion);
    
  }
  
  /// Muestra una ventana de diálogo para preguntarle al usuario si desea eliminar
  /// el elemento.
  private boolean confirmarEliminacion() {
    // a Sí le toca el valor 0. A No le toca el valor 1
    String[] opciones = { "Sí", "No" };
    // preguntar al usuario si quiere eliminar el elementoVista
    int respuesta = JOptionPane.showOptionDialog(
      this, // este diálogo será el padre del JOption
      String.format("¿Desea eliminar %s ?", describirElemento()),
      "Confirmar Eliminación", // título del mensaje
      JOptionPane.YES_NO_OPTION, // botones: Sí o No
      JOptionPane.WARNING_MESSAGE, // dice cuál es el ícono del mensaje
      null, // no usar un ícono personalizado, solo usar el ícono predeterminado
      opciones, // array con el colorTexto de las opciones que el usuario verá
      opciones[1] // de manera predeterminada, se activa el botón 1 = No
    );
    // si el usuario presiona el botón Sí, el JOptionPane devuelve el valor 0.
    // por lo que la confirmación es comparar si respuesta = 0
    return respuesta == 0;
  }
  
  /// Obtiene un texto que describe el elemento.
  ///
  /// @return Texto que describe el elemento.
  protected abstract String describirElemento();
  
  /// Obtiene la ruta del evento para la acción de guardar.
  ///
  /// @return Ruta del evento para la acción de guardar.
  protected abstract String getRutaEventoGuardar();
  
  /// Obtiene la ruta del evento para la acción de eliminar.
  ///
  /// @return Ruta del evento para la acción de eliminar.
  protected abstract String getRutaEventoEliminar();
  
  public AsociacionCamposFormulario getAsociacionCampos() { return this.asociacionCampos; }
  
  public PanelBase getPanelContenido() { return this.panelContenido; }
  
  @Override
  public FormularioElementoBase<EV, DTO> construir() {
    super.construir();
    return this;
  }
}
