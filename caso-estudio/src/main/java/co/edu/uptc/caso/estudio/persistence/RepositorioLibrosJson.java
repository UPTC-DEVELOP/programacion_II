package co.edu.uptc.caso.estudio.persistence;

import co.edu.uptc.caso.estudio.model.FabricaLibros;
import co.edu.uptc.caso.estudio.model.Libro;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Persistencia JSON para el catálogo de libros con siembra inicial si no existe el archivo
public class RepositorioLibrosJson implements IRepositorioLibros {

    private final String rutaArchivo;
    private final ManejadorArchivoJson<Libro> manejadorJson;

    public RepositorioLibrosJson() {
        this.rutaArchivo = ConfiguracionTienda.obtener("ruta.json.libros", "data/libros.json");
        Type tipoLista = new TypeToken<ArrayList<Libro>>() {
        }.getType();
        this.manejadorJson = new ManejadorArchivoJson<>(rutaArchivo, tipoLista);
    }

    // Carga los libros desde JSON o crea el catálogo inicial si el archivo aún no existe
    private List<Libro> cargar() {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            List<Libro> iniciales = obtenerLibrosIniciales();
            manejadorJson.guardarLista(iniciales);
            return iniciales;
        }
        List<Libro> leidos = manejadorJson.leerLista();
        List<Libro> normalizados = new ArrayList<>();
        for (Libro libro : leidos) {
            if (libro != null) {
                normalizados.add(normalizarInstancia(libro));
            }
        }
        return normalizados;
    }

    // Reconstruye la instancia de dominio mediante FabricaLibros
    private Libro normalizarInstancia(Libro l) {
        return FabricaLibros.crear(
                l.getIsbn(), l.getTitulo(), l.getAutor(), l.getAnio(),
                l.getCategoria(), l.getEditorial(), l.getPaginas(),
                l.getPrecioVenta(), l.getStock(), l.getFormato(), l.getPorcentajeIva());
    }

    // Catálogo base precargado cuando se inicializa por primera vez el archivo JSON
    private List<Libro> obtenerLibrosIniciales() {
        List<Libro> lista = new ArrayList<>();
        lista.add(FabricaLibros.crear("978-0132350884", "Clean Code", "Robert C. Martin", 2008,
                "Programación", "Prentice Hall", 464, 45000.0, 10, "Físico", 19.0));
        lista.add(FabricaLibros.crear("978-0201633610", "Design Patterns", "Erich Gamma", 1994,
                "Arquitectura", "Addison-Wesley", 395, 52000.0, 5, "Digital", 0.0));
        lista.add(FabricaLibros.crear("978-0134685991", "Effective Java", "Joshua Bloch", 2018,
                "Programación", "Addison-Wesley", 412, 48000.0, 8, "Físico", 19.0));
        lista.add(FabricaLibros.crear("978-8420652146", "Cien Años de Soledad", "Gabriel García Márquez", 1967,
                "Literatura", "Editorial Sudamericana", 471, 35000.0, 15, "Físico", 19.0));
        lista.add(FabricaLibros.crear("978-0596009205", "Head First Design Patterns", "Eric Freeman", 2004,
                "Arquitectura", "O'Reilly Media", 678, 60000.0, 3, "Físico", 19.0));
        lista.add(FabricaLibros.crear("978-0321356680", "Java Concurrency in Practice", "Brian Goetz", 2006,
                "Programación", "Addison-Wesley", 384, 55000.0, 7, "Digital", 0.0));
        return lista;
    }

    @Override
    public List<Libro> obtenerTodos() {
        return cargar();
    }

    // Busca un libro por ISBN ignorando mayúsculas y espacios externos
    @Override
    public Libro buscarPorIsbn(String isbn) {
        if (isbn == null) {
            return null;
        }
        String buscado = isbn.trim();
        List<Libro> lista = cargar();
        for (Libro libro : lista) {
            if (libro != null && libro.getIsbn() != null && libro.getIsbn().trim().equalsIgnoreCase(buscado)) {
                return libro;
            }
        }
        return null;
    }

    // Guarda un libro nuevo si no existe otro con el mismo ISBN
    @Override
    public boolean guardarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null) {
            return false;
        }
        String isbnNuevo = libro.getIsbn().trim();
        List<Libro> lista = cargar();
        for (Libro actual : lista) {
            if (actual != null && actual.getIsbn() != null && actual.getIsbn().trim().equalsIgnoreCase(isbnNuevo)) {
                return false;
            }
        }

        lista.add(normalizarInstancia(libro));
        manejadorJson.guardarLista(lista);
        return true;
    }

    // Actualiza los datos de un libro existente identificado por ISBN
    @Override
    public boolean actualizarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null) {
            return false;
        }
        String isbnObjetivo = libro.getIsbn().trim();
        List<Libro> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            Libro actual = lista.get(i);
            if (actual != null && actual.getIsbn() != null && actual.getIsbn().trim().equalsIgnoreCase(isbnObjetivo)) {
                lista.set(i, normalizarInstancia(libro));
                manejadorJson.guardarLista(lista);
                return true;
            }
        }
        return false;
    }

    // Elimina un libro por ISBN y guarda la lista si hubo cambios
    @Override
    public boolean eliminarLibro(String isbn) {
        if (isbn == null) {
            return false;
        }
        String isbnObjetivo = isbn.trim();
        List<Libro> lista = cargar();
        boolean removido = false;
        Iterator<Libro> it = lista.iterator();
        while (it.hasNext()) {
            Libro actual = it.next();
            if (actual != null && actual.getIsbn() != null && actual.getIsbn().trim().equalsIgnoreCase(isbnObjetivo)) {
                it.remove();
                removido = true;
            }
        }
        if (removido) {
            manejadorJson.guardarLista(lista);
        }
        return removido;
    }
}