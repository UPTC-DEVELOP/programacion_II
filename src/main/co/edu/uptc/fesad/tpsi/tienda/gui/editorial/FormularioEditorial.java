//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.FormularioElementoBase;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.Evento;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Editorial;

/// Representa un formulario para mostrar y editar los detalles de una editorial.
public class FormularioEditorial extends FormularioElementoBase {
  
  private JTextField txtNombre;
  

  
  /// Crea un nuevo objeto [FormularioEditorial][FormularioEditorial].
  public FormularioEditorial(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Editorial");
    
    Border sinBorde = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    
    JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
    panelCampos.setBorder(sinBorde);
    
    // crear los componentes de ingreso de datos
    this.txtNombre = new JTextField(30);
    JLabel lblNombre = new JLabel("Nombre:");
    lblNombre.setLabelFor(this.txtNombre);
    
    panelCampos.add(lblNombre);
    panelCampos.add(this.txtNombre);
    
    add(panelCampos, BorderLayout.NORTH);
    

  }
  
  /// Prepara el formulario para mostrar el elemento dado.
  /// 
  /// @param elemento Elemento que se quiere mostrar en el formulario.
  @Override
  public void preparar(Object elemento) {
    Editorial editorial = (Editorial) elemento;
    // evitar el error de objeto nulo
    if (elemento == null) {
      // entonces el formulario mostrará una editorial nueva con valores predeterminados
      editorial = new Editorial();
    }
    
    // el formulario manejará internamente este id
    setIDElemento(editorial.getId());
    
    // mostrar en el formulario los datos de la editorial
    this.txtNombre.setText(editorial.getNombre());

  }
  
  @Override
  public Object capturarDatos() {
    // crear un objeto editorial con los datos del formulario
    Editorial editorial = new Editorial(getIDElemento(), this.txtNombre.getText());
    return editorial;
  }
  
  


}
