package modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Hash PBKDF2 para registros nuevos; permite verificar cuentas anteriores. */
final class SeguridadContrasenas {
    private static final int ITERACIONES = 210000;
    private static final int BITS = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();
    private SeguridadContrasenas() {}

    static String generar(String clave) {
        byte[] sal = new byte[16];
        ALEATORIO.nextBytes(sal);
        byte[] hash = derivar(clave, sal, ITERACIONES);
        return "pbkdf2_sha256$" + ITERACIONES + "$" + Base64.getEncoder().encodeToString(sal)
            + "$" + Base64.getEncoder().encodeToString(hash);
    }

    static boolean esHash(String almacenada) {
        return almacenada != null && almacenada.startsWith("pbkdf2_sha256$");
    }

    static boolean verificar(String ingresada, String almacenada) {
        if (ingresada == null || almacenada == null) return false;
        if (!esHash(almacenada)) {
            return MessageDigest.isEqual(ingresada.getBytes(StandardCharsets.UTF_8),
                almacenada.getBytes(StandardCharsets.UTF_8));
        }
        try {
            String[] partes = almacenada.split("\\$", -1);
            if (partes.length != 4) return false;
            int iter = Integer.parseInt(partes[1]);
            if (iter < 10000 || iter > 1000000) return false;
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            return MessageDigest.isEqual(esperado, derivar(ingresada, sal, iter));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(String clave, byte[] sal, int iteraciones) {
        char[] chars = clave.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(chars, sal, iteraciones, BITS);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new IllegalStateException("No fue posible procesar la contrasena", e);
        } finally {
            spec.clearPassword();
            java.util.Arrays.fill(chars, '\0');
        }
    }
}
