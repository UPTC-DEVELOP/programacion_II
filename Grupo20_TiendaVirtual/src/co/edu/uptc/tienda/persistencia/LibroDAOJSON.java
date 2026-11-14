package co.edu.uptc.tienda.persistencia;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.tienda.excepcion.LibroExcepcion;
import co.edu.uptc.tienda.modelo.Libro;

public class LibroDAOJSON implements LibroDAO {

    private final File archivo;

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public LibroDAOJSON(String ruta) {
        archivo = new File(ruta);
    }

    private List<Libro> cargarLibros() throws LibroExcepcion {

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {

            Type tipoLista = new TypeToken<ArrayList<Libro>>() {}.getType();

            List<Libro> libros = gson.fromJson(reader, tipoLista);

            return libros == null ? new ArrayList<>() : libros;

        } catch (IOException e) {
            throw new LibroExcepcion("No se pudieron leer los libros del archivo " + archivo.getPath() + ".");
        } catch (JsonSyntaxException e) {
            throw new LibroExcepcion("El archivo " + archivo.getPath() + " no tiene un formato JSON válido.");
        }
    }

    private boolean guardarLibros(List<Libro> libros) throws LibroExcepcion {

        File carpeta = archivo.getParentFile();
        if (carpeta != null) {
            carpeta.mkdirs();
        }

        try (FileWriter writer = new FileWriter(archivo)) {

            gson.toJson(libros, writer);
            return true;

        } catch (IOException e) {
            throw new LibroExcepcion("No se pudieron guardar los libros en el archivo " + archivo.getPath() + ".");
        }
    }

    @Override
    public boolean registrar(Libro libro) throws LibroExcepcion {

        List<Libro> libros = cargarLibros();

        if (buscarPorIsbn(libro.getIsbn()) != null) {
            return false;
        }

        libros.add(libro);

        return guardarLibros(libros);
    }

    @Override
    public Libro buscarPorIsbn(String isbn) throws LibroExcepcion {

        List<Libro> libros = cargarLibros();

        for (Libro libro : libros) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }

        return null;
    }

    @Override
    public List<Libro> listar() throws LibroExcepcion {
        return cargarLibros();
    }

    @Override
    public boolean actualizar(Libro libroActualizado) throws LibroExcepcion {

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
    public boolean eliminar(String isbn) throws LibroExcepcion {

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
