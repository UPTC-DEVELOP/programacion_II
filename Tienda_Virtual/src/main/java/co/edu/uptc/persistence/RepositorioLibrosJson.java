package co.edu.uptc.persistence;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import co.edu.uptc.model.Libro;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLibrosJson implements IRepositorioLibros {
    private final String ARCHIVO = "libros.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public RepositorioLibrosJson() {
        File file = new File(ARCHIVO);
        if (!file.exists()) {
            cargarDatosIniciales();
        }
    }

    private void cargarDatosIniciales() {
        List<Libro> iniciales = new ArrayList<>();
        iniciales.add(new Libro("978-0132350884", "Clean Code", "Robert C. Martin", 2008, "Programación", "Prentice Hall", 464, 45000.0, 10, "Físico", 19.0));
        iniciales.add(new Libro("978-0201633610", "Design Patterns", "Erich Gamma", 1994, "Arquitectura", "Addison-Wesley", 395, 52000.0, 5, "Digital", 0.0));
        iniciales.add(new Libro("978-0134685991", "Effective Java", "Joshua Bloch", 2018, "Programación", "Addison-Wesley", 412, 48000.0, 8, "Físico", 19.0));
        iniciales.add(new Libro("978-8420652085", "Cien Años de Soledad", "Gabriel García Márquez", 1967, "Literatura", "Editorial Sudamericana", 471, 35000.0, 15, "Físico", 19.0));
        guardar(iniciales);
    }

    private List<Libro> cargar() {
        File file = new File(ARCHIVO);
        if (!file.exists()) return new ArrayList<>();
        try (Reader r = new FileReader(file)) {
            Type t = new TypeToken<ArrayList<Libro>>(){}.getType();
            List<Libro> res = gson.fromJson(r, t);
            return res != null ? res : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void guardar(List<Libro> lista) {
        try (Writer w = new FileWriter(ARCHIVO)) {
            gson.toJson(lista, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean guardarLibro(Libro libro) {
        List<Libro> lista = cargar();
        lista.add(libro);
        guardar(lista);
        return true;
    }

    @Override
    public boolean actualizarLibro(Libro libro) {
        List<Libro> lista = cargar();
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIsbn().equals(libro.getIsbn())) {
                lista.set(i, libro);
                guardar(lista);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        List<Libro> lista = cargar();
        boolean rem = lista.removeIf(l -> l.getIsbn().equals(isbn));
        if (rem) guardar(lista);
        return rem;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        return cargar().stream().filter(l -> l.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    @Override
    public List<Libro> obtenerTodos() {
        return cargar();
    }
}