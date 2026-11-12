//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroConFormatos;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa un repositorio que coordina los repositorios [RepositorioLibroJDBC] y
/// [RepositorioFormatoLibroJDBC] para guardar, buscar y eliminar un libro con sus dos
/// formatos en una sola transacción.
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
      
      // segundo, buscar los formatos que el libro ofrece. Si no encuentra el formato, el
      // formato queda vacío, y quiere decir que el libro no se ofrece en ese formato
      Optional<FormatoLibro> formatoImpreso = Optional.empty();
      Optional<FormatoLibro> formatoDigital = Optional.empty();
      
      for (FormatoLibro formato : this.repositorioFormato.buscarPorIdLibro(conexion, id)) {
        // determinar qué tipo de formato es
        if (formato.getTipoFormato() == TipoFormatoLibro.IMPRESO) {
          formatoImpreso = Optional.of(formato);
        }
        if (formato.getTipoFormato() == TipoFormatoLibro.DIGITAL) {
          formatoDigital = Optional.of(formato);
        }
      }
      
      // se encontraron el libro y los formatos que ofrece
      LibroConFormatos resultado = new LibroConFormatos(libro.get(), formatoImpreso, formatoDigital);
      return Optional.of(resultado);
    } catch (SQLException excepcion) {
      throw new ErrorTecnicoSistemaException(
        "Error de la base de datos al ejecutar la operación de buscar libro con formatos.", excepcion
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
}
