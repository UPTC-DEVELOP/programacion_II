//
package co.edu.uptc.tiendalibros.persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

/// Representa la clase base para guardar elementos en archivos.
public abstract class RepositorioArchivoBase<T> {
  
  /// Separador de las líneas que representan registros en el archivo.
  private static final String SEPARADOR_LINEA = "\r\n";
  
  /// Lista de los registros cargados en la memoria.
  private final List<T> registros;
  
  /// Generador de IDs consecutivos.
  private final AtomicLong contador;
  
  /// Rutal del archivo donde se guardan los elementos.
  private final Path rutaArchivo;
  
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
  /// @throws ErrorTecnicoSistemaException Si ocurre un error de entrada y salida al cargar
  ///                                      el archivo de datos.
  protected void inicializar() {
    // intentar cargar los datos
    try {
      // si el archivo no existe, se debe crear el archivo
      if (Files.exists(this.rutaArchivo) == false) {
        crearArchivo();
      }
      // cargar los registros del archivo
      cargarRegistros();
    } catch (IOException excepcion) {
      // ocurrió una excepción de entrada y salida.
      // se convierte en una excepción propia de error técnico del sistema
      throw new ErrorTecnicoSistemaException(
        String.format("No se pudo guardar el archivo de datos '%s'", this.rutaArchivo), excepcion
      );
    }
  }
  
  /// Crea el archivo con su línea de encabezado
  ///
  /// @throws IOException Si ocurre un error de entrada y salida.
  protected void crearArchivo() throws IOException {
    // Si no existen los directorios padre, se deben crear
    if (this.rutaArchivo.getParent() != null) {
      Files.createDirectories(this.rutaArchivo.getParent());
    }
    
    // crea el archivo al escribir la primera línea que es el encabezado
    Files.writeString(
      this.rutaArchivo,
      getEncabezado() + RepositorioArchivoBase.SEPARADOR_LINEA,
      StandardCharsets.UTF_8
    );
  }
  
  /// Carga los registros que existen en el archivo.
  ///
  /// @throws ErrorTecnicoSistemaException Si ocurre un error técnico del sistema al cargar
  ///                                      los registros.
  protected void cargarRegistros() {
    // limpiar los registros cargados anteriormente
    this.registros.clear();
    // valor del ID más grande que se ha encontrado hasta el momento
    long idMasGrande = 0;
    
    // intentar leer cada línea del archivo usando la gestión del recurso
    try (BufferedReader lector = Files.newBufferedReader(this.rutaArchivo, StandardCharsets.UTF_8)) {
      
      // contenido de la línea
      String linea;
      String lineaEncabezado = null;
      
      // leer todas las líneas del archivo hasta que no quede ninguna
      while ((linea = lector.readLine()) != null) {
        
        // saltarse las líneas vacías
        if (linea.isBlank()) {
          continue;
        }
        
        // la primera línea que no esté vacía se considera de encabezado
        if (lineaEncabezado == null) {
          lineaEncabezado = linea;
          continue;
        }
        
        // la línea puede tener datos por lo que se debe convertir
        Optional<T> registro = convertirLinea(linea);
        
        // si se obtuvo un registro, se debe procesar
        if (registro.isPresent()) {
          // agregar el registro a la lista
          this.registros.add(registro.get());
          // obtener el id del registro
          Long idRegistro = getId(registro.get());
          // revisar si el id del registro es mayor que el id más grande que se ha encontrado hasta
          // ahora
          if ((idRegistro != null) && idRegistro > idMasGrande) {
            idMasGrande = idRegistro;
          }
        }
      }
      
    } catch (IOException excepcion) {
      throw new ErrorTecnicoSistemaException(
        String.format(
          "Error al leer el archivo de datos: '%s'",
          this.rutaArchivo
        ),
        excepcion
      );
    }
    
    // al terminar de cargar todos los registros, se le asigna al contador el valor del id más
    // grande encontrado
    this.contador.set(idMasGrande);
  }
  
  /// Escribe todos los registros en el archivo. Se reemplaza todo el contenido anterior.
  protected void guardarArchivo() {
    // intentar guardar el archivo con gestión de recurso
    try (BufferedWriter escritor = Files.newBufferedWriter(
      this.rutaArchivo,
      StandardCharsets.UTF_8
    )) {
      // primero escribir el encabezado del archivo
      escritor.write(getEncabezado());
      escritor.newLine();
      
      // escribir cada registro
      for (T registro : this.registros) {
        escritor.write(convertirRegistro(registro));
        escritor.newLine();
      }
      
    } catch (IOException excepcion) {
      throw new ErrorTecnicoSistemaException(
        String.format(
          "Error al escribir el archivo de datos: '%s'",
          this.rutaArchivo
        ),
        excepcion
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
  
  /// Obtiene la lista de registros. Esta lista es una copia de la lista interna de
  /// registros.
  ///
  /// @return Copia de la lista de registros.
  public List<T> getRegistros() { return Collections.unmodifiableList(this.registros); }
  
  /// Busca el registro con el ID especificado.
  ///
  /// @param id ID del registro que se quiere buscar.
  /// @return El registro encontrado, o vacío si no existe.
  public Optional<T> buscarPorId(Long id) {
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
  public T guardar(T registro) throws ViolacionElementoNoExisteException {
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
  public boolean eliminar(Long id) {
    if (id == null) {
      return false;
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
  
  /// Obtiene la línea de encabezado del archivo (o sea, la primera línea).
  ///
  /// @return Línea de encabezado del archivo.
  protected abstract String getEncabezado();
  
  /// Establece la línea de encabezado del archivo.
  ///
  /// @param encabezado Línea de encabezado del archivo.
  protected abstract void setEncabezado(String encabezado);
  
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
  
  /// Convierte una línea de texto del archivo en un objeto del tipo T. Si la línea tiene
  /// errores devuelve vacío.
  ///
  /// @param linea Línea de texto leída del archivo.
  /// @return El objeto convertido, o vacío si la línea es inválida.
  protected abstract Optional<T> convertirLinea(String linea);
  
  /// Convierte un objeto del tipo T en una línea de texto para guardar en el archivo.
  ///
  /// @param registro Objeto que se quiere convertir.
  /// @return Línea de texto que representa el objeto.
  protected abstract String convertirRegistro(T registro);
  
}