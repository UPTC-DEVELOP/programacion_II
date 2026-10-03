package co.edu.uptc.persistence;

import co.edu.uptc.model.Libro;
import java.util.ArrayList;
import java.util.List;

public class RepositorioLibrosTexto implements IRepositorioLibros {

    private List<Libro> listaLibros;

    public RepositorioLibrosTexto() {
        this.listaLibros = new ArrayList<>();
        cargarDatosIniciales();
    }

    private void cargarDatosIniciales() {
        // Libro 1 de prueba
        listaLibros.add(new Libro(
                "978-0134685991",
                "Effective Java",
                "Joshua Bloch",
                2018,
                "Tecnología",
                "Addison-Wesley",
                412,
                120000.0,
                19.0,
                0.0,
                15,
                "Físico"
            ));

        // Libro 2 de prueba
        listaLibros.add(new Libro(
                "978-0307474728",
                "Cien años de soledad",
                "Gabriel García Márquez",
                1967,
                "Literatura",
                "Editorial Sudamericana",
                496,
                50000.0,
                19.0,
                0.0,
                20,
                "Físico"
            ));
    }

    @Override
    public boolean guardarLibro(Libro libro) {
        if (buscarPorIsbn(libro.getIsbn()) != null) {
            return false;
        }
        
        boolean resultado = listaLibros.add(libro);
        if (resultado) {
            ArchivoLoggerManager.registrarOperacion(
                "Registrado nuevo libro: " + libro.getTitulo() + " (ISBN: " + libro.getIsbn() + ")"
            );
        }
        return resultado;
    }

    @Override
    public boolean actualizarLibro(Libro libro) {
        Libro existente = buscarPorIsbn(libro.getIsbn());
        
        if (existente != null) {
            existente.setTitulo(libro.getTitulo());
            existente.setAutor(libro.getAutor());
            existente.setAnioPublicacion(libro.getAnioPublicacion());
            existente.setCategoria(libro.getCategoria());
            existente.setEditorial(libro.getEditorial());
            existente.setNumeroPaginas(libro.getNumeroPaginas());
            existente.setPrecio(libro.getPrecio());
            existente.setPorcentajeIva(libro.getPorcentajeIva());
            existente.setCantidadInventario(libro.getCantidadInventario());
            existente.setFormato(libro.getFormato());

            ArchivoLoggerManager.registrarOperacion("Actualizado libro: " + libro.getIsbn());
            return true;
        }
        return false;
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        Libro libro = buscarPorIsbn(isbn);
        
        if (libro != null) {
            listaLibros.remove(libro);
            ArchivoLoggerManager.registrarOperacion("Eliminado libro con ISBN: " + isbn);
            return true;
        }
        return false;
    }

    @Override
    public Libro buscarPorIsbn(String isbn) {
        for (Libro l : listaLibros) {
            if (l.getIsbn().equalsIgnoreCase(isbn)) {
                return l;
            }
        }
        return null;
    }

    @Override
    public List<Libro> obtenerTodos() {
        return new ArrayList<>(listaLibros);
    }
}
