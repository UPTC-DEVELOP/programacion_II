//
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Representa una ventana de diálogo con un formulario para mostrar, crear y editar
/// un elemento.
///
/// @param <EV> Tipo de ElementoVista que este diálogo maneja.
public class DialogoFormularioElemento<EV extends ElementoVista<?>> extends JDialog {

  /// Formulario que muestra el elemento.
  private FormularioElementoBase<EV, ?> formulario;
  
  /// Objeto que maneja eventos.
  private Evento evento;
  
  /// Indicador para saber si el diálogo se inicializó completamente.
  private boolean inicializado = false;
  
  /// Crea un objeto [DialogoFormularioElemento] como ventana para mostrar el formulario con
  /// un elemento.
  ///
  /// @param propietario Componente que es dueño o padre de la ventana de diálogo.
  /// @param titulo      Título que se mostrará en el diálogo.
  /// @param formulario  Formulario que se quiere mostrar en el diálogo.
  /// @param evento      Objeto que maneja eventos.
  protected DialogoFormularioElemento(
    Window propietario,
    String titulo,
    FormularioElementoBase<EV, ?> formulario,
    Evento evento
  ) {
    super(propietario, titulo, Dialog.ModalityType.APPLICATION_MODAL);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this.formulario = formulario;
    this.evento = evento;
  }
  
  /// Inicializa los componentes de esta ventana de diálogo.
  protected void inicializarComponente() {
    setLayout(new BorderLayout(5, 5));
    
    // agregar el formulario
    add(this.formulario, BorderLayout.CENTER);
  }
  
  /// Muestra esta ventana de diálogo por primera vez con su formulario respectivo y con el
  /// elemento vista especificado.
  ///
  /// @param elementoVista Elemento vista que se quiere visualizar el formulario.
  public void mostrar(EV elementoVista) {
    // mostrar esta ventana de diálogo por primera vez
    this.formulario.mostrar(elementoVista);
    // establecer el tamaño mínimo de la ventana de diálogo antes del pack()
    setMinimumSize(this.formulario.getTamanoMinimoDialogo());
    pack();
    setLocationRelativeTo(getOwner());
    setVisible(true);
  }
  
  /// Actualiza un ventana de diálogo que ya está visible con su formulario respectivo y con
  /// el elemento vista especificado.
  ///
  /// @param elementoVista Elemento vista que se quiere actualizar en el formulario.
  public void actualizar(EV elementoVista) {
    this.formulario.mostrar(elementoVista);
    // se supone que el diálogo ya está visible, en cambio, se pide actualizar el contenido y
    // redibujar el diálogo para que los errores se puedan ver
    // revalidate(): valida la jerarquía de los componentes para que los layout managers
    // detecten cambios
    // pack(): calculo nuevos tamaños de los componentes para ajustar la ventana de diálogo
    // setLocationRelativeTo(): centra la ventana teniendo en cuenta sus dimensiones.
    // repaint(): forzar un dibujo visual de los cambios en los componentes.
    revalidate();
    
    // pack();
    // setLocationRelativeTo(getOwner());
    repaint();
  }
  
  /// Cierra esta ventana de diálogo.
  public void cerrar() {
    dispose();
  }
  
  /// Obtiene el objeto que maneja eventos.
  ///
  /// @return Objeto que maneja eventos.
  public Evento getEvento() { return this.evento; }
  
  /// Completa la inicialización de esta ventana de diálogo y sus componentes.
  
  /// Termina la inicialización del componente. Invoca al método inicializarComponente().
  public DialogoFormularioElemento<EV> construir() {
    if (!this.inicializado) {
      this.inicializado = true;
      inicializarComponente();
    }
    return this;
  }
  
}
