//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import main.co.edu.uptc.fesad.tpsi.tienda.controladores.ControladorEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.controladores.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.componentes.PanelBase;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.Editorial;

/// Representa un formulario para mostrar y editar los detalles de una editorial.
public class FormularioEditorial extends PanelBase {
  
  /// ID de la editorial.
  /// - ID con valor numérico: la editorial existe en la base de datos
  /// - ID con valor null: la editorial todavía no existe en la base de datos.
  private Long idEditorial = null;
  
  private JTextField txtNombre;
  
  private JButton btnGuardar;
  
  /// Crea un nuevo objeto [FormularioEditoria][FormularioEditoria] con el enrutador de
  /// eventos dado.
  /// 
  /// @param enrutador Enrutador de los eventos.
  public FormularioEditorial(EnrutadorEventos enrutador) {
    super(enrutador);
  }
  
  @Override
  public void inicializarComponente() {
    setLayout(new BorderLayout(5, 5));
    Border borde = BorderFactory.createTitledBorder("Formulario Editorial");
    setBorder(borde);
    
    JPanel panelCampos = new JPanel(new GridLayout(0, 2, 5, 5));
    Border sinBorde = BorderFactory.createEmptyBorder(3, 3, 3, 3);
    panelCampos.setBorder(sinBorde);
    
    this.txtNombre = new JTextField(30);
    JLabel lblNombre = new JLabel("Nombre:");
    lblNombre.setLabelFor(this.txtNombre);
    
    panelCampos.add(lblNombre);
    panelCampos.add(this.txtNombre);
    
    add(panelCampos, BorderLayout.NORTH);
    
    JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 4));
    
    this.btnGuardar = new JButton("Guardar");
    
    this.btnGuardar
      .addActionListener((e) -> {
        // crear un objeto editorial con los datos del formulario
        Editorial editorial = new Editorial(this.idEditorial, this.txtNombre.getText());
        // pedirle al controlador que guarde la editorial
        getEnrutadorEventos().manejarEvento(ControladorEditorial.EDITORIAL_GUARDAR, editorial);
      });
    
    panelBotones.add(this.btnGuardar);
    add(panelBotones, BorderLayout.SOUTH);
    
  }
  
  /// Prepara el formulario para mostrar el elemento dado.
  /// 
  /// @param elemento Elemento que se quiere mostrar en el formulario.
  public void preparar(Object elemento) {
    Editorial editorial = (Editorial) elemento;
    // evitar el error de objeto nulo
    if (elemento == null) {
      editorial = new Editorial();
    }
    
    this.idEditorial = editorial.getId();
    this.txtNombre.setText(editorial.getNombre());
    // poner el foco al campo nombre
    this.txtNombre.requestFocusInWindow();
  }
}
