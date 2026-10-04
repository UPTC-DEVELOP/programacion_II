package com.tiendavirtual.negocio;
import com.tiendavirtual.modelo.Libro; import com.tiendavirtual.repositorio.LibroRepository; import com.tiendavirtual.util.CalculadoraIva; import java.time.Year; import java.util.List;
public class LibroService {
 private final LibroRepository repo; public LibroService(LibroRepository repo){this.repo=repo;}
 public void registrar(Libro l){validar(l);if(repo.buscarPorIsbn(l.getIsbn())!=null)throw new IllegalArgumentException("El ISBN ya existe.");repo.guardar(l);}
 public void actualizar(Libro l){validar(l);if(repo.buscarPorIsbn(l.getIsbn())==null)throw new IllegalArgumentException("El libro no existe.");repo.actualizar(l);}
 public void eliminar(String isbn){Libro l=repo.buscarPorIsbn(isbn);if(l==null)throw new IllegalArgumentException("El libro no existe.");if(l.isTieneVentasAsociadas())throw new IllegalStateException("No se puede eliminar: el libro tiene ventas asociadas.");repo.eliminar(isbn);}
 public List<Libro> listar(){return repo.listar();} public Libro buscar(String isbn){return repo.buscarPorIsbn(isbn);}
 public void marcarVentaAsociada(String isbn){Libro l=buscar(isbn);if(l==null)throw new IllegalArgumentException("El libro no existe.");l.setTieneVentasAsociadas(true);}
 public double precioFinal(double precio,double iva){return precio+(precio*iva);}
 private void validar(Libro l){
  if(l==null)throw new IllegalArgumentException("Libro obligatorio.");
  if(v(l.getIsbn()))throw new IllegalArgumentException("ISBN obligatorio.");
  if(v(l.getTitulo()))throw new IllegalArgumentException("Título obligatorio.");
  if(v(l.getAutor()))throw new IllegalArgumentException("Autor obligatorio.");
  if(l.getAnio()<1||l.getAnio()>Year.now().getValue())throw new IllegalArgumentException("Año inválido.");
  if(v(l.getCategoria())||v(l.getEditorial()))throw new IllegalArgumentException("Categoría y editorial son obligatorias.");
  if(l.getPaginas()<=0)throw new IllegalArgumentException("Páginas debe ser mayor que 0.");
  if(l.getPrecio()<=0)throw new IllegalArgumentException("Precio debe ser mayor que 0.");
  if(l.getStock()<0)throw new IllegalArgumentException("Stock no puede ser negativo.");
  if(!"Físico".equals(l.getFormato())&&!"Digital".equals(l.getFormato()))throw new IllegalArgumentException("Formato inválido.");
  if(l.getIva()!=.05&&l.getIva()!=.19)throw new IllegalArgumentException("IVA debe ser 5% o 19%.");
 }
 private boolean v(String s){return s==null||s.trim().isEmpty();}
}
