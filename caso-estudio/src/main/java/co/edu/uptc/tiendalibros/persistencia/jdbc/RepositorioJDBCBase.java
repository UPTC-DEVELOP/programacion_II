//
package co.edu.uptc.tiendalibros.persistencia.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IElemento;
import co.edu.uptc.tiendalibros.interfaces.IRepositorio;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa un repositorio base para los repositorios que guardan sus datos en una base
/// de datos relacional mediante la tecnología JDBC.
/// 
/// Implementa los pasos comunes en todos los repositorios JDBC: abrir la conexión,
/// preparar el comando SQL, ejecutar el comando, cerrar la conexión, manejar una
/// transacción y traducir errores SQL.
/// 
/// Recordar que en JDBC las operaciones usan índices basados en 1.
/// 
/// @param <T> Tipo de elemento que se guarda en la base de datos.
public abstract class RepositorioJDBCBase<T extends IElemento> implements IRepositorio<T> {
  
  /// Objeto que mantiene la conexión con la base de datos.
  private final ConexionBaseDatos conexionBaseDatos;
  
  /// Crea un nuevo repositorio de datos JDBC [RepositorioJDBCBase].
  /// 
  /// @param conexionBaseDatos Objeto que mantiente la conexión con la base de datos.
  protected RepositorioJDBCBase(ConexionBaseDatos conexionBaseDatos) {
    this.conexionBaseDatos = conexionBaseDatos;
  }
  
  @Override
  public T guardar(T elemento) throws ViolacionElementoNoExisteException {
    // no se puede guardar un elemento nulo
    if (elemento == null) {
      throw new IllegalArgumentException("Error: No se puede guardar un elemento nulo.");
    }
    
    // guardar es una acción que requiere transacción
    Optional<T> guardado = this.conexionBaseDatos
      .ejecutarEnTransaccion((conexion) -> guardar(conexion, elemento));
    
    // las operaciones insertar y actualizar devuelven un objeto T si fueron exitosas.
    // Un valor vacío significa que se quería actualizar un elemento que ya no existe en el
    // repositorio
    if (guardado.isEmpty()) {
      throw new ViolacionElementoNoExisteException(
        String.format("No se pudo encontrar el elemento con ID %d que se quería actualizar.", elemento.getId())
      );
    }
    
    return guardado.get();
  }
  
  @Override
  public boolean eliminar(Long id) {
    // si el ID es nulo, no se elimina nada
    if (id == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar con ID nulo");
    }
    // eliminar es una operación que se ejecuta en una transacción
    return this.conexionBaseDatos.ejecutarEnTransaccion((conexion) -> eliminar(conexion, id));
  }
  
  @Override
  public Optional<T> buscarPorId(Long id) {
    // si el id es nulo, se devueve un objeto vacío
    if (id == null) {
      return Optional.empty();
    }
    
    // intentar consultar el elemento
    try (Connection conexion = abrirConexion()) {
      return buscarPorId(conexion, id);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("buscar por ID", excepcion);
    }
  }
  
  @Override
  public List<T> listar() {
    // intentar obtener la lista de elementos
    try (Connection conexion = abrirConexion();
      PreparedStatement comando = crearComandoSQLListar(conexion)) {
      // convertir los resultados SQL en una lista de objetos Java
      return convertirFilas(comando);
    } catch (SQLException excepcion) {
      throw traducirExcepcion("listar", excepcion);
    }
  }
  
  /// Guarda el elemento y sus dependientes.
  /// 
  /// @param conexion Objeto que mantiene la conexión con la base de datos. El elemento
  ///                 guardado, o un valor vacío si se quería actualizar un elemento que ya
  ///                 no existe en la base de datos.
  /// @throws SQLException Si ocurre un error técnico durante la operació
  /// @throws SQLException Si ocurre un error técnico a la operación
  //
  protected Optional<T> guardar(Connection conexion, T elemento) throws SQLException {
    // todo depende del ID:
    // - si el elemento no tiene ID, el elemento se inserta
    // - si el elemento sí tiene ID, el elmento se actualiza
    T elementoGuardado;
    
    if (elemento.getId() == null) {
      elementoGuardado = insertar(conexion, elemento);
    } else {
      Optional<T> actualizado = actualizar(conexion, elemento);
      // si no existe, no hay nada más que guardar
      if (actualizado.isEmpty()) {
        return Optional.empty();
      }
      elementoGuardado = actualizado.get();
    }
    
    // guardar los elementos dependientes si los hay, y quedarse con el elemento padre
    return Optional.of(guardarElementosDependientes(conexion, elementoGuardado));
  }
  
