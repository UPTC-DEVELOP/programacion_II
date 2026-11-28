package co.edu.uptc.caso.estudio.negocio;

import co.edu.uptc.caso.estudio.model.Cliente;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Encapsula el hash SHA-256 y las reglas de validación de datos y contraseñas de usuario
public class SeguridadUsuario {

    public static final int LONGITUD_MINIMA_CONTRASENA = 6;
    private static final String REGEX_CORREO = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$";
    private static final String REGEX_TELEFONO = "\\d{7,15}";

    public String validarRegistro(Cliente cliente) {
        if (cliente == null) {
            return "Los datos del cliente son obligatorios.";
        }
        if (esVacio(cliente.getId())) {
            return "El ID / Cédula es obligatorio.";
        }
        if (esVacio(cliente.getNombreCompleto())) {
            return "El nombre completo es obligatorio.";
        }
        if (cliente.getCorreo() == null || !cliente.getCorreo().trim().matches(REGEX_CORREO)) {
            return "Ingrese un correo electrónico con formato válido (ej. usuario@dominio.com).";
        }
        if (cliente.getTelefono() != null && !esVacio(cliente.getTelefono())
                && !cliente.getTelefono().trim().matches(REGEX_TELEFONO)) {
            return "El teléfono debe contener solo números (entre 7 y 15 dígitos).";
        }
        return validarContrasena(cliente.getContrasenaHash(), "La contraseña");
    }

    public String validarContrasena(String contrasena, String etiquetaCampo) {
        if (contrasena == null || contrasena.length() < LONGITUD_MINIMA_CONTRASENA) {
            return etiquetaCampo + " debe tener mínimo " + LONGITUD_MINIMA_CONTRASENA + " caracteres.";
        }
        return null;
    }

    public String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible", e);
        }
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}