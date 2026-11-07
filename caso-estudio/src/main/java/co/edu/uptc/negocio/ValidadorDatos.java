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
    /** Contraseña: mínimo 8 caracteres, al menos una mayúscula y un dígito. */
    public static final Pattern CONTRASENA_PATTERN = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,}$");
    /** Identificación numérica (CC, TI, CE): de 1 a 10 dígitos. */
    public static final Pattern IDENTIFICACION_NUMERICA_PATTERN = Pattern.compile("^\\d{1,10}$");
    /** Pasaporte: de 1 a 10 caracteres alfanuméricos. */
    public static final Pattern PASAPORTE_PATTERN = Pattern.compile("^[A-Za-z0-9]{1,10}$");
    /** Celular nacional: exactamente 10 dígitos. */
    public static final Pattern CELULAR_PATTERN = Pattern.compile("^\\d{10}$");

    private static final String TIPO_PASAPORTE = "Pasaporte";

    public static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-13)?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");

    public void validarCliente(ClienteDto dto) throws ReglaNegocioException {
        if (dto == null) throw new ReglaNegocioException("El cliente no puede ser nulo.");
        validarTextoNoVacio(dto.getPrimerNombre(), "Primer nombre");
        validarTextoNoVacio(dto.getPrimerApellido(), "Primer apellido");
        validarTextoNoVacio(dto.getIdentificacion(), "Identificación");
        validarIdentificacion(dto.getTipoIdentificacion(), dto.getIdentificacion());
        validarTextoNoVacio(dto.getCorreoElectronico(), "Correo electrónico");
        if (!EMAIL_PATTERN.matcher(dto.getCorreoElectronico().trim()).matches()) {
            throw new ReglaNegocioException("El formato del correo electrónico no es válido.");
        }
        validarTextoNoVacio(dto.getCelular(), "Celular");
        validarCelular(dto.getCelular());
        validarTextoNoVacio(dto.getDireccion(), "Dirección");
        if (dto.getTipoCliente() == null) {
            throw new ReglaNegocioException("Debe seleccionar el tipo de cliente.");
        }
        if (dto.getRol() == null) {
            throw new ReglaNegocioException("Debe seleccionar el rol del usuario.");
        }
    }

    /**
     * Valida la identificación: máximo 10 posiciones. Para CC, TI y CE solo se
     * permiten dígitos; para Pasaporte se permiten letras y dígitos.
     *
     * @param tipoIdentificacion Tipo de documento (CC, TI, CE o Pasaporte)
     * @param identificacion     Número de identificación
     * @throws ReglaNegocioException si no cumple el formato
     */
    public void validarIdentificacion(String tipoIdentificacion, String identificacion)
            throws ReglaNegocioException {
        String valor = (identificacion == null) ? "" : identificacion.trim();
        if (TIPO_PASAPORTE.equalsIgnoreCase(tipoIdentificacion)) {
            if (!PASAPORTE_PATTERN.matcher(valor).matches()) {
                throw new ReglaNegocioException(
                        "El pasaporte debe tener máximo 10 caracteres, solo letras y números.");
            }
            return;
        }
        if (!IDENTIFICACION_NUMERICA_PATTERN.matcher(valor).matches()) {
            throw new ReglaNegocioException(
                    "La identificación debe contener solo dígitos y máximo 10 posiciones.");
        }
    }

    /**
     * Valida que el celular cumpla la estructura nacional de 10 dígitos.
     *
     * @param celular Número de celular
     * @throws ReglaNegocioException si no tiene exactamente 10 dígitos
     */
    public void validarCelular(String celular) throws ReglaNegocioException {
        String valor = (celular == null) ? "" : celular.trim();
        if (!CELULAR_PATTERN.matcher(valor).matches()) {
            throw new ReglaNegocioException("El celular debe tener exactamente 10 dígitos.");
        }
    }

    /**
     * Valida una contraseña NUEVA (registro o cambio de contraseña): mínimo 8
     * caracteres, al menos una letra mayúscula y al menos un dígito.
     *
     * <p>No se aplica al iniciar sesión, por lo que las contraseñas de los
     * clientes de ejemplo siguen funcionando.</p>
     *
     * @param contrasena Contraseña a validar
     * @throws ReglaNegocioException si no cumple la política
     */
    public void validarContrasenaNueva(String contrasena) throws ReglaNegocioException {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new ReglaNegocioException("La contraseña es obligatoria.");
        }
        if (!CONTRASENA_PATTERN.matcher(contrasena.trim()).matches()) {
            throw new ReglaNegocioException(
                    "La contraseña debe tener mínimo 8 caracteres, una mayúscula y un dígito.");
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
