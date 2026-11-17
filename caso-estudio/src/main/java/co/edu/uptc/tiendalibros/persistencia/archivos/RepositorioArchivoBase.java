//
package co.edu.uptc.tiendalibros.persistencia.archivos;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa la clase base de los repositorios que guardan sus elementos en un archivo.
///
/// Esta clase no sabe en qué formato está escrito el archivo (CSV, JSON, otro).
/// 
/// Mantiene los registros en la memoria, asigna los IDs, y lee y escribe el archivo.
/// 
/// Las clases derivadas definen el formato implementando los métodos
/// [#crearArchivoVacio(Path)], [#leerRegistros(Path)] y [#escribirRegistros(Path, List)].
///
/// @param <T> Tipo de elemento que se guarda en el archivo.
public abstract class RepositorioArchivoBase<T> {
  
  /// Lista de los registros cargados en la memoria.
  private final List<T> registros;
  
  /// Generador de IDs consecutivos.
  private final AtomicLong contador;
  
  /// Rutal del archivo donde se guardan los elementos.
  private final Path rutaArchivo;
  
  /// Indica si los datos del archivo ya se cargaron en la memoria.
  private boolean inicializado;
  
  /// Crea un nuevo repositorio de datos cargando los datos del archivo en la ruta
  /// especificada. Si el archivo no existe, lo crea con el encabezado correspondiente.
  ///
  /// @param rutaArchivo Ruta del archivo que contiene los datos.
  protected RepositorioArchivoBase(String rutaArchivo) {
    // convertir el string en un objeto Path propiamente
    this.rutaArchivo = Paths.get(rutaArchivo);
    this.registros = new ArrayList<>();
    this.contador = new AtomicLong(0);
  }
  
  /// Inicializa el repositorio cargando los datos desde el archivo. También crea el archivo
  /// si no existe.
  /// 
  /// Una clase derivada debe invocar a este método al final de su constructor para quedar
  /// completamente listo.
  ///
  /// @throws ErrorTecnicoSistemaException Si ocurre un error de entrada y salida al cargar
  ///                                      el archivo de datos.
  protected void inicializar() {
    // intentar cargar los datos
    try {
      // si el archivo no existe, se debe crear el archivo
      if (Files.exists(this.rutaArchivo) == false) {
        // Si no existen los directorios padre, se deben crear
        if (this.rutaArchivo.getParent() != null) {
          Files.createDirectories(this.rutaArchivo.getParent());
        }
        
        // crearel nuevo archivo dependiendo del formato
        crearArchivoVacio(this.rutaArchivo);
      }
      
      // cargar los registros del archivo
      cargarRegistros();
    } catch (IOException excepcion) {
      // ocurrió una excepción de entrada y salida.
      // se convierte en una excepción propia de error técnico del sistema
      throw new ErrorTecnicoSistemaException(
        String.format("No se pudo cargar el archivo de datos '%s'", this.rutaArchivo), excepcion
      );
    }
    this.inicializado = true;
  }
  
  /// Verifica si el repositorio ya cargó los datos del archivo.
  /// 
  /// @throws IllegalStateException Si el repositorio no se inicializó.
  private void verificarInicializado() {
    if (this.inicializado == false) {
      throw new IllegalStateException(
        "El repositorio no se inicializó. La clase derivada debe invocar inicializar() al final de su constructor."
      );
    }
  }
  
  /// Carga en la memoria los registros que existen en el archivo.
  /// 
  /// Ajusta el generador de IDs para que continúe después del ID más grande.
  ///
  /// @throws ErrorTecnicoSistemaException Si ocurre un error técnico del sistema al cargar
  ///                                      los registros.
  protected void cargarRegistros() {
    // leer los registros dependiendo del formato
    List<T> leidos;
    
    try {
      leidos = leerRegistros(this.rutaArchivo);
    } catch (IOException excepcion) {
      throw new ErrorTecnicoSistemaException(
        String.format("Error al leer el archivo de datos: '%s'", this.rutaArchivo), excepcion
      );
    }
    
    // limpiar los registros cargados anteriormente
    this.registros.clear();
    // valor del ID más grande que se ha encontrado hasta el momento
    long idMasGrande = 0;
    
    // recorrer los registros leídos para determinar cuál es el ID más grande
    for (T registro : leidos) {
      this.registros.add(registro);
      Long idRegistro = getId(registro);
      
      // revisar si el id del registro es mayor que el id más grande que se ha encontrado
      if ((idRegistro != null) && (idRegistro > idMasGrande)) {
        idMasGrande = idRegistro;
      }
    }
    // al terminar de cargar todos los registros, se le asigna al contador el valor del id más
    // grande encontrado
    this.contador.set(idMasGrande);
  }
  
  /// Escribe todos los registros en el archivo. Se reemplaza todo el contenido anterior.
  /// 
  /// @throws ErrorTecnicoSistemaException Si ocurre un error de entrada y salida.
  protected void guardarArchivo() {
    try {
      // escribir los registros en el archivo dependiendo del formato
      escribirRegistros(this.rutaArchivo, this.registros);
    } catch (IOException excepcion) {
      throw new ErrorTecnicoSistemaException(
        String.format("Error al escribir el archivo de datos: '%s'", this.rutaArchivo), excepcion
      );
    }
  }
  
  /// Actualiza el registro dado en la lista de registros.
  ///
  /// @param registro Registro que se quiere actualizar.
  /// @throws ViolacionElementoNoExisteException Si al momento de actualizar, no se encontró
  ///                                            el elemento en el repositorio.
  private T actualizarRegistro(T registro) throws ViolacionElementoNoExisteException {
    
    // recorrer la lista de registros
    for (int i = 0; i < this.registros.size(); i++) {
      // comparar si el registro actual tiene el mismo id del registro dado
      if (getId(this.registros.get(i)).equals(getId(registro))) {
        this.registros.set(i, registro);
        return registro;
      }
    }
    
    // aquí, se lanza la excepción por que no se encontró el elemento con ese ID
    throw new ViolacionElementoNoExisteException(
      String.format("No se encontró el registro con ID %d para actualizar.", getId(registro))
    );
  }
  
