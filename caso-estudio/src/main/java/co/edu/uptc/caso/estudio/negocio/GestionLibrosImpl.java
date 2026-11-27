package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Libro;
import co.edu.uptc.caso.estudio.persistence.IRepositorioLibros;
import co.edu.uptc.caso.estudio.persistence.RegistradorOperacionesTxt;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

// Implementación de reglas de negocio para libros con respaldo MySQL -> JSON y log TXT
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

    // Consulta MySQL primero y recurre al respaldo JSON si no hay registros
    @Override
    public List<Libro> listarLibros() {
        List<Libro> libros = repoMySQL != null ? repoMySQL.obtenerTodos() : null;
        if (libros == null || libros.isEmpty()) {
            List<Libro> respaldoJson = repoJson != null ? repoJson.obtenerTodos() : null;
            if (respaldoJson != null && !respaldoJson.isEmpty()) {
                return respaldoJson;
            }
            return new ArrayList<>();
        }
        return libros;
    }

    // Busca un libro por ISBN priorizando MySQL y luego JSON
    @Override
    public Libro buscarLibroPorIsbn(String isbn) {
        if (esVacio(isbn)) {
            return null;
        }
        Libro libro = repoMySQL != null ? repoMySQL.buscarPorIsbn(isbn) : null;
        if (libro != null) {
            return libro;
        }
        return repoJson != null ? repoJson.buscarPorIsbn(isbn) : null;
    }

    // Valida y guarda un libro nuevo en los repositorios disponibles
    @Override
    public String registrarLibro(Libro l) {
        String error = validarLibro(l, false);
        if (error != null) {
            return error;
        }

        boolean okMySQL = repoMySQL != null && repoMySQL.guardarLibro(l);
        boolean okJson = repoJson != null && repoJson.guardarLibro(l);

        if (!okMySQL && !okJson) {
            return "No se pudo guardar el libro en ningún repositorio.";
        }

        registrarEvento("LIBRO REGISTRADO - ISBN: " + l.getIsbn() + " | Título: " + l.getTitulo());
        return null;
    }

    // Valida y actualiza un libro existente en los repositorios disponibles
    @Override
    public String actualizarLibro(Libro l) {
        String error = validarLibro(l, true);
        if (error != null) {
            return error;
        }

        boolean okMySQL = repoMySQL != null && repoMySQL.actualizarLibro(l);
        boolean okJson = repoJson != null && repoJson.actualizarLibro(l);

        if (!okMySQL && !okJson) {
            return "No se encontró el libro para actualizar.";
        }

        registrarEvento("LIBRO ACTUALIZADO - ISBN: " + l.getIsbn() + " | Título: " + l.getTitulo());
        return null;
    }

    // Elimina un libro por ISBN y registra el evento si al menos un repositorio responde true
    @Override
    public boolean eliminarLibro(String isbn) {
        if (esVacio(isbn)) {
            return false;
        }

        boolean okMySQL = repoMySQL != null && repoMySQL.eliminarLibro(isbn);
        boolean okJson = repoJson != null && repoJson.eliminarLibro(isbn);

        if (okMySQL || okJson) {
            registrarEvento("LIBRO ELIMINADO - ISBN: " + isbn);
            return true;
        }
        return false;
    }

    // Ajusta el stock en memoria y persiste el cambio en los repositorios
    @Override
    public void ajustarStock(String isbn, int delta) {
        Libro l = buscarLibroPorIsbn(isbn);
        if (l != null) {
            int nuevoStock = Math.max(l.getStock() + delta, 0);
            l.setStock(nuevoStock);
            if (repoMySQL != null) {
                repoMySQL.actualizarLibro(l);
            }
            if (repoJson != null) {
                repoJson.actualizarLibro(l);
            }
        }
    }

    // Sincroniza el stock desde MySQL hacia el respaldo JSON
    @Override
    public void sincronizarStockRespaldo(String isbn) {
        if (esVacio(isbn) || repoMySQL == null || repoJson == null) {
            return;
        }
        Libro l = repoMySQL.buscarPorIsbn(isbn);
        if (l != null) {
            repoJson.actualizarLibro(l);
        }
    }

    // Centraliza las reglas de validación de campos y unicidad del ISBN
    private String validarLibro(Libro l, boolean esEdicion) {
        if (l == null) {
            return "El libro no puede ser nulo.";
        }
        if (esVacio(l.getIsbn())) {
            return "El ISBN es obligatorio.";
        }
        if (esVacio(l.getTitulo())) {
            return "El título no puede estar vacío.";
        }
        if (esVacio(l.getAutor())) {
            return "El autor no puede estar vacío.";
        }
        if (esVacio(l.getCategoria())) {
            return "Debe seleccionar una categoría.";
        }
        if (l.getFormato() == null) {
            return "Debe seleccionar un formato.";
        }

        int anioActual = Year.now().getValue();
        if (l.getAnio() < 1450 || l.getAnio() > anioActual) {
            return "Año inválido (entre 1450 y " + anioActual + ").";
        }
        if (l.getPaginas() <= 0) {
            return "El número de páginas debe ser mayor a 0.";
        }
        if (l.getPrecioVenta() <= 0) {
            return "El precio de venta debe ser mayor a $0.";
        }
        if (l.getStock() < 0) {
            return "El stock no puede ser negativo.";
        }
        if (l.getPorcentajeIva() < 0 || l.getPorcentajeIva() > 100) {
            return "El IVA debe estar entre 0% y 100%.";
        }

        if (!esEdicion && buscarLibroPorIsbn(l.getIsbn()) != null) {
            return "Ya existe un libro con el ISBN: " + l.getIsbn();
        }
        return null;
    }

    // Escribe el evento en el log TXT si el registrador fue inyectado
    private void registrarEvento(String mensaje) {
        if (registradorTxt != null) {
            registradorTxt.registrar(mensaje);
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}