 	package co.uptc.edu.modelo;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import co.uptc.edu.negocio.Libro;


public class ValidarLibro {

    public static final List<String> CATEGORIAS_POR_DEFECTO = Collections.unmodifiableList(Arrays.asList(
            "Novela", "Misterio", "Tecnología", "Superación", "Fábula", "Clásico",
            "Ciencia", "Historia", "Biografía", "Infantil", "Fantasía"));

    public static final List<String> FORMATOS = Collections.unmodifiableList(Arrays.asList("Físico", "Digital"));

    static final int MAX_TITULO = 150;
    static final int MAX_AUTOR = 100;
    static final int MAX_CATEGORIA = 50;
    static final int MAX_EDITORIAL = 100;
    static final int MAX_PAGINAS = 99_999;
    static final int MAX_STOCK = 999_999;
    static final double MAX_PRECIO = 99_999_999.99;
    static final int ANIO_MINIMO = 1000;

    /** 13 dígitos que empiezan por 978 o 979 (prefijo ISBN-13). */
    private static final Pattern PATRON_ISBN = Pattern.compile("^97[89]\\d{10}$");
    /** Letras (con tildes) y espacios; se aceptan también punto, apóstrofo y guion (ej. "Robert C. Martin"). */
    private static final Pattern PATRON_AUTOR = Pattern.compile("^\\p{L}[\\p{L} .'\\-]*$");
    private static final Pattern PATRON_ANIO = Pattern.compile("^\\d{4}$");

    private final List<String> categorias;

    public ValidarLibro() {
        this(CATEGORIAS_POR_DEFECTO);
    }

    /** Permite registrar otro conjunto de categorías sin modificar esta clase (OCP). */
    public ValidarLibro(Collection<String> categorias) {
        this.categorias = Collections.unmodifiableList(new ArrayList<>(categorias));
    }

    public List<String> getCategorias() { return categorias; }

    public List<String> getFormatos() { return FORMATOS; }

    public void validar(Libro libro) {
        if (libro == null) {
            throw new excepcionLibro("El libro no puede ser nulo.");
        }
        isbn(libro.getIsbn());
        titulo(libro.getTituloLibro());
        autor(libro.getAutorLibro());
        anio(libro.getAnioPublicacion());
        categoria(libro.getCategoria());
        editorial(libro.getEditorial());
        paginas(libro.getNumPaginas());
        precio(libro.getPrecioVenta());
        stock(libro.getStock());
        formato(libro.getTipoFormato());
    }

    private void isbn(String v) {
        if (vacio(v)) {
            throw new excepcionLibro("El ISBN es obligatorio.");
        }
        if (!PATRON_ISBN.matcher(v).matches()) {
            throw new excepcionLibro("El ISBN debe tener 13 dígitos y empezar por 978 o 979.");
        }
    }

    private void titulo(String v) {
        if (vacio(v)) {
            throw new excepcionLibro("El título es obligatorio.");
        }
        if (v.length() > MAX_TITULO) {
            throw new excepcionLibro("El título no puede superar " + MAX_TITULO + " caracteres.");
        }
    }

    private void autor(String v) {
        if (vacio(v)) {
            throw new excepcionLibro("El autor es obligatorio.");
        }
        if (v.length() > MAX_AUTOR) {
            throw new excepcionLibro("El autor no puede superar " + MAX_AUTOR + " caracteres.");
        }
        if (!PATRON_AUTOR.matcher(v).matches()) {
            throw new excepcionLibro("El autor solo puede contener letras y espacios.");
        }
    }

    private void anio(String v) {
        if (vacio(v)) {
            throw new excepcionLibro("El año de publicación es obligatorio.");
        }
        int actual = Year.now().getValue();
        if (!PATRON_ANIO.matcher(v).matches()
                || Integer.parseInt(v) < ANIO_MINIMO || Integer.parseInt(v) > actual) {
            throw new excepcionLibro("El año debe tener 4 dígitos y estar entre " + ANIO_MINIMO + " y " + actual + ".");
        }
    }

    private void categoria(String v) {
        if (vacio(v)) {
            throw new excepcionLibro("La categoría es obligatoria.");
        }
        if (v.length() > MAX_CATEGORIA || !contiene(categorias, v)) {
            throw new excepcionLibro("La categoría debe ser una de las registradas: " + String.join(", ", categorias) + ".");
        }
    }

    private void editorial(String v) {
        if (vacio(v)) {
            throw new excepcionLibro("La editorial es obligatoria.");
        }
        if (v.length() > MAX_EDITORIAL) {
            throw new excepcionLibro("La editorial no puede superar " + MAX_EDITORIAL + " caracteres.");
        }
    }

    private void paginas(int v) {
        if (v <= 0 || v > MAX_PAGINAS) {
            throw new excepcionLibro("El número de páginas debe ser un entero entre 1 y " + MAX_PAGINAS + ".");
        }
    }

    private void precio(double v) {
        if (Double.isNaN(v) || Double.isInfinite(v) || v < 0) {
            throw new excepcionLibro("El precio de venta debe ser un valor mayor o igual a 0.");
        }
        if (v > MAX_PRECIO) {
            throw new excepcionLibro("El precio de venta no puede tener más de 10 dígitos.");
        }
        if (BigDecimal.valueOf(v).scale() > 2) {
            throw new excepcionLibro("El precio de venta admite máximo 2 decimales.");
        }
    }

    private void stock(int v) {
        if (v < 0 || v > MAX_STOCK) {
            throw new excepcionLibro("El stock debe ser un entero entre 0 y " + MAX_STOCK + ".");
        }
    }

    private void formato(String v) {
        if (vacio(v) || !contiene(FORMATOS, v)) {
            throw new excepcionLibro("El formato debe ser \"Físico\" o \"Digital\".");
        }
    }

    private static boolean vacio(String v) {
        return v == null || v.trim().isEmpty();
    }

    private static boolean contiene(List<String> opciones, String valor) {
        for (String o : opciones) {
            if (o.equals(valor)) {
                return true;
            }
        }
        return false;
    }
}
