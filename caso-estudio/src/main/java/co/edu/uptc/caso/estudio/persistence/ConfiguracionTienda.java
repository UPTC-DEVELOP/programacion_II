package co.edu.uptc.caso.estudio.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Carga propiedades de configuración de la tienda desde el classpath o carpeta config/
public class ConfiguracionTienda {

    private static final Properties PROPIEDADES = new Properties();
    private static final String RUTA_ARCHIVO = "config/tienda.properties";

    static {
        cargar();
    }

    private ConfiguracionTienda() {
    }

    // Intenta cargar tienda.properties desde resources y luego desde config/tienda.properties
    private static void cargar() {
        try (InputStream is = ConfiguracionTienda.class.getClassLoader()
                .getResourceAsStream("tienda.properties")) {
            if (is != null) {
                PROPIEDADES.load(is);
                return;
            }
        } catch (IOException ignored) {
            // Si no existe en resources, se intenta cargar desde el sistema de archivos
        }

        try (FileInputStream fis = new FileInputStream(RUTA_ARCHIVO)) {
            PROPIEDADES.load(fis);
        } catch (IOException e) {
            System.err.println("Aviso: Usando valores por defecto (no se encontró tienda.properties).");
        }
    }

    // Retorna el valor limpio asociado a la clave o el valor por defecto si no está definido
    public static String obtener(String clave, String valorPorDefecto) {
        if (clave == null) {
            return valorPorDefecto;
        }
        String valor = PROPIEDADES.getProperty(clave);
        if (valor == null || valor.trim().isEmpty()) {
            return valorPorDefecto;
        }
        return valor.trim();
    }
}