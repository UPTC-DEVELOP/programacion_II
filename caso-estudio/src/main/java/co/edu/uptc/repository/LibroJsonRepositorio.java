package co.edu.uptc.repository;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.model.Libro;

public class LibroJsonRepositorio implements ILibroRepositorio {

    private final Path archivo;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type tipoLista = new TypeToken<List<Libro>>() {}.getType();
    private final List<Libro> libros;

    public LibroJsonRepositorio(String ruta) {
        this.archivo = Path.of(ruta);
        this.libros = cargar();
    }

    private List<Libro> cargar() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
            List<Libro> lista = gson.fromJson(lector, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + archivo, e);
        }
    }

    private void guardarArchivo() {
        try {
            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }
            try (Writer escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
                gson.toJson(libros, tipoLista, escritor);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo escribir " + archivo, e);
        }
    }

    @Override
    public void guardar(Libro libro) {
    	 System.out.println("Guardando en: " + archivo.toAbsolutePath());
        libros.add(libro);
        guardarArchivo();
    }
    

    @Override
    public Libro buscar(String isbn) {
        return libros.stream()
                .filter(l -> l.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void actualizar(Libro libro) {
        for (int i = 0; i < libros.size(); i++) {
            if (libros.get(i).getIsbn().equals(libro.getIsbn())) {
                libros.set(i, libro);
                guardarArchivo();
                return;
            }
        }
    }

    @Override
    public void eliminar(String isbn) {
        boolean eliminado = libros.removeIf(l -> l.getIsbn().equals(isbn));
        if (eliminado) {
            guardarArchivo();
        }
    }

    @Override
    public List<Libro> listar() {
        return libros;
    }
}
	
