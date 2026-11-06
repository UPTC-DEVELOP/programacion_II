//
package co.edu.uptc.tiendalibros.gui.editorial;

import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import co.edu.uptc.tiendalibros.gui.componentes.AsociacionCamposFormulario;
import co.edu.uptc.tiendalibros.gui.componentes.PanelCamposBase;
import co.edu.uptc.tiendalibros.gui.componentes.RestriccionesCelda;
import co.edu.uptc.tiendalibros.gui.componentes.ValidadorFormulario;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

//
public class PanelCamposEditorial extends PanelCamposBase<ElementoVistaEditorial, Editorial> {
  
  /// Campo de texto para el nombre de la editorial.
  private JTextField txtNombre;
  
  /// Componente para mostrar errores del nombre
  private JLabel notaErrorNombre;
  
  /// Crea un nuevo objeto [PanelCamposEditorial].
  public PanelCamposEditorial(AsociacionCamposFormulario asociacionCampos) {
    super(asociacionCampos);
  }
  
  @Override
  protected void inicializarComponente() {
    FabricaEstilos.fabrica(this)
      .bordeSeccion("Datos de la editorial");
    setBackground(null);
    setLayout(new GridBagLayout());
    
    // crear el campo nombre
    this.txtNombre = new JTextField();
    FabricaEstilos.fabrica(this.txtNombre)
      .campoTexto(30);
    
    // crear las notas de errores para cada campo
    this.notaErrorNombre = new JLabel();
    FabricaEstilos.fabrica(this.notaErrorNombre)
      .notaError();
    
    // asociar de manera lógica los campos con sus notas de error
    asociarCampo("nombre", this.txtNombre, this.notaErrorNombre);
    
    ubicarComponentes();
    
  }
  
  @Override
  public void prepararCampos(ElementoVistaEditorial elementoVista) {
    setElementoVista(elementoVista);
    
    // obtener el objeto libro que es el elemento principal que maneja el panel
    Editorial editorial = elementoVista.getElemento();
    
    // obtener el nombre de la editorial
    this.txtNombre.setText(editorial.getNombre());
  }
  
  private void ubicarComponentes() {
    // crear las etiquetas de los campos
    JLabel lblNombre = new JLabel("Nombre:");
    
    // crear una restricción base para todos los componentes del formulario
    RestriccionesCelda base = new RestriccionesCelda().insets(4, 4, 0, 4)
      .anclarIzquierda();
    
    // ubicar la etiqueta nombre
    agregar(
      lblNombre,
      base.clonar()
        .gridy(0)
        .gridx(0)
    );
    // agregar el campo nombre
    agregar(
      this.txtNombre,
      base.clonar()
        .gridy(0)
        .gridx(1)
        .expandirHorizontal()
    );
    // agregar la nota de error del titulo
    agregar(
      this.notaErrorNombre,
      base.clonar()
        .gridy(1)
        .gridx(1)
        .expandirHorizontal()
    );
  }
  
  @Override
  public ResultadoValidacion validarCampos() {
    return new ValidadorFormulario().getErrores();
  }
  
  @Override
  public Editorial capturarDatos() {
    // obtener la editorial el cual es el objeto principal manejado por el panel
    Editorial editorial = getElementoVista().getElemento();
    // necesitamos el id de la editorial
    Long idEditorial = editorial.getId();
    
    // obtener el nombre
    String nombre = this.txtNombre.getText()
      .trim();
    
    // crear un nuevo objeto editorial a partir de sus atributos
    return new Editorial(
      idEditorial, nombre
    );
    
  }
  
}
