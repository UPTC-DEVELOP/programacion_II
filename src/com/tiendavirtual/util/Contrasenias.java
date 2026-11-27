package com.tiendavirtual.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Guarda contraseñas de forma segura (PBKDF2 con sal aleatoria).
 * Formato guardado: pbkdf2$iteraciones$sal$hash
 * Las contraseñas antiguas en texto plano siguen funcionando
 * y se convierten a hash la próxima vez que el cliente inicia sesión.
 */
public final class Contrasenias {

    private static final String PREFIJO = "pbkdf2$";
    private static final int ITERACIONES = 120000;
    private static final int BYTES_SAL = 16;
    private static final int BITS_HASH = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private Contrasenias() {
    }

    public static String hashear(String plana) {
        byte[] sal = new byte[BYTES_SAL];
        ALEATORIO.nextBytes(sal);
        byte[] hash = derivar(plana, sal, ITERACIONES);
        return PREFIJO + ITERACIONES + "$" + codificar(sal) + "$" + codificar(hash);
    }

    public static boolean esHash(String guardada) {
        return guardada != null
                && guardada.startsWith(PREFIJO)
                && guardada.split("\\$").length == 4;
    }

    public static boolean verificar(String plana, String guardada) {
        if (plana == null || guardada == null) {
            return false;
        }

        if (!esHash(guardada)) {
            // Contraseña antigua en texto plano.
            return MessageDigest.isEqual(
                    plana.getBytes(StandardCharsets.UTF_8),
                    guardada.getBytes(StandardCharsets.UTF_8));
        }

        try {
            String[] partes = guardada.split("\\$");
            int iteraciones = Integer.parseInt(partes[1]);
            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] esperado = Base64.getDecoder().decode(partes[3]);
            byte[] calculado = derivar(plana, sal, iteraciones);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(String plana, byte[] sal, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    plana.toCharArray(), sal, iteraciones, BITS_HASH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No se pudo procesar la contraseña.", e);
        }
    }

    private static String codificar(byte[] datos) {
        return Base64.getEncoder().encodeToString(datos);
    }
}
