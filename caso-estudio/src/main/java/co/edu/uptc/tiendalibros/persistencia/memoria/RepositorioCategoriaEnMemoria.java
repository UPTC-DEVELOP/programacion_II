package co.edu.uptc.tiendalibros.persistencia.memoria;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import co.edu.uptc.tiendalibros.interfaces.IRepositorioCategoria;
import co.edu.uptc.tiendalibros.modelo.Categoria;
import co.edu.uptc.tiendalibros.negocio.excepciones.ViolacionElementoNoExisteException;

public class RepositorioCategoriaEnMemoria implements IRepositorioCategoria {
  
  private final List<Categoria> categorias;
  
  private final AtomicLong contador;
  
  public RepositorioCategoriaEnMemoria() {
    this.categorias = new ArrayList<Categoria>();
    this.contador = new AtomicLong(0);
    
    // Datos de ejemplo
    try {
      guardar(new Categoria("Novela", "Libros de ficción narrativa", "CAT001"));
      guardar(new Categoria("Ciencia", "Libros relacionados con ciencia y conocimiento", "CAT002"));
      guardar(new Categoria("Historia", "Libros sobre hechos históricos", "CAT003"));
    } catch (ViolacionElementoNoExisteException excepcion) {
      // no debería ocurrir este error porque son categorías nuevas, sin ID
      throw new IllegalStateException("No se pudieron crear las categorías de ejemplo.", excepcion);
    }
  }
  
  @Override
  public Categoria guardar(Categoria categoria) throws ViolacionElementoNoExisteException {
    // si el elemento es nulo, no se puede guardar nada
    if (categoria == null) {
      throw new IllegalArgumentException("Error: No se puede guardar una categoría nula.");
    }
    
    // todo depende del id. Si el id es nulo, la categoría aún no se ha guardado
    if (categoria.getId() == null) {
      Long idNuevo = this.contador.incrementAndGet();
      
      Categoria categoriaNueva = new Categoria(
        idNuevo, categoria.getNombre(), categoria.getDescripcion(), categoria.getCodigo()
      );
      
      this.categorias.add(categoriaNueva);
      return categoriaNueva;
    }
    
    // el id no es nulo: se busca la categoría que se quiere actualizar
    for (int i = 0; i < this.categorias.size(); i++) {
      if (this.categorias.get(i)
        .getId()
        .equals(categoria.getId())) {
        // reemplazar el objeto categoría
        this.categorias.set(i, categoria);
        return categoria;
      }
    }
    
    // en este punto no se encontró la categoría que se quería actualizar
    throw new ViolacionElementoNoExisteException(
      String.format("No se pudo encontrar la categoría '%s' que se quería editar y guardar", categoria.getNombre())
    );
  }
  
  @Override
  public boolean eliminar(Long id) {
    
    if (id == null) {
      throw new IllegalArgumentException("Error: No se puede eliminar con un ID nulo.");
    }
    
    Iterator<Categoria> iterador = this.categorias.iterator();
    
    while (iterador.hasNext()) {
      
      Categoria categoria = iterador.next();
      
      if (categoria.getId()
        .equals(id)) {
        iterador.remove();
        return true;
      }
    }
    
    return false;
  }
  
  @Override
  public Optional<Categoria> buscarPorId(Long id) {
    
    if (id == null) {
      return Optional.empty();
    }
    
    for (Categoria categoriaActual : this.categorias) {
      
      if (categoriaActual.getId()
        .equals(id)) {
        return Optional.of(categoriaActual);
      }
    }
    
    return Optional.empty();
  }
  
  @Override
  public List<Categoria> listar() {
    return new ArrayList<Categoria>(this.categorias);
  }
}