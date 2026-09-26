//
package main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
  
  private JButton btnEliminar;
  
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
    
    // crear el botón eliminar pero deshabilitado inicialmente
    this.btnEliminar = new JButton("Eliminar");
    this.btnEliminar.setEnabled(false);
    
    this.btnEliminar.addActionListener((e) -> {
      // primero, se le pregunta al usuario si realmente quiere eliminar la editorial
      // TODO: consultar cómo establecer que la opción predeterminada sea 'NO'
      int respuesta = JOptionPane.showConfirmDialog(
        this,
        String.format("¿Desea eliminar la editorial '%s' ?", this.txtNombre.getText()),
        "Confirmar Eliminación",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
      );
      
      // segundo, enviar la solicitud de eliminación sólo si el usuario confirmó
      if (respuesta == JOptionPane.YES_OPTION) {
      // enviarle al enrutador el ID de la editorial porque el controlador solo necesita saber
      // el ID de la editorial que se quiere eliminar.
        getEnrutadorEventos().manejarEvento(ControladorEditorial.EDITORIAL_ELIMINAR, this.idEditorial);
      }
    });
    
    // crear el botón guardar
    this.btnGuardar = new JButton("Guardar");
    
    this.btnGuardar
      .addActionListener((e) -> {
        // crear un objeto editorial con los datos del formulario
        Editorial editorial = new Editorial(this.idEditorial, this.txtNombre.getText());
        // pedirle al controlador que guarde la editorial
        getEnrutadorEventos().manejarEvento(ControladorEditorial.EDITORIAL_GUARDAR, editorial);
      });
    
    panelBotones.add(this.btnEliminar);
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
      // entonces el formulario mostrará una editorial nueva con valores predeterminados
      editorial = new Editorial();
    }
    
    // el formulario manejará internamente este id
    this.idEditorial = editorial.getId();
    
    // mostrar en el formulario los datos de la editorial
    this.txtNombre.setText(editorial.getNombre());
    
    // habilitar el botón eliminar sólo si la editorial ya existe (o sea, porque tiene un ID)
    if (this.idEditorial != null) {
      this.btnEliminar.setEnabled(true);
    } else {
      // ID es nulo, luego la editorial no existe, o es solo un objeto editorial predeterminado
      // en blanco
      this.btnEliminar.setEnabled(false);
    }
    
    // poner el foco al campo nombre
    this.txtNombre.requestFocusInWindow();
  }
}
