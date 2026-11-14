package co.edu.uptc.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// SeguridadHash centraliza el hash de contraseñas y mantiene esa decisión fuera del modelo.
public final class SeguridadHash {
    private SeguridadHash() { }
    public static String hash(String texto) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
            StringBuilder resultado = new StringBuilder();
            for (byte b : bytes) {
				resultado.append(String.format("%02x", b));
			}
            return resultado.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no está disponible en el JDK.", e);
        }
    }
}
