package co.edu.uptc.negocio;

import java.util.Calendar;
import java.util.List;
import co.edu.uptc.model.Libro;
import co.edu.uptc.persistence.IRepositorioLibros;

public class GestionLibrosImpl implements IGestionLibros {

    private final IRepositorioLibros repoMySQL;
    private final IRepositorioLibros repoJson;

    public GestionLibrosImpl(IRepositorioLibros repoMySQL, IRepositorioLibros repoJson) {
        this.repoMySQL = repoMySQL;
        this.repoJson = repoJson;
    }

    @Override
    public List<Libro> listarLibros() {
        return repoMySQL.obtenerTodos();
    }

    @Override
    public Libro buscarLibroPorIsbn(String isbn) {
        return repoMySQL.buscarPorIsbn(isbn);
    }

    @Override
    public String registrarLibro(Libro l) {
        String error = validarLibro(l, false);
        if (error != null) return error;
        boolean ok = repoMySQL.guardarLibro(l);
        if (ok) repoJson.guardarLibro(l);
        return ok ? null : "No se pudo guardar el libro en el repositorio.";
    }

    @Override
    public String actualizarLibro(Libro l) {
        String error = validarLibro(l, true);
        if (error != null) return error;
        boolean ok = repoMySQL.actualizarLibro(l);
        if (ok) repoJson.actualizarLibro(l);
        return ok ? null : "No se encontró el libro para actualizar.";
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        boolean ok = repoMySQL.eliminarLibro(isbn);
        if (ok) repoJson.eliminarLibro(isbn);
        return ok;
    }

    @Override
    public void ajustarStock(String isbn, int delta) {
        Libro l = repoMySQL.buscarPorIsbn(isbn);
        if (l != null) {
            l.setStock(Math.max(l.getStock() + delta, 0));
            repoMySQL.actualizarLibro(l);
            repoJson.actualizarLibro(l);
        }
    }

    private String validarLibro(Libro l, boolean esEdicion) {
        if (l == null) return "El libro no puede ser nulo.";
        if (l.getIsbn() == null || l.getIsbn().isBlank()) return "El ISBN es obligatorio.";
        if (l.getTitulo() == null || l.getTitulo().isBlank()) return "El título no puede estar vacío.";
        if (l.getAutor() == null || l.getAutor().isBlank()) return "El autor no puede estar vacío.";
        if (l.getCategoria() == null || l.getCategoria().isBlank()) return "Debe seleccionar una categoría.";

        int anioActual = Calendar.getInstance().get(Calendar.YEAR);
        if (l.getAnio() < 1450 || l.getAnio() > anioActual) return "Año inválido (entre 1450 y " + anioActual + ").";
        if (l.getPaginas() <= 0) return "El número de páginas debe ser mayor a 0.";
        if (l.getPrecioVenta() <= 0) return "El precio de venta debe ser mayor a $0.";
        if (l.getStock() < 0) return "El stock no puede ser negativo.";
        if (l.getPorcentajeIva() < 0 || l.getPorcentajeIva() > 100) return "El IVA debe estar entre 0% y 100%.";

        if (!esEdicion && repoMySQL.buscarPorIsbn(l.getIsbn()) != null) {
            return "Ya existe un libro con el ISBN: " + l.getIsbn();
        }
        return null;
    }
}
