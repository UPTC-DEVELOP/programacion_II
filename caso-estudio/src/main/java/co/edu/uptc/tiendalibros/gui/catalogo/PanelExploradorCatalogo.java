/// 
package co.edu.uptc.tiendalibros.gui.catalogo;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import co.edu.uptc.tiendalibros.gui.componentes.PanelBase;
import co.edu.uptc.tiendalibros.gui.estilos.FabricaEstilos;
import co.edu.uptc.tiendalibros.gui.eventos.Evento;

/// Panel que muestra el catálogo de libros como una lista vertical de fichas.
public class PanelExploradorCatalogo extends PanelBase {
  
  /// Espacio vertical en píxeles entre una ficha y la siguiente.
  private static final int ESPACIO_ENTRE_FICHAS = 10;
  
  /// Crea el panel del explorador del catálogo.
  ///
  /// @param evento Objeto que las fichas usan para notificar los clics de sus botones.
  public PanelExploradorCatalogo(Evento evento) {
    super(evento);
  }
  
  @Override
  protected void inicializarComponente() {
    super.inicializarComponente();
    // usar un boxlayout vertical para apilar las fichas
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
  }
  
  /// Muestra la lista de libros, reemplazando la lista que se estuviera mostrando antes.
  ///
  /// Cada libro se muestra en su propia ficha [FichaLibroCatalogo].
  /// 
  /// Si la lista está vacía, se muestra un mensaje de catálogo vacío.
  ///
  /// @param evFichas Lista de elementos vista de las fichas que se desea mostrar.
  public void listar(List<ElementoVistaFichaLibro> evFichas) {
    // quitar todas las fichas anteriores
    removeAll();
    
    // la lista está vacía, entonces se muestra un mensaje al usuario
    if (evFichas.isEmpty()) {
      mostrarCatalogoVacio();
    } else {
      // recorrer la lista de elementos ficha
      for (ElementoVistaFichaLibro evFicha : evFichas) {
        // obtener la ficha
        FichaLibroCatalogo ficha = (FichaLibroCatalogo) new FichaLibroCatalogo(getEvento()).construir();
        
        ficha.mostrar(evFicha);
        
        add(ficha);
        add(Box.createVerticalStrut(PanelExploradorCatalogo.ESPACIO_ENTRE_FICHAS));
      }
    }
    
    // pedirle a Swing que redibuje porque se agregaron componentes dinámicamente
    revalidate();
    repaint();
  }
  
  /// Muestra un mensaje cuando no hay libros comprables en el catálogo.
  private void mostrarCatalogoVacio() {
    JLabel mensaje = new JLabel("No hay libros disponibles en el catálogo por el momento.");
    FabricaEstilos.fabrica(mensaje)
      .etiquetaComoCampo();
    add(mensaje);
  }
}
