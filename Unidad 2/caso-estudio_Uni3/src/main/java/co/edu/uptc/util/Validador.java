package co.edu.uptc.util;

import co.edu.uptc.exception.ValidacionException;
import java.time.Year;
import java.util.regex.Pattern;

// Validador revisa formatos de entrada antes de que los servicios apliquen reglas de negocio.
public final class Validador {
    private static final Pattern CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern TELEFONO = Pattern.compile("^[0-9]{7,10}$");
    private static final Pattern NOMBRE = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");
    private Validador() { }

    public static void obligatorio(String valor, String campo) throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) throw new ValidacionException("El campo " + campo + " es obligatorio.");
    }
    public static void nombre(String valor) throws ValidacionException {
        obligatorio(valor, "nombre");
        if (valor.trim().length() < 3 || !NOMBRE.matcher(valor.trim()).matches()) throw new ValidacionException("El nombre debe tener mínimo 3 letras.");
    }
    public static void correo(String valor) throws ValidacionException {
        obligatorio(valor, "correo");
        if (!CORREO.matcher(valor.trim()).matches()) throw new ValidacionException("El correo no tiene un formato válido.");
    }
    public static void telefono(String valor) throws ValidacionException {
        obligatorio(valor, "teléfono");
        if (!TELEFONO.matcher(valor.trim()).matches()) throw new ValidacionException("El teléfono debe tener entre 7 y 10 dígitos.");
    }
    public static void contrasena(String valor) throws ValidacionException {
        if (valor == null || valor.length() < 6) throw new ValidacionException("La contraseña debe tener mínimo 6 caracteres.");
    }
    public static void isbn(String valor) throws ValidacionException {
        obligatorio(valor, "ISBN");
        String limpio = valor.replace("-", "").trim();
        if (limpio.length() != 10 && limpio.length() != 13 || !limpio.matches("[0-9]+")) throw new ValidacionException("El ISBN debe tener 10 o 13 dígitos.");
    }
    public static void anio(int valor) throws ValidacionException {
        if (valor < 1450 || valor > Year.now().getValue()) throw new ValidacionException("El año de publicación no es válido.");
    }
    public static void positivo(double valor, String campo) throws ValidacionException {
        if (valor <= 0) throw new ValidacionException("El campo " + campo + " debe ser mayor que cero.");
    }
    public static void noNegativo(int valor, String campo) throws ValidacionException {
        if (valor < 0) throw new ValidacionException("El campo " + campo + " no puede ser negativo.");
    }
    public static void iva(double valor) throws ValidacionException {
        if (valor != 5.0 && valor != 19.0) throw new ValidacionException("El IVA solo puede ser 5 o 19.");
    }
    public static void descuento(double valor) throws ValidacionException {
        if (valor < 0 || valor > 90) throw new ValidacionException("El descuento debe estar entre 0 y 90.");
    }
    public static void cantidad(int valor) throws ValidacionException {
        if (valor <= 0) throw new ValidacionException("La cantidad debe ser positiva.");
    }
}
