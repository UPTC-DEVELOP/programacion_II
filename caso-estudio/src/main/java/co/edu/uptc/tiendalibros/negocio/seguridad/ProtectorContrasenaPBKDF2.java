package co.edu.uptc.tiendalibros.negocio.seguridad;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import co.edu.uptc.tiendalibros.modelo.ContrasenaProtegida;
import co.edu.uptc.tiendalibros.negocio.ErrorTecnicoSistemaException;

/// Protege contraseñas con el algoritmo **PBKDF2** (PBKDF2WithHmacSHA256), que viene en
/// el JDK.
///
/// Con este algoritmo, nunca se guarda una contraseña en texto simple.
/// 
/// Con este algoritmo, se guarda el resultado de aplicar una función matemática
/// (HMAC-SHA256) repetidamente:
/// 
/// paso 1. se genera una sal distinta para cada contraseña,
/// 
/// paso 2. se calcula la contraseña encriptada (el hash) teniendo en cuenta la contraseña
/// en texto simple, la sal y el número de repeticiones.
/// 
/// paso 3. se guarda la contraseña encriptada (el hash) y la sal.
public class ProtectorContrasenaPBKDF2 implements IProtectorContrasena {
  
  /// Algoritmo del JDK que para calcular el hash a partir de la contraseña y la sal.
  private static final String ALGORITMO_DERIVACION = "PBKDF2WithHmacSHA256";
  
  /// Cuántas veces se repite el cálculo de la encriptación. OWASP recomienda 600 000 para
  /// PBKDF2 con HMAC-SHA256.
  private static final int NUMERO_ITERACIONES = 600_000;
  
  /// Tamaño de la sal en bytes.
  /// 
  /// 16 bytes (128 bits) son suficientes para evitar duplicados. En Base64 ocupa
  /// 24 caracteres.
  private static final int LONGITUD_SAL_EN_BYTES = 16;
  
  /// Tamaño del hash en bits. SHA-256 produce 256 bits. En Base64 ocupa 44 caracteres.
  private static final int LONGITUD_HASH_EN_BITS = 256;
  
  /// Generador de números aleatorios seguro y con criptografía para producir la sal.
  private final SecureRandom generadorAleatorio;
  
  /// Crea un objeto [ProtectorContrasenaPBKDF2] para proteger contraseñas PBKDF2.
  public ProtectorContrasenaPBKDF2() {
    this.generadorAleatorio = new SecureRandom();
  }
  
  @Override
  public ContrasenaProtegida proteger(char[] contrasena) {
    // paso 1: crear una sal nueva y aleatoria
    byte[] sal = new byte[ProtectorContrasenaPBKDF2.LONGITUD_SAL_EN_BYTES];
    this.generadorAleatorio.nextBytes(sal);
    
    // paso 2: calcular el hash con la contraseña y la sal
    byte[] hash = calcularHash(contrasena, sal);
    
    // paso 3: pasar la sal y el hash a texto para guardarlos
    return new ContrasenaProtegida(
      Base64.getEncoder()
        .encodeToString(hash),
      Base64.getEncoder()
        .encodeToString(sal)
    );
  }
  
  @Override
  public boolean verificar(char[] contrasena, ContrasenaProtegida protegida) {
    byte[] sal;
    byte[] hashGuardado;
    
    // obtener la sal y la contraseña protegida guardada
    try {
      sal = Base64.getDecoder()
        .decode(protegida.getSal());
      hashGuardado = Base64.getDecoder()
        .decode(protegida.getHash());
    } catch (IllegalArgumentException excepcion) {
      // el valor que viene de la base de datos debe ser un texto que Base64.
      throw new ErrorTecnicoSistemaException("La contraseña guardada no tiene el formato esperado.", excepcion);
    }
    
    // repetir el cálculo con la sal guardada
    byte[] hashCalculado = calcularHash(contrasena, sal);
    
    // MessageDigest.isEqual compara las contraseñas encriptadas comparando bytes
    return MessageDigest.isEqual(hashCalculado, hashGuardado);
  }
  
  /// Calcula el hash PBKDF2 de una contraseña con la sal dada.
  ///
  /// @param contrasena Contraseña en claro (sin proteger).
  /// @param sal        Sal que se usa en el cálculo.
  /// @return El hash calculado.
  /// @throws ErrorTecnicoSistemaException Si ocurre un error en el JDK, el algoritmo o
  ///                                      los parámetros.
  private byte[] calcularHash(char[] contrasena, byte[] sal) {
    // PBEKeySpec hace su propia copia de la contraseña. Se borra al terminar.
    PBEKeySpec especificacion = new PBEKeySpec(
      contrasena, sal, ProtectorContrasenaPBKDF2.NUMERO_ITERACIONES, ProtectorContrasenaPBKDF2.LONGITUD_HASH_EN_BITS
    );
    try {
      SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ProtectorContrasenaPBKDF2.ALGORITMO_DERIVACION);
      return fabrica.generateSecret(especificacion)
        .getEncoded();
    } catch (GeneralSecurityException excepcion) {
      // Error al buscar el algoritmo en el JDK o hay un problema con la contraseña
      throw new ErrorTecnicoSistemaException("No se pudo calcular el hash de la contraseña.", excepcion);
    } finally {
      // siempre borrar la contraseña de la memoria RAM
      especificacion.clearPassword();
    }
  }
}