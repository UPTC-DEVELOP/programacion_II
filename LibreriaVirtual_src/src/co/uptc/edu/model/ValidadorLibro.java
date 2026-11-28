package co.uptc.edu.model;

import java.time.Year;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

// Reglas de la especificación de datos del libro (DAD)
// Lanza IllegalArgumentException con un mensaje listo para mostrar en pantalla
public final class ValidadorLibro {

    public static final List<String> CATEGORIAS = Collections.unmodifiableList(Arrays.asList(
            "Novela", "Misterio", "Tecnología", "Superación", "Fábula", "Clásico",
            "Ciencia", "Historia", "Biografía", "Infantil", "Fantasía"));
    public static final List<String> FORMATOS = Collections.unmodifiableList(Arrays.asList("Físico", "Digital"));

    private static final Pattern PATRON_ISBN = Pattern.compile("^97[89]\\d{10}$");
    private static final Pattern PATRON_ANIO = Pattern.compile("^\\d{4}$");

    private ValidadorLibro() {}

    public static void validar(Libro libro) {
        if (libro == null) {
            throw new IllegalArgumentException("El libro no puede ser nulo.");
        }
        if (libro.getIsbn() == null || !PATRON_ISBN.matcher(libro.getIsbn()).matches()) {
            throw new IllegalArgumentException("El ISBN debe tener 13 dígitos y empezar por 978 o 979.");
        }
        texto(libro.getTituloLibro(), 150, "El título");
        texto(libro.getAutorLibro(), 100, "El autor");
        String anio = libro.getAnioPublicacion();
        if (anio == null || !PATRON_ANIO.matcher(anio).matches() || Integer.parseInt(anio) > Year.now().getValue()) {
            throw new IllegalArgumentException("El año debe tener 4 dígitos y no puede ser futuro.");
        }
        if (!CATEGORIAS.contains(libro.getCategoria())) {
            throw new IllegalArgumentException("La categoría debe ser una de las registradas.");
        }
        texto(libro.getEditorial(), 100, "La editorial");
        if (libro.getNumPaginas() < 1 || libro.getNumPaginas() > 99999) {
            throw new IllegalArgumentException("El número de páginas debe estar entre 1 y 99999.");
        }
        if (libro.getPrecioVenta() < 0 || libro.getPrecioVenta() > 99999999.99) {
            throw new IllegalArgumentException("El precio debe ser un valor entre 0 y 99.999.999,99.");
        }
        if (libro.getStock() < 0 || libro.getStock() > 999999) {
            throw new IllegalArgumentException("El stock debe estar entre 0 y 999999.");
        }
        if (!FORMATOS.contains(libro.getTipoFormato())) {
            throw new IllegalArgumentException("El formato debe ser \"Físico\" o \"Digital\".");
        }
    }

    private static void texto(String valor, int maximo, String campo) {
        if (valor == null || valor.trim().isEmpty() || valor.length() > maximo) {
            throw new IllegalArgumentException(campo + " es obligatorio y no puede superar " + maximo + " caracteres.");
        }
    }
}
