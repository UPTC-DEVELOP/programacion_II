//
package co.edu.uptc.tiendalibros.gui.administracion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import co.edu.uptc.tiendalibros.gui.componentes.FormularioElementoBase;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.empleado.DatosEmpleado;

/// Representa un formulario para crear y editar un empleado y su cuenta de usuario.
public class FormularioEmpleado extends FormularioElementoBase<ElementoVistaEmpleado, DatosEmpleado> {
  
  /// Panel de campos del empleado.
  private PanelCamposEmpleado panelCamposEmpleado;
  
  /// Crea un nuevo objeto [FormularioEmpleado].
  ///
  /// @param evento Objeto que maneja eventos.
  public FormularioEmpleado(Evento evento) {
    super(evento);
  }
  
  @Override
  public void inicializarComponente() {
    super.inicializarComponente("Formulario Empleado");
    
    getPanelContenido().setLayout(new BorderLayout());
    getPanelContenido().setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
    
    JPanel panelContenedor = new JPanel();
    panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.Y_AXIS));
    panelContenedor.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    
    // panel de los campos del empleado
    this.panelCamposEmpleado = (PanelCamposEmpleado) new PanelCamposEmpleado(getAsociacionCampos()).construir();
    this.panelCamposEmpleado.setAlignmentX(Component.LEFT_ALIGNMENT);
    panelContenedor.add(this.panelCamposEmpleado);
    
    getPanelContenido().add(panelContenedor, BorderLayout.CENTER);
  }
  
  @Override
  public Dimension getTamanoMinimoDialogo() { return new Dimension(680, 760); }
  
  @Override
  public void prepararCampos(ElementoVistaEmpleado elementoVista) {
    this.panelCamposEmpleado.prepararCampos(elementoVista);
  }
  
  @Override
  public ResultadoValidacion validarFormulario() {
    return this.panelCamposEmpleado.validarCampos();
  }
  
  @Override
  public DatosEmpleado capturarDatos() {
    return this.panelCamposEmpleado.capturarDatos();
  }
  
  @Override
  protected String describirElemento() {
    DatosEmpleado datos = getElementoVista().getElemento();
    
    return String.format(
      "al empleado '%s %s' y a su cuenta de usuario",
      datos.nombres(),
      datos.apellidos()
    );
  }
  
  @Override
  protected String getRutaEventoGuardar() { return ControladorEmpleado.EMPLEADO_GUARDAR; }
  
  @Override
  protected String getRutaEventoEliminar() { return ControladorEmpleado.EMPLEADO_ELIMINAR; }
}