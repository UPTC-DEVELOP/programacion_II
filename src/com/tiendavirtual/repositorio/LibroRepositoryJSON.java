package com.tiendavirtual.repositorio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tiendavirtual.modelo.Libro;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LibroRepositoryJSON implements LibroRepository {

    private final String archivo = "libros.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Libro> cargar() {
        File file = new File(archivo);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(file)) {
            Type tipo = new TypeToken<List<Libro>>() {}.getType();
            List<Libro> lista = gson.fromJson(reader, tipo);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Error al leer el archivo JSON", e);
        }
    }

    private void guardarArchivo(List<Libro> libros) {
        try (FileWriter writer = new FileWriter(archivo)) {
            gson.toJson(libros, writer);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo JSON", e);
        }
    }

    @Override
    public void guardar(Libro libro) {
        List<Libro> libros = cargar();
        libros.add(libro);
        guardarArchivo(libros);
    }

    @Override
    public void actualizar(Libro libro) {
        List<Libro> libros = cargar();

        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getIsbn().equalsIgnoreCase(libro.getIsbn())) {
                libros.set(i, libro);
                guardarArchivo(libros);
                return;
            }
        }
    }

    @Override
    public void eliminar(String isbn) {
        List<Libro> libros = cargar();
        libros.removeIf(l -> l.getIsbn().equalsIgnoreCase(isbn));
        guardarArchivo(libros);
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        for (Libro libro : cargar()) {
            if (libro.getIsbn().equalsIgnoreCase(isbn)) {
                return libro;
            }
        }
        return null;
    }

    @Override
    public List<Libro> listar() {
        return cargar();
    }
}