package co.edu.uptc.tienda.persistencia;

import java.util.List;

import co.edu.uptc.tienda.interfaces.IGestionLibro;
import co.edu.uptc.tienda.modelo.Libro;
import co.edu.uptc.tienda.modelo.Libros;

public class LocalLibro implements IGestionLibro {

    private Libros libros;
    private long siguienteIdLibro;

    public LocalLibro() {
        this.libros = new Libros();
        this.siguienteIdLibro = 1L;
    }

    @Override
    public void agregar(Libro libro) {
        libro.setIdLibro(this.siguienteIdLibro);
        this.siguienteIdLibro++;

        libros.agregarLibro(libro);
    }

    @Override
    public List<Libro> listar() {
        return libros.listarLibros();
    }

    @Override
    public Libro buscar(String titulo) {
        return libros.buscarLibro(titulo);
    }

    @Override
    public boolean actualizar(Libro libroActualizar) {
        return libros.actualizarLibro(libroActualizar);
    }

    @Override
    public boolean eliminar(String titulo) {
        return libros.eliminarLibro(titulo);
    }
}
