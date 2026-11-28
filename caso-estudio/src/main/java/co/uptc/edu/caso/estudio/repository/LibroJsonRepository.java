package co.uptc.edu.caso.estudio.repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.uptc.edu.caso.estudio.negocio.Libro;

public class LibroJsonRepository implements LibroRepository {

    private static final String ARCHIVO = "datos/libros.json";

    private Gson gson;

    public LibroJsonRepository() {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void guardar(Libro libro) {

        List<Libro> libros = listar();

        libros.add(libro);

        guardarLista(libros);

        System.out.println("Libro guardado en JSON.");
    }

    @Override
    public List<Libro> listar() {

        File archivo = new File(ARCHIVO);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {

            Type tipoLista = new TypeToken<List<Libro>>() {
            }.getType();

            List<Libro> libros = gson.fromJson(reader, tipoLista);

            if (libros == null) {
                return new ArrayList<>();
            }

            return libros;

        } catch (Exception e) {

            System.out.println("Error al leer JSON: "
                    + e.getMessage());

            return new ArrayList<>();
        }
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {

        List<Libro> libros = listar();

        for (Libro libro : libros) {

            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }

        return null;
    }

    @Override
    public void actualizar(Libro libro) {

        List<Libro> libros = listar();

        for (int i = 0; i < libros.size(); i++) {

            if (libros.get(i).getIsbn().equals(libro.getIsbn())) {

                libros.set(i, libro);

                guardarLista(libros);

                System.out.println("Libro actualizado en JSON.");

                return;
            }
        }

        System.out.println("No se encontró el libro para actualizar.");
    }

    @Override
    public void eliminar(String isbn) {

        List<Libro> libros = listar();

        for (int i = 0; i < libros.size(); i++) {

            if (libros.get(i).getIsbn().equals(isbn)) {

                libros.remove(i);

                guardarLista(libros);

                System.out.println("Libro eliminado del JSON.");

                return;
            }
        }

        System.out.println("No se encontró el libro para eliminar.");
    }

    private void guardarLista(List<Libro> libros) {

        try {

            File archivo = new File(ARCHIVO);

            File carpeta = archivo.getParentFile();

            if (carpeta != null && !carpeta.exists()) {
                carpeta.mkdirs();
            }

            try (FileWriter writer = new FileWriter(archivo)) {

                gson.toJson(libros, writer);
            }

        } catch (Exception e) {

            System.out.println("Error al guardar JSON: "
                    + e.getMessage());
        }
    }
}