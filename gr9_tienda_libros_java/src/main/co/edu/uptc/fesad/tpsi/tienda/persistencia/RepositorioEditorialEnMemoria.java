/// 
package main.co.edu.uptc.fesad.tpsi.tienda.persistencia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import main.co.edu.uptc.fesad.tpsi.tienda.negocio.Editorial;

/// Representa el repositorio que almacena las editoriales guardando los datos en la
/// memoria RAM.
public class RepositorioEditorialEnMemoria extends RepositorioEditorialBase {
  
  private final List<Editorial> editoriales;
  
  /// Truco para generar Ids consecutivos
  private final AtomicLong contador;
  
  /// Crea un nuevo objeto [RepositorioEditorialEnMemoria][RepositorioEditorialEnMemoria].
  public RepositorioEditorialEnMemoria() {
    this.editoriales = new ArrayList<Editorial>();
    // el contador se inicia en 10
    this.contador = new AtomicLong(10);
    
    // datos de ejemplo para probar la interfaz gráfica
    guardar(new Editorial("Océano"));
    guardar(new Editorial("Planeta"));
    guardar(new Editorial("Pearson"));
    guardar(new Editorial("ECOE"));
    guardar(new Editorial("McGraw-Hill"));
  }
  
  @Override
  public Editorial guardar(Editorial elemento) {
    // si el elemento es nulo, no se puede guardar nada
    if (elemento == null) {
      return null;
    }
    
    // obtener la editorial que se quiere guardar
    Editorial editorial = elemento;
    
    // hacer las validaciones al objeto editorial
    validarEditorial(editorial);
    
    // todo depende del id
    // si el id es nulo, la editorial no se ha guardado en la base de datos
    if (editorial.getId() == null) {
      // usar el truco para generar un id único
      Long idNuevo = this.contador.incrementAndGet();
      editorial.setId(idNuevo);
      // agregar la editorial a la base de datos
      this.editoriales.add(editorial);
      return editorial;
    }
    // el id no es nulo, luego toca reemplazar la editorial en la base de datos
    else {
      for (int i = 0; i < this.editoriales.size(); i++) {
        if (this.editoriales.get(i)
          .getId()
          .equals(editorial.getId())) {
          // reemplazar el objeto editorial
          this.editoriales.set(i, editorial);
          return editorial;
        }
      }
    }
  
    // TODO: analizar si se puede encontrar una mejor alternativa a lanzar una excepción
    throw new IllegalArgumentException(
      "No se encontró la editorial con ID: " + editorial.getId()
    );
  }
  
  @Override
  public boolean eliminar(Long id) {
    // si el id es nulo, no se puede eliminar nada
    if (id == null) {
      return false;
    }
    
    // convertir el el objeto en un valor numérico Long
    Long idEditorial = id;
    
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
        .equals(idEditorial)) {
        iterador.remove();
        // eliminar la editorial fue una operación exitosa, terminar
        return true;
      }
    }
    
    // no encontró la editorial con ese ID y no se pudo eliminar
    return false;
  }
  
  @Override
  public List<Editorial> buscarPorId(Long id) {
    // se incializa una lista vacía
    List<Editorial> resultados = new ArrayList<Editorial>();
    
    // si el id es nulo, se devuelve la lista vacía
    if (id == null) {
      return resultados;
    }
    
    // obtener el id de la editorial
    Long idEditorial = id;
    
    // buscar la editorial que concida con el id
    for (Editorial editorialActual : this.editoriales) {
      if (editorialActual.getId()
        .equals(idEditorial)) {
        // se encontró una coincidencia, entonces agregarla a la lista de resultados
        resultados.add(editorialActual);
      }
    }
    
    // devolver los resultados de la búsqueda
    return resultados;
  }
  
  @Override
  public List<Editorial> listar() {
    // TODO: Investigar cómo hacer que esta lista que se retorna quede "congelada" para evitar
    // que le agreguen más editoriales sin pasar primero por el método guardar
    return this.editoriales;
  }

}
