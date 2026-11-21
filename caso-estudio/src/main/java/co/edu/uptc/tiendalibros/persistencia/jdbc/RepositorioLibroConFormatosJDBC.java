//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa un repositorio que coordina los repositorios [RepositorioLibroJDBC] y
/// [RepositorioFormatoLibroJDBC] para guardar, buscar, eliminar y listar un libro con sus
/// dos formatos en una sola transacción.
public class RepositorioLibroConFormatosJDBC implements IRepositorioLibroConFormatos {
  /// Objeto que mantiene la conexión con la base de datos.
  private final ConexionBaseDatos conexionBaseDatos;
  
  /// Repositorio JDBC de los libros.
  private final RepositorioLibroJDBC repositorioLibro;
  
  /// Repositorio JDBC de los formatos de libros.
  private final RepositorioFormatoLibroJDBC repositorioFormato;
  
  /// Crea un nuevo objeto [RepositorioLibroConFormatosJDBC] para coordinar los repositorios
  /// [RepositorioLibroJDBC] y [RepositorioFormatoLibroJDBC]
  ///
  /// @param conexionBaseDatos  Objeto que mantiene la conexión con la base de datos.
  /// @param repositorioLibro   Repositorio JDBC de los libros.
  /// @param repositorioFormato Repositorio JDBC de los formatos de libros.
  public RepositorioLibroConFormatosJDBC(
    ConexionBaseDatos conexionBaseDatos,
    RepositorioLibroJDBC repositorioLibro,
    RepositorioFormatoLibroJDBC repositorioFormato
  ) {
    this.conexionBaseDatos = conexionBaseDatos;
    this.repositorioLibro = repositorioLibro;
    this.repositorioFormato = repositorioFormato;
  }

  @Override
  public LibroConFormatos guardar(LibroConFormatos libroConFormatos) throws ViolacionElementoNoExisteException {
    // no se puede guardar un elemento nulo
    if (libroConFormatos == null) {
      throw new IllegalArgumentException("Error: No se puede guardar un libro con formatos nulo.");
    }
    
    // el libro y sus dos formatos se guardan en una sola transacción
    Optional<LibroConFormatos> guardado = this.conexionBaseDatos
      .ejecutarEnTransaccion((conexion) -> guardar(conexion, libroConFormatos));
    
    // un valor vacío significa que algo que se quería actualizar ya no existe
    if (guardado.isEmpty()) {
      throw new ViolacionElementoNoExisteException(
        "No se pudo actualizar el libro o alguno de sus formatos porque ya no existe."
      );
    }
    return guardado.get();
  }
  
  @Override
  public Optional<LibroConFormatos> buscarPorId(Long id) {
    // si el id es nulo, no se busca nada
    if (id == null) {
      return Optional.empty();
    }
    
    // una sola conexión o transacción para obtener el libro y sus formatos
    try (Connection conexion = this.conexionBaseDatos.abrir()) {
      // primero, buscar el libro
      Optional<Libro> libro = this.repositorioLibro.buscarPorId(conexion, id);
      
      if (libro.isEmpty()) {
        // si no hay un libro con ese ID, se devuelve vacío
        return Optional.empty();
      }
      
      // segundo, buscar los formatos que el libro ofrece y construir el libro completo
      List<FormatoLibro> formatos = this.repositorioFormato.buscarPorIdLibro(conexion, id);
      return Optional.of(construirLibroConFormatos(libro.get(), formatos));
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException(
        "Error de la base de datos al ejecutar la operación de buscar libro con formatos.", excepcion
      );
    }
  }
  
  @Override
  public List<LibroConFormatos> listar() {
    // obtener los libros y sus formatos en una sola conexión
    try (Connection conexion = this.conexionBaseDatos.abrir()) {
      
      List<LibroConFormatos> resultados = new ArrayList<>();
      
      // primero, obtener la lista de libros desde el repositorio
      for (Libro libro : this.repositorioLibro.listar(conexion)) {
        // segundo, obtener los formatos de cada libro buscando en el repositorio por el ID del
        // libro
        List<FormatoLibro> formatos = this.repositorioFormato.buscarPorIdLibro(conexion, libro.getId());
        // construir el libro completo que es el libro con sus formatos
        resultados.add(construirLibroConFormatos(libro, formatos));
      }
      
      return resultados;
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException(
        "Error de la base de datos al ejecutar la operación de listar libros con formatos.", excepcion
      );
    }
  }
  
