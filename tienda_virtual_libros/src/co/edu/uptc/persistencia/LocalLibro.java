package co.edu.uptc.persistencia;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import co.edu.uptc.interfaces.ILibroRepositorio;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.dto.FiltroLibroDto;
import co.edu.uptc.modelo.enums.Categoria;
import co.edu.uptc.modelo.enums.Formato;

/**
 * IMPLEMENTACIÓN EN MEMORIA del repositorio de libros.
 * Implementa ILibroRepositorio: la capa de negocio solo ve la interfaz.
 * Carga datos de ejemplo (semillas) para que el catálogo no aparezca vacío.
 */
public class LocalLibro implements ILibroRepositorio {

    private final List<Libro> baseDeDatos = new ArrayList<>();

    public LocalLibro() {
        cargarDatosDeEjemplo();
    }

    // -------------------------- ILibroRepositorio --------------------------

    @Override
    public void guardar(Libro libro) {
        if (libro != null) {
            baseDeDatos.add(libro);
        }
    }

    @Override
    public void actualizar(Libro libro) {
        if (libro == null) return;
        for (int i = 0; i < baseDeDatos.size(); i++) {
            if (baseDeDatos.get(i).getIsbn().equals(libro.getIsbn())) {
                baseDeDatos.set(i, libro);   // reemplaza en la misma posición
                return;
            }
        }
        baseDeDatos.add(libro);
    }

    @Override
    public void eliminar(String isbn) {
        if (isbn == null) return;
        baseDeDatos.removeIf(l -> isbn.equals(l.getIsbn()));
    }

    @Override
    public boolean existePorIsbn(String isbn) {
        return consultarPorIsbn(isbn) != null;
    }

    @Override
    public Libro consultarPorIsbn(String isbn) {
        if (isbn == null) return null;
        return baseDeDatos.stream()
                .filter(l -> isbn.equals(l.getIsbn()))
                .findFirst().orElse(null);
    }

    @Override
    public List<Libro> listarTodos() {
        return new ArrayList<>(baseDeDatos); // Retorna copia para evitar modificación externa
    }

    @Override
    public List<Libro> listarConFiltro(FiltroLibroDto filtro) {
        if (filtro == null) return listarTodos();
        return baseDeDatos.stream().filter(libro -> {
            boolean coincideTitulo = filtro.getTitulo() == null || filtro.getTitulo().trim().isEmpty()
                    || libro.getTitulo().toLowerCase().contains(filtro.getTitulo().toLowerCase());
            boolean coincideAutor = filtro.getAutor() == null || filtro.getAutor().trim().isEmpty()
                    || libro.getAutores().stream().anyMatch(a -> a.toLowerCase().contains(filtro.getAutor().toLowerCase()));
            boolean coincideCategoria = filtro.getCategoria() == null || libro.getCategoria() == filtro.getCategoria();
            return coincideTitulo && coincideAutor && coincideCategoria;
        }).collect(Collectors.toList());
    }

    // -------------------------- Datos de ejemplo --------------------------

    private void cargarDatosDeEjemplo() {
        guardar(new Libro("9788437604947", "Cien anos de soledad",
                List.of("Gabriel Garcia Marquez"), 1967, "Diana", 480, 59000.0, 12,
                Formato.FISICO, Categoria.OTROS));

        guardar(new Libro("9780134685991", "Effective Java",
                List.of("Joshua Bloch"), 2018, "Addison Wesley", 416, 180000.0, 8,
                Formato.FISICO, Categoria.TECNOLOGIA));

        guardar(new Libro("9780451524935", "1984",
                List.of("George Orwell"), 1949, "Signet Classics", 328, 45000.0, 20,
                Formato.DIGITAL, Categoria.CIENCIA_FICCION));

        guardar(new Libro("9788420430263", "Breve historia del mundo",
                List.of("Ernest Gombrich"), 1936, "Ariel", 352, 62000.0, 5,
                Formato.FISICO, Categoria.HISTORIA));

        guardar(new Libro("9780141439518", "Orgullo y prejuicio",
                List.of("Jane Austen"), 1813, "Penguin Classics", 432, 38000.0, 15,
                Formato.DIGITAL, Categoria.ROMANCE));
    }
}
