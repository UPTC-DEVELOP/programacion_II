//
package co.edu.uptc.tiendalibros.persistencia;

import java.util.List;
import java.util.Optional;

/// @param <T>
public abstract class RepositorioElementoEnCSVBase<T> extends RepositorioArchivoBase<T> {
  
  /// Separador de los atributos o columnas de una fila en el archivo CSV.
  private final String separador;
  
  /// Cantidad de atributos o columnas que cada fila en el archivo CSV debe tener.
  private final int cantidadAtributos;
  
  /// Índice de la columna id.
  private final int columnaId;
  
  /// Encabezado del archivo CSV.
  private String encabezado;
  
  /// Crea un nuevo objeto [RepositorioElementoEnCSVBase] a de los datos especificados.
  ///
  /// @param rutaArchivo       Ruta del archivo CSV donde se guardarán los libros. Si el
  ///                          archivo VS no existe, se crea automáticamente.
  /// @param separador         Separador que se utiliza para separar los atributos o columnas
  ///                          del CSV.
  /// @param cantidadAtributos Cantidad de atributos o columnas del CSV.
  /// @param columnaId         Índice de la columna que representa el ID único de la fila.
  /// @param encabezado        Encabezado del archivo CSV.
  public RepositorioElementoEnCSVBase(
    String rutaArchivo,
    String separador,
    int cantidadAtributos,
    int columnaId,
    String encabezado
  ) {
    super(rutaArchivo);
    this.separador = separador;
    this.cantidadAtributos = cantidadAtributos;
    this.columnaId = columnaId;
    this.encabezado = encabezado;
    inicializar();
  }
  
  /// Obtiene el separador de los atributos o columnas de una fila en el archivo CSV.
  ///
  /// @return Separador de los atributos o columnas de una fila en el archivo CSV.
  public String getSeparador() { return this.separador; }
  
  /// Obtiene la antidad de atributos o columnas del CSV.
  ///
  /// @return Cantidad de atributos o columnas del CSV.
  public int getCantidadAtributos() { return this.cantidadAtributos; }
  
  /// Obtiene el índice de la columna que representa el ID único de la fila.
  ///
  /// @return Índice de la columna que representa el ID único de la fila
  public int getColumnaId() { return this.columnaId; }
  
  @Override
  public String getEncabezado() { return this.encabezado; }
  
  @Override
  public void setEncabezado(String encabezado) {
    this.encabezado = encabezado;
  }
  
  @Override
  public Optional<T> convertirLinea(String linea) {
    // se divide la línea en un array teniendo en cuenta que se debe escapar el | porque es un
    // caracter especial
    // el -1 significa que se deben conservar los grupos (atributos) vacíos
    String[] campos = linea.split("\\" + getSeparador(), -1);
    
    // si el array de campos no tiene la cantidad de atributos esperada, no es una línea
    // válida y se devuelve un valor vacío
    if (campos.length < getCantidadAtributos()) {
      return Optional.empty();
    }
    
    // convertir los campos
    return convertirCampos(campos);
    
  }
  
  /// Convierte el array de campos dados en un objeto.
  ///
  /// @param campos Array de campos.
  /// @returns Un objeto a partir de los campos, o un objeto vacío.
  public abstract Optional<T> convertirCampos(String[] campos);
  
  /// @throws NumberFormatException
  protected static Long convertirALong(String campo) {
    String valor = campo.trim();
    return valor.isEmpty() ? null : Long.parseLong(valor);
  }
  
  public List<T> listar() {
    return super.getRegistros();
  }
}