  /// Inserta el elemento en la base de datos.
  /// 
  /// @param conexion Objeto que mantiene la conexión con la base de datos.
  /// @param elemento Elemento que se quiere insertar.
  /// @return El elemento insertado con su ID generado en la base de datos.
  /// @throws SQLException                 Si ocurre un error técnico a la operación de
  ///                                      insertar.
  /// @throws ErrorTecnicoSistemaException Si ocurre un error técnico a la operación
  ///                                      de insertar.
  protected T insertar(Connection conexion, T elemento) throws SQLException {
    
    try (
      // preparar el comando SQL para insertar. El comando debe devolver el ID generado
      PreparedStatement comando = crearComandoSQLInsertar(conexion, elemento)) {
      
      // ejecutar el comando INSERT
      comando.executeUpdate();
      // la base de datos generó el ID el cual debe asignarse al elemento. El ID está en un
      // ResultSet que se puede autocerrar
      try (ResultSet ids = comando.getGeneratedKeys()) {
        if (ids.next()) {
          // asignar el id que se encuentra en la columna 1 del ResultSet. Se devuelve el elemento y
          // su ID como operación exitosa de inserción
          return asignarId(elemento, ids.getLong(1));
        }
      }
      // aquí, algo pasó con el comando de inserción porque no hubo ID generado
      throw new ErrorTecnicoSistemaException("La base de datos no devolvió el ID del elemento guardado.", null);
      
    }
  }
  
  /// Actualiza el elemento en la base de datos.
  /// 
  /// @param conexion Objeto que mantiene la conexión con la base de datos.
  /// @param elemento Elemento que se quiere actualizar.
  /// @return El elemento actualizado. O un valor vacío si no existe una fila en la base de
  ///         datos con ese ID.
  /// @throws SQLException Si ocurre un error técnico a la operación de actualizar.
  protected Optional<T> actualizar(Connection conexion, T elemento)
    throws SQLException {
    try (
      // preparar el comando SQL para actualizar
      PreparedStatement comando = crearComandoSQLActualizar(conexion, elemento)) {
      
      // Si hubo 0 filas encontradas significa que el elemento no existe
      if (comando.executeUpdate() == 0) {
        return Optional.empty();
      }
      // devolver el elemento que se actualizó
      return Optional.of(elemento);
    }
  }
  
  /// Elimina el elemento y sus elementos dependientes.
  /// 
  /// @param conexion Objeto que mantiene la conexión con la base de datos.
  /// @param id       ID del elemento que se quiere eliminar.
  /// @return `true` si el elemento se eliminó. `false` si no se encontró.
  /// @throws SQLException Si ocurre un error técnico en la operación de eliminar.
  protected boolean eliminar(Connection conexion, Long id) throws SQLException {
    // primero, se eliminar los elementos dependientes
    eliminarElementosDependientes(conexion, id);
    
    // intentar eliminar el elemento principal
    try (PreparedStatement comando = crearComandoSQLEliminar(conexion, id)) {
      // delete es como una operación update
      int filasAfectadas = comando.executeUpdate();
      return (filasAfectadas > 0);
    }
  }
  
  /// Busca el elemento con el ID especificado.
  /// 
  /// @param conexion Objeto que mantiene la conexión con la base de datos.
  /// @param id       ID del elemento que se quiere buscar.
  /// @return El elemento que se econtró. O vacío si el elemento no se encontró.
  /// @throws SQLException Si ocurre un error técnico en la operación de buscar.
  protected Optional<T> buscarPorId(Connection conexion, Long id) throws SQLException {
    // intentar encontrar el elemento
    try (PreparedStatement comando = crearComandoSQLBuscarPorId(conexion, id)) {
      // obtener la lista de elementos
      List<T> encontrados = convertirFilas(comando);
      
      if (encontrados.isEmpty()) {
        return Optional.empty();
      }
      // devolver el primer elemento que se encontró (se supone que cada ID es único)
      return Optional.of(encontrados.get(0));
      
    }
  }
  
