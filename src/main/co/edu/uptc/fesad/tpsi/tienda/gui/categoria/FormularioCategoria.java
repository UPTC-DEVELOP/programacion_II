package main.co.edu.uptc.fesad.tpsi.tienda.gui.categoria;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.FormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Categoria;

public class FormularioCategoria extends FormularioElementoBase {
  
  private JTextField txtNombre;
  private JTextField txtDescripcion;
  private JTextField txtCodigo;
  
  public FormularioCategoria(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    
    super.inicializarComponente(
      "Formulario Categoría"
    );
    
    Border sinBorde = BorderFactory.createEmptyBorder(
      3,
      3,
      3,
      3
    );
    
    JPanel panelCampos = new JPanel(
      new GridLayout(0, 2, 5, 5)
    );
    
    panelCampos.setBorder(sinBorde);
    
    // Nombre
    this.txtNombre = new JTextField(30);
    
    JLabel lblNombre = new JLabel("Nombre:");
    
    lblNombre.setLabelFor(
      this.txtNombre
    );
    
    panelCampos.add(lblNombre);
    panelCampos.add(this.txtNombre);
    
    // Descripción
    this.txtDescripcion = new JTextField(30);
    
    JLabel lblDescripcion = new JLabel("Descripción:");
    
    lblDescripcion.setLabelFor(
      this.txtDescripcion
    );
    
    panelCampos.add(lblDescripcion);
    panelCampos.add(this.txtDescripcion);
    
    // Código
    this.txtCodigo = new JTextField(30);
    
    JLabel lblCodigo = new JLabel("Código:");
    
    lblCodigo.setLabelFor(
      this.txtCodigo
    );
    
    panelCampos.add(lblCodigo);
    panelCampos.add(this.txtCodigo);
    
    add(
      panelCampos,
      BorderLayout.NORTH
    );
  }
  
  @Override
  public void preparar(Object elemento) {
    
    Categoria categoria = (Categoria) elemento;
    
    if (elemento == null) {
      categoria = new Categoria();
    }
    
    setIDElemento(
      categoria.getId()
    );
    
    this.txtNombre.setText(
      categoria.getNombre()
    );
    
    this.txtDescripcion.setText(
      categoria.getDescripcion()
    );
    
    this.txtCodigo.setText(
      categoria.getCodigo()
    );
  }
  
  @Override
  public Object capturarDatos() {
    
    Categoria categoria = new Categoria(
      getIDElemento(),
      this.txtNombre.getText(),
      this.txtDescripcion.getText(),
      this.txtCodigo.getText()
    );
    
    return categoria;
  }
}