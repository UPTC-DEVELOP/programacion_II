/// 
package co.edu.uptc.tiendalibros.gui.componentes;

import java.util.Optional;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import co.edu.uptc.tiendalibros.gui.estilos.IconoImagen;

/// Representa un panel que debe mostrar pestañas en un [JTabbedPane].
public interface PanelConPestanas {
  
  /// Tamaño del lado del ícono de una pestana.
  static final int LADO_ICONO_PESTANA = 32;
  
  //// Agrega una pestaña con título e ícono. Si el ícono no carga, la pestaña queda solo
  /// con título.
  ///
  /// @param titulo        Título de la pestaña.
  /// @param icono         Ícono de la pestaña.
  /// @param panelPestanas El panel que contiene las pestañas.
  /// @param contenido     Panel con el contenido de la pestaña.
  default void agregarPestana(String titulo, IconoImagen icono, JTabbedPane panelPestanas, JComponent contenido) {
    // obtener el ícono para la pestana
    Optional<ImageIcon> imagen = icono.icono(PanelConPestanas.LADO_ICONO_PESTANA);
    if (imagen.isPresent()) {
      // agregar el tab con título, imagen y contenido
      panelPestanas.addTab(titulo, imagen.get(), contenido);
    } else {
      panelPestanas.addTab(titulo, contenido);
    }
  }
}
