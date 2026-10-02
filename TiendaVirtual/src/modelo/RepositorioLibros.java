package modelo;

import java.io.IOException;
import java.util.ArrayList;

import excepciones.ExcepcionValidacion;
import persistencia.PersistenciaLibros;

public class RepositorioLibros {
    private ArrayList<Libro> libros;

    public RepositorioLibros() {
        try {
            libros = PersistenciaLibros.cargarLibros();
        } catch (IOException e) {
            libros = new ArrayList<Libro>();
            System.err.println("No fue posible cargar libros.txt.");
        }

        if (libros.isEmpty()) {
            cargarLibrosIniciales();
        }
    }

    public ArrayList<Libro> getLibros() {
        return new ArrayList<Libro>(libros);
    }

    public void anadirLibro(Libro libro) throws ExcepcionValidacion {
        if (libro.getTitulo().trim().isEmpty()) {
            throw new ExcepcionValidacion("Ingrese el título del libro.");
        }

        if (libro.getAutor().trim().isEmpty()) {
            throw new ExcepcionValidacion("Ingrese el autor del libro.");
        }

        if (libro.getPrecio() <= 0) {
            throw new ExcepcionValidacion("El precio debe ser mayor que cero.");
        }

        libros.add(libro);
        guardarCambios();
    }

    public void quitarLibro(int posicion) throws ExcepcionValidacion {
        if (posicion < 0 || posicion >= libros.size()) {
            throw new ExcepcionValidacion("Seleccione un libro válido.");
        }

        Libro eliminado = libros.remove(posicion);

        try {
            PersistenciaLibros.guardarTodos(libros);
        } catch (IOException e) {
            libros.add(posicion, eliminado);
            throw new ExcepcionValidacion("No fue posible actualizar el archivo de libros.");
        }
    }

    private void cargarLibrosIniciales() {
        libros.add(new Libro("Cien años de soledad", "Gabriel García Márquez", "Físico", 45000));
        libros.add(new Libro("El principito", "Antoine de Saint-Exupéry", "Digital", 18000));
        libros.add(new Libro("1984", "George Orwell", "Físico", 32000));
        libros.add(new Libro("Don Quijote de la Mancha", "Miguel de Cervantes", "Digital", 25000));

        try {
            PersistenciaLibros.guardarTodos(libros);
        } catch (IOException e) {
            System.err.println("No fue posible crear libros.txt con los datos iniciales.");
        }
    }

    private void guardarCambios() throws ExcepcionValidacion {
        try {
            PersistenciaLibros.guardarTodos(libros);
        } catch (IOException e) {
            libros.remove(libros.size() - 1);
            throw new ExcepcionValidacion("No fue posible guardar el libro en el archivo de datos.");
        }
    }
}
