package co.edu.uptc.tiendalibros.gui.autor;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.componentes.ValidadorFormulario;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Autor;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

public class FormularioAutor extends FormularioElementoBase<ElementoVistaAutor, Autor> {

  private JTextField txtNombre;
  private JTextField txtApellidos;

  public FormularioAutor(Evento evento) {
    super(evento);
  }

  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Autor");

    Border sinBorde = BorderFactory.createEmptyBorder(3, 3, 3, 3);

    JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
    panelCampos.setBorder(sinBorde);

    this.txtNombre = new JTextField(30);
    JLabel lblNombre = new JLabel("Nombre:");
    lblNombre.setLabelFor(this.txtNombre);

    panelCampos.add(lblNombre);
    panelCampos.add(this.txtNombre);

    this.txtApellidos = new JTextField(30);
    JLabel lblApellidos = new JLabel("Apellidos:");
    lblApellidos.setLabelFor(this.txtApellidos);

    panelCampos.add(lblApellidos);
    panelCampos.add(this.txtApellidos);

    getPanelContenido().setLayout(new BorderLayout());
    getPanelCampos().add(panelCampos, BorderLayout.NORTH);
  }

  @Override
  public void prepararCampos(ElementoVistaAutor elementoVista) {
    Autor autor = elementoVista.getElemento();
    this.txtNombre.setText(autor.getNombre());
    this.txtApellidos.setText(autor.getApellidos());
  }

  @Override
  public ResultadoValidacion validarFormulario() {
    return new ValidadorFormulario().getErrores();
  }

  @Override
  public Autor capturarDatos() {
    Autor autorActual = getElementoVista().getElemento();
    Long id = autorActual != null ? autorActual.getId() : null;
    return new Autor(id, this.txtNombre.getText(), this.txtApellidos.getText());
  }

  @Override
  protected String describirElemento() {
    Autor autor = getElementoVista().getElemento();
    return String.format("el autor '%s %s'", autor.getNombre(), autor.getApellidos());
  }

  @Override
  protected String getRutaEventoGuardar() { return ControladorAutor.AUTOR_GUARDAR; }

  @Override
  protected String getRutaEventoEliminar() { return ControladorAutor.AUTOR_ELIMINAR; }

}