  /// Ejecuta el comando SQL preparado con una cláusula `SELECT` y convierte cada fila del
  /// resultado en un elemento con sus dependencias.
  /// 
  /// Se usa la misma conexión para cargar el elemento y sus dependencias.
  /// 
  /// @param comando Comando SQL preparado con la cláusula `SELECT`.
  /// @throws SQLException Si ocurre un error técnico durante la conversión.
  protected List<T> convertirFilas(PreparedStatement comando) throws SQLException {
    // paso 1: leer y convertir todas las filas de la consulta
    List<T> elementos = new ArrayList<>();
    
    try (ResultSet filas = comando.executeQuery()) {
      while (filas.next()) {
        elementos.add(convertirFila(filas));
      }
    }
    
    // paso 2: completar cada elemento con sus dependientes
    List<T> completos = new ArrayList<>();
    for (T elemento : elementos) {
      completos.add(cargarElementosDependientes(comando.getConnection(), elemento));
    }
    return completos;
  }
  
  /// Abre una conexión nueva con la base de datos.
  /// 
  /// Debería usarse con try-with-resources.
  /// 
  /// @return Un objeto que mantiene la conexión con la base de datos.
  /// @throws SQLException Si ocurre un error al abrir la conexión.
  protected Connection abrirConexion() throws SQLException {
    return this.conexionBaseDatos.abrir();
  }
  
  /// Crea un comando SQL preparado para insertar el elemento especificado.
  /// 
  /// Internamente, crea un objeto [PreparedStatement] con una sentencia SQL `INSERT` con los
  /// parámetros *`?`* asignados a sus atributos.
  /// 
  /// El comando SQL preparado debe tener asignado el valor
  /// [Statement#RETURN_GENERATED_KEYS]:
  /// 
  /// ```
  /// PreparedStatement comando = conexion.prepareStatement(
  ///   sql,
  ///   Statement.RETURN_GENERATED_KEYS
  /// );
  /// ```
  /// 
  /// @param conexion Conexión con la base de datos.
  /// @param elemento Elemento que se quiere insertar en la base de datos.
  /// @return Comando SQL preparado para insertar.
  /// @throws SQLException Si ocurre un error de tipo SQL en la base de datos.
  protected abstract PreparedStatement crearComandoSQLInsertar(Connection conexion, T elemento)
    throws SQLException;
  
  /// Crea un comando SQL preparado para actualizar el elemento especificado.
  /// 
  /// Internamente, crea un objeto [PreparedStatement] con una sentencia SQL `UPDATE` con los
  /// parámetros *`?`* asignados a sus atributos.
  /// 
  /// @param conexion Conexión con la base de datos.
  /// @param elemento Elemento que se quiere actualizar en la base de datos.
  /// @return Comando SQL preparado para actualizar.
  /// @throws SQLException Si ocurre un error de tipo SQL en la base de datos.
  protected abstract PreparedStatement crearComandoSQLActualizar(Connection conexion, T elemento) throws SQLException;
  
  /// Crea un comando SQL preparado para eliminar el elemento especificado.
  /// 
  /// Internamente, crea un objeto [PreparedStatement] con una sentencia SQL `DELETE` con los
  /// parámetros *`?`* asignados a sus atributos.
  /// 
  /// @param conexion Conexión con la base de datos.
  /// @param id       ID del elemento que se quiere eliminar en la base de datos.
  /// @return Comando SQL preparado para eliminar.
  /// @throws SQLException Si ocurre un error de tipo SQL en la base de datos.
  protected abstract PreparedStatement crearComandoSQLEliminar(Connection conexion, Long id) throws SQLException;
  
