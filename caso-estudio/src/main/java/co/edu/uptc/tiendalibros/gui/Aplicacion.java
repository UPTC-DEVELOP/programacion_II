//
package co.edu.uptc.tiendalibros.gui;

import co.edu.uptc.tiendalibros.gui.administracion.ControladorEmpleado;
import co.edu.uptc.tiendalibros.gui.autor.ControladorAutor;
import co.edu.uptc.tiendalibros.gui.catalogo.ControladorExploradorCatalogo;
import co.edu.uptc.tiendalibros.gui.categoria.ControladorCategoria;
import co.edu.uptc.tiendalibros.gui.clientes.ControladorCliente;
import co.edu.uptc.tiendalibros.gui.editorial.ControladorEditorial;
import co.edu.uptc.tiendalibros.gui.eventos.EnrutadorEventos;
import co.edu.uptc.tiendalibros.gui.libro.ControladorLibro;
import co.edu.uptc.tiendalibros.gui.login.ControladorLogin;
import co.edu.uptc.tiendalibros.gui.navegacion.ControladorNavegacion;
import co.edu.uptc.tiendalibros.interfaces.IControlador;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutor;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutorLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioCliente;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEmpleado;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibroConFormatos;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioUsuario;
import co.edu.uptc.tiendalibros.negocio.GestionAutor;
import co.edu.uptc.tiendalibros.negocio.GestionCategoria;
import co.edu.uptc.tiendalibros.negocio.GestionCliente;
import co.edu.uptc.tiendalibros.negocio.catalogo.GestionExploradorCatalogo;
import co.edu.uptc.tiendalibros.negocio.editorial.GestionEditorial;
import co.edu.uptc.tiendalibros.negocio.empleado.GestionEmpleado;
import co.edu.uptc.tiendalibros.negocio.libro.GestionLibro;
import co.edu.uptc.tiendalibros.negocio.seguridad.GestionSeguridad;
import co.edu.uptc.tiendalibros.negocio.seguridad.IProtectorContrasena;
import co.edu.uptc.tiendalibros.negocio.seguridad.ProtectorContrasenaPBKDF2;
import co.edu.uptc.tiendalibros.negocio.seguridad.SesionUsuario;
import co.edu.uptc.tiendalibros.persistencia.archivos.RepositorioAutorLibroEnCSV;
import co.edu.uptc.tiendalibros.persistencia.jdbc.ConexionBaseDatos;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioCategoriaJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioClienteJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioEditorialJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioEmpleadoJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioFormatoLibroJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioLibroConFormatosJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioLibroJDBC;
import co.edu.uptc.tiendalibros.persistencia.jdbc.RepositorioUsuarioJDBC;
import co.edu.uptc.tiendalibros.persistencia.memoria.RepositorioAutorEnMemoria;

/// Representa la aplicación.
/// 
/// Orquesta los componentes de abajo hacia arriba en este orden:
///
/// 1. los repositorios (persistencia).
/// 2. los gestores de la lógica del negocio (negocio), que necesitan de los repositorios.
/// 3. los controladores de la interfaz gráfica, necesitan de los gestores.
public class Aplicacion {
  
  /// Ventana principal de la aplicación.
  private final VentanaPrincipal ventanaPrincipal;
  
  /// Enrutador que redirige los eventos.
  private final EnrutadorEventos enrutadorEventos;
  
  /// Objeto que mantiene la conexión con la base de datos.
  private final ConexionBaseDatos conexionBaseDatos;
  
  // Repositorios: se declaran con el tipo de la interfaz
  
  private IRepositorioEditorial repositorioEditorial;
  private IRepositorioCategoria repositorioCategoria;
  private IRepositorioLibro repositorioLibro;
  private IRepositorioLibroConFormatos repositorioLibroConFormatos;
  private IRepositorioAutor repositorioAutor;
  private IRepositorioAutorLibro repositorioAutorLibro;
  private IRepositorioUsuario repositorioUsuario;
  private IRepositorioEmpleado repositorioEmpleado;
  private IRepositorioCliente repositorioCliente;
  
