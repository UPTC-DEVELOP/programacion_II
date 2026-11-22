package co.edu.uptc.caso.estudio.persistence;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfiguracionTienda {

    private static final Properties PROPIEDADES = new Properties();

    static {
        cargar();
    }

    private ConfiguracionTienda() {}

    private static void cargar() {
        try (InputStream is = ConfiguracionTienda.class.getClassLoader().getResourceAsStream("tienda.properties")) {
            if (is != null) {
                PROPIEDADES.load(is);
                return;
            }
        } catch (IOException ignored) {}

        try (FileInputStream fis = new FileInputStream("config/tienda.properties")) {
            PROPIEDADES.load(fis);
        } catch (IOException e) {
            System.err.println("Aviso: Usando valores por defecto (no se encontró tienda.properties).");
        }
    }

    public static String obtener(String clave, String valorPorDefecto) {
        return PROPIEDADES.getProperty(clave, valorPorDefecto);
    }
}