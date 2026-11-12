package co.edu.uptc.persistence.json;

import co.edu.uptc.model.Resena;
import co.edu.uptc.persistence.ResenaRepository;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// ResenaJsonRepository conserva reseñas y permite consultar si ya existe una combinación cliente-libro.
public class ResenaJsonRepository implements ResenaRepository {
    private final Path archivo;
    public ResenaJsonRepository(Path archivo) { this.archivo = archivo; ArchivoJson.leer(archivo); }
    @Override public List<Resena> listar() {
        List<Resena> resultado = new ArrayList<Resena>();
        for (Map<String,String> m : ArchivoJson.leer(archivo)) resultado.add(new Resena(m.get("isbn"), m.get("correo"), Integer.parseInt(m.get("calificacion")), m.get("comentario"), m.get("fecha")));
        return resultado;
    }
    @Override public void guardar(Resena resena) {
        List<Map<String,String>> filas = ArchivoJson.leer(archivo);
        Map<String,String> m = new LinkedHashMap<String,String>(); m.put("isbn",resena.getIsbn()); m.put("correo",resena.getCorreoCliente()); m.put("calificacion",String.valueOf(resena.getCalificacion())); m.put("comentario",resena.getComentario()); m.put("fecha",resena.getFecha()); filas.add(m); ArchivoJson.escribir(archivo, filas);
    }
    @Override public boolean existeParaCliente(String isbn, String correo) { for (Resena r : listar()) if (r.getIsbn().equals(isbn) && r.getCorreoCliente().equalsIgnoreCase(correo)) return true; return false; }
}
