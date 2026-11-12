package co.edu.uptc.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// FechaUtil evita formatos de fecha repetidos y facilita la persistencia en texto.
public final class FechaUtil {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private FechaUtil() { }
    public static String ahora() { return LocalDateTime.now().format(FORMATO); }
    public static String formatear(LocalDateTime fecha) { return fecha.format(FORMATO); }
}
