package co.edu.uptc.persistencia;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import co.edu.uptc.excepciones.PersistenciaException;
import co.edu.uptc.modelo.dto.CompraDtos.FacturaDto;
/*
 * GUARDA LAS FACTURAS GENERADAS EN FORMATO JSON EN EL SISTEMA DE ARCHIVOS LOCAL.
 * 
 * @author: Equipo de desarrollo grupo 7 UPTC
 * */
/*modificacion:Brayan Panqueva*/
public class FacturaJsonRepository {
    private static final String RUTA_POR_DEFECTO = "data/facturas.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path ruta;

    public FacturaJsonRepository() { this(RUTA_POR_DEFECTO); }
    public FacturaJsonRepository(String ruta) {
        this.ruta = Paths.get(ruta);
        inicializar();
    }
    public synchronized void guardar(FacturaDto factura) {
        if (factura == null) return;
        List<FacturaDto> facturas = leer();
        facturas.removeIf(actual -> actual.idVenta() == factura.idVenta());
        facturas.add(factura);
        escribir(facturas);
    }
    private void inicializar() {
        try {
            Path parent = ruta.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            if (!Files.exists(ruta) || Files.size(ruta) == 0) escribir(new ArrayList<>());
        } catch (IOException e) { throw new PersistenciaException("No se pudo inicializar facturas.json", e); }
    }
    private List<FacturaDto> leer() {
        try {
            if (!Files.exists(ruta)) return new ArrayList<>();
            String texto = Files.readString(ruta, StandardCharsets.UTF_8);
            if (texto.isBlank()) return new ArrayList<>();
            List<FacturaDto> datos = gson.fromJson(texto, new TypeToken<List<FacturaDto>>() { }.getType());
            return datos == null ? new ArrayList<>() : datos;
        } catch (IOException | JsonParseException e) { throw new PersistenciaException("No se pudo leer facturas.json", e); }
    }
    private void escribir(List<FacturaDto> facturas) {
        try (Writer writer = Files.newBufferedWriter(ruta, StandardCharsets.UTF_8)) { gson.toJson(facturas, writer); }
        catch (IOException e) { throw new PersistenciaException("No se pudo escribir facturas.json", e); }
    }
}
