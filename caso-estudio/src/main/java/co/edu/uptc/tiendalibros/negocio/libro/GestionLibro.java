//
package co.edu.uptc.tiendalibros.negocio.libro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutorLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.EstadoEnCatalogo;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.Permiso;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroCantidadPaginasPositivas;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroEstadoNoNulo;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroInventarioPositivo;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroPrecioPositivo;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroTipoIVANoNulo;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroAnioPublicacionValido;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroCategoriaExiste;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroConFormatosAlMenosUnFormato;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroEditorialExiste;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroExiste;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroImagenLongitudMaxima;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroImagenValida;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroIsbnLongitudMaxima;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroIsbnNoVacio;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroIsbnSoloDigitosYGuiones;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroIsbnUnico;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroTituloLongitudMaxima;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroTituloNoVacio;
import co.edu.uptc.tiendalibros.negocio.seguridad.SesionUsuario;
import co.edu.uptc.tiendalibros.negocio.seguridad.reglas.ReglaUsuarioAutorizado;

/// Representa un gestor de la lógica de negocio para los libros como obras intelectuales,
/// junto con sus formatos comerciales impreso y digital.
///
/// Un libro se registra, se actualiza y se elimina siempre como un todo con sus formatos,
/// por eso la clase [LibroConFormatos].
public class GestionLibro {
  
  /// Repositorio de datos de los libros.
  private final IRepositorioLibro repositorioLibro;
  
  /// Repositorio de datos de las asociaciones autor-libro.
  private final IRepositorioAutorLibro repositorioAutorLibro;
  
  /// Repositorio que guarda, busca y elimina un libro junto con sus dos formatos en una
  /// sola operación atómica.
  private final IRepositorioLibroConFormatos repositorioLibroConFormatos;
  
  /// Repositorio de datos de las editoriales.
  private final IRepositorioEditorial repositorioEditorial;
  
  /// Repositorio de datos de las categorías.
  private final IRepositorioCategoria repositorioCategoria;
  
  /// Lista de reglas de validación que se aplican al guardar un libro.
  private final List<IReglaValidacion<Libro>> reglasValidacionGuardarLibro;
  
  /// Lista de reglas de validación que se aplican al guardar un libro con sus formatos como
  /// un todo.
  private final List<IReglaValidacion<LibroConFormatos>> reglasValidacionGuardarLibroConFormatos;
  
  /// Lista de reglas de validación que se aplican al guardar un formato de libro impreso.
  private final List<IReglaValidacion<FormatoLibro>> reglasValidacionGuardarFormatoLibroImpreso;
  
  /// Lista de reglas de validación que se aplican al guardar un formato de libro digital.
  private final List<IReglaValidacion<FormatoLibro>> reglasValidacionGuardarFormatoLibroDigital;
  
  /// Lista de reglas de validación que se aplican al eliminar un libro.
  private final List<IReglaValidacion<Libro>> reglasValidacionEliminarLibro;
  
  /// Sesión del usuario que usa el programa, para verificar sus permisos.
  private final SesionUsuario sesionUsuario;
  
