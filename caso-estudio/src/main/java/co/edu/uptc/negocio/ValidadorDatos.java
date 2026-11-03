package co.edu.uptc.negocio;

import co.edu.uptc.excepciones.ReglaNegocioException;
import co.edu.uptc.modelo.dto.ClienteDto;
import co.edu.uptc.modelo.dto.LibroDto;
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

    public void validarIsbnDeDto(String isbn) throws ReglaNegocioException {
        if (isbn == null || !ISBN_PATTERN.matcher(isbn).matches()) {
            throw new ReglaNegocioException("El ISBN no es válido o está vacío.");
        }
    }

    public void validarIsbnDeDto(LibroDto dto) throws ReglaNegocioException {
        if (dto == null) {
            throw new ReglaNegocioException("El libro no puede ser nulo.");
        }
        validarIsbnDeDto(dto.getIsbn());
    }

    public void validarLibro(LibroDto dto) throws ReglaNegocioException {
        validarIsbnDeDto(dto);
        validarTextoNoVacio(dto.getTitulo(), "Título");
        if (dto.getAutores() == null || dto.getAutores().isEmpty()) {
            throw new ReglaNegocioException("Debe indicar al menos un autor.");
        }
        if (dto.getAnioPublicacion() < 1000 || dto.getAnioPublicacion() > 2100) {
            throw new ReglaNegocioException("El año de publicación no es válido.");
        }
        if (dto.getPrecioVenta() <= 0) {
            throw new ReglaNegocioException("El precio de venta debe ser mayor que cero.");
        }
        if (dto.getStock() < 0) {
            throw new ReglaNegocioException("El stock no puede ser negativo.");
        }
        if (dto.getCategoria() == null) {
            throw new ReglaNegocioException("Debe seleccionar una categoría.");
        }
        if (dto.getFormato() == null) {
            throw new ReglaNegocioException("Debe seleccionar el formato (Físico o Digital).");
        }
    }

    public void validarTextoNoVacio(String valor, String nombreCampo) throws ReglaNegocioException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioException("El campo " + nombreCampo + " no puede estar vacío.");
        }
    }
}