  @Override
  public List<LibroConFormatos> listarComprables() {
    // obtener los libros y sus formatos en una sola conexión
    try (Connection conexion = this.conexionBaseDatos.abrir()) {
      // paso 1: obtener los formatos que se pueden comprar
      // en este paso se obtienen formatos individuales
      // (id_formato 1 )
      // (id_formato 3 )
      // (id_formato 5 )
      // (id_formato 10)
      List<FormatoLibro> comprables = this.repositorioFormato.listarComprables(conexion);
      
      // paso 2: agrupar los formatos por el ID de su libro manteniendo el orden de la consulta:
      // en este paso tendremos para cada formato esta asociación:
      // (idLibro, formato1, formato2). formato1 y formato2 pueden ser impreso o digital
      Map<Long, List<FormatoLibro>> formatosPorLibro = new LinkedHashMap<>();
      
      // vamos a recorrer todos los formatos comprables
      for (FormatoLibro comprable : comprables) {
        // queremos tener una asociación así:
        // idLibro -> [formato1, formato2]
        
        // primero, debe existir el array para el idLibro
        // idLibro -> new ArrayList()
        List<FormatoLibro> formatosDelLibro = formatosPorLibro.getOrDefault(comprable.getIdLibro(), new ArrayList<>());
        // segundo, se agrega el formato comprable actual
        formatosDelLibro.add(comprable);
        // tercero, se agrega la lista en el mapa Map porque está creada pero falta guardarla
        formatosPorLibro.put(comprable.getIdLibro(), formatosDelLibro);
      }
      
      // paso 3: construir el libro con sus formatos comprables
      // queremos tener una asociación así:
      // (libro: "Química Básica", impreso, digital), donde impreso o digital pueden ser vacíos
      List<LibroConFormatos> resultados = new ArrayList<>();
      
      // recorrer el conjunto de formatos por libro
      // actualmente, tiene elementos así:
      // idLibro -> [formato1, formato2]
      for (Entry<Long, List<FormatoLibro>> pareja : formatosPorLibro.entrySet()) {
        // buscar en el repositorio el libro con ese ID
        Long idLibro = pareja.getKey();
        Optional<Libro> libro = this.repositorioLibro.buscarPorId(conexion, idLibro);
        
        // se supone que en la base de datos, un formato tiene un id_libro (clave foránea) que
        // apunta al ID del libro.
        // Si se obtuvo un libro vacío, la base de datos está en un estado inconsistente y esta
        // operación no puede continuar
        if (libro.isEmpty()) {
          throw new ErrorTecnicoSistemaException(
            String.format(
              "Un formato referencia el libro %d, que no existe en la base de datos.",
              idLibro
            ), null
          );
        }
        
        // bien, tenemos el libro, ahora construimos un libro con sus formatos
        List<FormatoLibro> formatos = pareja.getValue();
        LibroConFormatos libroConFormatos = construirLibroConFormatos(libro.get(), formatos);
        // agregar el libro construido a la lista de resultados
        resultados.add(libroConFormatos);
      }
      // hemos terminado, se devuelven los resultados
      return resultados;
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException(
        "Error de la base de datos al ejecutar la operación de listar libros comprables.", excepcion
      );
    }
  }
  
  @Override
  public boolean eliminar(Long id) {
    // no se puede eliminar si el ID es nulo
    if (id == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar con ID nulo");
    }
    // eliminar el libro y sus formatos se hace en la misma transacción
    return this.conexionBaseDatos.ejecutarEnTransaccion((conexion) -> eliminar(conexion, id));
  }
  
