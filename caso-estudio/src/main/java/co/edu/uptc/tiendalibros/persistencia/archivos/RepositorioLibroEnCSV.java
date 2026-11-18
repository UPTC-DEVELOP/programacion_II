//
package co.edu.uptc.tiendalibros.persistencia.archivos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.interfaces.IRepositorioLibro;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.modelo.Libro;
import co.edu.uptc.tiendalibros.modelo.LibroBuilder;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Representa el repositorio que almacena los libros guardando los datos en un archivo
/// CSV.
///
/// El archivo CSV usa como separador el caracter: |
///
/// Una fila del archivo CSV representa un libro en esta forma:
///
/// ```
/// id | isbn | titulo | anioPublicacion | idCategoria | idEditorial | imagenLibro
/// ```
///
/// La primera fila del archivo CSV contiene los encabezados.
///
/// El archivo CSV se lee por completo para cargar los datos de todos los libros.
///
/// El archivo CSV se escribe por completo cada vez que se guarde un libro, guardando todos
/// los libros a la vez.
/// 
/// Al guardar, el archivo CSV solo contiene los IDs de la categoría y de la editorial.
/// 
/// Al leer, todo libro construido por este repositorio sale con su categoría y su
/// editorial completas.
/// 
/// Cuando se haga una operación que necesite devolver un libro completo siempre debe
/// invocarse el método [#completar(Libro)] para que el libro tenga su categoría y su
/// editorial asociadas:
/// 
/// ```
/// return completar(libro);
/// return Optional.of(completar(libro));
/// ```
/// 
/// ```
/// ```
/// 
/// ```
public class RepositorioLibroEnCSV extends RepositorioElementoEnCSVBase<Libro> implements IRepositorioLibro {
  
  /// Encabezado de los atributos o columnas en la primera fila del archivo CSV.
  private static final String ENCABEZADO = "id|isbn|titulo|anioPublicacion|idCategoria|idEditorial|imagenLibro";
  
  /// Índice de la columna isbn.
  private static final int COLUMNA_ISBN = 1;
  
  /// Índice de la columna título del libro.
  private static final int COLUMNA_TITULO = 2;
  
  /// Índice de la columna año de publicación del libro.
  private static final int COLUMNA_ANIO_PUBLICACION = 3;
  
  /// Índice de la columna id de la categoría del libro.
  private static final int COLUMNA_ID_CATEGORIA = 4;
  
  /// Índice de la columna id de la editorial del libro.
  private static final int COLUMNA_ID_EDITORIAL = 5;
  
  /// Índice de la columna imagen del libro.
  private static final int COLUMNA_IMAGEN_LIBRO = 6;
  
  /// Repositorio que se usa para consultar la categoría de cada libro.
  private final IRepositorioCategoria repositorioCategoria;
  
  /// Repositorio que se usa para consultar la editorial de cada libro.
  private final IRepositorioEditorial repositorioEditorial;
  
  /// Crea un nuevo objeto [RepositorioLibroEnCSV] a partir del archivo CSV especificado.
  ///
  /// @param rutaArchivo          Ruta del archivo CSV donde se guardarán los libros. Si el
  ///                             archivo CSV no existe, se crea automáticamente.
  /// @param repositorioCategoria Repositorio de las categorías de los libros.
  /// @param repositorioEditorial Repositorio de las editoriales de los libros.
  public RepositorioLibroEnCSV(
    String rutaArchivo,
    IRepositorioCategoria repositorioCategoria,
    IRepositorioEditorial repositorioEditorial
  ) {
    // total atributos = id + atributos
    super(rutaArchivo, "|", 1 + 6, 0, RepositorioLibroEnCSV.ENCABEZADO);
    
    this.repositorioCategoria = repositorioCategoria;
    this.repositorioEditorial = repositorioEditorial;
    
    inicializar();
  }
  
  @Override
  protected Long getId(Libro libro) {
    return libro.getId();
  }
  
  @Override
  protected Libro asignarId(Libro libro, Long id) {
    return new LibroBuilder()
      .id(id)
      .isbn(libro.getIsbn())
      .titulo(libro.getTitulo())
      .anioPublicacion(libro.getAnioPublicacion())
      .categoria(libro.getCategoria())
      .editorial(libro.getEditorial())
      .listaAutoria(libro.getListaAutoria())
      .imagenLibro(libro.getImagenLibro())
      .construir();
  }
  
