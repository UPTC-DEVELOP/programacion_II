package co.edu.uptc.tiendalibros.gui.categoria;

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
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

public class FormularioCategoria extends FormularioElementoBase<ElementoVistaCategoria, Categoria> {

  private JTextField txtNombre;
  private JTextField txtDescripcion;
  private JTextField txtCodigo;

  public FormularioCategoria(Evento evento) {
    super(evento);
  }

  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Categoría");

    Border sinBorde = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
    panelCampos.setBorder(sinBorde);

    this.txtNombre = new JTextField(30);
    JLabel lblNombre = new JLabel("Nombre:");
    lblNombre.setLabelFor(this.txtNombre);
    panelCampos.add(lblNombre);
    panelCampos.add(this.txtNombre);

    this.txtDescripcion = new JTextField(30);
    JLabel lblDescripcion = new JLabel("Descripción:");
    lblDescripcion.setLabelFor(this.txtDescripcion);
    panelCampos.add(lblDescripcion);
    panelCampos.add(this.txtDescripcion);

    this.txtCodigo = new JTextField(30);
    JLabel lblCodigo = new JLabel("Código:");
    lblCodigo.setLabelFor(this.txtCodigo);
    panelCampos.add(lblCodigo);
    panelCampos.add(this.txtCodigo);

    getPanelCampos().setLayout(new BorderLayout());
    getPanelCampos().add(panelCampos, BorderLayout.NORTH);
  }

  @Override
  public void prepararCampos(ElementoVistaCategoria elementoVista) {
    Categoria categoria = elementoVista.getElemento();
    this.txtNombre.setText(categoria.getNombre());
    this.txtDescripcion.setText(categoria.getDescripcion());
    this.txtCodigo.setText(categoria.getCodigo());
  }

  @Override
  public ResultadoValidacion validarFormulario() {
    return new ValidadorFormulario().getErrores();
  }

  @Override
  public Categoria capturarDatos() {
    Categoria categoriaActual = getElementoVista().getElemento();
    Long id = categoriaActual != null ? categoriaActual.getId() : null;
    return new Categoria(id, this.txtNombre.getText(), this.txtDescripcion.getText(), this.txtCodigo.getText());
  }

  @Override
  protected String describirElemento() {
    Categoria categoria = getElementoVista().getElemento();
    return String.format("la categoría '%s'", categoria.getNombre());
  }

  @Override
  protected String getRutaEventoGuardar() { return ControladorCategoria.CATEGORIA_GUARDAR; }

  @Override
  protected String getRutaEventoEliminar() { return ControladorCategoria.CATEGORIA_ELIMINAR; }

}