  /// Crea un comando SQL preparado para buscar el elemento con el ID especificado.
  /// 
  /// Internamente, crea un objeto [PreparedStatement] con una sentencia SQL `SELECT` con los
  /// parámetros *`?`* asignados a sus atributos.
  /// 
  /// @param conexion Conexión con la base de datos.
  /// @param id       ID del elemento que se quiere buscar en la base de datos.
  /// @return Comando SQL preparado para buscar por ID.
  /// @throws SQLException Si ocurre un error de tipo SQL en la base de datos.
  protected abstract PreparedStatement crearComandoSQLBuscarPorId(Connection conexion, Long id) throws SQLException;
  
  /// Crea un comando SQL preparado para obtener la lista de elementos guardados en la base
  /// de datos.
  /// 
  /// Internamente, crea un objeto [PreparedStatement] con una sentencia SQL `SELECT` con los
  /// parámetros *`?`* asignados a sus atributos.
  /// 
  /// @param conexion Conexión con la base de datos.
  /// @return Comando SQL preparado para obtener la lista de elementos guardados en la base
  ///         de datos.
  /// @throws SQLException Si ocurre un error de tipo SQL en la base de datos.
  protected abstract PreparedStatement crearComandoSQLListar(Connection conexion) throws SQLException;
  
  /// Crea una nueva copia del elemento y le asigna el ID especificado.
  /// 
  /// @param elemento Elemento al que se quiere asignar el ID.
  /// @param id       ID del elemento.
  /// @return Una copia del elemento con el ID.
  protected abstract T asignarId(T elemento, Long id);
  
  /// Convierte la fila actual de un conjunto de resultados en un elemento.
  /// 
  /// @param fila Fila actual del conjunto de resultados [ResultSet].
  /// @return El elemento [IElemento] esperado de la conversión.
  /// @throws SQLException Si ocurre un error SQL al convertir la fila.
  protected abstract T convertirFila(ResultSet fila) throws SQLException;
  
  /// Guarda los elementos dependientes del elemento principal en la misma transacción que
  /// el elemento principal.
  /// 
  /// Primero se guarda el elemento principal, antes de invocar este método para guardar los
  /// elementos dependientes.
  /// 
  /// El elemento principal debió haberse previamente guardado y tener con su ID generado.
  ///
  /// @param conexion Conexión con la transacción abierta.
  /// @param guardado El elemento con ID del que quiere guardar sus dependientes.
  /// @return El elemento con los IDs de sus hijos asignados.
  /// @throws SQLException Si ocurre un error al guardar los elementos dependientes.
  protected T guardarElementosDependientes(Connection conexion, T elementoGuardado) throws SQLException {
    return elementoGuardado;
  }
  
  /// Elimina los elementos dependientes del elemento principal en la misma transacción que
  /// el elemento principal.
  /// 
  /// Se invoca este método para eliminar los elementos dependientes, antes de eliminar el
  /// elemento principal.
  /// 
  /// @param conexion Conexión con la transacción abierta.
  /// @param id       ID del elemento principal del que se quiere eliminar sus elementos
  ///                 dependientes.
  /// @throws SQLException Si ocurre un error al eliminar los elementos dependientes.
  protected void eliminarElementosDependientes(Connection conexion, Long id) throws SQLException {
    // las subclases que tengan elementos dependientes definen qué hacer en este método
  }
  
  /// Carga los elementos dependientes del elemento principal.
  /// 
  /// Se debe invocar cada vez que se cargue el elemento principal de la base de datos.
  /// 
  /// @param conexion Conexión con la transacción abierta.
  /// @param id       ID del elemento principal del que se cargar sus dependientes.
  /// @return El mismo elemento con sus dependientes cargados.
  /// @throws SQLException Si ocurre un error al cargar los elementos dependientes.
  protected T cargarElementosDependientes(Connection conexion, T elemento) throws SQLException {
    return elemento;
  }
  
  /// Traduce un error de SQL en un error técnico del sistema.
  /// 
  /// @param operacion Nombre de la operación que tuvo el error.
  /// @param excepcion Excepción de SQL que se generó.
  /// @return Un objeto [ErrorTecnicoSistemaExcepcion] con el error traducido.
  protected ErrorTecnicoSistemaException traducirExcepcion(String operacion, SQLException excepcion) {
    return new ErrorTecnicoSistemaException(
      String.format("Error de la base de datos al ejecutar la operación de %s.", operacion), excepcion
    );
  }
}
