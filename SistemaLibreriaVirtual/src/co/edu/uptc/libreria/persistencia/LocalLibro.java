package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.interfaces.IGestionLibro;
import co.edu.uptc.modelo.Libro;

public class LocalLibro implements IGestionLibro {

    private List<Libro> libros;

    public LocalLibro() {

        libros = new ArrayList<>();
    }

    @Override
    public void guardar(Libro libro) {

        libros.add(libro);
    }

    @Override
    public void actualizar(Libro libro) {

        for (int i = 0; i < libros.size(); i++) {

            if (libros.get(i).getCodigo()
                    .equals(libro.getCodigo())) {

                libros.set(i, libro);
            }
        }
    }

    @Override
    public void eliminar(String codigo) {

        for (Libro libro : libros) {

            if (libro.getCodigo().equals(codigo)) {

                libros.remove(libro);
                break;
            }
        }
    }

    @Override
    public List<Libro> listar() {

        return libros;
    }
}