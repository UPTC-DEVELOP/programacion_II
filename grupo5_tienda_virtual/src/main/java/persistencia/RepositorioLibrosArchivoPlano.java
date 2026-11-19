package persistencia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import model.FormatoLibro;
import model.Libro;

/**
 * Repositorio de libros en archivo de texto plano (datos/libros.txt).
 */
public class RepositorioLibrosArchivoPlano implements IRepositorio<Libro> {

    private static final String RUTA_POR_DEFECTO = "datos/libros.txt";
    private static final String SEPARADOR = "\t";

    private final String rutaArchivo;
    private final List<Libro> libros;

    public RepositorioLibrosArchivoPlano() {
        this(RUTA_POR_DEFECTO);
    }

    public RepositorioLibrosArchivoPlano(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
        this.libros = new ArrayList<Libro>();
        cargar();
    }

    @Override
    public List<Libro> listar() {
        return libros;
    }

    @Override
    public void agregar(Libro elemento) {
        if (elemento != null) {
            libros.add(elemento);
        }
    }

    @Override
    public boolean eliminar(Libro elemento) {
        if (elemento == null) {
            return false;
        }
        return libros.remove(elemento);
    }

    @Override
    public void cargar() {
        libros.clear();
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            int numeroLinea = 0;
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) {
                    continue;
                }
                libros.add(parsearLinea(linea, numeroLinea));
            }
        } catch (IOException e) {
            throw new PersistenciaException("Error al leer el archivo de libros: " + rutaArchivo, e);
        }
    }

    /**
     * Convierte una linea con formato:
     * isbn\ttitulo\tautor\tanioPublicacion\tcategoria\teditorial\tnumeroPaginas\tprecio\tporcentajeIva\tcantidadDisponible\tformato
     */
    private Libro parsearLinea(String linea, int numeroLinea) {
        String[] partes = linea.split(SEPARADOR, -1);
        if (partes.length < 11) {
            throw new PersistenciaException(
                    "Formato invalido en libros.txt en la linea " + numeroLinea + ": se esperaban 11 columnas.");
        }

        try {
            Libro libro = new Libro();
            libro.setIsbn(partes[0].trim());
            libro.setTitulo(partes[1]);
            libro.setAutor(partes[2]);
            libro.setAnioPublicacion(Integer.parseInt(partes[3].trim()));
            libro.setCategoria(partes[4]);
            libro.setEditorial(partes[5]);
            libro.setNumeroPaginas(Integer.parseInt(partes[6].trim()));
            libro.setPrecio(Double.parseDouble(partes[7].trim()));
            libro.setPorcentajeIva(Double.parseDouble(partes[8].trim()));
            libro.setCantidadDisponible(Integer.parseInt(partes[9].trim()));
            libro.setFormato(FormatoLibro.valueOf(partes[10].trim().toUpperCase()));
            return libro;
        } catch (IllegalArgumentException e) {
            throw new PersistenciaException(
                    "Error de datos en libros.txt en la linea " + numeroLinea + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void guardar() {
        // Implementacion en C14
    }
}
