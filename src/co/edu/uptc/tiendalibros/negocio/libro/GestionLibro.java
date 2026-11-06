//
package co.edu.uptc.tiendalibros.negocio.libro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutorLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioFormatoLibro;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.FormatoLibroBuilder;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.negocio.IReglaValidacion;
import co.edu.uptc.tiendalibros.negocio.Respuesta;
import co.edu.uptc.tiendalibros.negocio.RespuestaExitosa;
import co.edu.uptc.tiendalibros.negocio.RespuestaFallida;
import co.edu.uptc.tiendalibros.negocio.ResultadoValidacion;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroCantidadPaginasPositivas;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroInventarioPositivo;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroPrecioPositivo;
import co.edu.uptc.tiendalibros.negocio.formatoLibro.reglas.ReglaFormatoLibroTipoIVANoNulo;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroCategoriaExiste;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroEditorialExiste;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroExiste;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroImagenValida;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroIsbnNoVacio;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroIsbnUnico;
import co.edu.uptc.tiendalibros.negocio.libro.reglas.ReglaLibroTituloNoVacio;

/// Representa un gestor de la lógica de negocio para los libros como obras intelectuales.
///
public class GestionLibro {
  
  /// Repositorio de datos de los libros.
  private final IRepositorioLibro repositorioLibro;
  
  /// Repositorio de datos de las asociaciones autor-libro
  private final IRepositorioAutorLibro repositorioAutorLibro;
  
  /// Repositorio de datos de los formatos de libros.
  private final IRepositorioFormatoLibro repositorioFormato;
  
  /// Repositorio de datos de las editoriales.
  private final IRepositorioEditorial repositorioEditorial;
  
  /// Repositorio de datos de las categorías.
  private final IRepositorioCategoria repositorioCategoria;
  
  /// Lista de reglas de validación que se aplican al guardar un libro.
  private List<IReglaValidacion<Libro>> reglasValidacionGuardarLibro;
  
  /// Lista de reglas de validación que se aplican al guardar un formato de libro impreso.
  private List<IReglaValidacion<FormatoLibro>> reglasValidacionGuardarFormatoLibroImpreso;
  
  /// Lista de reglas de validación que se aplican al guardar un formato de libro digital.
  private List<IReglaValidacion<FormatoLibro>> reglasValidacionGuardarFormatoLibroDigital;
  
  /// Crea un nuevo objeto [GestionLibro].
  ///
  /// @param repositorioLibro      Repositorio de datos de los libros.
  /// @param repositorioFormato    Repositorio de datos de los formatos de libros.
  /// @param repositorioEditorial  Repositorio de datos de las editoriales.
  /// @param repositorioCategoria  Repositorio de datos de las categorías.
  /// @param repositorioAutorLibro Repositorio de datos de las asociaciones autor-libro.
  public GestionLibro(
    IRepositorioLibro repositorioLibro,
    IRepositorioFormatoLibro repositorioFormato,
    IRepositorioEditorial repositorioEditorial,
    IRepositorioCategoria repositorioCategoria,
    IRepositorioAutorLibro repositorioAutorLibro
  ) {
    this.repositorioLibro = repositorioLibro;
    this.repositorioFormato = repositorioFormato;
    this.repositorioEditorial = repositorioEditorial;
    this.repositorioCategoria = repositorioCategoria;
    this.repositorioAutorLibro = repositorioAutorLibro;
    
    // agregar las reglas de validación para guardar un libro, ordenadas por atributo
    this.reglasValidacionGuardarLibro = new ArrayList<>(
      List.of(
        new ReglaLibroTituloNoVacio(),
        new ReglaLibroIsbnNoVacio(),
        new ReglaLibroIsbnUnico(this.repositorioLibro),
        new ReglaLibroEditorialExiste(this.repositorioEditorial),
        new ReglaLibroCategoriaExiste(this.repositorioCategoria),
        new ReglaLibroImagenValida()
      )
    );
    
    // agregar las reglas de validación para guardar cada formato de libro. Se usa un prefijo
    // para distinguir los errores de cada formato
    this.reglasValidacionGuardarFormatoLibroImpreso = crearReglasFormato("impreso");
    this.reglasValidacionGuardarFormatoLibroDigital = crearReglasFormato("digital");
  }
  
