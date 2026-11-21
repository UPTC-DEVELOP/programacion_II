package co.edu.uptc.persistence;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.model.Libro;

public class RepositorioLibrosJson implements IRepositorioLibros {
    private final String ARCHIVO = "libros.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Libro> cargar() {
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            List<Libro> iniciales = obtenerLibrosIniciales();
            guardar(iniciales);
            return iniciales;
        }
        try (Reader r = new FileReader(file)) {
            Type t = new TypeToken<ArrayList<Libro>>(){}.getType();
            List<Libro> res = gson.fromJson(r, t);
            return res != null ? res : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private List<Libro> obtenerLibrosIniciales() {
        List<Libro> lista = new ArrayList<>();
        lista.add(new Libro("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Programación", "Prentice Hall", 464, 45000.0, 10, "Físico", 19.0));
        lista.add(new Libro("978-0201633610", "Design Patterns", "Erich Gamma", 1994, "Arquitectura", "Addison-Wesley", 395, 52000.0, 5, "Digital", 0.0));
        lista.add(new Libro("978-0134685991", "Effective Java", "Joshua Bloch", 2018, "Programación", "Addison-Wesley", 412, 48000.0, 8, "Físico", 19.0));
        lista.add(new Libro("978-8420652146", "Cien Años de Soledad", "Gabriel García Márquez", 1967, "Literatura", "Editorial Sudamericana", 471, 35000.0, 15, "Físico", 19.0));
        lista.add(new Libro("978-0596009205", "Head First Design Patterns", "Eric Freeman", 2004, "Arquitectura", "O'Reilly Media", 678, 60000.0, 3, "Físico", 19.0));
        lista.add(new Libro("978-0321356680", "Java Concurrency in Practice", "Brian Goetz", 2006, "Programación", "Addison-Wesley", 384, 55000.0, 7, "Digital", 0.0));
        return lista;
    }

    private boolean guardar(List<Libro> lista) {
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(lista, w);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Libro> obtenerTodos() {
        return cargar();
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        if (isbn == null) return null;
        return cargar().stream()
                .filter(l -> l.getIsbn() != null && l.getIsbn().equalsIgnoreCase(isbn.trim()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean guardarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null) return false;
        List<Libro> lista = cargar();
        if (buscarPorIsbn(libro.getIsbn()) != null) return false;
        lista.add(libro);
        return guardar(lista);
    }

    @Override
    public boolean actualizarLibro(Libro libro) {
        if (libro == null || libro.getIsbn() == null) return false;
        List<Libro> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIsbn() != null && lista.get(i).getIsbn().equalsIgnoreCase(libro.getIsbn())) {
                lista.set(i, libro);
                return guardar(lista);
            }
        }
        return false;
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        if (isbn == null) return false;
        List<Libro> lista = cargar();
        boolean rem = lista.removeIf(l -> l.getIsbn() != null && l.getIsbn().equalsIgnoreCase(isbn));
        if (rem) {
            return guardar(lista);
        }
        return false;
    }
}