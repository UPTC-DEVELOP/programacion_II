
package persistencia;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import negocio.Libro;

public class LibroDAOJSON implements LibroDAO {

    private final String archivo = "libros.json";

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private List<Libro> cargarLibros() {

        File fichero = new File(archivo);

        if (!fichero.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(fichero)) {

            Type tipoLista = new TypeToken<ArrayList<Libro>>() {}.getType();

            List<Libro> libros = gson.fromJson(reader, tipoLista);

            return libros == null ? new ArrayList<>() : libros;

        } catch (IOException e) {
            System.out.println("Error al leer JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private boolean guardarLibros(List<Libro> libros) {

        try (FileWriter writer = new FileWriter(archivo)) {

            gson.toJson(libros, writer);
            return true;

        } catch (IOException e) {
            System.out.println("Error al guardar JSON: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean registrar(Libro libro) {

        List<Libro> libros = cargarLibros();

        if (buscarPorIsbn(libro.getIsbn()) != null) {
            return false;
        }

        libros.add(libro);

        return guardarLibros(libros);
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {

        List<Libro> libros = cargarLibros();

        for (Libro libro : libros) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }

        return null;
    }

    @Override
    public List<Libro> listar() {
        return cargarLibros();
    }

    @Override
    public boolean actualizar(Libro libroActualizado) {

        List<Libro> libros = cargarLibros();

        for (int i = 0; i < libros.size(); i++) {

            if (libros.get(i).getIsbn()
                    .equals(libroActualizado.getIsbn())) {

                libros.set(i, libroActualizado);

                return guardarLibros(libros);
            }
        }

        return false;
    }

    @Override
    public boolean eliminar(String isbn) {

        List<Libro> libros = cargarLibros();

        boolean eliminado = libros.removeIf(
                libro -> libro.getIsbn().equals(isbn)
        );

        if (eliminado) {
            return guardarLibros(libros);
        }

        return false;
    }
}