  /// Crea un nuevo objeto [GestionLibro].
  ///
  /// @param repositorioLibro            Repositorio de datos de los libros.
  /// @param repositorioLibroConFormatos Repositorio que guarda un libro con sus formatos de
  ///                                    forma atómica.
  /// @param repositorioEditorial        Repositorio de datos de las editoriales.
  /// @param repositorioCategoria        Repositorio de datos de las categorías.
  /// @param repositorioAutorLibro       Repositorio de datos de las
  /// @param sesionUsuario               Sesión del usuario, para verificar sus permisos.
  ///                                    asociaciones autor-libro.
  public GestionLibro(
    IRepositorioLibro repositorioLibro,
    IRepositorioLibroConFormatos repositorioLibroConFormatos,
    IRepositorioEditorial repositorioEditorial,
    IRepositorioCategoria repositorioCategoria,
    IRepositorioAutorLibro repositorioAutorLibro,
    SesionUsuario sesionUsuario
  ) {
    this.repositorioLibro = repositorioLibro;
    this.repositorioLibroConFormatos = repositorioLibroConFormatos;
    this.repositorioEditorial = repositorioEditorial;
    this.repositorioCategoria = repositorioCategoria;
    this.repositorioAutorLibro = repositorioAutorLibro;
    this.sesionUsuario = sesionUsuario;
    
    // agregar las reglas de validación para guardar un libro, ordenadas por atributo
    this.reglasValidacionGuardarLibro = new ArrayList<>(
      List.of(
        new ReglaLibroTituloNoVacio(),
        new ReglaLibroTituloLongitudMaxima(),
        new ReglaLibroIsbnNoVacio(),
        new ReglaLibroIsbnLongitudMaxima(),
        new ReglaLibroIsbnSoloDigitosYGuiones(),
        new ReglaLibroIsbnUnico(this.repositorioLibro),
        new ReglaLibroAnioPublicacionValido(),
        new ReglaLibroEditorialExiste(this.repositorioEditorial),
        new ReglaLibroCategoriaExiste(this.repositorioCategoria),
        new ReglaLibroImagenValida(),
        new ReglaLibroImagenLongitudMaxima()
      )
    );
    
    // agregar las reglas de validación para eliminar un libro.
    // TODO: agregar ReglaLibroSinVentasAsociadas cuando exista el módulo de ventas.
    this.reglasValidacionEliminarLibro = new ArrayList<>();
    
    // agregar las reglas de validación del libro con sus formatos
    this.reglasValidacionGuardarLibroConFormatos = new ArrayList<>(
      List.of(
        new ReglaLibroConFormatosAlMenosUnFormato()
      )
    );
    
    // agregar las reglas de validación para guardar cada formato de libro. Se usa un prefijo
    // para distinguir los errores de cada formato
    this.reglasValidacionGuardarFormatoLibroImpreso = crearReglasFormato("impreso");
    this.reglasValidacionGuardarFormatoLibroDigital = crearReglasFormato("digital");
  }
  
  /// Guarda el libro con sus dos formatos impreso y digital en el repositorio de datos.
  ///
  /// El libro debe ofrecerse al menos en un formato. Los formatos son opcionales: solo se
  /// validan y se guardan los formatos que el libro ofrece.
  /// 
  /// Si el libro es nuevo (ID = nulo), se inserta el libro, y luego los formatos que ofrece.
  ///
  /// Si el libro ya existe (ID != nulo), se actualiza el libro y se actualizan sus formatos.
  /// 
  /// Un formato que el libro tenía guardado y que ya no ofrece se elimina.
  /// 
  /// `libroConFormatos` no es nulo.
  /// 
  /// Si la respuesta es exitosa, el libro y sus formatos quedaron guardados y la respuesta
  /// los trae con sus IDs asignados.
  /// 
  /// Si la respuesta es fallida, ni el libro ni sus formatos se modificaron en el
  /// repositorio.
  ///
  /// @param libroConFormatos Libro con sus dos formatos que se quiere guardar.
  /// @return Respuesta con el resultado de la operación de guardar.
  public Respuesta<LibroConFormatos> guardar(LibroConFormatos libroConFormatos) {
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // primero, verificar que el usuario tiene permiso. Si no lo tiene, no se valida nada más
    boolean esElementoNuevo = (libroConFormatos.getId() == null);
    
    IReglaValidacion<LibroConFormatos> reglaAutorizacion = null;
    
    if (esElementoNuevo) {
      reglaAutorizacion = new ReglaUsuarioAutorizado<LibroConFormatos>(
        this.sesionUsuario, "crear libros", Permiso.LIBRO_CREAR
      );
    } else {
      reglaAutorizacion = new ReglaUsuarioAutorizado<LibroConFormatos>(
        this.sesionUsuario, "editar libros",
        Permiso.LIBRO_EDITAR
      );
    }
    
    reglaAutorizacion.validar(errores, libroConFormatos);
    
    if (errores.hayErrores()) {
      return new RespuestaFallida<>(Optional.of(libroConFormatos), "No se guardó el libro.", errores);
    }
    
    if (esElementoNuevo) {
      // un libro nuevo siempre empieza con sus formatos en estado PENDIENTE
      forzarEstadoPendiente(libroConFormatos);
    }
    
    // obtener el libro como obra intelectual
    Libro libro = libroConFormatos.getLibro();
    
    // copiar la lista de reglas de validación para guardar
    List<IReglaValidacion<Libro>> reglasParaValidar = new ArrayList<>(this.reglasValidacionGuardarLibro);
    
    // agregar la regla de negocio de verificar primero que el libro exista si se quiere
    // actualizar el libro
    if (libro.getId() != null) {
      reglasParaValidar.addFirst(new ReglaLibroExiste(this.repositorioLibro));
    }
    
    // preparar los atributos del libro antes de validar
    prepararParaGuardar(libro);
    
    // validar las reglas de negocio del libro
    for (IReglaValidacion<Libro> regla : reglasParaValidar) {
      regla.validar(errores, libro);
    }
    
    // validar las reglas de negocio del libro con sus formatos
    for (IReglaValidacion<LibroConFormatos> regla : this.reglasValidacionGuardarLibroConFormatos) {
      regla.validar(errores, libroConFormatos);
    }
    
    // validar las reglas de negocio de cada formato de libro que el libro ofrece
    validarFormato(libroConFormatos.getFormatoImpreso(), this.reglasValidacionGuardarFormatoLibroImpreso, errores);
    validarFormato(libroConFormatos.getFormatoDigital(), this.reglasValidacionGuardarFormatoLibroDigital, errores);
    
    // si las reglas de validación provocaron errores se notifica la respuesta
    if (errores.hayErrores()) {
      return new RespuestaFallida<>(
        Optional.of(libroConFormatos),
        "Hay errores en los datos ingresados para el libro.",
        errores
      );
    }
    
    // intentar guardar el libro y sus formatos en los repositorios
    try {
      LibroConFormatos guardado = this.repositorioLibroConFormatos.guardar(libroConFormatos);
      
      return new RespuestaExitosa<>(Optional.of(guardado), "Se guardó el libro.");
    } catch (ViolacionElementoNoExisteException excepcion) {
      errores.agregarGeneral(excepcion.getMessage());
      return new RespuestaFallida<>(
        Optional.of(libroConFormatos),
        String.format("No se pudo guardar el libro '%s'.", libro.getTitulo()),
        errores
      );
    }
  }
  
