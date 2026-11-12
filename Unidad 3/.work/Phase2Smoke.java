import co.edu.uptc.exception.*;
import co.edu.uptc.util.*;

public class Phase2Smoke {
    public static void main(String[] args) throws Exception {
        Validador.correo("ana@correo.com");
        Validador.telefono("3001234567");
        Validador.isbn("978-958-00-0001-1");
        boolean rechazo = false;
        try { Validador.iva(10); } catch (ValidacionException e) { rechazo = true; }
        if (!rechazo) throw new AssertionError("IVA inválido no rechazado");
        if (SeguridadHash.hash("Admin123").length() != 64) throw new AssertionError("hash");
        String ruta = Rutas.libros().toString().replace('\\', '/');
        if (!ruta.endsWith("data/libros.json")) throw new AssertionError("ruta");
        if (!FormatoMoneda.formatear(119000).contains("119.000")) throw new AssertionError("moneda");
        if (FechaUtil.ahora().length() != 19) throw new AssertionError("fecha");
        System.out.println("PHASE2_UTILS_OK");
    }
}
