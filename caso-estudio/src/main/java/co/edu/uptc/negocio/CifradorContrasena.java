package co.edu.uptc.negocio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para cifrar contraseñas con el algoritmo SHA-256.
 *
 * <p>Las contraseñas nunca se guardan en texto plano: se almacena su hash
 * (64 caracteres hexadecimales). Para iniciar sesión se calcula el hash de la
 * contraseña ingresada y se compara con el hash almacenado.</p>
 *
 * <p>Es una clase de utilidad: no se instancia (constructor privado).</p>
 */
public final class CifradorContrasena {

    private static final String ALGORITMO = "SHA-256";

    private CifradorContrasena() {
    }

    /**
     * Calcula el hash SHA-256 de una contraseña.
     *
     * @param contrasena Contraseña en texto plano
     * @return Hash en hexadecimal (64 caracteres), o null si la contraseña es null
     */
    public static String hash(String contrasena) {
        if (contrasena == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITMO);
            byte[] bytes = digest.digest(contrasena.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 está disponible en toda JVM estándar
            throw new IllegalStateException("El algoritmo " + ALGORITMO + " no está disponible.", e);
        }
    }

    /**
     * Indica si una contraseña en texto plano corresponde al hash almacenado.
     *
     * @param contrasena     Contraseña ingresada por el usuario
     * @param hashAlmacenado Hash guardado en el sistema
     * @return true si coinciden
     */
    public static boolean coincide(String contrasena, String hashAlmacenado) {
        if (contrasena == null || hashAlmacenado == null || hashAlmacenado.isEmpty()) {
            return false;
        }
        return hashAlmacenado.equals(hash(contrasena));
    }
}