  /// Elimina el libro con el ID especificado, todos sus formatos y las asociaciones con los
  /// autores.
  /// 
  /// El libro solo se elimina si cumple todas las reglas de validación para eliminar.
  /// 
  /// Si la respuesta es exitosa, el libro, sus formatos y sus asociaciones con los autores
  /// ya no existen porque fueron borrados del repositorio.
  /// 
  /// Si la respuesta es fallida, el libro conserva sus formatos y sus autores, y todavía
  /// están en el repositorio.
  ///
  /// @param id ID del libro que se quiere eliminar.
  /// @return Respuesta con el resultado de la operación de eliminar.
  public Respuesta<Libro> eliminar(Long id) {
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // primero, verificar que el usuario tiene permiso. Si no lo tiene, no se busca nada
    new ReglaUsuarioAutorizado<Long>(this.sesionUsuario, "eliminar libros", Permiso.LIBRO_ELIMINAR)
      .validar(errores, id);
    
    // buscar el libro como obra intelectual
    Optional<Libro> libro = this.repositorioLibro.buscarPorId(id);
    
    // si no se encontró el libro con ese id, se debe enviar una respuesta fallida
    if (libro.isEmpty()) {
      errores.agregarViolacion(
        new ViolacionElementoNoExisteException("El libro ya no existe en el repositorio de datos.")
      );
      return new RespuestaFallida<>(Optional.empty(), "No se pudo eliminar el libro.", errores);
    }
    
    // verificar las reglas de negocio para eliminar el libro
    for (IReglaValidacion<Libro> regla : this.reglasValidacionEliminarLibro) {
      regla.validar(errores, libro.get());
    }
    
    // si las reglas de validación provocaron errores se notifica la respuesta
    if (errores.hayErrores()) {
      return new RespuestaFallida<>(
        libro,
        String.format(
          "No se puede eliminar el libro '%s'.",
          libro.get()
            .getTitulo()
        ),
        errores
      );
    }
    
    // primero, eliminar el libro con sus formatos en una sola operación atómica y verificar
    // que realmente se eliminó
    if (!this.repositorioLibroConFormatos.eliminar(id)) {
      errores.agregarGeneral("El repositorio no pudo eliminar el libro.");
      return new RespuestaFallida<>(libro, "No se pudo eliminar el libro.", errores);
    }
    
    // finalmente, eliminar las asociaciones autor-libro. Se hace al final: si algo falla
    // antes, el libro conserva sus autores
    this.repositorioAutorLibro.eliminarPorLibro(id);
    
    return new RespuestaExitosa<>(libro, "Se eliminó el libro.");
  }
  
