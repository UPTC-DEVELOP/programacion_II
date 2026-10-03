package main.co.edu.uptc.fesad.tpsi.tienda.persistencia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import main.co.edu.uptc.fesad.tpsi.tienda.interfaces.IRepositorioAutor;
import main.co.edu.uptc.fesad.tpsi.tienda.modelo.Autor;

public class RepositorioAutorenMemoria implements IRepositorioAutor {
  
  private final List<Autor> autores;
  
  /// Truco para generar Ids consecutivos
  private final AtomicLong contador;
  
  /// Crea un nuevo objeto [RepositorioEditorialEnMemoria][RepositorioEditorialEnMemoria].
  public RepositorioAutorenMemoria() {
    this.autores = new ArrayList<Autor>();
    // el contador se inicia en 10
    this.contador = new AtomicLong(10);
    
    // datos de ejemplo para probar la interfaz gráfica
    guardar(new Autor("marcos", "torres"));
    guardar(new Autor("camilo", "sexto"));
    guardar(new Autor("lilia", "dueñas"));
    
  }
  
  @Override
  public Autor guardar(Autor elemento) {
    // si el elemento es nulo, no se puede guardar nada
    if (elemento == null) {
      return null;
    }
    
    // obtener la editorial que se quiere guardar
    Autor autor = elemento;
    
    // todo depende del id
    // si el id es nulo, la editorial no se ha guardado en la base de datos
    if (autor.getId() == null) {
      // usar el truco para generar un id único
      Long idNuevo = this.contador.incrementAndGet();
      autor.setId(idNuevo);
      // agregar la editorial a la base de datos
      this.autores.add(autor);
      return autor;
    }
    // el id no es nulo, luego toca reemplazar la editorial en la base de datos
    else {
      for (int i = 0; i < this.autores.size(); i++) {
        if (this.autores.get(i)
          .getId()
          .equals(autor.getId())) {
          // reemplazar el objeto editoria
          this.autores.set(i, autor);
          return autor;
        }
      }
    }
    
    // TODO: analizar si se puede encontrar una mejor alternativa a lanzar una excepción
    throw new IllegalArgumentException(
      "No se encontró el autor con ID: " + autor.getId()
    );
  }
  
  @Override
  public boolean eliminar(Long id) {
    // si el id es nulo, no se puede eliminar nada
    if (id == null) {
      return false;
    }
    
    // Eliminar la editorial con el ID dado, pero esta vez usando la técnica de iterador
    Iterator<Autor> iterador = this.autores.iterator();
    while (iterador.hasNext()) {
      Autor autor = iterador.next();
      if (autor.getId()
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
  public List<Autor> buscarPorId(Long id) {
    // se incializa una lista vacía
    List<Autor> resultados = new ArrayList<Autor>();
    
    // si el id es nulo, se devuelve la lista vacía
    if (id == null) {
      return resultados;
    }
    
    // buscar la editorial que concida con el id
    for (Autor autorActual : this.autores) {
      if (autorActual.getId()
        .equals(id)) {
        // se encontró una coincidencia, entonces agregarla a la lista de resultados
        resultados.add(autorActual);
      }
    }
    
    // devolver los resultados de la búsqueda
    return resultados;
  }
  
  @Override
  public List<Autor> listar() {
    // TODO: Investigar cómo hacer que esta lista que se retorna quede "congelada" para evitar
    // que le agreguen más editoriales sin pasar primero por el método guardar
    // por ahora, devolver una copia de la lista
    return new ArrayList<Autor>(this.autores);
  }
}