  /// Guarda el libro con sus dos formatos impreso y digital en el repositorio de datos.
  ///
  ///
  /// Si el libro es nuevo (ID = nulo), se guarda el libro, y luego sus dos formatos impreso
  /// y digital.
  ///
  /// Si el libro ya existe (ID != nulo), se actualiza el libro y se actualizan sus formatos.
  ///
  /// @param libroConFormatos Libro con sus dos formatos que se quiere guardar.
  /// @return Respuesta con el resultado de la operación de guardar..
  public Respuesta<LibroConFormatos> guardar(LibroConFormatos libroConFormatos) {
    ResultadoValidacion errores = new ResultadoValidacion();
    
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
    
    // validar las reglas de negocio de cada formato de libro
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
      Libro libroGuardado = this.repositorioLibro.guardar(libro);
      
      FormatoLibro impresoGuardado = guardarFormato(libroGuardado.getId(), libroConFormatos.getFormatoImpreso());
      FormatoLibro digitalGuardado = guardarFormato(libroGuardado.getId(), libroConFormatos.getFormatoDigital());
      
      return new RespuestaExitosa<>(
        Optional.of(new LibroConFormatos(libroGuardado, impresoGuardado, digitalGuardado)),
        "Se guardó el libro."
      );
    } catch (ViolacionElementoNoExisteException excepcion) {
      errores.agregarGeneral(excepcion.getMessage());
      return new RespuestaFallida<>(
        Optional.of(libroConFormatos),
        String.format("No se pudo guardar el libro '%s'.", libro.getTitulo()),
        errores
      );
    }
  }
  
  /// Elimina el libro con el ID especificad, todos sus formatos y las asociaciones con los
  /// autores.
  ///
  /// @param id ID del libro que se quiere eliminar.
  /// @return Respuesta con el resultado de la operación de eliminar.
  public Respuesta<Libro> eliminar(Long id) {
    ResultadoValidacion errores = new ResultadoValidacion();
    
    // buscar el libro como obra intelectual
    Optional<Libro> libro = this.repositorioLibro.buscarPorId(id);
    
    // si no se encontró el libro con ese id, se debe enviar una respuesta fallida
    if (libro.isEmpty()) {
      errores.agregarViolacion(
        new ViolacionElementoNoExisteException("El libro ya no existe en el repositorio de datos.")
      );
      return new RespuestaFallida<>(Optional.empty(), "No se pudo eliminar el libro.", errores);
    }
    
    // TODO: aquí debe verificarse que el libro y sus formatos no tengan ventas asociadas
    
    // primero, eliminar las asociaciones autor-libro y los formatos del libro
    this.repositorioAutorLibro.eliminarPorLibro(id);
    eliminarFormatosLibro(id);
    
    // finalmente, eliminar el libro y verificar que realmente se eliminó
    if (!this.repositorioLibro.eliminar(id)) {
      errores.agregarGeneral("El repositorio no pudo eliminar el libro.");
      return new RespuestaFallida<>(libro, "No se pudo eliminar el libro.", errores);
    }
    
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
    // buscar el libro como obra intelectual
    Optional<Libro> libro = this.repositorioLibro.buscarPorId(id);
    
    // si el libro como obra intelectual no existe, entonces no hay libro con formatos
    if (libro.isEmpty()) {
      return Optional.empty();
    }
    
    // crear formatos con valores predeterminados, en el caso raro de que al libro guardado le
    // falte un formato comercial
    FormatoLibro formatoImpreso = new FormatoLibroBuilder().tipoFormatoLibro(TipoFormatoLibro.IMPRESO)
      .construir();
    FormatoLibro formatoDigital = new FormatoLibroBuilder().tipoFormatoLibro(TipoFormatoLibro.DIGITAL)
      .construir();
    
    // obtener los formatos comerciales del libro
    for (FormatoLibro formato : this.repositorioFormato.buscarPorIdLibro(id)) {
      // verificar el tipo de formato y asignar el formato respectivo
      if (formato.getTipoFormato() == TipoFormatoLibro.IMPRESO) {
        formatoImpreso = formato;
      } else if (formato.getTipoFormato() == TipoFormatoLibro.DIGITAL) {
        formatoDigital = formato;
      }
    }
    
    // devolver el libro con sus formatos
    return Optional.of(new LibroConFormatos(libro.get(), formatoImpreso, formatoDigital));
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
  
  /// Guardar el formato comercial asociándolo con el ID del libro.
  ///
  /// Si el formato no tiene ID propio, entonces el formato se crea en el repositorio. Si el
  /// formato ya tiene ID propio, entonces el formato se actualiza en el repositorio.
  ///
  /// @return El formato guardado, incluyendo su ID generado.
  /// @throws ViolacionElementoNoExisteException Si el formato tiene ID pero ya no existe en
  ///                                            el repositorio.
  private FormatoLibro guardarFormato(Long idLibro, FormatoLibro formato) throws ViolacionElementoNoExisteException {
    formato.setIdLibro(idLibro);
    return this.repositorioFormato.guardar(formato);
  }
  
  /// Elimina todos los formatos asociados al libro con el ID especificado.
  ///
  /// @param idLibro ID del libro cuyos formatos se quieren eliminar.
  private void eliminarFormatosLibro(Long idLibro) {
    List<FormatoLibro> formatos = this.repositorioFormato.buscarPorIdLibro(idLibro);
    for (FormatoLibro formato : formatos) {
      this.repositorioFormato.eliminar(formato.getId());
    }
  }
  
  /// Aplica las reglas de validación de un formato de libro y acumula los errores en el
  /// resultado especificado.
  ///
  /// @param formato Formato de libro que se quiere validar.
  /// @param reglas  Lista de reglas que se aplican al formato.
  /// @param errores Objeto en el que se acumulan los errores de validación.
  private void validarFormato(
    FormatoLibro formato,
    List<IReglaValidacion<FormatoLibro>> reglas,
    ResultadoValidacion errores
  ) {
    for (IReglaValidacion<FormatoLibro> regla : reglas) {
      regla.validar(errores, formato);
    }
  }
  
  /// Crea las reglas de validación que se aplican a un formato de libro.
  ///
  /// @param prefijo Prefijo para anteponer a los nombres de atributo para distinguirlos.
  ///                Ejemplo: `impreso` o `digital.
  /// @return Lista con las reglas de validación de un formato de libro.
  private static List<IReglaValidacion<FormatoLibro>> crearReglasFormato(String prefijo) {
    return new ArrayList<>(
      List.of(
        new ReglaFormatoLibroInventarioPositivo(prefijo + ".inventario"),
        new ReglaFormatoLibroCantidadPaginasPositivas(prefijo + ".cantidadPaginas"),
        new ReglaFormatoLibroPrecioPositivo(prefijo + ".precioBase"),
        new ReglaFormatoLibroTipoIVANoNulo(prefijo + ".tipoIVA")
      )
    );
  }
  
}
