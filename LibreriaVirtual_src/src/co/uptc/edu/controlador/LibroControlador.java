package co.uptc.edu.controlador;

import java.util.List;

import co.uptc.edu.model.Libro;
import co.uptc.edu.model.Tienda;
import co.uptc.edu.model.ValidadorLibro;

// Controlador del inventario de libros: convierte los datos del formulario y delega en la Tienda
public class LibroControlador {

    private final Tienda tienda;
    private final SesionControlador sesion;

    public LibroControlador(Tienda tienda, SesionControlador sesion) {
        this.tienda = tienda;
        this.sesion = sesion;
    }

    public List<Libro> listarInventario() {
        return tienda.getInventario();
    }

    public List<String> getCategorias() {
        return ValidadorLibro.CATEGORIAS;
    }

    public List<String> getFormatos() {
        return ValidadorLibro.FORMATOS;
    }

    // Recibe los campos tal como vienen del formulario, los convierte y registra el libro
    public void registrarLibro(String isbn, String titulo, String autor, String anio, String categoria,
                               String editorial, String paginas, String precio, String stock, String formato) {
        sesion.exigirAdministrador("registrar libros");
        Libro libro = new Libro(limpiar(isbn), limpiar(titulo), limpiar(autor), limpiar(anio), categoria,
                limpiar(editorial), entero(paginas, "El número de páginas"), decimal(precio, "El precio"),
                entero(stock, "El stock"), formato);
        tienda.agregarLibro(libro);
    }

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private static int entero(String valor, String campo) {
        try {
            return Integer.parseInt(limpiar(valor));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " debe ser un número entero.");
        }
    }

    private static double decimal(String valor, String campo) {
        try {
            return Double.parseDouble(limpiar(valor).replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(campo + " debe ser un número válido.");
        }
    }
}
