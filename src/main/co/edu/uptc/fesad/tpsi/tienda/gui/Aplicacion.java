//
package main.co.edu.uptc.fesad.tpsi.tienda.gui;

import main.co.edu.uptc.fesad.tpsi.tienda.gui.editorial.ControladorEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.gui.eventos.EnrutadorEventos;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IControlador;
import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IRepositorioEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.negocio.GestionEditorial;
import main.co.edu.uptc.fesad.tpsi.tienda.persistencia.RepositorioEditorialEnMemoria;

/// Representa la aplicación.
public class Aplicacion {
  

  private final VentanaPrincipal ventanaPrincipal;
  
  private final EnrutadorEventos enrutadorEventos;
  
  /// Crea un nuevo objeto de controlador de aplicación.
  public Aplicacion(
    VentanaPrincipal ventana
  
  ) {
    // asignar la ventana principal
    this.ventanaPrincipal = ventana;
    
    // obtener el objeto enrutador
    this.enrutadorEventos = this.ventanaPrincipal.getEnrutador();

    
    // continuar creando los otros componentes de la aplicación
    registrarControladores();
  }
  
  public void iniciar() {
    this.ventanaPrincipal.setVisible(true);
  }

  
  public void registrarControladores() {
    // panel del contenido principal de la ventana
    PanelContenidoPrincipal contenidoPrincipal = this.ventanaPrincipal.getPanelContenidoPrincipal();
    
 // crear los repositorios de datos. Se usa el poliformismo para adaptarse a cualquier
    // repositorio: en este caso, un repositorio en la memoria RAM.
    IRepositorioEditorial repositorioEditorial = new RepositorioEditorialEnMemoria();
    
    // crear los gestores de lógica del negocio
    GestionEditorial gestorEditorial = new GestionEditorial(repositorioEditorial);
    
    // crear los controladores que intermedian entre la interfaz gráfica y la capa de lógica
    
    IControlador[] controladores = {
      new ControladorNavegacion(contenidoPrincipal, this.enrutadorEventos),
      // el controlador de un editorial necesita el panel que contiene los editoriales, y necesita el gestor de lógica de editoriales
      new ControladorEditorial(
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelEditoriales(),
        gestorEditorial
      )
    };
    
    // cada controlador es responsable de registrar sus rutas de eventos
    for (IControlador controlador : controladores) {
      controlador.registrarEventos(this.enrutadorEventos);
    }
  }
  

  
}