  // Gestores de la lógica del negocio:. Reciben interfaces de repositorio por el
  // constructor.
  
  private GestionSeguridad gestorSeguridad;
  private GestionEditorial gestorEditorial;
  private GestionCategoria gestorCategoria;
  private GestionAutor gestorAutor;
  private GestionLibro gestorLibro;
  private GestionExploradorCatalogo gestorExplorador;
  private GestionCliente gestorCliente;
  private GestionEmpleado gestorEmpleado;

  /// Crea un nuevo objeto de controlador de aplicación.
  ///
  /// @param ventana  Ventana principal de la aplicación.
  /// @param conexion Objeto que mantiene la conexión con la base de datos.
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
    
    // orquestar la aplicación de abajo hacia arriba
    crearRepositorios();
    crearGestores();
    registrarControladores();
  }
  
  /// Inicia la aplicación.
  public void iniciar() {
    this.ventanaPrincipal.setVisible(true);
  }
  
  /// Paso 1. Crea los repositorios.
  private void crearRepositorios() {
    crearRepositoriosDelCatalogoEnJDBC();
    crearRepositoriosDeCuentasEnJDBC();
    crearRepositoriosDeAutores();
  }
  
  /// Crea los repositorios del catálogo (editoriales, categorías, libros y formatos) en la
  /// base de datos usando la tecnología JDCB.
  ///
  private void crearRepositoriosDelCatalogoEnJDBC() {
    // crear las objetos de los repositorios concretos
    RepositorioEditorialJDBC editorialJDBC = new RepositorioEditorialJDBC(this.conexionBaseDatos);
    RepositorioCategoriaJDBC categoriaJDBC = new RepositorioCategoriaJDBC(this.conexionBaseDatos);
    RepositorioLibroJDBC libroJDBC = new RepositorioLibroJDBC(this.conexionBaseDatos, categoriaJDBC, editorialJDBC);
    RepositorioFormatoLibroJDBC formatoJDBC = new RepositorioFormatoLibroJDBC(this.conexionBaseDatos);
    
    // asignar los objetos concretos a las interfaces
    this.repositorioEditorial = editorialJDBC;
    this.repositorioCategoria = categoriaJDBC;
    this.repositorioLibro = libroJDBC;
    this.repositorioLibroConFormatos = new RepositorioLibroConFormatosJDBC(
      this.conexionBaseDatos, libroJDBC, formatoJDBC
    );
  }
  
  /// Crea los repositorios de las cuentas de usuario, los empleados y los clientes en la
  /// base de datos usando la tecnología JDBC.
  ///
  /// El repositorio de empleados coordina al repositorio de usuarios para guardar la cuenta
  /// de cada empleado en la misma transacción.
  private void crearRepositoriosDeCuentasEnJDBC() {
    RepositorioUsuarioJDBC usuarioJDBC = new RepositorioUsuarioJDBC(this.conexionBaseDatos);
    
    this.repositorioUsuario = usuarioJDBC;
    this.repositorioEmpleado = new RepositorioEmpleadoJDBC(this.conexionBaseDatos, usuarioJDBC);
    this.repositorioCliente = new RepositorioClienteJDBC(this.conexionBaseDatos);
  }
  
  /// Crea los repositorios de los autores.
  private void crearRepositoriosDeAutores() {
    this.repositorioAutor = new RepositorioAutorEnMemoria();
    this.repositorioAutorLibro = new RepositorioAutorLibroEnCSV("datos/autores_libros.csv");
  }
  
  /// Paso 2. Crea los gestores de la lógica del negocio.
  /// 
  /// Cada gestor recibe los repositorios que necesita como interfaces.
  private void crearGestores() {
    // la sesión es la única fuente de información que dice cuál usuario está conectado.
    // la sesión se comparte entre los gestores para tener la información del usuario
    SesionUsuario sesionUsuario = new SesionUsuario();
    IProtectorContrasena protectorContrasena = new ProtectorContrasenaPBKDF2();
    
    // Gestión de Seguridad
    this.gestorSeguridad = new GestionSeguridad(this.repositorioUsuario, protectorContrasena, sesionUsuario);
    
    // Gestión del Catálogo
    this.gestorEditorial = new GestionEditorial(this.repositorioEditorial, this.repositorioLibro, sesionUsuario);
    this.gestorCategoria = new GestionCategoria(this.repositorioCategoria);
    this.gestorAutor = new GestionAutor(this.repositorioAutor);
    
    // Gestión de Libros
    // el gestor de libros necesita de los repositorios de libros, de libros con formatos,
    // editoriales, categorías y asociaciones autor-libro
    this.gestorLibro = new GestionLibro(
      this.repositorioLibro, this.repositorioLibroConFormatos, this.repositorioEditorial,
      this.repositorioCategoria, this.repositorioAutorLibro, sesionUsuario
    );
    
    // Gestión del Explorador
    
    // el gestor del explorador del catálogo necesita el repositorio de libros con
    // formatos para hacer la lista de los libros que se pueden comprar
    this.gestorExplorador = new GestionExploradorCatalogo(this.repositorioLibroConFormatos);
    
    // Gestión de Clientes
    this.gestorCliente = new GestionCliente(this.repositorioCliente, this.repositorioUsuario, this.gestorSeguridad);
    
    // Gestión de Empleados
    this.gestorEmpleado = new GestionEmpleado(
      this.repositorioEmpleado, this.repositorioUsuario, this.gestorSeguridad, sesionUsuario
    );
  }
  
  /// Paso 3. Crea los controladores que intermedian entre la interfaz gráfica y la capa de
  /// lógica, y les pide que registren sus rutas de eventos.
  private void registrarControladores() {
    // panel del contenido principal de la ventana
    PanelContenidoPrincipal contenidoPrincipal = this.ventanaPrincipal.getPanelContenidoPrincipal();
    
    IControlador[] controladores = {
      new ControladorVentanaPrincipal(this.enrutadorEventos, this.ventanaPrincipal),
      new ControladorNavegacion(
        this.enrutadorEventos, contenidoPrincipal,
        this.ventanaPrincipal.getMenuNavegacion(), this.gestorSeguridad
      ),
      
      new ControladorLogin(this.enrutadorEventos, contenidoPrincipal.getPanelLogin(), this.gestorSeguridad),
      
      // el controlador de editoriales necesita el panel de gestión de libros, y
      // necesita el gestor de lógica de editoriales
      new ControladorEditorial(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelEditoriales(),
        this.gestorEditorial
      ),
      
      new ControladorAutor(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelAutores(),
        this.gestorAutor
      ),
      
      new ControladorCategoria(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelCategorias(),
        this.gestorCategoria
      ),
      
      // el controlador de libro necesita el panel de gestión de libros y los gestores de
      // libros, categorías y editoriales
      new ControladorLibro(
        this.enrutadorEventos, contenidoPrincipal.getPanelGestionCatalogo()
          .getPanelLibros(),
        this.gestorLibro, this.gestorCategoria, this.gestorEditorial
      ),
      
      // controlador para la gestión de empleados
      new ControladorEmpleado(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelAdministracionSistema()
          .getPanelEmpleados(),
        this.gestorEmpleado
      ),
      
      // controlador para la gestión de clientes
      new ControladorCliente(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelGestionClientes(),
        this.gestorCliente
      ),
      
      // el controlador del explorador del catálogo necesita el panel que muestra las fichas
      // de los libros y el gestor de lógica del explorador
      new ControladorExploradorCatalogo(
        this.enrutadorEventos,
        contenidoPrincipal.getPanelExploracionCatalogo()
          .getPanelExploradorCatalogo(),
        this.gestorExplorador
      ),
    
    };
    
    // cada controlador es responsable de registrar sus rutas de eventos
    for (IControlador controlador : controladores) {
      controlador.registrarEventos();
    }
  }
  
}