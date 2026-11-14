package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.persistencia.LibroDAO;
import co.edu.uptc.tienda.persistencia.LibroDAOJSON;
import co.edu.uptc.tienda.persistencia.LibroDAOMariaDB;

public class ControlLibro {

    private List<Libro> libros;
    private List<LibroDAO> persistencias;
    private String avisoCarga;

    public ControlLibro() {
        this(crearPersistencias());
    }

    public ControlLibro(List<LibroDAO> persistencias) {
        this.persistencias = persistencias;
        libros = new ArrayList<>();
        cargar();
    }

    private static List<LibroDAO> crearPersistencias() {
        List<LibroDAO> persistencias = new ArrayList<>();
        persistencias.add(new LibroDAOMariaDB());
        persistencias.add(new LibroDAOJSON("datos/libros.json"));
        return persistencias;
    }

    private void cargar() {
        List<String> errores = new ArrayList<>();
        for (LibroDAO persistencia : persistencias) {
            try {
                libros = persistencia.listar();
                break;
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        if (!errores.isEmpty()) {
            avisoCarga = String.join("\n", errores);
        }
    }

    public String getAvisoCarga() {
        return avisoCarga;
    }

    public boolean registrarLibro(Libro libro) throws LibroExcepcion {

        if (libro == null) {
            return false;
        }

        if (buscarLibro(libro.getIsbn()) != null) {
            return false;
        }

        libros.add(libro);

        List<String> errores = new ArrayList<>();
        for (LibroDAO persistencia : persistencias) {
            try {
                persistencia.registrar(libro);
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        avisarErrores(errores);
        return true;
    }

    public Libro buscarLibro(String isbn) {

        for (Libro libro : libros) {

            if (libro.getIsbn().equalsIgnoreCase(isbn)) {
                return libro;
            }
        }

        return null;
    }

    public boolean actualizarLibro(String isbn, String titulo,
            String autor, int anioPublicacion, String editorial,
            int numPaginas, double precio, double ivaPorcentaje,
            int stock, String formato) throws LibroExcepcion {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            return false;
        }

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setEditorial(editorial);
        libro.setNumPaginas(numPaginas);
        libro.setPrecio(precio);
        libro.setIvaPorcentaje(ivaPorcentaje);
        libro.setStock(stock);
        libro.setFormato(formato);

        List<String> errores = new ArrayList<>();
        for (LibroDAO persistencia : persistencias) {
            try {
                persistencia.actualizar(libro);
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        avisarErrores(errores);
        return true;
    }

    public boolean eliminarLibro(String isbn) throws LibroExcepcion {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            return false;
        }

        libros.remove(libro);

        List<String> errores = new ArrayList<>();
        for (LibroDAO persistencia : persistencias) {
            try {
                persistencia.eliminar(libro.getIsbn());
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        avisarErrores(errores);
        return true;
    }

    public List<Libro> listarLibros() {
        return libros;
    }

    private void avisarErrores(List<String> errores) throws LibroExcepcion {
        if (!errores.isEmpty()) {
            throw new LibroExcepcion("El cambio se hizo en el programa, pero no se guardó en todas partes:\n"
                    + String.join("\n", errores));
        }
    }
}
