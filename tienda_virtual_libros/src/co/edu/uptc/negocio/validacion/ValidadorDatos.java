package co.edu.uptc.negocio.validacion;

import co.edu.uptc.negocio.modelo.Categoria;
import co.edu.uptc.negocio.modelo.Formato;
import co.edu.uptc.negocio.validacion.ValidadorDatos;

import java.time.Year;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Clase responsable de validar los datos de entrada según los RF01-RF04.
 * 
 * Principio aplicado: Single Responsibility.
 * Esta clase SOLO valida, no persiste ni muestra mensajes.
 * Retorna boolean o lanza excepciones para que el llamador decida qué hacer.
 * 
 * @author Oscar Clavijo
 */

public class ValidadorDatos {
	
	  // Patrón para validar ISBN-13: 13 dígitos, puede tener guiones
    private static final Pattern PATRON_ISBN = Pattern.compile("^(\\d{3}-?)?\\d{1,5}-?\\d{1,7}-?\\d{1,7}-?\\d$");
    
    // Patrón para validar que no haya caracteres especiales raros en títulos
    private static final Pattern PATRON_TITULO = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ0-9\\s.,;:!?()\\-]+$");

    /**
     * Valida que el ISBN tenga formato correcto (13 dígitos).
     * RF01: "El ISBN debe ser único en el sistema"
     */
    
    public static boolean validarISBN(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        // Remover guiones para contar solo dígitos
        String soloDigitos = isbn.replace("-", "");
        return soloDigitos.length() == 13 && soloDigitos.matches("\\d+");
    }

    /**
     * Valida que el título no esté vacío y tenga longitud máxima de 100 caracteres.
     * RF01: "El título no puede estar vacío"
     */
    public static boolean validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }
        return titulo.trim().length() <= 100;
    }

    /**
     * Valida la lista de autores.
     * RF01: Máximo 10 autores, cada uno máx 80 caracteres, no vacíos.
     */
    public static boolean validarAutores(List<String> autores) {
        if (autores == null || autores.isEmpty()) {
            return false;
        }
        if (autores.size() > 10) {
            return false;
        }
        for (String autor : autores) {
            if (autor == null || autor.trim().isEmpty() || autor.trim().length() > 80) {
                return false;
            }
        }
        return true;
    }

    /**
     * Valida el año de publicación.
     * RF01: "Mayor a 1400 y menor o igual al año actual"
     */
    public static boolean validarAnioPublicacion(int anio) {
        int anioActual = Year.now().getValue();
        return anio > 1400 && anio <= anioActual;
    }

    /**
     * Valida que la categoría sea una de las opciones válidas del enum.
     * (El tipo enum ya garantiza esto en tiempo de compilación,
     *  pero este método existe para validaciones desde String en GUI)
     */
    public static boolean validarCategoria(Categoria categoria) {
        return categoria != null;
    }

    /**
     * Valida que la editorial no esté vacía y tenga máx 80 caracteres.
     */
    public static boolean validarEditorial(String editorial) {
        if (editorial == null || editorial.trim().isEmpty()) {
            return false;
        }
        return editorial.trim().length() <= 80;
    }

    /**
     * Valida el número de páginas.
     * RF01: "Mayor a 1"
     */
    public static boolean validarNumPaginas(int numPaginas) {
        return numPaginas > 1;
    }

    /**
     * Valida el precio de venta.
     * RF01: "Mayor a 0, máximo 999999.99"
     */
    public static boolean validarPrecioVenta(double precio) {
        return precio > 0 && precio <= 999999.99;
    }

    /**
     * Valida el stock inicial.
     * RF01: "Mayor o igual a 0"
     */
    public static boolean validarStock(int stock) {
        return stock >= 0;
    }

    /**
     * Valida que el formato sea válido.
     */
    public static boolean validarFormato(Formato formato) {
        return formato != null;
    }

    /**
     * Validación completa de todos los campos de un libro nuevo (RF01).
     * Retorna un mensaje descriptivo si hay error, o null si todo está bien.
     * 
     * Este método es el que usará el PanelAdministradorRegistro antes de
     * intentar registrar el libro.
     */
    public static String validarLibroCompleto(String isbn, String titulo,
                                               List<String> autores, int anioPublicacion,
                                               Categoria categoria, String editorial,
                                               int numPaginas, double precioVenta,
                                               int stock, Formato formato) {
        if (!validarISBN(isbn)) {
            return "El ISBN debe tener exactamente 13 dígitos numéricos.";
        }
        if (!validarTitulo(titulo)) {
            return "El título no puede estar vacío y debe tener máximo 100 caracteres.";
        }
        if (!validarAutores(autores)) {
            return "Debe haber entre 1 y 10 autores, cada uno con máximo 80 caracteres.";
        }
        if (!validarAnioPublicacion(anioPublicacion)) {
            return "El año de publicación debe estar entre 1401 y " + Year.now().getValue() + ".";
        }
        if (!validarCategoria(categoria)) {
            return "La categoría no es válida.";
        }
        if (!validarEditorial(editorial)) {
            return "La editorial no puede estar vacía y debe tener máximo 80 caracteres.";
        }
        if (!validarNumPaginas(numPaginas)) {
            return "El número de páginas debe ser mayor a 1.";
        }
        if (!validarPrecioVenta(precioVenta)) {
            return "El precio debe ser mayor a 0 y menor o igual a 999,999.99.";
        }
        if (!validarStock(stock)) {
            return "El stock no puede ser negativo.";
        }
        if (!validarFormato(formato)) {
            return "El formato no es válido.";
        }
        return null; // Todo válido
    }
}
