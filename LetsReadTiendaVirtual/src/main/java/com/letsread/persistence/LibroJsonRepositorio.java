package com.letsread.persistence;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.letsread.model.Libro;

/**
 * Guarda los libros en un archivo JSON usando Gson.
 */
public class LibroJsonRepositorio implements LibroRepositorio {

    private static final String RUTA_POR_DEFECTO = "libros_letsread.json";
    private static final Type TIPO_LISTA = new TypeToken<List<Libro>>() { }.getType();

    private final Path ruta;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public LibroJsonRepositorio() {
        this(RUTA_POR_DEFECTO);
    }

    public LibroJsonRepositorio(String rutaArchivo) {
        this.ruta = Paths.get(rutaArchivo);
    }

    @Override
    public List<Libro> listar() {
        return cargar();
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        for (Libro libro : cargar()) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

    @Override
    public void guardar(Libro libro) {
        List<Libro> libros = cargar();
        for (Libro existente : libros) {
            if (existente.getIsbn().equals(libro.getIsbn())) {
                throw new IllegalArgumentException("Ya existe un libro con ISBN " + libro.getIsbn());
            }
        }
        libros.add(libro);
        escribir(libros);
    }

    @Override
    public void actualizar(Libro libro) {
        List<Libro> libros = cargar();
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getIsbn().equals(libro.getIsbn())) {
                libros.set(i, libro);
                escribir(libros);
                return;
            }
        }
        throw new IllegalArgumentException("No existe un libro con ISBN " + libro.getIsbn());
    }

    @Override
    public void eliminar(String isbn) {
        List<Libro> libros = cargar();
        if (libros.removeIf(l -> l.getIsbn().equals(isbn))) {
            escribir(libros);
        }
    }

    private List<Libro> cargar() {
        if (!Files.exists(ruta)) {
            return new ArrayList<>();
        }
        try (Reader lector = Files.newBufferedReader(ruta, StandardCharsets.UTF_8)) {
            List<Libro> libros = gson.fromJson(lector, TIPO_LISTA);
            return libros != null ? libros : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer " + ruta + ": " + e.getMessage(), e);
        }
    }

    private void escribir(List<Libro> libros) {
        try (Writer escritor = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) {
            gson.toJson(libros, TIPO_LISTA, escritor);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir " + ruta + ": " + e.getMessage(), e);
        }
    }
}