package usuariosjdbc.persistencia;
import usuariosjdbc.modelo.Usuario;
import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
/** Exportacion informativa sin contrasenas: MySQL sigue siendo el almacenamiento principal. */
public class PersistenciaUsuarios {
    private static String esc(String s) {return s.replace("\\","\\\\").replace("\"","\\\"").replace("\n","\\n");}
    public void guardarUsuarioJSON(Usuario u) {
        String json="{\n  \"nombre\": \""+esc(u.getNombre())+"\",\n  \"correo\": \""+esc(u.getCorreo())+"\"\n}\n";
        try {Files.writeString(Path.of("usuarios-publicos.json"),json,StandardCharsets.UTF_8);}
        catch(IOException e) {throw new IllegalStateException("No se pudo exportar JSON",e);}
    }
}
