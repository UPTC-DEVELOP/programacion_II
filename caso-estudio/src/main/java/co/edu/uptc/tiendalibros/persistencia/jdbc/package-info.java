/// Contiene los repositorios que guardan los datos en una base de datos relacional usando
/// la tecnología JDBC.
/// 
/// ## Uso de la Conexión Compartida
/// 
/// Cada operación pública de un repositorio JDBC abre una conexión con la base de datos y
/// la cierra al terminar.
/// 
/// Sin embargo, algunas operaciones necesitan datos provenientes de consultas adicionales
/// para completarse satisfactoriamente.
/// 
/// Por ejemplo, obtener un [Libro][co.edu.uptc.tiendalibros.modelo.Libro] de la base de
/// datos, implica consultar su [Categoria][co.edu.uptc.tiendalibros.modelo.Categoria] y su
/// [Editorial][co.edu.uptc.tiendalibros.modelo.Editorial]. Una conexión compartida permite
/// obtener un libro completo en la misma transacción.
/// 
/// En cambio, si se usaran conexiones independientes para obtener la categoría y la
/// editorial de un libro, los métodos públicos, tales como `listar()` libros, tendrían que
/// abrir muchas conexiones.
/// 
/// Esto también podría pasar con el módulo de ventas: Carrito de ventas, y Lista de libros
/// comprados.
/// 
/// Por este motivo existen los métodos de **conexión compartida**:
///
/// - Se reconocen porque reciben [Connection][java.sql.Connection] `conexion` como primer
///   parámetro.
/// - No abren ni cierran la conexión con la base de datos: el método invocador fue el que
///   abrió la conexión y la debe cerrar, pero no el método invocado.
/// - Son `protected` porque solo son métodos de las clases de este paquete y sus
///   subclases.
/// - Son `protected` porque nos permite compartir la conexión dentro de las clases de este
///   paquete `jdbc`.
/// - Los métodos `crearComandoSQL*` reciben la conexión compartida para crear el comando
///   SQL ([PreparedStatement][java.sql.PreparedStatement]). Devuelven el comando al método
///   invocador el cual se encarga de ejecutar el comando SQL y cerrarlo.
package co.edu.uptc.tiendalibros.persistencia.jdbc;