  /// Guarda el libro y sus dos formatos con la conexión de la transacción abierta.
  ///
  /// Si el libro o algún formato ya no existe, deshace lo escrito y devuelve un valor vacío,
  /// para evitar guardar un libro incompleto.
  ///
  /// @param conexion        Conexión con la base de datos
  /// @param libroSinGuardar El libro que se quiere guardar.
  /// @return El libro con sus formatos guardados. Vacío si algo que se quería actualizar ya
  ///         no existe.
  private Optional<LibroConFormatos> guardar(Connection conexion, LibroConFormatos libroSinGuardar)
    throws SQLException {
    // primero, guardar el libro
    Optional<Libro> libro = this.repositorioLibro.guardar(conexion, libroSinGuardar.getLibro());
    // si guardar el libro falló, se termina la operación
    if (libro.isEmpty()) {
      return Optional.empty();
    }
    
    // el libro se guardó y tiene su ID.
    Long idLibro = libro.get()
      .getId();
    
    // ahora se asigna cada formato:
    // - el formato se guarda si el libro lo ofrece.
    // - el formato se elimina si el libro dejó de ofrecerlo.
    
    Optional<FormatoLibro> impreso = guardarFormato(
      conexion,
      idLibro,
      TipoFormatoLibro.IMPRESO,
      libroSinGuardar.getFormatoImpreso()
    );
    
    Optional<FormatoLibro> digital = guardarFormato(
      conexion,
      idLibro,
      TipoFormatoLibro.DIGITAL,
      libroSinGuardar.getFormatoDigital()
    );
    
    // guardarFormato devuelve vacío en dos casos:
    // - el libro no ofrece el formato: vacío es lo esperado, no es un fallo.
    // - el libro sí ofrece el formato, pero el repositorio no lo guardó porque el formato
    // tenía ID y ya no existe porque otro usuario lo eliminó (la actualización no afectó
    // ninguna fila): es un fallo.
    // Por eso un formato falló solo si el libro lo ofrecía y el resultado quedó vacío.
    
    boolean falloImpreso = (libroSinGuardar.getFormatoImpreso()
      .isPresent() && impreso.isEmpty());
    
    boolean falloDigital = (libroSinGuardar.getFormatoDigital()
      .isPresent() && digital.isEmpty());
    
    // si guardar (asignar) alguno de los formatos falló, se debe deshacer lo que se guardó o
    // escribió
    if (falloImpreso || falloDigital) {
      // se hace rollback, y pues el commit no tendrá nada que confirmar
      conexion.rollback();
      return Optional.empty();
    }
    
    // devolver el libro con sus formatos
    LibroConFormatos resultado = new LibroConFormatos(libro.get(), impreso, digital);
    return Optional.of(resultado);
  }
  
  /// Guarda el formato del libro dependiendo de si se ofrece o no.
  /// 
  /// El formato del libro se guarda bajo estas condiciones:
  /// 
  /// - Si el libro ofrece el formato, el formato se asocia al libro y se guarda.
  /// - Si el libro no ofrece el formato, el formato se elimina.
  /// 
  /// @param conexion    Conexión de la transacción abierta.
  /// @param idLibro     ID del libro ya guardado.
  /// @param tipoFormato Tipo del formato que se guarda.
  /// @param formato     Formato que el libro ofrece. Vacío si no lo ofrece.
  /// @return El formato guardado. Vacío si el libro no ofrece el formato. Vacío si el
  ///         formato que se quería actualizar ya no existe.
  private Optional<FormatoLibro> guardarFormato(
    Connection conexion,
    Long idLibro,
    TipoFormatoLibro tipoFormato,
    Optional<FormatoLibro> formato
  ) throws SQLException {
    // si el libro no ofrece el formato, se debe eliminar ese formato guardado
    if (formato.isEmpty()) {
      this.repositorioFormato.eliminarPorIdLibroYTipo(conexion, idLibro, tipoFormato);
      // bien, se eliminó el formato, ahora el libro no tiene asignado ese formato
      return Optional.empty();
    }
    
    // aquí, el libro sí ofrece el formato, se debe asociar con el ID del libro y se guarda
    formato.get()
      .setIdLibro(idLibro);
    return this.repositorioFormato.guardar(conexion, formato.get());
  }
  
  /// Elimina los formatos y luego el libro con la conexión de la transacción abierta. Los
  /// formatos se eliminan primero porque tienen una clave foránea hacia el libro.
  private boolean eliminar(Connection conexion, Long id) throws SQLException {
    this.repositorioFormato.eliminarPorIdLibro(conexion, id);
    return this.repositorioLibro.eliminar(conexion, id);
  }
  
  /// Construye un libro con sus formatos opcionales.
  /// 
  /// El formato impreso queda vacío si la lista de formatos no tiene el formato impreso.
  /// 
  /// El formato digital queda vacío si la lista de formatos no tiene el formato digital.
  /// 
  /// @param libro    Libro como obra intelectual.
  /// @param formatos Formatos del libro que se quieren asociar al libro.
  /// @return El libro con sus formatos impreso y digital opcionales.
  private LibroConFormatos construirLibroConFormatos(Libro libro, List<FormatoLibro> formatos) {
    // crear objetos formato de libro provisionales vacíos
    Optional<FormatoLibro> formatoImpreso = Optional.empty();
    Optional<FormatoLibro> formatoDigital = Optional.empty();
    
    // recorrer la lista de formatos y asignar cada tipo de formato.
    // al final, si no se encuentra un formato en la lista, el formato queda vacío
    for (FormatoLibro formato : formatos) {
      // determinar el tipo de formato
      
      if (formato.getTipoFormato() == TipoFormatoLibro.IMPRESO) {
        formatoImpreso = Optional.of(formato);
      }
      
      if (formato.getTipoFormato() == TipoFormatoLibro.DIGITAL) {
        formatoDigital = Optional.of(formato);
      }
    }
    
    return new LibroConFormatos(libro, formatoImpreso, formatoDigital);
  }
}
