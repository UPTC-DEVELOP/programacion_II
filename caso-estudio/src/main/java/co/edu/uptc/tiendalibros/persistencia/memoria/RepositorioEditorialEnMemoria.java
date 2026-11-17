//
package co.edu.uptc.tiendalibros.persistencia.memoria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioEditorial;
import co.edu.uptc.tiendalibros.modelo.Editorial;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

/// Representa el repositorio que almacena las editoriales guardando los datos en la
/// memoria RAM.
public class RepositorioEditorialEnMemoria implements IRepositorioEditorial {
  
  /// Lista de las editoriales.
  private final List<Editorial> editoriales;
  
  /// Generador de IDs consecutivos.
  private final AtomicLong contador;
  
  /// Crea un nuevo objeto [RepositorioEditorialEnMemoria][RepositorioEditorialEnMemoria].
  public RepositorioEditorialEnMemoria() {
    
    this.editoriales = new ArrayList<>();
    this.contador = new AtomicLong(0);
    
    // datos de ejemplo para probar la interfaz gráfica
    
    try {
      guardar(new Editorial("Océano"));
      guardar(new Editorial("Planeta"));
      guardar(new Editorial("Pearson"));
      guardar(new Editorial("ECOE"));
      guardar(new Editorial("McGraw-Hill"));
    } catch (ViolacionElementoNoExisteException excepcion) {
      // no debería ocurrir este error porque son editoriales nuevas, sin ID
      throw new IllegalStateException("No se pudieron crear las editoriales de ejemplo.", excepcion);
    }
  }
  
  @Override
  public Editorial guardar(Editorial editorial) throws ViolacionElementoNoExisteException {
    // si el elemento es nulo, no se puede guardar nada
    if (editorial == null) {
      throw new IllegalArgumentException("Error: No se puede guardar una editorial nula.");
    }
    
    // todo depende del id
    // si el id es nulo, la editorial no se ha guardado en la base de datos
    if (editorial.getId() == null) {
      // generar nuevo ID
      Long idNuevo = this.contador.incrementAndGet();
      
      Editorial editorialNueva = new Editorial(idNuevo, editorial.getNombre());
      // agregar la editorial al repositorio
      this.editoriales.add(editorialNueva);
      return editorialNueva;
    }
    
    // el id no es nulo, luego se busca la editorial que se quiere guardar
    for (int i = 0; i < this.editoriales.size(); i++) {
      if (this.editoriales.get(i)
        .getId()
        .equals(editorial.getId())) {
        // reemplazar el objeto editorial
        this.editoriales.set(i, editorial);
        // retornar la editorial
        return editorial;
      }
    }
    
    // en este punto no debería ocurrir que no encontró la editorial
    throw new ViolacionElementoNoExisteException(
      String.format("No se pudo encontrar la editorial '%s' que se quería editar y guardar", editorial.getNombre())
    );
  }
  
  @Override
  public boolean eliminar(Long id) {
    // si el id es nulo, no se puede eliminar nada
    if (id == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar con un ID nulo.");
    }
    
    /*
     * Este bucle generó una excepción java.util.ConcurrentModificationException y no se puede
     * usar. recorrer la lista de editoriales y eliminar la editorial con el ID dado
     * 
     * int i = 0; for (Editorial editorial : this.editoriales) { if (editorial.getId() ==
     * idEditorial) { this.editoriales.remove(i); }
     * 
     * i++; }
     */
    
    // Eliminar la editorial con el ID dado, pero esta vez usando la técnica de iterador
    Iterator<Editorial> iterador = this.editoriales.iterator();
    while (iterador.hasNext()) {
      Editorial editorial = iterador.next();
      if (editorial.getId()
        .equals(id)) {
        iterador.remove();
        // eliminar la editorial fue una operación exitosa, terminar
        return true;
      }
    }
    
    // no encontró la editorial con ese ID y no se pudo eliminar
    return false;
  }
  
  @Override
  public Optional<Editorial> buscarPorId(Long id) {
    // si el id es nulo, se devuelve un objeto vacío
    if (id == null) {
      return Optional.empty();
    }
    
    // buscar la editorial que coincida con el id
    for (Editorial actual : this.editoriales) {
      if (actual.getId()
        .equals(id)) {
        // se encontró una coincidencia, entonces retornarla como resultado
        return Optional.of(actual);
      }
    }
    
    // no hubo resultados
    return Optional.empty();
  }
  
  @Override
  public List<Editorial> listar() {
    // devolver una copia inmodificable de la lista de editoriales
    return Collections.unmodifiableList(this.editoriales);
  }
  
  @Override
  public List<Editorial> buscarPorNombre(String nombre) {
    List<Editorial> resultados = new ArrayList<>();
    
    for (Editorial editorial : this.editoriales) {
      if (editorial.getNombre()
        .equalsIgnoreCase(nombre.trim())) {
        resultados.add(editorial);
      }
    }
    
    return resultados;
  }
  
}