  @Override
  public Optional<Libro> convertirCampos(String[] campos) {
    
    // intentar convertir cada campo a su tipo de dato respectivo
    try {
      // convertir el id
      Long id = Long.parseLong(campos[getColumnaId()].trim());
      // convertir el isbn
      String isbn = campos[RepositorioLibroEnCSV.COLUMNA_ISBN].trim();
      // convertir el título
      String titulo = campos[RepositorioLibroEnCSV.COLUMNA_TITULO].trim();
      // convertir el año de publicación
      int anioPublicacion = Integer.parseInt(campos[RepositorioLibroEnCSV.COLUMNA_ANIO_PUBLICACION].trim());
      // convertir el id de la categoría
      Long idCategoria = convertirALong(campos[RepositorioLibroEnCSV.COLUMNA_ID_CATEGORIA].trim());
      // convertir el id de la editorial
      Long idEditorial = convertirALong(campos[RepositorioLibroEnCSV.COLUMNA_ID_EDITORIAL].trim());
      // convertir la imagen del libro
      String imagenLibro = campos[RepositorioLibroEnCSV.COLUMNA_IMAGEN_LIBRO].trim();
      
      // como el archivo CSV de libro solo tiene los IDs de categoría y editorial, se crean
      // objetos provisionales
      Categoria categoria = crearCategoriaProvisional(idCategoria);
      Editorial editorial = crearEditorialProvisional(idEditorial);
      
      // nota: se crea un objeto libro a partir de los datos del archivo CSV. Pero aún falta que
      // el gestor de libros revise si el libro cumple con las reglas de negocio
      
      // bien, aquí ya se puede retornar el libro con los datos leídos
      Libro elLibro = new LibroBuilder()
        .id(id)
        .isbn(isbn)
        .titulo(titulo)
        .anioPublicacion(anioPublicacion)
        .categoria(categoria)
        .editorial(editorial)
        .imagenLibro(imagenLibro)
        .construir();
      
      return Optional.of(elLibro);
    }
    // si al leer los campos ocurren errores, se ignora el resultado
    catch (IllegalArgumentException excepcion) {
      return Optional.empty();
    }
    
  }
  
  /// Crea una categoría provisional con solo el ID.
  /// 
  /// Devuelve `null` si el libro no tiene categoría en el archivo.
  private Categoria crearCategoriaProvisional(Long idCategoria) {
    if (idCategoria == null) {
      return null;
    }
    return new Categoria(idCategoria, null, null, null);
  }
  
  /// Crea una editorial provisional con solo el ID.
  /// 
  /// Devuelve `null` si el libro no tiene editorial en el archivo.
  private Editorial crearEditorialProvisional(Long idEditorial) {
    if (idEditorial == null) {
      return null;
    }
    return new Editorial(idEditorial, null);
  }
  
  @Override
  public String convertirRegistro(Libro libro) {
    // formar una línea String separada con el separador
    return String.join(
      getSeparador(),
      String.valueOf(libro.getId()),
      textoParaCelda(libro.getIsbn()),
      textoParaCelda(libro.getTitulo()),
      String.valueOf(libro.getAnioPublicacion()),
      libro.getCategoria() == null ? "": String.valueOf(libro.getCategoria().getId()),
      libro.getEditorial() == null ? "": String.valueOf(libro.getEditorial().getId()),
      textoParaCelda(libro.getImagenLibro())
    );
  }
  
  /// Devuelve una copia del libro con su categoría y su editorial completas.
  ///
  /// Asegura que un libro cargado desde el repositorio tenga sus objetos categoría y
  /// editorial asociados y actualizados. Así, se evita cargar datos viejos y
  /// desactualizados.
  ///
  /// @param libro Libro del repositorio que se quiere completar.
  /// @return Copia del libro con su categoría y su editorial completas.
  /// @throws ErrorTecnicoSistemaException Si el libro referencia una categoría o una
  ///                                      editorial que no existe.
  private Libro completar(Libro libro) {
    // buscar la categoría y la editorial
    Categoria categoria = buscarCategoria(libro.getCategoria());
    Editorial editorial = buscarEditorial(libro.getEditorial());
    // para completar el libro con sus objetos se crea una copia con el mismo ID
    Libro copia = asignarId(libro, libro.getId());
    copia.setCategoria(categoria);
    copia.setEditorial(editorial);
    
    return copia;
  }
  
