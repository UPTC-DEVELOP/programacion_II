package pruebas;
import modelo.*;
import java.nio.file.*;
public class PruebaCarrito {
 public static void main(String[] a) throws Exception {
  Files.createDirectories(Path.of("datos"));
  Path f=Path.of("datos","carrito.json");byte[] backup=Files.exists(f)?Files.readAllBytes(f):null;
  try {
    Files.deleteIfExists(f);
    Libro l=new Libro("Libro Test","Autor Test","Fisico",12000);
    RepositorioCarrito c=new RepositorioCarrito();c.cargar(java.util.List.of(l));c.agregar(l,2);
    if(c.calcularTotal()!=24000)throw new AssertionError("agregar");
    c.actualizarCantidad(0,3);if(c.calcularTotal()!=36000)throw new AssertionError("actualizar");
    RepositorioCarrito d=new RepositorioCarrito();d.cargar(java.util.List.of(l));if(d.calcularTotal()!=36000)throw new AssertionError("cargar");
    d.eliminar(0);if(d.calcularTotal()!=0)throw new AssertionError("eliminar");
    d.agregar(l,1);d.vaciar();if(d.calcularTotal()!=0)throw new AssertionError("vaciar");
    System.out.println("PRUEBA_CARRITO_OK");
  } finally {if(backup==null)Files.deleteIfExists(f);else Files.write(f,backup);}
 }
}
