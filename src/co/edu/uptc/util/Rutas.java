package co.edu.uptc.util;

import java.nio.file.Path;
import java.nio.file.Paths;

// Rutas concentra los archivos de datos para que ninguna otra capa hardcodee ubicaciones.
public final class Rutas {
    private static final Path DATA = Paths.get("data");
    private Rutas() { }
    public static Path carpetaDatos() { return DATA; }
    public static Path libros() { return DATA.resolve("libros.json"); }
    public static Path clientes() { return DATA.resolve("clientes.json"); }
    public static Path pedidos() { return DATA.resolve("pedidos.json"); }
    public static Path carritos() { return DATA.resolve("carritos.json"); }
    public static Path resenas() { return DATA.resolve("resenas.json"); }
    public static Path registroOperaciones() { return DATA.resolve("registro_operaciones.txt"); }
    public static Path recibos() { return DATA.resolve("recibos"); }
}