  /// Carga la lista de libros del repositorio de datos.
  ///
  /// @return Lista con los libros cargados del repositorio.
  public List<Libro> listar() {
    return this.repositorioLibro.listar();
  }
  
  /// Busca el libro con el ID especificado junto con sus dos formatos.
  ///
  /// @param id ID del libro que se quiere buscar.
  /// @return El libro encontrado con sus dos formatos. Vacío si el libro no existe.
  public Optional<LibroConFormatos> buscarLibroConFormatos(Long id) {
    return this.repositorioLibroConFormatos.buscarPorId(id);
  }
  
  /// Prepara el libro para ser guardado: normaliza los atributos de texto eliminando
  /// espacios al inicio y al final.
  ///
  /// @param libro Libro que se desea preparar.
  public void prepararParaGuardar(Libro libro) {
    libro.setIsbn(
      libro.getIsbn() == null ? ""
        : libro.getIsbn()
          .trim()
    );
    libro.setTitulo(
      libro.getTitulo() == null ? ""
        : libro.getTitulo()
          .trim()
    );
    libro.setImagenLibro(
      libro.getImagenLibro() == null ? ""
        : libro.getImagenLibro()
          .trim()
    );
  }
  
  /// Indica si el usuario de la sesión actual tiene permiso para crear libros.
  /// 
  /// La interfaz lo usa para habilitar o deshabilitar el botón de crear.
  ///
  /// @return `true` si el usuario puede crear editoriales.
  public boolean usuarioPuedeCrear() {
    return this.sesionUsuario.tienePermiso(Permiso.LIBRO_CREAR);
  }
  
  /// Aplica las reglas de validación de un formato de libro y acumula los errores en el
  /// resultado especificado.
  /// 
  /// Si el libro no ofrece el formato, no hay nada que validar.
  ///
  /// @param formato Formato de libro que se quiere validar. Vacío si el libro no lo ofrece.
  /// @param reglas  Lista de reglas que se aplican al formato.
  /// @param errores Objeto en el que se acumulan los errores de validación.
  private void validarFormato(
    Optional<FormatoLibro> formato,
    List<IReglaValidacion<FormatoLibro>> reglas,
    ResultadoValidacion errores
  ) {
    // no hay formato, no se valida nada
    if (formato.isEmpty()) {
      return;
    }
    
    for (IReglaValidacion<FormatoLibro> regla : reglas) {
      regla.validar(errores, formato.get());
    }
  }
  
  /// Crea las reglas de validación que se aplican a un formato de libro.
  ///
  /// @param prefijo Prefijo para anteponer a los nombres de atributo para distinguirlos.
  ///                Ejemplo: `impreso` o `digital`.
  /// @return Lista con las reglas de validación de un formato de libro.
  private static List<IReglaValidacion<FormatoLibro>> crearReglasFormato(String prefijo) {
    return new ArrayList<>(
      List.of(
        new ReglaFormatoLibroInventarioPositivo(prefijo + ".inventario"),
        new ReglaFormatoLibroCantidadPaginasPositivas(prefijo + ".cantidadPaginas"),
        new ReglaFormatoLibroPrecioPositivo(prefijo + ".precioBase"),
        new ReglaFormatoLibroTipoIVANoNulo(prefijo + ".tipoIVA"),
        new ReglaFormatoLibroEstadoNoNulo(prefijo + ".estadoEnCatalogo")
      )
    );
  }
  
  /// Pone en estado PENDIENTE los formatos que ofrece un libro nuevo, sin importar el
  /// estado que haya elegido el usuario.
  ///
  /// @param libroConFormatos Libro nuevo con sus formatos. No es nulo.
  private void forzarEstadoPendiente(LibroConFormatos libroConFormatos) {
    if (libroConFormatos.getFormatoImpreso()
      .isPresent()) {
      libroConFormatos.getFormatoImpreso()
        .get()
        .setEstadoEnCatalogo(EstadoEnCatalogo.PENDIENTE);
    }
    
    if (libroConFormatos.getFormatoDigital()
      .isPresent()) {
      libroConFormatos.getFormatoDigital()
        .get()
        .setEstadoEnCatalogo(EstadoEnCatalogo.PENDIENTE);
    }
  }
  
}
