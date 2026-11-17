//
package co.edu.uptc.tiendalibros.persistencia.archivos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa la clase base de los repositorios que guardan sus elementos en un archivo
/// CSV.
///
/// El archivo CSV tiene una primera línea de encabezado, y luego una línea por registro
/// con los atributos separados por un separador.
///
/// @param <T> Tipo de elemento que se guarda en el archivo CSV.
public abstract class RepositorioElementoEnCSVBase<T> extends RepositorioArchivoBase<T> {
  
  /// Separador de las líneas que representan registros en el archivo.
  private static final String SEPARADOR_LINEA = "\r\n";
  
  /// Separador de los atributos o columnas de una fila en el archivo CSV.
  private final String separador;
  
  /// Cantidad de atributos o columnas que cada fila en el archivo CSV debe tener.
  private final int cantidadAtributos;
  
  /// Índice de la columna id.
  private final int columnaId;
  
  /// Encabezado del archivo CSV.
  private final String encabezado;
  
  /// Crea un nuevo objeto [RepositorioElementoEnCSVBase] a partir de los datos
  /// especificados.
  ///
  /// @param rutaArchivo       Ruta del archivo CSV donde se guardarán los elementos. Si el
  ///                          archivo CSV no existe, se crea automáticamente.
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
  }
  
  /// Obtiene el separador de los atributos o columnas de una fila en el archivo CSV.
  ///
  /// @return Separador de los atributos o columnas de una fila en el archivo CSV.
  public String getSeparador() { return this.separador; }
  
  /// Obtiene la cantidad de atributos o columnas del CSV.
  ///
  /// @return Cantidad de atributos o columnas del CSV.
  public int getCantidadAtributos() { return this.cantidadAtributos; }
  
  /// Obtiene el índice de la columna que representa el ID único de la fila.
  ///
  /// @return Índice de la columna que representa el ID único de la fila
  public int getColumnaId() { return this.columnaId; }
  
  /// Obtiene la línea de encabezado del archivo CSV (o sea, la primera línea).
  ///
  /// @return Línea de encabezado del archivo CSV.
  public String getEncabezado() { return this.encabezado; }
  
  @Override
  protected void crearArchivoVacio(Path ruta) throws IOException {
    // un archivo CSV sin registros solo tiene la línea de encabezado
    Files.writeString(
      ruta,
      getEncabezado() + RepositorioElementoEnCSVBase.SEPARADOR_LINEA,
      StandardCharsets.UTF_8
    );
  }
  
  @Override
  protected List<T> leerRegistros(Path ruta) throws IOException {
    List<T> registros = new ArrayList<>();
    
    // leer cada línea del archivo usando la gestión del recurso
    try (BufferedReader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
      
      // contenido de la línea
      String linea;
      String lineaEncabezado = null;
      
      // contador de la línea actual
      int numeroLinea = 0;
      
      // leer todas las líneas del archivo hasta que no quede ninguna
      while ((linea = lector.readLine()) != null) {
        numeroLinea++;
        
        // saltarse las líneas vacías
        if (linea.isBlank()) {
          continue;
        }
        
        // la primera línea que no esté vacía se considera de encabezado
        if (lineaEncabezado == null) {
          lineaEncabezado = linea;
          continue;
        }
        
        // la línea tiene datos por lo que se debe convertir
        Optional<T> registro = convertirLinea(linea);
        
        // si la línea no pudo ser convertida, se detiene la ejecución, y se le avisa al usuario
        if (registro.isEmpty()) {
          throw new ErrorTecnicoSistemaException(
            String.format(
              "La línea %d del archivo de datos '%s' no tiene el formato esperado.",
              numeroLinea,
              ruta
            ),
            null
          );
        }
        
        registros.add(registro.get());
      }
    }
    
    return registros;
  }
  
  @Override
  protected void escribirRegistros(Path ruta, List<T> registros) throws IOException {
    try (BufferedWriter escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
      // primero escribir el encabezado del archivo
      escritor.write(getEncabezado());
      escritor.newLine();
      
      // escribir cada registro
      for (T registro : registros) {
        escritor.write(convertirRegistro(registro));
        escritor.newLine();
      }
    }
  }
  
  /// Convierte una línea de texto del archivo CSV en un objeto del tipo T. Si la línea
  /// tiene errores devuelve vacío.
  ///
  /// @param linea Línea de texto leída del archivo.
  /// @return El objeto convertido, o vacío si la línea es inválida.
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
  /// @return Un objeto a partir de los campos, o un objeto vacío.
  public abstract Optional<T> convertirCampos(String[] campos);
  
  /// Convierte un objeto del tipo T en una línea de texto para guardar en el archivo CSV.
  ///
  /// @param registro Objeto que se quiere convertir.
  /// @return Línea de texto que representa el objeto.
  protected abstract String convertirRegistro(T registro);
  
  /// Convierte el texto nulo y los separadores en un texto apropiado para CSV.
  protected String textoParaCelda(String texto) {
    if (texto == null) {
      return "";
    }
    return texto.replace(getSeparador(), " ")
      .replace("\r", " ")
      .replace("\n", " ");
  }
  
  /// Convierte un campo de texto en un número `Long`.
  ///
  /// @param campo Texto del campo. Si es vacío, se devuelve `null`.
  /// @return El número, o `null` si el campo está vacío.
  /// @throws NumberFormatException Si el campo no es un número.
  protected static Long convertirALong(String campo) {
    String valor = campo.trim();
    return valor.isEmpty() ? null : Long.parseLong(valor);
  }
}