  /// Obtiene la lista de registros. Esta lista es una vista de solo lectura de la lista
  /// interna de registros.
  ///
  /// @return Copia de la lista de registros.
  /// @throws IllegalStateException Si el repositorio no se inicializó.
  public List<T> getRegistros() {
    // verificar que el repositorio haya cargado los datos del archivo
    verificarInicializado();
    return Collections.unmodifiableList(this.registros);
  }
  
  /// Obtiene la lista de registros guardados en el archivo.
  /// 
  /// @return Lista de solo lectura con los registros. O, vacía si no hay registros.
  /// @throws IllegalStateException Si el repositorio no se inicializó.
  public List<T> listar() {
    return getRegistros();
  }
  
  /// Busca el registro con el ID especificado.
  ///
  /// @param id ID del registro que se quiere buscar.
  /// @return El registro encontrado, o vacío si no existe.
  /// @throws IllegalStateException Si el repositorio no se inicializó.
  public Optional<T> buscarPorId(Long id) {
    // verificar que el repositorio haya cargado los datos del archivo
    verificarInicializado();
    
    if (id == null) {
      return Optional.empty();
    }
    
    // recorrer la lista de registros hasta encontrar el elemento que coincida con el ID dado
    for (T actual : this.registros) {
      if (getId(actual).equals(id)) {
        return Optional.of(actual);
      }
    }
    
    return Optional.empty();
  }
  
  /// Guarda el registro especificado en el repositorio. Si el ID es nulo, crea un nuevo
  /// registro asignándole un ID. Si el ID no es nulo, actualiza el registro existente.
  ///
  /// @param registro Registro que se quiere guardar.
  /// @return El registro guardado con su ID asignado.
  /// @throws IllegalArgumentException           Si `registro` es un objeto nulo.
  /// @throws ViolacionElementoNoExisteException Si se intenta actualizar un registro cuyo ID
  ///                                            ya no existe en el repositorio.
  /// @throws IllegalStateException              Si el repositorio no se inicializó.
  public T guardar(T registro) throws ViolacionElementoNoExisteException {
    // verificar que el repositorio haya cargado los datos del archivo
    verificarInicializado();
    
    if (registro == null) {
      throw new IllegalArgumentException(
        "No se puede guardar un registro nulo."
      );
    }
    
    T registroGuardado;
    
    if (getId(registro) == null) {
      // se quiere crear un nuevo registro, y se debe asignar un nuevo ID
      Long idNuevo = this.contador.incrementAndGet();
      registroGuardado = asignarId(registro, idNuevo);
      this.registros.add(registroGuardado);
    } else {
      // se quiere actualizar un registro existente
      registroGuardado = actualizarRegistro(registro);
    }
    
    guardarArchivo();
    return registroGuardado;
  }
  
  /// Elimina el registro con el ID especificado.
  ///
  /// @param id ID del registro que se quiere eliminar.
  /// @return `true` si se eliminó, `false` si no se encontró.
  /// @throws IllegalArgumentException Si `id` es nulo.
  /// @throws IllegalStateException    Si el repositorio no se inicializó.
  public boolean eliminar(Long id) {
    // verificar que el repositorio haya cargado los datos del archivo
    verificarInicializado();
    
    if (id == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar con ID nulo");
    }
    
    // es mejor utilizar un iterador para eliminar un elemento de la lista mientras se está
    // recorriendo
    Iterator<T> iterador = this.registros.iterator();
    while (iterador.hasNext()) {
      if (getId(iterador.next()).equals(id)) {
        iterador.remove();
        guardarArchivo();
        return true;
      }
    }
    
    return false;
  }
  

  
  /// Obtiene el ID del registro dado.
  ///
  /// @param registro Registro del que se quiere obtener el ID.
  /// @return ID del registro.
  protected abstract Long getId(T registro);
  
  /// Asigna el ID especificado al registro dado creando una copia del registro con el ID
  /// asignado.
  ///
  /// @param registro Registro original.
  /// @param id       Nuevo ID que se le quiere asignar.
  /// @return Nuevo registro con el ID asignado (es decir, una copia junto a su ID).
  protected abstract T asignarId(T registro, Long id);
  
  /// Crea un archivo nuevo que representa un repositorio vacío según el formato.
  /// 
  /// @param ruta Ruta del archivo que se quiere crear.
  /// @throws IOException Si ocurre un error de entrada y salida.
  protected abstract void crearArchivoVacio(Path ruta) throws IOException;
  
  /// Lee todos los registros del archivo según el formato.
  /// 
  /// @param ruta Ruta del archivo que se quiere leer.
  /// @return Lista con los registros leídos.
  /// @throws IOException                  Si ocurre un error de entrada y salida.
  /// @throws ErrorTecnicoSistemaException Si el contenido del archivo no tiene el
  ///                                      formato esperado.
  protected abstract List<T> leerRegistros(Path ruta) throws IOException;
  
  /// Escribe todos los registros en el archivo según el formato.
  /// 
  /// Todo el contenido del archivo se reemplaza.
  /// 
  /// @param ruta      Ruta del archivo donde se escriben los registros.
  /// @param registros Registros que se quieren escribir.
  /// @throws IOException Si ocurre un error de entrada y salida.
  protected abstract void escribirRegistros(Path ruta, List<T> registros) throws IOException;
  
}