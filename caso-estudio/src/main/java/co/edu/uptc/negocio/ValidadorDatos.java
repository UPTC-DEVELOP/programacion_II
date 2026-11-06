package co.edu.uptc.negocio;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.excepciones.ValidacionException;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.LibroDto;
import co.edu.uptc.modelo.enums.Formato;
import java.time.Year;
import java.util.regex.Pattern;

/**
 * Capa de VALIDACIÓN de datos (reglas de formato, no reglas de negocio).
 * La capa de negocio la usa antes de tocar la persistencia.
 */
public class ValidadorDatos {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-13)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

    public void validarCliente(ClienteDto dto) throws ReglaNegocioException {
        if (dto == null) throw new ReglaNegocioException("El cliente no puede ser nulo.");
        validarTextoNoVacio(dto.getPrimerNombre(), "Primer nombre");
        validarTextoNoVacio(dto.getPrimerApellido(), "Primer apellido");
        validarTextoNoVacio(dto.getIdentificacion(), "Identificación");
        validarTextoNoVacio(dto.getCorreoElectronico(), "Correo electrónico");
        if (!EMAIL_PATTERN.matcher(dto.getCorreoElectronico().trim()).matches()) {
            throw new ReglaNegocioException("El formato del correo electrónico no es válido.");
        }
        validarTextoNoVacio(dto.getCelular(), "Celular");
        validarTextoNoVacio(dto.getDireccion(), "Dirección");
        if (dto.getTipoCliente() == null) {
            throw new ReglaNegocioException("Debe seleccionar el tipo de cliente.");
        }
        if (dto.getRol() == null) {
            throw new ReglaNegocioException("Debe seleccionar el rol del usuario.");
        }
    }

    public void validarContrasenaNueva(String contrasena) throws ReglaNegocioException {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new ReglaNegocioException("La contraseña es obligatoria.");
        }
        if (contrasena.trim().length() < 4) {
            throw new ReglaNegocioException("La contraseña debe tener al menos 4 caracteres.");
        }
    }

    public void validarIsbn(String isbn) throws ReglaNegocioException {
        validarIsbnDeDto(isbn);
    }

    /**
     * Valida que el ISBN tenga exactamente 13 dígitos (RF01).
     *
     * <p>Se aceptan guiones y espacios como separadores visuales, pero se
     * ignoran para comprobar la longitud. El ISBN es la clave primaria del
     * catálogo y no puede repetirse ni modificarse.</p>
     *
     * @param isbn ISBN a validar
     * @throws ValidacionException si el ISBN está vacío o no tiene 13 dígitos
     */
    public void validarIsbnDeDto(String isbn) throws ReglaNegocioException {
        String limpio = (isbn == null) ? "" : isbn.replace("-", "").replace(" ", "").trim();
        if (limpio.length() != 13 || !limpio.matches("\\d{13}")) {
            throw new ValidacionException("El ISBN debe contener exactamente 13 dígitos.");
        }
    }

    public void validarIsbnDeDto(LibroDto dto) throws ReglaNegocioException {
        if (dto == null) {
            throw new ValidacionException("El libro no puede ser nulo.");
        }
        validarIsbnDeDto(dto.getIsbn());
    }

    /**
     * Valida los datos de un libro según las reglas de negocio de RF01.
     *
     * <p>Reglas aplicadas:</p>
     * <ul>
     *   <li>ISBN con exactamente 13 dígitos.</li>
     *   <li>Título no vacío y de máximo 100 caracteres.</li>
     *   <li>Al menos un autor.</li>
     *   <li>Año de publicación mayor que 1400 y no superior al año actual.</li>
     *   <li>Precio de venta mayor que cero.</li>
     *   <li>Stock mayor o igual que cero.</li>
     *   <li>Categoría y formato seleccionados.</li>
     *   <li>Si el formato es FISICO y se informan peso/dimensiones, estos deben
     *       ser válidos.</li>
     * </ul>
     *
     * <p>Nota sobre el IVA: el proyecto existente trata el precio de catálogo
     * como base imponible y aplica el IVA (19% para DIGITAL) en la capa del
     * carrito mediante CalculadoraImpuesto. Por eso NO se multiplica el precio
     * aquí: hacerlo provocaría un doble cobro de IVA y rompería RF13.</p>
     *
     * @param dto Libro a validar
     * @throws ReglaNegocioException si algún dato incumple una regla
     */
    public void validarLibro(LibroDto dto) throws ReglaNegocioException {
        validarIsbnDeDto(dto);
        validarTextoNoVacio(dto.getTitulo(), "Título");

        String titulo = dto.getTitulo().trim();
        if (titulo.length() > 100) {
            throw new ValidacionException("El título no puede superar los 100 caracteres.");
        }

        if (dto.getAutores() == null || dto.getAutores().isEmpty()) {
            throw new ReglaNegocioException("Debe indicar al menos un autor.");
        }

        int anioActual = Year.now().getValue();
        if (dto.getAnioPublicacion() <= 1400 || dto.getAnioPublicacion() > anioActual) {
            throw new ValidacionException("El año de publicación debe ser mayor que 1400 y no superar "
                    + anioActual + ".");
        }

        if (dto.getPrecioVenta() <= 0) {
            throw new ValidacionException("El precio de venta debe ser mayor que cero.");
        }

        if (dto.getStock() < 0) {
            throw new ValidacionException("El stock no puede ser negativo.");
        }

        if (dto.getCategoria() == null) {
            throw new ReglaNegocioException("Debe seleccionar una categoría.");
        }
        if (dto.getFormato() == null) {
            throw new ReglaNegocioException("Debe seleccionar el formato (Físico o Digital).");
        }

        if (dto.getFormato() == Formato.FISICO) {
            if (dto.getPeso() != null && dto.getPeso() <= 0) {
                throw new ValidacionException("El peso de un libro físico debe ser mayor que cero.");
            }
            if (dto.getDimensiones() != null && dto.getDimensiones().trim().isEmpty()) {
                throw new ValidacionException("Las dimensiones de un libro físico no pueden estar vacías.");
            }
        }
    }

    public void validarTextoNoVacio(String valor, String nombreCampo) throws ReglaNegocioException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioException("El campo " + nombreCampo + " no puede estar vacío.");
        }
    }
}
