package co.edu.uptc.tiendalibros.persistencia.memoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioAutor;
import co.edu.uptc.tiendalibros.modelo.Autor;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

public class RepositorioAutorEnMemoria implements IRepositorioAutor {
  
  private final List<Autor> autores;
  
  /// Truco para generar Ids consecutivos
  private final AtomicLong contador;
  
  /// Crea un nuevo objeto [RepositorioAutorEnMemoria][RepositorioAutorEnMemoria].
  public RepositorioAutorEnMemoria() {
    this.autores = new ArrayList<Autor>();
    // el contador se inicia en 0
    this.contador = new AtomicLong(0);
    
    // datos de ejemplo para probar la interfaz gráfica
    try {
      guardar(new Autor("Silvia", "Vásquez-Lavado"));
      guardar(new Autor("camilo", "sexto"));
      guardar(new Autor("lilia", "dueñas"));
    } catch (ViolacionElementoNoExisteException excepcion) {
      // no debería ocurrir este error porque son autores nuevos, sin ID
      throw new IllegalStateException("No se pudieron crear los autores de ejemplo.", excepcion);
    }
  }
  
  @Override
  public Autor guardar(Autor autor) throws ViolacionElementoNoExisteException {
    // si el elemento es nulo, no se puede guardar nada
    if (autor == null) {
      throw new IllegalArgumentException("Error: No se puede guardar un autor nulo.");
    }
    
    // todo depende del id. Si el id es nulo, el autor aún no se ha guardado
    if (autor.getId() == null) {
      // usar el truco para generar un id único
      Long idNuevo = this.contador.incrementAndGet();
      
      Autor autorNuevo = new Autor(idNuevo, autor.getNombre(), autor.getApellidos());
      
      // agregar el autor al repositorio
      this.autores.add(autorNuevo);
      return autorNuevo;
    }
    
    // el id no es nulo: se busca el autor que se quiere actualizar
    for (int i = 0; i < this.autores.size(); i++) {
      if (this.autores.get(i)
        .getId()
        .equals(autor.getId())) {
        // reemplazar el objeto autor
        this.autores.set(i, autor);
        return autor;
      }
    }
    
    // en este punto no se encontró el autor que se quería actualizar
    throw new ViolacionElementoNoExisteException(
      String.format("No se pudo encontrar el autor '%s' que se quería editar y guardar", autor.getNombre())
    );
  }
  
  @Override
  public boolean eliminar(Long id) {
    // si el id es nulo, no se puede eliminar nada
    if (id == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar con un ID nulo.");
    }
    
    // Eliminar el autor con el ID dado, pero esta vez usando la técnica de iterador
    Iterator<Autor> iterador = this.autores.iterator();
    while (iterador.hasNext()) {
      Autor autor = iterador.next();
      if (autor.getId()
        .equals(id)) {
        iterador.remove();
        // eliminar el autor fue una operación exitosa, terminar
        return true;
      }
    }
    
    // no encontró el autor con ese ID y no se pudo eliminar
    return false;
  }
  
  @Override
  public Optional<Autor> buscarPorId(Long id) {
    // si el id es nulo, se devuelve un valor vacío
    if (id == null) {
      return Optional.empty();
    }
    
    // buscar el autor que coincida con el id
    for (Autor autorActual : this.autores) {
      if (autorActual.getId()
        .equals(id)) {
        // se encontró una coincidencia, entonces retornarla como resultado
        return Optional.of(autorActual);
      }
    }
    
    // no hubo resultados
    return Optional.empty();
  }
  
  @Override
  public List<Autor> listar() {
    return new ArrayList<Autor>(this.autores);
  }
}
