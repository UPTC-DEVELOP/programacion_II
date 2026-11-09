package co.edu.uptc.persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.interfaces.IAuditoria;
import co.edu.uptc.modelo.dto.RegistroAuditoriaDto;
import co.edu.uptc.modelo.enums.AccionAuditoria;
/*
 * GUARDA AUDITORIA Y ACCIONES DEL ADIN EN TXT.
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
/*modificacion:Brayan Panqueva*/
public class AuditoriaTxtRepository implements IAuditoria {
    private final Path ruta;
    public AuditoriaTxtRepository() { this("data/auditoria_admin.txt"); }
    public AuditoriaTxtRepository(String ruta) {
        this.ruta = Paths.get(ruta);
        try {
            Path parent = this.ruta.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            if (!Files.exists(this.ruta)) Files.createFile(this.ruta);
        } catch (IOException e) { throw new PersistenciaException("No se pudo inicializar la auditoría TXT", e); }
    }
    @Override public synchronized void registrar(AccionAuditoria accion, String isbn, String usuario, String detalles) {
        try {
            int id = listar().size() + 1;
            String linea = String.format("%d | %s | %s | %s | %s | %s%n", id,
                    LocalDateTime.now(), accion, usuario == null ? "" : usuario,
                    isbn == null ? "" : isbn, detalles == null ? "" : detalles.replace("\n", " "));
            Files.writeString(ruta, linea, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
        } catch (IOException e) { throw new PersistenciaException("No se pudo registrar auditoría TXT", e); }
    }
    @Override public synchronized List<RegistroAuditoriaDto> listar() {
        List<RegistroAuditoriaDto> resultado = new ArrayList<>();
        try {
            int id = 1;
            for (String linea : Files.readAllLines(ruta, StandardCharsets.UTF_8)) {
                String[] partes = linea.split(" \\| ", 6);
                if (partes.length < 6) continue;
                AccionAuditoria accion;
                try { accion = AccionAuditoria.valueOf(partes[2]); } catch (IllegalArgumentException e) { continue; }
                resultado.add(new RegistroAuditoriaDto(id++, accion, partes[4], partes[3], partes[1], partes[5]));
            }
            return resultado;
        } catch (IOException e) { throw new PersistenciaException("No se pudo leer auditoría TXT", e); }
    }
}
