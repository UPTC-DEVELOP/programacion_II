///
package co.edu.uptc.tiendalibros.gui.componentes;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

import co.edu.uptc.tiendalibros.gui.avisos.Aviso;
import co.edu.uptc.tiendalibros.gui.avisos.TipoAviso;

/// Representa un panel para mostrar avisos al usuario.
public class PanelAvisos extends PanelBase {
  
  /// Etiqueta que muestra el ícono del aviso.
  private JLabel lblIcono;
  /// Etiqueta que muestra el mensaje del aviso.
  private JLabel lblMensaje;
  /// Botón para cerrar el aviso.
  private JButton btnCerrar;
  
  /// Temporizador que cuenta el tiempo para cerrar automáticamente un aviso.
  private Timer temporizador;
  
  /// Crea un nuevo objeto del panel de avisos.
  public PanelAvisos() {
    
  }
  
  @Override
  protected void inicializarComponente() {
    setLayout(new BorderLayout(10, 0));
    // top, left, bottom, right
    setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 4));
    
    this.lblIcono = new JLabel(" ");
    this.lblMensaje = new JLabel(" ");
    this.btnCerrar = new JButton("×");
    this.btnCerrar.setToolTipText("Cerrar aviso");
    this.btnCerrar.setBorderPainted(false); // quitar el borde del botón
    this.btnCerrar.setContentAreaFilled(false); // quitar el relleno del botón
    this.btnCerrar.setFocusable(false); // el botón no tendrá el foco
    this.btnCerrar.addActionListener((e) -> cerrar());
    
    // inicializar el temporizador en cero y decirle que cuando se complete el tiempo debe
    // llamar al método cerrar().
    this.temporizador = new Timer(0, (e) -> cerrar());
    this.temporizador.setRepeats(false); // enviar solo un evento de temporización a su listener
    
    add(this.lblIcono, BorderLayout.WEST);
    add(this.lblMensaje, BorderLayout.CENTER);
    add(this.btnCerrar, BorderLayout.EAST);
    
    cerrar();
  }
  
  /// Muestra el aviso al usuario.
  ///
  /// @param aviso Aviso que quiere mostrar al usuario.
  public void mostrar(Aviso aviso) {
    // configurar los componentes según el tipo de aviso
    InfoPanelAviso estilo = PanelAvisos.estiloDe(aviso.getTipoAviso());
    
    this.lblIcono.setText(estilo.simbolo());
    this.lblMensaje.setText(aviso.getMensaje());
    
    setBackground(estilo.colorFondo());
    this.lblIcono.setForeground(estilo.colorTexto());
    this.lblMensaje.setForeground(estilo.colorTexto());
    
    this.lblIcono.setBackground(estilo.colorFondo());
    this.lblMensaje.setBackground(estilo.colorFondo());
    
    setOpaque(true); // el panel muestra todo su color de fondo
    
    // el botón cerrar aparece
    this.btnCerrar.setVisible(true);
    
    // obviamente, este es un aviso nuevo, y se debe reiniciar el temporizador para cerrar el
    // aviso automáticamente.
    // los avisos que tengan 0 milisegundos significa que el usuario los cierra manualmente
    this.temporizador.stop();
    if (estilo.milisegundos() > 0) {
      this.temporizador.setInitialDelay(estilo.milisegundos());
      this.temporizador.restart();
    }
    
    repaint();
  }
  
  /// Cierra el aviso.
  public void cerrar() {
    this.temporizador.stop();
    this.lblIcono.setText(" ");
    this.lblMensaje.setText(" ");
    this.btnCerrar.setVisible(false);
    setOpaque(false);
    revalidate();
    repaint();
  }
  
  /// Obtiene una configuración para el panel de aviso según el tipo del aviso.
  ///
  /// @param tipo Tipo de aviso para generar el estilo
  /// @return InfoPanelAviso Configuración para el panel de aviso.
  private static InfoPanelAviso estiloDe(TipoAviso tipo) {
    return switch (tipo) {
      case TipoAviso.EXITO       -> new InfoPanelAviso(new Color(223, 240, 216), new Color(33, 95, 35), "✔", 4000);
      case TipoAviso.INFORMACION -> new InfoPanelAviso(new Color(217, 237, 247), new Color(30, 85, 120), "ℹ", 6000);
      case TipoAviso.ADVERTENCIA -> new InfoPanelAviso(new Color(252, 248, 227), new Color(125, 95, 40), "⚠", 8000);
      case TipoAviso.ERROR       -> new InfoPanelAviso(new Color(242, 222, 222), new Color(150, 40, 40), "✖", 0);
    };
  }
}
