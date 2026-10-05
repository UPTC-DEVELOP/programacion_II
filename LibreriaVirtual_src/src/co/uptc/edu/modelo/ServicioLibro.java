package co.uptc.edu.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.uptc.edu.negocio.Libro;



public class ServicioLibro {

    private final RepositorioLibro repositorio;
    private final ValidarLibro validador;

    public ServicioLibro(RepositorioLibro repositorio, ValidarLibro validador) {
        this.repositorio = repositorio;
        this.validador = validador;
    }

    // ---------- Create ----------
    public Libro registrar(Libro libro) {
        Libro limpio = normalizar(libro);
        validador.validar(limpio);
        if (repositorio.existe(limpio.getIsbn())) {
            throw new excepcionLibro("Ya existe un libro registrado con el ISBN " + limpio.getIsbn() + ".");
        }
        repositorio.guardar(limpio);
        return limpio;
    }

    
    public List<Libro> listar() {
        return Collections.unmodifiableList(repositorio.listar());
    }

    public Libro obtener(String isbn) {
        return repositorio.buscarPorIsbn(isbn == null ? "" : isbn.trim())
                .orElseThrow(() -> new excepcionLibro("El libro con ISBN " + isbn + " no existe."));
    }

    
    public List<Libro> buscar(String texto, String categoria) {
        String t = texto == null ? "" : texto.trim().toLowerCase();
        boolean filtraCategoria = categoria != null && !categoria.trim().isEmpty();
        List<Libro> resultado = new ArrayList<>();
        for (Libro l : repositorio.listar()) {
            if (filtraCategoria && !l.getCategoria().equals(categoria)) {
                continue;
            }
            if (t.isEmpty() || coincide(l, t)) {
                resultado.add(l);
            }
        }
        return resultado;
    }

    // ---------- Update ----------
    public Libro actualizar(Libro libro) {
        Libro limpio = normalizar(libro);
        validador.validar(limpio);
        if (!repositorio.existe(limpio.getIsbn())) {
            throw new excepcionLibro("El libro con ISBN " + limpio.getIsbn() + " no existe.");
        }
        repositorio.guardar(limpio);
        return limpio;
    }

    // ---------- Delete  ----------
    public void eliminar(String isbn) {
        Libro l = obtener(isbn);
        repositorio.eliminar(l.getIsbn());
    }

    // --
    public List<String> getCategorias() { return validador.getCategorias(); }

    public List<String> getFormatos() { return validador.getFormatos(); }

    public void vaciar() { repositorio.eliminarTodos(); }

   
    private boolean coincide(Libro l, String t) {
        return l.getIsbn().toLowerCase().contains(t)
                || l.getTituloLibro().toLowerCase().contains(t)
                || l.getAutorLibro().toLowerCase().contains(t)
                || l.getEditorial().toLowerCase().contains(t);
    }

 
    private Libro normalizar(Libro l) {
        if (l == null) {
            throw new excepcionLibro("El libro no puede ser nulo.");
        }
        return new Libro(limpiar(l.getIsbn()), limpiar(l.getTituloLibro()), limpiar(l.getAutorLibro()),
                limpiar(l.getAnioPublicacion()), limpiar(l.getCategoria()), limpiar(l.getEditorial()),
                l.getNumPaginas(), l.getPrecioVenta(), l.getStock(), limpiar(l.getTipoFormato()));
    }

    private static String limpiar(String v) {
        return v == null ? "" : v.trim();
    }
}
