//
package co.edu.uptc.tiendalibros.gui;

import co.edu.uptc.tiendalibros.gui.autor.ControladorAutor;
import co.edu.uptc.tiendalibros.gui.categoria.ControladorCategoria;
import co.edu.uptc.tiendalibros.gui.editorial.ControladorEditorial;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.gui.libro.ControladorLibro;
import co.edu.uptc.tiendalibros.gui.navegacion.ControladorNavegacion;
import co.edu.uptc.tiendalibros.interfaces.IControlador;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutor;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutorLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibroConFormatos;
import co.edu.uptc.tiendalibros.negocio.GestionAutor;
import co.edu.uptc.tiendalibros.negocio.GestionCategoria;
import co.edu.uptc.tiendalibros.negocio.editorial.GestionEditorial;
import co.edu.uptc.tiendalibros.negocio.libro.GestionLibro;
import co.edu.uptc.tiendalibros.persistencia.archivos.RepositorioAutorLibroEnCSV;
import co.edu.uptc.tiendalibros.persistencia.jdbc.ConexionBaseDatos;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioEditorialJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioFormatoLibroJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioLibroConFormatosJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioLibroJDBC;
import co.edu.uptc.tiendalibros.persistencia.memoria.RepositorioAutorenMemoria;
import co.edu.uptc.tiendalibros.persistencia.memoria.RepositorioCategoriaEnMemoria;

/// Representa la aplicación.
public class Aplicacion {
  
  /// Ventana principal de la aplicación.
  private final VentanaPrincipal ventanaPrincipal;
  
  /// Enrutador que redirige los eventos.
  private final EnrutadorEventos enrutadorEventos;
  
  /// Objeto que mantiene la conexión con la base de datos.
  private final ConexionBaseDatos conexionBaseDatos;
  
  /// Crea un nuevo objeto de controlador de aplicación.
  ///
  /// @param ventana           Ventana principal de la aplicación.
  /// @param conexionBaseDatos Objeto que mantiene la conexión con la base de datos.
  public Aplicacion(
    VentanaPrincipal ventana,
    ConexionBaseDatos conexion
  
  ) {
    // asignar la ventana principal
    this.ventanaPrincipal = ventana;
    
    // obtener el objeto enrutador
    this.enrutadorEventos = this.ventanaPrincipal.getEnrutador();
    
    // asignar la conexión con la base de datos
    this.conexionBaseDatos = conexion;
    
    // continuar creando los otros componentes de la aplicación
    registrarControladores();
  }
  
  /// Inicia la aplicación.
  public void iniciar() {
    this.ventanaPrincipal.setVisible(true);
  }
  
  /// Registra los controladores de la aplicación.
  public void registrarControladores() {
    // panel del contenido principal de la ventana
    PanelContenidoPrincipal contenidoPrincipal = this.ventanaPrincipal.getPanelContenidoPrincipal();
    
    // crear los repositorios de datos.
    IRepositorioEditorial repositorioEditorial = new RepositorioEditorialJDBC(this.conexionBaseDatos);
    
    IRepositorioAutor repositorioAutor = new RepositorioAutorenMemoria();
    
    IRepositorioCategoria repositorioCategoria = new RepositorioCategoriaEnMemoria();
    
    IRepositorioAutorLibro repositorioAutorLibro = new RepositorioAutorLibroEnCSV("datos/autores_libros.csv");
    
    RepositorioLibroJDBC repositorioLibroJDBC = new RepositorioLibroJDBC(this.conexionBaseDatos);
    
    RepositorioFormatoLibroJDBC repositorioFormatoLibroJDBC = new RepositorioFormatoLibroJDBC(this.conexionBaseDatos);
    
    IRepositorioLibro repositorioLibro = repositorioLibroJDBC;
    
    IRepositorioLibroConFormatos repositorioLibroConFormatos = new RepositorioLibroConFormatosJDBC(
      this.conexionBaseDatos, repositorioLibroJDBC, repositorioFormatoLibroJDBC
    );
    
    // crear los gestores de lógica del negocio
    GestionEditorial gestorEditorial = new GestionEditorial(repositorioEditorial, repositorioLibro);
    
    GestionAutor gestorAutor = new GestionAutor(repositorioAutor);
    
    GestionCategoria gestorCategoria = new GestionCategoria(repositorioCategoria);
    
 // el gestor de libros necesita el repositorio de libros, el de libros con formatos,
    // editoriales, categorías y asociaciones autor-libro
    GestionLibro gestorLibro = new GestionLibro(
      repositorioLibro, repositorioLibroConFormatos, repositorioEditorial, repositorioCategoria,
      repositorioAutorLibro
    );
    
    // crear los controladores que intermedian entre la interfaz gráfica y la capa de lógica
    
    IControlador[] controladores = {
      new ControladorVentanaPrincipal(this.enrutadorEventos, this.ventanaPrincipal),
      new ControladorNavegacion(this.enrutadorEventos, contenidoPrincipal, this.ventanaPrincipal.getMenuNavegacion()),
      
      // el controlador de editoriales necesita el panel de gestión de libros, y
      // necesita el gestor de lógica de editoriales
      new ControladorEditorial(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelEditoriales(),
        gestorEditorial
      ),
      
      new ControladorAutor(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelAutores(),
        gestorAutor
      ),
      
      new ControladorCategoria(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelCategorias(),
        gestorCategoria
      ),
      
      // el controlador de libro necesita el panel de gestión de libros y los gestores de
      // libros,
      // categorías y editoriales
      new ControladorLibro(
        this.enrutadorEventos, contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelLibros(),
        gestorLibro, gestorCategoria, gestorEditorial
      ),
    };
    
    // cada controlador es responsable de registrar sus rutas de eventos
    for (IControlador controlador : controladores) {
      controlador.registrarEventos();
    }
  }
  
}
