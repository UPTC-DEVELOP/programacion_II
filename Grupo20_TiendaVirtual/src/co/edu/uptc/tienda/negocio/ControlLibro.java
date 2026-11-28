package co.edu.uptc.tienda.negocio;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.tienda.excepcion.CompraExcepcion;
import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.interfaz.IGestionCompra;
import co.edu.uptc.tienda.interfaz.IRegistroOperaciones;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.persistencia.LibroDAO;
import co.edu.uptc.tienda.persistencia.LibroDAOJSON;
import co.edu.uptc.tienda.persistencia.LibroDAOMariaDB;
import co.edu.uptc.tienda.persistencia.RegistroOperacionesTxt;

public class ControlLibro {

    private List<Libro> libros;
    private List<LibroDAO> persistencias;
    private IGestionCompra compras;
    private IRegistroOperaciones registro;
    private String avisoCarga;

    public ControlLibro() {
        this(crearPersistencias());
    }

    public ControlLibro(List<LibroDAO> persistencias) {
        this(persistencias, new GestionCompra(), new RegistroOperacionesTxt("datos/operaciones.txt"));
    }

    public ControlLibro(List<LibroDAO> persistencias, IGestionCompra compras, IRegistroOperaciones registro) {
        this.persistencias = persistencias;
        this.compras = compras;
        this.registro = registro;
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
        registro.registrar("Libros", "Registrar libro", "ISBN " + libro.getIsbn() + ", " + libro.getTitulo());
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
            String autor, int anioPublicacion, String categoria, String editorial,
            int numPaginas, double precio, double ivaPorcentaje,
            int stock, String formato) throws LibroExcepcion {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            return false;
        }

        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setAnioPublicacion(anioPublicacion);
        libro.setCategoria(categoria);
        libro.setEditorial(editorial);
        libro.setNumPaginas(numPaginas);
        libro.setPrecio(precio);
        libro.setIvaPorcentaje(ivaPorcentaje);
        libro.setStock(stock);
        libro.setFormato(formato);

        List<String> errores = new ArrayList<>();
        for (LibroDAO persistencia : persistencias) {
            try {
                if (!persistencia.actualizar(libro)) {
                    persistencia.registrar(libro);
                }
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        registro.registrar("Libros", "Actualizar libro", "ISBN " + isbn + ", " + titulo);
        avisarErrores(errores);
        return true;
    }

    public void descontarStock(String isbn, int cantidad) throws LibroExcepcion {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            throw new LibroExcepcion("No existe un libro con el ISBN " + isbn + ".");
        }

        if (cantidad <= 0 || cantidad > libro.getStock()) {
            throw new LibroExcepcion("No hay inventario suficiente de \"" + libro.getTitulo() + "\".");
        }

        libro.setStock(libro.getStock() - cantidad);

        List<String> errores = new ArrayList<>();
        for (LibroDAO persistencia : persistencias) {
            try {
                if (!persistencia.actualizar(libro)) {
                    persistencia.registrar(libro);
                }
            } catch (LibroExcepcion e) {
                errores.add(e.getMessage());
            }
        }
        avisarErrores(errores);
    }

    public boolean eliminarLibro(String isbn) throws LibroExcepcion {

        Libro libro = buscarLibro(isbn);

        if (libro == null) {
            return false;
        }

        if (tieneVentas(isbn)) {
            throw new LibroExcepcion("No se puede eliminar el libro porque tiene ventas asociadas.");
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
        registro.registrar("Libros", "Eliminar libro", "ISBN " + libro.getIsbn() + ", " + libro.getTitulo());
        avisarErrores(errores);
        return true;
    }

    public boolean tieneVentas(String isbn) throws LibroExcepcion {
        try {
            return compras.tieneVentas(isbn);
        } catch (CompraExcepcion e) {
            throw new LibroExcepcion("No se pudo revisar si el libro tiene ventas:\n" + e.getMessage());
        }
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
