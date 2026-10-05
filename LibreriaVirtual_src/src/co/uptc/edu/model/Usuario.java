package co.uptc.edu.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Usuario del sistema (cliente o administrador).
 * Valida sus datos al construirse y al modificarse; la contraseña nunca se guarda en texto plano.
 */
public class Usuario {

    private static final int ITERACIONES = 65536;
    private static final int LONGITUD_CLAVE_BITS = 256;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private String nombre;
    private String apellido;
    private String correo;
    private String direccion;
    private String telefono;
    private Rol tipoUsuario;
    private byte[] salt;
    private byte[] hashContrasena;

    public Usuario(String nombre, String apellido, String correo, String direccion,
                   String telefono, Rol tipoUsuario, String contrasena) {
        this.nombre = ValidadorUsuario.nombre(nombre, "El nombre");
        this.apellido = ValidadorUsuario.nombre(apellido, "El apellido");
        this.correo = ValidadorUsuario.correo(correo);
        this.direccion = ValidadorUsuario.direccion(direccion);
        this.telefono = ValidadorUsuario.telefono(telefono);
        this.tipoUsuario = ValidadorUsuario.tipoUsuario(tipoUsuario);
        cambiarContrasena(contrasena);
    }

    // ---------- Consulta ----------
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getTelefono() { return telefono; }
    public Rol getTipoUsuario() { return tipoUsuario; }
    public String getNombreCompleto() { return nombre + " " + apellido; }

    // ---------- Comportamiento del diagrama ----------

    /** Verifica la contraseña ingresada contra la almacenada. */
    public boolean iniciarSesion(String contrasena) {
        if (contrasena == null) {
            return false;
        }
        return MessageDigest.isEqual(hashContrasena, calcularHash(contrasena, salt));
    }

    /** Actualiza los datos personales (RF-11). Si algún dato es inválido no se modifica nada. */
    public void actualizarDatos(String nombre, String apellido, String direccion, String telefono) {
        String n = ValidadorUsuario.nombre(nombre, "El nombre");
        String a = ValidadorUsuario.nombre(apellido, "El apellido");
        String d = ValidadorUsuario.direccion(direccion);
        String t = ValidadorUsuario.telefono(telefono);
        this.nombre = n;
        this.apellido = a;
        this.direccion = d;
        this.telefono = t;
    }

    /** Valida y guarda una contraseña nueva (solo se almacena su hash con sal). */
    public void cambiarContrasena(String nuevaContrasena) {
        ValidadorUsuario.contrasena(nuevaContrasena);
        byte[] nuevaSal = new byte[16];
        ALEATORIO.nextBytes(nuevaSal);
        this.hashContrasena = calcularHash(nuevaContrasena, nuevaSal);
        this.salt = nuevaSal;
    }

    // Solo GestionUsuarios puede cambiar el correo y el rol, para garantizar unicidad y reglas de negocio.
    void cambiarCorreo(String nuevoCorreo) { this.correo = ValidadorUsuario.correo(nuevoCorreo); }
    void cambiarTipoUsuario(Rol nuevoTipo) { this.tipoUsuario = ValidadorUsuario.tipoUsuario(nuevoTipo); }

    private static byte[] calcularHash(String contrasena, byte[] sal) {
        try {
            PBEKeySpec spec = new PBEKeySpec(contrasena.toCharArray(), sal, ITERACIONES, LONGITUD_CLAVE_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No fue posible procesar la contraseña.", e);
        }
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " <" + correo + ">";
    }
}
