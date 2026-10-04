package co.edu.uptc.libreria.negocio;

import java.util.List;

import co.edu.uptc.libreria.interfaces.IGestionLibro;
import co.edu.uptc.libreria.modelo.Libro;

public class GestionLibro {

    private IGestionLibro libro;

    public GestionLibro(IGestionLibro libro) {
        super();
        this.libro = libro;
    }

    public void guardarLibro(Libro libroNuevo) {

        libro.guardar(libroNuevo);
    }

    public void actualizarLibro(Libro libroNuevo) {

        libro.actualizar(libroNuevo);
    }

    public void eliminarLibro(String codigo) {

        libro.eliminar(codigo);
    }

    public List<Libro> listarLibros() {

        return libro.listar();
    }
}