  /// Busca la categoría especificada en el repositorio.
  /// 
  /// @param categoria Categoría que se quiere buscar.
  /// @return Un objeto [Categoria] si la categoría sí fue encontrada en el repositorio. O,
  ///         `null` si la categoría no fue encontrada en el repositorio.
  private Categoria buscarCategoria(Categoria categoria) {
    // si el libro en el archivo no tiene categoría, se queda sin categoría
    if (categoria == null) {
      return null;
    }
    
    // buscar la categoría
    Optional<Categoria> encontrada = this.repositorioCategoria.buscarPorId(categoria.getId());
    if (encontrada.isEmpty()) {
      throw new ErrorTecnicoSistemaException(
        String.format("El archivo de libros referencia la categoría %d, pero no existe.", categoria.getId()),
        null
      );
    }
    return encontrada.get();
  }
  
  /// Busca la editorial especificada en el repositorio.
  /// 
  /// @param editorial Editorial que se quiere buscar.
  /// @return Un objeto [Editorial] si la editorial sí fue encontrada en el repositorio. O,
  ///         `null` si la editorial no fue encontrada en el respositorio.
  private Editorial buscarEditorial(Editorial editorial) {
    // si el libro en el archivo no tiene editorial se queda sin editorial
    if (editorial == null) {
      return null;
    }
    Optional<Editorial> encontrada = this.repositorioEditorial.buscarPorId(editorial.getId());
    if (encontrada.isEmpty()) {
      throw new ErrorTecnicoSistemaException(
        String.format("El archivo de libros referencia la editorial %d, pero no existe.", editorial.getId()),
        null
      );
    }
    return encontrada.get();
  }
  
  @Override
  public List<Libro> listar() {
    List<Libro> completos = new ArrayList<>();
    for (Libro libro : super.listar()) {
      completos.add(completar(libro));
    }
    return completos;
  }
  
  @Override
  public Optional<Libro> buscarPorId(Long id) {
    Optional<Libro> encontrado = super.buscarPorId(id);
    if (encontrado.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(completar(encontrado.get()));
  }
  
  @Override
  public List<Libro> buscarPorTitulo(String titulo) {
    // si el título es vacío, se devuelve una lista vacía
    if (titulo == null || titulo.isBlank()) {
      return new ArrayList<Libro>();
    }
    
    // preparar el criterio de búsqueda de título
    String tituloBusqueda = titulo.trim()
      .toLowerCase();
    
    // lista de resultados
    List<Libro> resultados = new ArrayList<>();
    
    // buscar el libro que concida parcialmente con el título
    for (Libro actual : this.listar()) {
      if (actual.getTitulo()
        .toLowerCase()
        .contains(tituloBusqueda)) {
        // se encontró una coincidencia, entonces agregarla a la lista de resultados
        // se debe devolver el libro completo
        resultados.add(completar(actual));
      }
    }
    
    return resultados;
  }
  
  @Override
  public Optional<Libro> buscarPorIsbn(String isbn) {
    if (isbn == null || isbn.isBlank()) {
      return Optional.empty();
    }
    // buscar los libros que tengan el mismo ISBN
    for (Libro libro : getRegistros()) {
      if (isbn.trim()
        .equals(libro.getIsbn())) {
        // se debe devolver el libro completo
        return Optional.of(completar(libro));
      }
    }
    return Optional.empty();
  }
  
  @Override
  public boolean existeLibroConEditorial(Editorial editorial) {
    if (editorial == null) {
      throw new IllegalArgumentException("Error: La editorial no puede ser nula.");
    }
    
    // una editorial sin ID no se ha guardado y, por tanto, no puede tener libros
    if (editorial.getId() == null) {
      return false;
    }
    
    // recorrer los registros buscando la editorial
    for (Libro libro : getRegistros()) {
      if (editorial.equals(libro.getEditorial())) {
        return true;
      }
    }
    
    // no se encontró un libro con esa editorial
    return false;
  }
  
  @Override
  public boolean existeLibroConCategoria(Categoria categoria) {
    // no se puede buscar una categoría nula
    if (categoria == null) {
      throw new IllegalArgumentException("Error: La categoría no puede ser nula.");
    }
    
    // una categoría sin ID no se ha guardado y, por tanto, no puede tener libros
    if (categoria.getId() == null) {
      return false;
    }
    
    // recorrer los registros buscando la categoría
    for (Libro libro : getRegistros()) {
      if (categoria.equals(libro.getCategoria())) {
        return true;
      }
    }
    // no se encontró un libro con esa categoría
    return false;
  }
  
}
