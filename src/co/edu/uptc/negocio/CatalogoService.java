package co.edu.uptc.negocio;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CatalogoService {

    private static final Pattern PATRON_ISBN = Pattern.compile("^[A-Za-z0-9]{13}$");
    private static final double PRECIO_MAXIMO = 9_999_999_999.0;   // hasta 10 dígitos
    private static final int STOCK_MAXIMO = 999_999;               // hasta 6 dígitos
    private static final int PAGINAS_MAXIMO = 99_999;              // hasta 5 dígitos
    private static final int ANIO_MINIMO = 1000;                   // 4 dígitos

    private final LibroRepositorio libroRepositorio;
    private final VentaRepositorio ventaRepositorio;
    private final BitacoraRepositorio bitacora;

    public CatalogoService(LibroRepositorio libroRepositorio,
                           VentaRepositorio ventaRepositorio,
                           BitacoraRepositorio bitacora) {
        this.libroRepositorio = libroRepositorio;
        this.ventaRepositorio = ventaRepositorio;
        this.bitacora = bitacora;
    }

    public void registrarLibro(Libro libro) throws ValidacionException {
        validarLibro(libro);
       
        if (libroRepositorio.existe(libro.getIsbn())) {
            throw new ValidacionException(
                    "Ya existe un libro registrado con el ISBN " + libro.getIsbn() + ".");
        }
        libroRepositorio.guardar(libro);
        bitacora.registrar("REGISTRAR_LIBRO", descripcion(libro));
    }

    public void actualizarLibro(Libro libro) throws ValidacionException {
        validarLibro(libro);
        
        if (!libroRepositorio.existe(libro.getIsbn())) {
            throw new ValidacionException(
                    "No existe ningún libro registrado con el ISBN " + libro.getIsbn() + ".");
        }
        libroRepositorio.guardar(libro);
        bitacora.registrar("ACTUALIZAR_LIBRO", descripcion(libro));
    }

    public void eliminarLibro(String isbn) throws ValidacionException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new ValidacionException("Debe indicar el ISBN del libro a eliminar.");
        }
        if (!libroRepositorio.existe(isbn)) {
            throw new ValidacionException("El libro con ISBN " + isbn + " no existe en el catálogo.");
        }
       
        if (ventaRepositorio.existenVentasDelLibro(isbn)) {
            throw new ValidacionException(
                    "No se puede eliminar el libro " + isbn + " porque tiene ventas asociadas.");
        }
        libroRepositorio.eliminar(isbn);
        bitacora.registrar("ELIMINAR_LIBRO", "ISBN=" + isbn);
    }

    public List<Libro> listarLibros() {
        return libroRepositorio.obtenerTodos();
    }

    public List<Libro> buscarLibros(String criterio) throws ValidacionException {
        
        if (criterio == null || criterio.trim().isEmpty()) {
            return listarLibros();
        }
        if (criterio.length() > 200) {
            throw new ValidacionException("El criterio de búsqueda no puede superar 200 caracteres.");
        }
        final String patron = criterio.trim().toLowerCase();
        return libroRepositorio.obtenerTodos().stream()
                .filter(l -> l.getTitulo().toLowerCase().contains(patron)
                        || l.getAutor().toLowerCase().contains(patron)
                        || l.getCategoria().toLowerCase().contains(patron))
                .collect(Collectors.toList());
    }

    public Optional<Libro> buscarPorIsbn(String isbn) {
        return libroRepositorio.buscarPorIsbn(isbn);
    }

    // ===================== Validaciones de negocio =====================

    private void validarLibro(Libro libro) throws ValidacionException {
        if (libro == null) {
            throw new ValidacionException("No se recibió la información del libro.");
        }
        
        validarTexto(libro.getIsbn(), "ISBN", 13);
        if (!PATRON_ISBN.matcher(libro.getIsbn()).matches()) {
            throw new ValidacionException("El ISBN debe tener exactamente 13 caracteres alfanuméricos.");
        }
        
        validarTexto(libro.getTitulo(), "Título", 200);
        validarTexto(libro.getAutor(), "Autor(es)", 150);
        validarTexto(libro.getCategoria(), "Categoría", 50);
        validarTexto(libro.getEditorial(), "Editorial", 100);

        int anioActual = Year.now().getValue();
        if (libro.getAnioPublicacion() < ANIO_MINIMO || libro.getAnioPublicacion() > anioActual) {
            throw new ValidacionException(
                    "El año de publicación debe ser de 4 dígitos y no puede ser futuro (máximo "
                            + anioActual + ").");
        }
       
        if (libro.getPrecioBase() <= 0 || libro.getPrecioBase() > PRECIO_MAXIMO) {
            throw new ValidacionException("El precio base debe ser mayor a 0 y de máximo 10 dígitos.");
        }
        
        if (libro.getPorcentajeDescuento() < 0 || libro.getPorcentajeDescuento() > 100) {
            throw new ValidacionException("El porcentaje de descuento debe estar entre 0 y 100.");
        }
       
        if (libro.getCantidadDisponible() < 0 || libro.getCantidadDisponible() > STOCK_MAXIMO) {
            throw new ValidacionException("La cantidad disponible debe estar entre 0 y " + STOCK_MAXIMO + ".");
        }
        
        if (libro.getNumeroPaginas() < 0 || libro.getNumeroPaginas() > PAGINAS_MAXIMO) {
            throw new ValidacionException("El número de páginas debe ser mayor a 0 y de máximo 5 dígitos.");
        }
    }

    private void validarTexto(String valor, String campo, int maximo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException("El campo \"" + campo + "\" es obligatorio.");
        }
        if (valor.length() > maximo) {
            throw new ValidacionException(
                    "El campo \"" + campo + "\" no puede superar " + maximo + " caracteres.");
        }
    }

    private String descripcion(Libro l) {
        return "ISBN=" + l.getIsbn() + ", titulo=" + l.getTitulo()
                + ", formato=" + l.getFormato().getEtiqueta()
                + ", precioBase=" + l.getPrecioBase()
                + ", descuento=" + l.getPorcentajeDescuento() + "%"
                + ", stock=" + l.getCantidadDisponible();
    }
}
