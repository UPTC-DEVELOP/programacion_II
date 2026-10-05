package co.uptc.edu.negocio;

import java.util.regex.Pattern;


final class ValidadorUsuario {

    static final int MAX_NOMBRE = 50;
    static final int MAX_CORREO = 100;
    static final int MAX_DIRECCION = 100;
    static final int MIN_CONTRASENA = 8;
    static final int MAX_CONTRASENA = 64;

    private static final Pattern PATRON_NOMBRE = Pattern.compile("^\\p{L}+([ '\\-]\\p{L}+)*$");
    private static final Pattern PATRON_CORREO = Pattern.compile("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\d{7,10}$");

    private ValidadorUsuario() {}

    static String nombre(String valor, String campo) {
        String v = limpiar(valor);
        if (v.isEmpty()) {
            throw new IllegalArgumentException(campo + " es obligatorio.");
        }
        if (v.length() > MAX_NOMBRE) {
            throw new IllegalArgumentException(campo + " no puede superar " + MAX_NOMBRE + " caracteres.");
        }
        if (!PATRON_NOMBRE.matcher(v).matches()) {
            throw new IllegalArgumentException(campo + " solo puede contener letras y espacios.");
        }
        return v;
    }

    /** Devuelve el correo en minúsculas, que es como se guarda y se compara. */
    static String correo(String valor) {
        String v = limpiar(valor).toLowerCase();
        if (v.isEmpty()) {
            throw new IllegalArgumentException("El correo es obligatorio.");
        }
        if (v.length() > MAX_CORREO || !PATRON_CORREO.matcher(v).matches()) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }
        return v;
    }

    static String direccion(String valor) {
        String v = limpiar(valor);
        if (v.isEmpty()) {
            throw new IllegalArgumentException("La dirección es obligatoria.");
        }
        if (v.length() > MAX_DIRECCION) {
            throw new IllegalArgumentException("La dirección no puede superar " + MAX_DIRECCION + " caracteres.");
        }
        return v;
    }

    static String telefono(String valor) {
        String v = limpiar(valor);
        if (!PATRON_TELEFONO.matcher(v).matches()) {
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos numéricos.");
        }
        return v;
    }

    static Rol tipoUsuario(Rol rol) {
        if (rol != Rol.CLIENTE && rol != Rol.ADMIN) {
            throw new IllegalArgumentException("El tipo de usuario debe ser Cliente o Administrador.");
        }
        return rol;
    }

    /** Mínimo 8 caracteres, al menos una letra y un número. */
    static void contrasena(String valor) {
        if (valor == null || valor.length() < MIN_CONTRASENA) {
            throw new IllegalArgumentException("La contraseña debe tener al menos " + MIN_CONTRASENA + " caracteres.");
        }
        if (valor.length() > MAX_CONTRASENA) {
            throw new IllegalArgumentException("La contraseña no puede superar " + MAX_CONTRASENA + " caracteres.");
        }
        boolean letra = false;
        boolean digito = false;
        for (char c : valor.toCharArray()) {
            if (Character.isLetter(c)) letra = true;
            if (Character.isDigit(c)) digito = true;
        }
        if (!letra || !digito) {
            throw new IllegalArgumentException("La contraseña debe combinar letras y números.");
        }
    }

    private static String limpiar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
