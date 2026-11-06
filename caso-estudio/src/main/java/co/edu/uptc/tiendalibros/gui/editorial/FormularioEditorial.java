//
package co.edu.uptc.tiendalibros.gui.editorial;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;

/// Representa un formulario para mostrar y editar los detalles de una editorial.
public class FormularioEditorial extends FormularioElementoBase<ElementoVistaEditorial, Editorial> {
  
  /// Panel de campos de la editorial
  private PanelCamposEditorial panelCamposEditorial;
  
  /// Crea un nuevo objeto [FormularioEditorial][FormularioEditorial].
  public FormularioEditorial(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Editorial");
    
    getPanelContenido().setLayout(new BorderLayout());
    getPanelContenido().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
    JPanel panelContenedor = crearPanelContenedor();
    
    getPanelContenido().add(panelContenedor, BorderLayout.CENTER);
  }
  
  private JPanel crearPanelContenedor() {
    JPanel panelContenedor = new JPanel();
    panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
    panelContenedor.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    
    // panel de los campos de la editorial
    this.panelCamposEditorial = (PanelCamposEditorial) new PanelCamposEditorial(getAsociacionCampos()).construir();
    
    this.panelCamposEditorial.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    panelContenedor.add(this.panelCamposEditorial);
    panelContenedor.add(Box.createVerticalStrut(8));
    
    return panelContenedor;
  }
  
  @Override
  public Dimension getTamanoMinimoDialogo() { return new Dimension(560, 280); }
  
  @Override
  public void prepararCampos(ElementoVistaEditorial elementoVista) {
    
    this.panelCamposEditorial.prepararCampos(elementoVista);
    
  }
  
  @Override
  public ResultadoValidacion validarFormulario() {
    // validar si los campos de la editorial tienen errores
    ResultadoValidacion errores = this.panelCamposEditorial.validarCampos();
    
    return errores;
  }
  
  @Override
  public Editorial capturarDatos() {
    return this.panelCamposEditorial.capturarDatos();
  }
  
  @Override
  protected String describirElemento() {
    Editorial editorial = getElementoVista().getElemento();
    
    return String.format(
      "la editorial '%s'",
      editorial.getNombre()
    );
  }
  
  @Override
  protected String getRutaEventoGuardar() { return ControladorEditorial.EDITORIAL_GUARDAR; }
  
  @Override
  protected String getRutaEventoEliminar() { return ControladorEditorial.EDITORIAL_ELIMINAR; }
  
}
