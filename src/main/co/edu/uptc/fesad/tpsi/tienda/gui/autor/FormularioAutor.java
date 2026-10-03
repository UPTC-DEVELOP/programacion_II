package main.co.edu.uptc.fesad.tpsi.tienda.gui.autor;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.FormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;

public class FormularioAutor extends FormularioElementoBase {
  
  private JTextField txtNombre;
  private JTextField txtApellidos;
  
  /// Crea un nuevo objeto [FormularioEditorial][FormularioEditorial].
  public FormularioAutor(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Autor");
    
    Border sinBorde = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    
    JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
    panelCampos.setBorder(sinBorde);
    
    // crear los componentes de ingreso de datos
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
    
    add(panelCampos, BorderLayout.NORTH);
    
  }
  
  /// Prepara el formulario para mostrar el elemento dado.
  /// 
  /// @param elemento Elemento que se quiere mostrar en el formulario.
  @Override
  public void preparar(Object elemento) {
    Autor autor = (Autor) elemento;
    // evitar el error de objeto nulo
    if (elemento == null) {
      // entonces el formulario mostrará una editorial nueva con valores predeterminados
      autor = new Autor();
    }
    
    // el formulario manejará internamente este id
    setIDElemento(autor.getId());
    
    // mostrar en el formulario los datos de la editorial
    this.txtNombre.setText(autor.getNombre());
    this.txtApellidos.setText(autor.getApellidos());
    
  }
  
  @Override
  public Object capturarDatos() {
    // crear un objeto editorial con los datos del formulario
    Autor autor = new Autor(getIDElemento(), this.txtNombre.getText(), this.txtApellidos.getText());
    return autor;
  }
}
