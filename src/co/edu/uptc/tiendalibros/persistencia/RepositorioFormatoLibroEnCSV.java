//
package co.edu.uptc.tiendalibros.persistencia;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioFormatoLibro;
import co.edu.uptc.tiendalibros.modelo.Dinero;
import co.edu.uptc.tiendalibros.modelo.EstadoLibroCatalogo;
import co.edu.uptc.tiendalibros.modelo.FormatoLibro;
import co.edu.uptc.tiendalibros.modelo.FormatoLibroBuilder;
import co.edu.uptc.tiendalibros.modelo.TipoFormatoLibro;
import co.edu.uptc.tiendalibros.modelo.TipoIVA;

/// Representa el repositorio que almacena los formatos de libros guardando los datos en
/// un archivo CSV.
///
/// El archivo CVS usa como separador el caracter: |
///
/// Una fila del archivo CSV representa un formato de libro en esta forma:
///
///    id|idLibro|tipoFormato|precioBase|tipoIVA|inventario|cantidadPaginas|estadoLibroCatalogo
///
/// La primera fila del archivo CSV contiene los encabezados.
///
/// El archivo CVS se lee por completo para cargar los datos de todos los libros.
///
/// El archivo CVS se escribe por completo cada vez que se guarde un libro, guardando todos
/// los libros a la vez.
public class RepositorioFormatoLibroEnCSV extends RepositorioElementoEnCSVBase<FormatoLibro>
  implements IRepositorioFormatoLibro {
  
  /// Encabezado de los atributos o columnas en la primera fila del archivo CSV.
  private static final String ENCABEZADO = "id|idLibro|tipoFormato|precioBase|tipoIVA|inventario|cantidadPaginas|estadoLibroCatalogo";
  
  /// Índice de la columna idLibro.
  private static final int COLUMNA_ID_LIBRO = 1;
  
  /// Índice de la columna tipoFormato.
  private static final int COLUMNA_TIPO_FORMATO = 2;
  
  /// Índice de la columna precioBase.
  private static final int COLUMNA_PRECIO_BASE = 3;
  
  /// Índice de la columna tipoIva.
  private static final int COLUMNA_TIPO_IVA = 4;
  
  /// Índice de la columna inventario.
  private static final int COLUMNA_INVENTARIO = 5;
  
  /// Índice de la columna cantidadPaginas.
  private static final int COLUMNA_CANTIDAD_PAGINAS = 6;
  
  /// Índice de la columna estadoLibroCatalogo.
  private static final int COLUMNA_ESTADO_LIBRO_CATALOGO = 7;
  
  /// Crea un nuevo objeto [RepositorioFormatoLibroEnCSV] a partir del archivo CSV
  /// especificado.
  ///
  /// @param rutaArchivo Ruta del archivo CSV donde se guardarán los libros. Si el archivo VS
  ///                    no existe, se crea automáticamente.
  public RepositorioFormatoLibroEnCSV(String rutaArchivo) {
    // total atributos = id + atributos
    super(rutaArchivo, "|", 1 + 7, 0, RepositorioFormatoLibroEnCSV.ENCABEZADO);
  }
  
  @Override
  protected Long getId(FormatoLibro formatoLibro) {
    return formatoLibro.getId();
  }
  
  @Override
  protected FormatoLibro asignarId(FormatoLibro formatoLibro, Long id) {
    return new FormatoLibroBuilder()
      .id(id)
      .idLibro(formatoLibro.getIdLibro())
      .tipoFormatoLibro(formatoLibro.getTipoFormato())
      .precioBase(formatoLibro.getPrecioBase())
      .tipoIVA(formatoLibro.getTipoIVA())
      .inventario(formatoLibro.getInventario())
      .cantidadPaginas(formatoLibro.getCantidadPaginas())
      .estadoLibroCatalogo(formatoLibro.getEstadoLibroCatalogo())
      .construir();
  }
  
  @Override
  public Optional<FormatoLibro> convertirCampos(String[] campos) {
    // intentar convertir cada campo a su tipo de dato respectivo
    try {
      // convertir el id
      Long id = Long.parseLong(campos[getColumnaId()].trim());
      // convertir el idLibro
      Long idLibro = convertirALong(campos[RepositorioFormatoLibroEnCSV.COLUMNA_ID_LIBRO].trim());
      // convertir el formato de libro
      TipoFormatoLibro tipoFormatoLibro = TipoFormatoLibro
        .convertirString(campos[RepositorioFormatoLibroEnCSV.COLUMNA_TIPO_FORMATO].trim());
      // convertir el precio base
      BigDecimal valorPrecioBase = new BigDecimal(campos[RepositorioFormatoLibroEnCSV.COLUMNA_PRECIO_BASE].trim());
      Dinero precioBase = new Dinero(valorPrecioBase);
      // convertir el tipo IVA
      TipoIVA tipoIVA = TipoIVA.convertirString(campos[RepositorioFormatoLibroEnCSV.COLUMNA_TIPO_IVA].trim());
      // convertir el inventario
      int inventario = Integer.parseInt(campos[RepositorioFormatoLibroEnCSV.COLUMNA_INVENTARIO].trim());
      // convertir la cantidad de páginas
      int cantidadPaginas = Integer.parseInt(campos[RepositorioFormatoLibroEnCSV.COLUMNA_CANTIDAD_PAGINAS].trim());
      // convertir el estado del libro en el catálogo
      String estadoLibroCatalogoString = campos[RepositorioFormatoLibroEnCSV.COLUMNA_ESTADO_LIBRO_CATALOGO].trim();
      EstadoLibroCatalogo estadoLibroCatalogo = EstadoLibroCatalogo
        .convertirAbreviatura(estadoLibroCatalogoString);
      
      // retornar el formato de libro con los datos leídos
      FormatoLibro formatoLibro = new FormatoLibroBuilder()
        .id(id)
        .idLibro(idLibro)
        .tipoFormatoLibro(tipoFormatoLibro)
        .precioBase(precioBase)
        .tipoIVA(tipoIVA)
        .inventario(inventario)
        .cantidadPaginas(cantidadPaginas)
        .estadoLibroCatalogo(estadoLibroCatalogo)
        .construir();
      return Optional.of(formatoLibro);
    }
    // si al leer los campos ocurren errores, se ignora el resultado
    catch (IllegalArgumentException excepcion) {
      return Optional.empty();
    }
  }
  
  @Override
  public String convertirRegistro(FormatoLibro formatoLibro) {
    return String.join(
      getSeparador(),
      String.valueOf(formatoLibro.getId()),
      String.valueOf(formatoLibro.getIdLibro()),
      formatoLibro.getTipoFormato() == null ? ""
        : formatoLibro.getTipoFormato()
          .name(),
      String.valueOf(
        formatoLibro.getPrecioBase()
          .getCantidad()
          .toPlainString()
      ),
      formatoLibro.getTipoIVA() == null ? ""
        : formatoLibro.getTipoIVA()
          .name(),
      String.valueOf(formatoLibro.getInventario()),
      String.valueOf(formatoLibro.getCantidadPaginas()),
      formatoLibro.getEstadoLibroCatalogo()
        .getAbreviatura()
    );
  }
  
  @Override
  public List<FormatoLibro> buscarPorIdLibro(Long idLibro) {
    if (idLibro == null) {
      return new ArrayList<>();
    }
    // buscar los formatos de libros que tengan el mismo ID
    List<FormatoLibro> resultados = new ArrayList<>();
    for (FormatoLibro edicion : getRegistros()) {
      if (idLibro.equals(edicion.getIdLibro())) {
        resultados.add(edicion);
      }
    }
    return resultados;
  }
  
}
