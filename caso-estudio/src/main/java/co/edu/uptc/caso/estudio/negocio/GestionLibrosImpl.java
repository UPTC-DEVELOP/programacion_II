package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.persistence.IRepositorioLibros;
import co.edu.uptc.caso.estudio.persistence.RegistradorOperacionesTxt;
import java.time.Year;
import java.util.List;

public class GestionLibrosImpl implements IGestionLibros {

    private final IRepositorioLibros repoMySQL;
    private final IRepositorioLibros repoJson;
    private final RegistradorOperacionesTxt registradorTxt;

    public GestionLibrosImpl(IRepositorioLibros repoMySQL, IRepositorioLibros repoJson) {
        this(repoMySQL, repoJson, null);
    }

    public GestionLibrosImpl(IRepositorioLibros repoMySQL, IRepositorioLibros repoJson,
                             RegistradorOperacionesTxt registradorTxt) {
        this.repoMySQL = repoMySQL;
        this.repoJson = repoJson;
        this.registradorTxt = registradorTxt;
    }

    @Override
    public List<Libro> listarLibros() {
        List<Libro> libros = repoMySQL.obtenerTodos();
        if (libros == null || libros.isEmpty()) {
            List<Libro> respaldoJson = repoJson.obtenerTodos();
            if (respaldoJson != null && !respaldoJson.isEmpty()) {
                return respaldoJson;
            }
        }
        return libros;
    }

    @Override
    public Libro buscarLibroPorIsbn(String isbn) {
        if (isbn == null || isbn.isBlank()) return null;
        Libro libro = repoMySQL.buscarPorIsbn(isbn);
        return (libro != null) ? libro : repoJson.buscarPorIsbn(isbn);
    }

    @Override
    public String registrarLibro(Libro l) {
        String error = validarLibro(l, false);
        if (error != null) return error;

        boolean okMySQL = repoMySQL.guardarLibro(l);
        boolean okJson = repoJson.guardarLibro(l);

        if (!okMySQL && !okJson) {
            return "No se pudo guardar el libro en ningún repositorio.";
        }

        registrarEvento("LIBRO REGISTRADO - ISBN: " + l.getIsbn() + " | Título: " + l.getTitulo());
        return null;
    }

    @Override
    public String actualizarLibro(Libro l) {
        String error = validarLibro(l, true);
        if (error != null) return error;

        boolean okMySQL = repoMySQL.actualizarLibro(l);
        boolean okJson = repoJson.actualizarLibro(l);

        if (!okMySQL && !okJson) {
            return "No se encontró el libro para actualizar.";
        }

        registrarEvento("LIBRO ACTUALIZADO - ISBN: " + l.getIsbn() + " | Título: " + l.getTitulo());
        return null;
    }

    @Override
    public boolean eliminarLibro(String isbn) {
        if (isbn == null || isbn.isBlank()) return false;

        boolean okMySQL = repoMySQL.eliminarLibro(isbn);
        boolean okJson = repoJson.eliminarLibro(isbn);

        if (okMySQL || okJson) {
            registrarEvento("LIBRO ELIMINADO - ISBN: " + isbn);
            return true;
        }
        return false;
    }

    @Override
    public void ajustarStock(String isbn, int delta) {
        Libro l = buscarLibroPorIsbn(isbn);
        if (l != null) {
            l.setStock(Math.max(l.getStock() + delta, 0));
            repoMySQL.actualizarLibro(l);
            repoJson.actualizarLibro(l);
        }
    }
    
    @Override
    public void sincronizarStockRespaldo(String isbn) {
        Libro l = repoMySQL.buscarPorIsbn(isbn);
        if (l != null) {
            repoJson.actualizarLibro(l);
        }
    }

    private String validarLibro(Libro l, boolean esEdicion) {
        if (l == null) return "El libro no puede ser nulo.";
        if (l.getIsbn() == null || l.getIsbn().isBlank()) return "El ISBN es obligatorio.";
        if (l.getTitulo() == null || l.getTitulo().isBlank()) return "El título no puede estar vacío.";
        if (l.getAutor() == null || l.getAutor().isBlank()) return "El autor no puede estar vacío.";
        if (l.getCategoria() == null || l.getCategoria().isBlank()) return "Debe seleccionar una categoría.";
        if (l.getFormato() == null) return "Debe seleccionar un formato.";

        int anioActual = Year.now().getValue();
        if (l.getAnio() < 1450 || l.getAnio() > anioActual) {
            return "Año inválido (entre 1450 y " + anioActual + ").";
        }
        if (l.getPaginas() <= 0) return "El número de páginas debe ser mayor a 0.";
        if (l.getPrecioVenta() <= 0) return "El precio de venta debe ser mayor a $0.";
        if (l.getStock() < 0) return "El stock no puede ser negativo.";
        if (l.getPorcentajeIva() < 0 || l.getPorcentajeIva() > 100) {
            return "El IVA debe estar entre 0% y 100%.";
        }

        if (!esEdicion && buscarLibroPorIsbn(l.getIsbn()) != null) {
            return "Ya existe un libro con el ISBN: " + l.getIsbn();
        }
        return null;
    }

    private void registrarEvento(String mensaje) {
        if (registradorTxt != null) {
            registradorTxt.registrar(mensaje);
        }
    }
}