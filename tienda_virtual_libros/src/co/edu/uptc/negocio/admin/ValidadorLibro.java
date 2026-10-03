package co.edu.uptc.negocio.admin;


import java.time.Year;
import java.util.List;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.interfaces.IValidadorLibro;
import co.edu.uptc.modelo.dto.LibroDto;


/**
 * CLASE ValidadorLibro  (paquete: negocio)  implements IValidadorLibro
 * ---------------------------------------------------------------------------
 * Concentra las validaciones mínimas de aceptación de RF01/RF02 y la tabla de
 * especificación de datos (longitudes, rangos, obligatoriedad).
 *
 * Principios: SRP (solo valida), OCP (se amplía creando otro IValidadorLibro).
 *
 * NOTA sobre "validar peso y dimensiones si es FISICO" (RF01): las entradas del
 * RF no incluyen peso ni dimensiones y el prototipo tampoco tiene esos campos.
 * Queda como PUNTO A CONFIRMAR con el Product Owner (docente). Si lo exige, se
 * agregan campos al modelo y una regla más aquí, sin tocar el resto.
 */
public class ValidadorLibro implements IValidadorLibro {

    private static final int ISBN_LONGITUD = 13;
    private static final int TITULO_MAX = 100;
    private static final int AUTOR_MAX = 80;
    private static final int MAX_AUTORES = 10;
    private static final int EDITORIAL_MAX = 80;
    private static final int ANIO_MINIMO_EXCLUSIVO = 1400;
    private static final double PRECIO_MAX = 999_999.99;
    private static final int STOCK_MAX = 99_999;

    /** Letras (con tildes), números, espacios y signos básicos de puntuación. */
    private static final String PATRON_TEXTO = "[\\p{L}\\p{N} .,:;'\\-¿?¡!()&]+";

    @Override
    public void validarDatos(LibroDto libro) throws ReglaNegocioException {
        validarIsbn(libro.getIsbn());
        validarTitulo(libro.getTitulo());
        validarAutores(libro.getAutores());
        validarAnio(libro.getAnioPublicacion());
        if (libro.getCategoria() == null) {
            throw new ReglaNegocioException("Debe seleccionar una categoría válida.");
        }
        validarEditorial(libro.getEditorial());
        if (libro.getNumPaginas() <= 1) {
            throw new ReglaNegocioException("El número de páginas debe ser mayor a 1.");
        }
        validarPrecio(libro.getPrecioVenta());
        validarStock(libro.getStock());
        if (libro.getFormato() == null) {
            throw new ReglaNegocioException("Debe seleccionar el formato (Físico o Digital).");
        }
    }

    // ------------------ Reglas individuales (métodos pequeños) ------------------

    private void validarIsbn(String isbn) throws ReglaNegocioException {
        if (isbn == null || isbn.trim().length() != ISBN_LONGITUD) {
            throw new ReglaNegocioException("El ISBN debe tener exactamente 13 caracteres.");
        }
        if (!esIsbn13Valido(isbn.trim())) {
            throw new ReglaNegocioException(
                "El ISBN no es un ISBN-13 válido (solo dígitos y dígito de control correcto).");
        }
    }

    /**
     * Algoritmo oficial ISBN-13: se multiplican los 12 primeros dígitos por
     * 1 y 3 alternadamente, se suman, y el dígito 13 debe ser (10 - suma%10)%10.
     */
    private boolean esIsbn13Valido(String isbn) {
        int suma = 0;
        for (int i = 0; i < ISBN_LONGITUD; i++) {
            char c = isbn.charAt(i);
            if (!Character.isDigit(c)) {
                return false;
            }
            int digito = c - '0';
            if (i < 12) {
                suma += (i % 2 == 0) ? digito : digito * 3;
            } else {
                return digito == (10 - (suma % 10)) % 10;
            }
        }
        return false;
    }

    private void validarTitulo(String titulo) throws ReglaNegocioException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new ReglaNegocioException("El título no puede estar vacío.");
        }
        if (titulo.trim().length() > TITULO_MAX) {
            throw new ReglaNegocioException("El título admite máximo " + TITULO_MAX + " caracteres.");
        }
        if (!titulo.trim().matches(PATRON_TEXTO)) {
            throw new ReglaNegocioException("El título contiene caracteres especiales no permitidos.");
        }
    }

    private void validarAutores(List<String> autores) throws ReglaNegocioException {
        if (autores == null || autores.isEmpty()) {
            throw new ReglaNegocioException("Debe registrar al menos un autor.");
        }
        if (autores.size() > MAX_AUTORES) {
            throw new ReglaNegocioException("Se permiten máximo " + MAX_AUTORES + " autores.");
        }
        for (String autor : autores) {
            if (autor == null || autor.trim().isEmpty() || autor.trim().length() > AUTOR_MAX) {
                throw new ReglaNegocioException(
                    "Cada autor debe tener entre 1 y " + AUTOR_MAX + " caracteres.");
            }
        }
    }

    private void validarAnio(int anio) throws ReglaNegocioException {
        int actual = Year.now().getValue();
        if (anio <= ANIO_MINIMO_EXCLUSIVO || anio > actual) {
            throw new ReglaNegocioException(
                "El año de publicación debe ser mayor a 1400 y menor o igual a " + actual + ".");
        }
    }

    private void validarEditorial(String editorial) throws ReglaNegocioException {
        if (editorial == null || editorial.trim().isEmpty()) {
            throw new ReglaNegocioException("La editorial no puede estar vacía.");
        }
        if (editorial.trim().length() > EDITORIAL_MAX) {
            throw new ReglaNegocioException("La editorial admite máximo " + EDITORIAL_MAX + " caracteres.");
        }
    }

    private void validarPrecio(double precio) throws ReglaNegocioException {
        if (precio <= 0) {
            throw new ReglaNegocioException("El precio de venta debe ser mayor a 0.");
        }
        if (precio > PRECIO_MAX) {
            throw new ReglaNegocioException("El precio de venta no puede superar 999.999,99.");
        }
    }

    private void validarStock(int stock) throws ReglaNegocioException {
        if (stock < 0) {
            throw new ReglaNegocioException("La cantidad en inventario no puede ser negativa.");
        }
        if (stock > STOCK_MAX) {
            throw new ReglaNegocioException("La cantidad en inventario no puede superar " + STOCK_MAX + ".");
        }
    }
}